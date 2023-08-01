package com.example.studentnotifyapp.Admin;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentnotifyapp.BaseAcitvity;
import com.example.studentnotifyapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SendPdf extends BaseAcitvity {

    private static final int REQ_CODE = 201;
    private CardView addPdf;
    private EditText pdfTitle;
    private TextView pdfTextView;
    private Button sendPdfBtn;

    private String pdfName,title;

    private final int REQ=1;
    private Uri pdfData;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    String downloadUrl ="";
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_pdf);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        pdfTitle = findViewById(R.id.pdf_title);
        addPdf = findViewById(R.id.select_pdf);
        pdfTextView = findViewById(R.id.selected_pdf);
        sendPdfBtn = findViewById(R.id.btn_pdf);

        addPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission()){
                    openStorage();
                }
                else {
                    ActivityCompat.requestPermissions(SendPdf.this,new String[]{READ_EXTERNAL_STORAGE},REQ_CODE);
                }
            }
        });

        sendPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = pdfTitle.getText().toString().trim();
                if(title.isEmpty())
                {
                    pdfTitle.setError("This field must be filled");
                    pdfTitle.requestFocus();
                }
                else if(pdfData==null)
                {
                    Toast.makeText(SendPdf.this, "Please select pdf", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadPdf();
                }

            }
        });

    }
    private void uploadPdf()
    {
        pd.setTitle("Please wait...");
        pd.setMessage("Sending pdf");
        pd.show();
        StorageReference reference = storageReference.child("pdf/"+ pdfName+"-"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri uri = uriTask.getResult();
                uploadData(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(SendPdf.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadData(String downloadUrl) {
        String uniqueKey = databaseReference.child("pdf").push().getKey();

        HashMap data = new HashMap();
        data.put("pdfTitle",title);
        data.put("pdfUrl",downloadUrl);

        databaseReference.child("pdf").child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(SendPdf.this, "pdf sent successfully", Toast.LENGTH_SHORT).show();
                pdfTitle.setText(null);
                pdfData = null;
                pdfTextView.setText("No file selected");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(SendPdf.this, "Failed to upload pdf", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openStorage()
    {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pdf file"),REQ);

    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode==RESULT_OK)
        {
            pdfData = data.getData();

            if(pdfData.toString().startsWith("content://"))
            {
                Cursor cursor = null;
                try {

                    cursor = SendPdf.this.getContentResolver().query(pdfData,null,null,null);
                    if(cursor != null && cursor.moveToFirst())
                    {
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
            else if(pdfData.toString().startsWith("file://"))
            {
                pdfName = new File(pdfData.toString()).getName();
            }

            pdfTextView.setText(pdfName);

        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQ_CODE){
            if(grantResults.length>0){
                int storage = grantResults[0];

                boolean checkStorage = storage == PackageManager.PERMISSION_GRANTED;

                if(checkStorage){
                    openStorage();
                }
                else{
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.layout_send_pdf),"This permission in required to select pdf from your device",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }

        }

    }

    public boolean checkPermission()
    {
        int result = ActivityCompat.checkSelfPermission(this,READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}