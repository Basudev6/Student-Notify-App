package com.example.studentnotifyapp.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.studentnotifyapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SendNotice extends AppCompatActivity {

    private CardView addImage;
    ImageView selectedImg;

    EditText title,notice;
    Button uploadImg,uploadTxt;

    private final int REQ =1;
    private Bitmap bitmap;
    private DatabaseReference reference;
    private StorageReference storageReference;

    private ProgressDialog pd;
    String downloadUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notice);

        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        addImage = findViewById(R.id.notice_img);
        selectedImg = findViewById(R.id.selected_img);
        title = findViewById(R.id.notice_title);
        notice = findViewById(R.id.notice_text);
        uploadImg= findViewById(R.id.btn_notice_img);
        uploadTxt = findViewById(R.id.btn_notice_text);



        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallery();
            }
        });
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             String noticeTitle = title.getText().toString().trim();
                if(noticeTitle.isEmpty())
                {
                    title.setError("This field must be filled");
                    title.requestFocus();
                }
                else if(bitmap == null)
                {
                    Toast.makeText(SendNotice.this, "Image not selected", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadImage();
                }
            }
        });
    }

    private void uploadImage()
    {

        pd.setMessage("Sending...");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Notice").child(finalImg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImg);
        uploadTask.addOnCompleteListener(SendNotice.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    uploadData();
                                }
                            });
                        }
                    });
                }
                else {
                    pd.dismiss();
                    Toast.makeText(SendNotice.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void uploadData()
    {
        reference = reference.child("NoticeImage");
        final String uniqueKey = reference.push().getKey();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        String titleNotice = title.getText().toString();

        ImageNoticeData imageNoticeData = new ImageNoticeData(titleNotice,downloadUrl,date,time,uniqueKey);

        reference.child(uniqueKey).setValue(imageNoticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(SendNotice.this, "Notice sent successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(SendNotice.this, "Something went wrong in", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void openGallery()
    {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery,REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode==RESULT_OK)
        {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            }
            catch (IOException e){
                e.printStackTrace();
            }
            selectedImg.setImageBitmap(bitmap);

        }
    }
}