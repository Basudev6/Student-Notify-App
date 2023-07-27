package com.example.studentnotifyapp.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.studentnotifyapp.MainActivity;
import com.example.studentnotifyapp.R;
import com.example.studentnotifyapp.Student.HomeStudentFragment;
import com.example.studentnotifyapp.Student.StudentPage;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class NotificationService extends com.google.firebase.messaging.FirebaseMessagingService{

    private final String CHANNEL_ID ="Channel_Id";


    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Intent intent = new Intent(this, StudentPage.class);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(manager);
        }


        PendingIntent intent1 = PendingIntent.getActivities(this, 0, new Intent[]{intent}, PendingIntent.FLAG_ONE_SHOT);

        Notification notification;

            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(message.getData().get("title"))
                    .setSmallIcon(R.drawable.luncher_img)
                    .setAutoCancel(true)
                    .setContentIntent(intent1)
                    .build();

        manager.notify(notificationId, notification);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager manager) {

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"channelName",NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("My description");
        channel.enableLights(true);


        manager.createNotificationChannel(channel);
    }
}
