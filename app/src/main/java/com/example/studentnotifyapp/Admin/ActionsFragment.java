package com.example.studentnotifyapp.Admin;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.studentnotifyapp.CheckInternet.InternetReceiver;
import com.example.studentnotifyapp.R;

public class ActionsFragment extends Fragment {


    BroadcastReceiver broadcastReceiver=null;
    private boolean isReceiverRegistered = false;

    public ActionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_actions, container, false);

        broadcastReceiver = new InternetReceiver();
        internetStatus();

        CardView register = (CardView)v.findViewById(R.id.stu_register);
        CardView viewStudent = (CardView)v.findViewById(R.id.view_student);
        CardView noticeSend = (CardView)v.findViewById(R.id.send_notice);
        CardView discuss = (CardView)v.findViewById(R.id.admin_discuss);
        CardView pdfSend = (CardView)v.findViewById(R.id.send_pdf); 
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registerIntent = new Intent(getContext(),StudentRegister.class);
                startActivity(registerIntent);
            }
        });
        
        viewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent studentIntent = new Intent(getContext(),ViewStudent.class);
                startActivity(studentIntent);
            }
        });
        
        noticeSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent noticeIntent = new Intent(getContext(),SendNotice.class);
                startActivity(noticeIntent);
            }
        });

        discuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent discussIntent = new Intent(getContext(), Discuss.class);
                startActivity(discussIntent);

            }
        });

        pdfSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pdfIntent = new Intent(getContext(),SendPdf.class);
                startActivity(pdfIntent);
            }
        });



        return v;
    }
    public void internetStatus()
    {
        if(!isReceiverRegistered)
        {
            getActivity().registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            isReceiverRegistered = true;
        }
    }

    @Override
    public void onPause() {
        if (isReceiverRegistered) {
            getActivity().unregisterReceiver(broadcastReceiver);
            isReceiverRegistered = false;
        }
        super.onPause();
    }

}