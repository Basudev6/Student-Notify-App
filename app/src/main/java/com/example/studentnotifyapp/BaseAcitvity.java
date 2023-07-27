package com.example.studentnotifyapp;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentnotifyapp.CheckInternet.InternetReceiver;

public class BaseAcitvity extends AppCompatActivity {

    private boolean isReceiverRegistered = false;
    BroadcastReceiver broadcastReceiver=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        broadcastReceiver = new InternetReceiver();
        internetStatus();
    }
    public void internetStatus()
    {
        if(!isReceiverRegistered)
        {
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            isReceiverRegistered = true;
        }
    }

    @Override
    protected void onPause() {
        if (isReceiverRegistered) {
            unregisterReceiver(broadcastReceiver);
            isReceiverRegistered = false;
        }
        super.onPause();
    }

}
