package com.example.studentnotifyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentnotifyapp.Admin.AdminPage;

public class Login extends AppCompatActivity {

    EditText txt_username,txt_password;
    Button btn_student,btn_admin;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = this.getSharedPreferences("login",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(sharedPreferences.getString("isLogin","false").equals("yes"))
        {
            openAdminDash();
        }

        init();

        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username =txt_username.getText().toString().trim();
                String password = txt_password.getText().toString();

                if(username.equals("admin") && password.equals("admin"))
                {
                    editor.putString("isLogin","yes");
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
                Toast.makeText(Login.this, "student button clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void init()
    {
       txt_username = findViewById(R.id.login_user);
       txt_password = findViewById(R.id.login_password);
       btn_student = findViewById(R.id.student_login);
       btn_admin = findViewById(R.id.admin_login);
    }

    public void openAdminDash()
    {
        Intent loginIntent = new Intent(getApplicationContext(),AdminPage.class);
        startActivity(loginIntent);
        finish();
    }
}