package com.example.studentnotifyapp.Admin;

public class HelperClass {
    String fullname,username, address, phone ,password;

    public HelperClass(String fullname, String username,String address, String phone, String password) {
        this.fullname=fullname;
        this.username = username;
        this.address = address;
        this.phone = phone;
        this.password = password;
    }

    public String getFullname(){
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HelperClass() {
    }
}
