package com.example.t_edits_app_tobias;

public class User {

    public String fullname, phoneNo, email, userType;

    public User() {
    }

    public User(String fullname, String phoneNo, String email, String userType) {
        this.fullname = fullname;
        this.phoneNo = phoneNo;
        this.email = email;
        this.userType = userType;

    }

    public User(String fullname, String phoneNo) {
        this.fullname = fullname;
        this.phoneNo = phoneNo;
    }
}
