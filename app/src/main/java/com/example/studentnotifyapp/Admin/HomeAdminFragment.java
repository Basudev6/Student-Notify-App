package com.example.studentnotifyapp.Admin;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.studentnotifyapp.R;


public class HomeAdminFragment extends Fragment {


    public HomeAdminFragment() {
        // Required empty public constructor

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_admin, container, false);
    }

}