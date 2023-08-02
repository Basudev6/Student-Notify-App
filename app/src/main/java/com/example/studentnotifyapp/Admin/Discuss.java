package com.example.studentnotifyapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.studentnotifyapp.BaseAcitvity;
import com.example.studentnotifyapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Discuss extends BaseAcitvity {

    EditText message;
    ImageView sendMessage;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    private RecyclerView msgRecycler;

    private List<MessageData> list;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);


        message = findViewById(R.id.message);
        sendMessage = findViewById(R.id.msg_send);

        msgRecycler = findViewById(R.id.messsageRecycler);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("discuss");

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message1 = message.getText().toString().trim();
                final String uniqueKey = reference.push().getKey();

                if (message1.isEmpty()) {
                    message.setError("This field is not filled");
                } else {
                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                    String date = currentDate.format(calForDate.getTime());

                    Calendar calForTime = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                    String time = currentTime.format(calForTime.getTime());

                    String username = getSharedPreferences("login", MODE_PRIVATE).getString("username", "");

                    String fullname = "Admin";
                    MessageData messageData = new MessageData(fullname, username, message1, date, time);
                    reference.child(uniqueKey).setValue(messageData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            message.setText(null);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Discuss.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
        getMessage();
    }

    public void getMessage() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageData data = dataSnapshot.getValue(MessageData.class);
                    list.add(data);
                }
                adapter = new MessageAdapter(getApplicationContext(), list);
                msgRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                msgRecycler.setAdapter(adapter);
                msgRecycler.scrollToPosition(list.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Discuss.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}