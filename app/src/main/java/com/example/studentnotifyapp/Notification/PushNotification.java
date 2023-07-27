package com.example.studentnotifyapp.Notification;

public class PushNotification {
    private NotificationData data;
    private String condition;

    public PushNotification(NotificationData data, String condition) {
        this.data = data;
        this.condition = condition;
    }

    public NotificationData getData() {
        return data;
    }

    public void setData(NotificationData data) {
        this.data = data;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
