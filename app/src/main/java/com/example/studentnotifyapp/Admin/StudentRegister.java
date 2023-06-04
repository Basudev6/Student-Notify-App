package com.example.studentnotifyapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.studentnotifyapp.R;

public class StudentRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
    }
}