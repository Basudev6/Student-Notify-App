package com.example.studentnotifyapp;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studentnotifyapp.CheckInternet.InternetReceiver;

public class BaseFragment extends Fragment {
    private boolean isReceiverRegistered = false;
    BroadcastReceiver broadcastReceiver=null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        broadcastReceiver = new InternetReceiver();
        internetStatus();
        super.onCreate(savedInstanceState);
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
