package com.example.studentnotifyapp.Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.studentnotifyapp.R;

public class Actions extends Fragment {



    public Actions() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_actions, container, false);

        CardView register = (CardView)v.findViewById(R.id.stu_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registerIntent = new Intent(getContext(),StudentRegister.class);
                startActivity(registerIntent);
            }
        });

        return v;
    }
}