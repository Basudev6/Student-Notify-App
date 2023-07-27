package com.example.studentnotifyapp.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.studentnotifyapp.R;
import com.github.chrisbanes.photoview.PhotoView;

public class FullImageView extends AppCompatActivity {


    private PhotoView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_view);

        imageView = findViewById(R.id.fullImage);

        String image = getIntent().getStringExtra("image");

        Glide.with(this).load(image).placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background).into(imageView);


    }
}