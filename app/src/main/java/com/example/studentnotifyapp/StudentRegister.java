package com.example.studentnotifyapp;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentRegister extends BaseAcitvity {

    EditText fullname,username, address, phone, password,cpassword;
    Button signup;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        fullname = findViewById(R.id.stu_fullname);
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

                        if(username.getText().toString().trim().isEmpty())
                        {
                            Toast.makeText(StudentRegister.this, "All field must be filled", Toast.LENGTH_SHORT).show();
                        }
                        else if(snapshot.hasChild(username.getText().toString().trim()))
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
        String StudentFullname = fullname.getText().toString().trim();
        String StudentName = username.getText().toString().trim();
        String StudentAddress = address.getText().toString();
        String StudentPhone = phone.getText().toString();
        String StudentPassword = password.getText().toString();
        String StudentCPassword = cpassword.getText().toString();

        if(StudentFullname.isEmpty() || StudentName.isEmpty() || StudentAddress.isEmpty() || StudentPassword.isEmpty())
        {
            Toast.makeText(StudentRegister.this, "All field must be filled", Toast.LENGTH_SHORT).show();
        }
        else if (StudentPhone.length()!=10) {
            phone.setError("Invalid password");
        }
        else if(StudentPassword.length()<8)
        {
            password.setError("Password must be at least 8 character");
        }

        else if (password.getText().toString().equals(cpassword.getText().toString())) {
            String StuHashPass;
            try {
                StuHashPass = hashPassword(StudentPassword);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            StudentData helperClass = new StudentData(StudentFullname, StudentName, StudentAddress, StudentPhone, StuHashPass,false);
            reference.child(StudentName).setValue(helperClass);
            Toast.makeText(StudentRegister.this, "Student Registered Successfully. Wait for admin to approve.", Toast.LENGTH_SHORT).show();
            finish();

        }
        else {
            Toast.makeText(StudentRegister.this, "Password and Confirm Password must be same", Toast.LENGTH_SHORT).show();
        }
    }
    public String hashPassword(String pass) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(pass.getBytes());
        BigInteger bigInt = new BigInteger(1,messageDigest);
        return bigInt.toString(16);
    }
}