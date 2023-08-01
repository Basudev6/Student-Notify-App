package com.example.studentnotifyapp;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentnotifyapp.Admin.AdminPage;
import com.example.studentnotifyapp.Student.StudentPage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class Login extends BaseAcitvity {


    EditText txt_username,txt_password;
    Button btn_student,btn_admin;
    TextView signup;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        sharedPreferences = this.getSharedPreferences("login",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(sharedPreferences.getString("isLogin","false").equals("yesAdmin"))
        {
            openAdminDash();
        }
        if(sharedPreferences.getString("isLogin","false").equals("yesStudent"))
        {
            openStudentDash();
        }

        init();



        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username =txt_username.getText().toString().trim();
                String password = txt_password.getText().toString();


                if(username.equals("admin") && password.equals("admin"))
                {
                    editor.putString("isLogin","yesAdmin");
                    editor.putString("username",username);
                    editor.commit();

                    openAdminDash();


                }
                else{
                    Toast.makeText(Login.this, "Incorrect Username and Password", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username =txt_username.getText().toString().trim();
                String password = txt_password.getText().toString();


                if(username.isEmpty()|| password.isEmpty())
                {
                    Toast.makeText(Login.this, "All field must be filled", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String username =txt_username.getText().toString().trim();
                            String password = txt_password.getText().toString();

                            String HashPassword;
                            try {
                               HashPassword = hashPassword(password);
                            } catch (NoSuchAlgorithmException e) {
                                throw new RuntimeException(e);
                            }
                            if (dataSnapshot.hasChild(username)) {

                                DataSnapshot userSnapshot = dataSnapshot.child(username);

                                Boolean status = userSnapshot.child("status").getValue(Boolean.class);
                                if(status)
                                {
                                    String passwordFromDB = userSnapshot.child("password").getValue(String.class);
                                    if (passwordFromDB.equals(HashPassword)) {
                                        editor.putString("isLogin","yesStudent");
                                        editor.putString("username",username);
                                        editor.commit();

                                        openStudentDash();
                                        FirebaseMessaging.getInstance().subscribeToTopic("studentnotifyapp");

                                    }
                                    else {

                                        Toast.makeText(Login.this, "Invalid username and password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(Login.this, "Admin has not approved you yet.Please retry after some time or contact to admin", Toast.LENGTH_SHORT).show();
                                }
                                 
                                
                            } 
                            else {
                                Toast.makeText(Login.this, "Invalid username and password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle any errors
                        }
                    });

                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(getApplicationContext(),StudentRegister.class);
                startActivity(signupIntent);
            }
        });
    }

    public void init()
    {
       txt_username = findViewById(R.id.login_user);
       txt_password = findViewById(R.id.login_password);
       btn_student = findViewById(R.id.student_login);
       btn_admin = findViewById(R.id.admin_login);
       signup = findViewById(R.id.txt_signup);
    }


    public void openAdminDash()
    {
        Intent loginIntent = new Intent(getApplicationContext(),AdminPage.class);
        startActivity(loginIntent);
        finish();
    }

    public void openStudentDash()
    {
        Intent stuLoginIntent = new Intent(getApplicationContext(), StudentPage.class);
        startActivity(stuLoginIntent);
        finish();
    }
    public String hashPassword(String pass) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(pass.getBytes());
        BigInteger bigInt = new BigInteger(1,messageDigest);
        return bigInt.toString(16);
    }

}