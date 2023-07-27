package com.example.studentnotifyapp.CheckInternet;

import static android.view.View.VISIBLE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.studentnotifyapp.R;
import com.google.android.material.snackbar.Snackbar;

public class InternetReceiver extends BroadcastReceiver {

    private Dialog dialog;
    private boolean isDialogShowing = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        String status = CheckInternet.getNetworkInfo(context);
        if (status.equals("connected")) {

            if (isDialogShowing && dialog != null) {
                dialog.dismiss();
                isDialogShowing = false;
                dialog = null;

            }

        } else if (status.equals("disconnected")) {

            showNoInternetDialog(context);
            isDialogShowing = true;
        }

    }

    private void showNoInternetDialog(Context context) {

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {

                dialog = new Dialog(context);
                dialog.setContentView(R.layout.no_internet_layout);
                dialog.setCancelable(false);
                dialog.show();


                Button btnDismiss = dialog.findViewById(R.id.btn_Dismiss);
                btnDismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            isDialogShowing = false;
                            dialog=null;
                        }
                    }
                });

            }

        }
    }
}
