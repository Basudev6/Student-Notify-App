package com.example.studentnotifyapp.Admin;

public class MessageData {
    String fullname,username,message,date,time;

    public MessageData(String fullname,String username, String message, String date, String time) {
        this.fullname = fullname;
        this.username = username;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getFullname() {

        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MessageData() {
    }
}
