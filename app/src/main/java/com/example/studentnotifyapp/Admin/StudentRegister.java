package com.example.studentnotifyapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentnotifyapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentRegister extends AppCompatActivity {

    EditText username, address, phone, password,cpassword;
    Button signup;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        username = findViewById(R.id.stu_username);
        address = findViewById(R.id.stu_address);
        phone = findViewById(R.id.stu_Phone);
        password = findViewById(R.id.stu_password);
        cpassword = findViewById(R.id.stu_cpassword);
        signup = findViewById(R.id.register_submit);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseDatabase = FirebaseDatabase.getInstance();
                reference = firebaseDatabase.getReference("users");

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.hasChild(username.getText().toString().trim()))
                        {
                            Toast.makeText(StudentRegister.this, "Username already exists", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Signup();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        });



    }
    public void Signup()
    {
        String StudentName = username.getText().toString().trim();
        String StudentAddress = address.getText().toString();
        String StudentPhone = phone.getText().toString();
        String StudentPassword = password.getText().toString();
        String StudentCPassword = cpassword.getText().toString();

        if(StudentName.isEmpty() || StudentAddress.isEmpty() || StudentPhone.isEmpty() || StudentPassword.isEmpty())
        {
            Toast.makeText(StudentRegister.this, "All field must be filled", Toast.LENGTH_SHORT).show();
        }

        else if (password.getText().toString().equals(cpassword.getText().toString()))
        {
            Toast.makeText(StudentRegister.this, "Student Registered Sucessfully", Toast.LENGTH_SHORT).show();
            username.setText(null);
            address.setText(null);
            phone.setText(null);
            password.setText(null);
            cpassword.setText(null);
            HelperClass helperClass = new HelperClass(StudentAddress,StudentPhone,StudentPassword);
            reference.child(StudentName).setValue(helperClass);
        }
        else {
            Toast.makeText(StudentRegister.this, "Password and Confirm Password must be same", Toast.LENGTH_SHORT).show();
        }
    }
}