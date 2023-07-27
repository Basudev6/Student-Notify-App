package com.example.studentnotifyapp.Student;

public class ImageNoticeData {

    public ImageNoticeData() {
    }

    String title,description, image, date, time;

    public ImageNoticeData(String title,String description, String image, String date, String time) {
        this.description = description;
        this.title = title;
        this.image = image;
        this.date = date;
        this.time = time;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description=description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

}
