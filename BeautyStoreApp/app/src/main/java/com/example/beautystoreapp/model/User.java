package com.example.beautystoreapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String user_ID;
    private String user_FName;
    private String user_LName;
    private String address;
    private String userEmail;
    private String userPassword;


    public User() {
        super();
        // TODO Auto-generated constructor stub
    }

    public User(String user_ID, String user_FName, String user_LName, String address, String userEmail, String userPassword) {
        this.user_ID = user_ID;
        this.user_FName = user_FName;
        this.user_LName = user_LName;
        this.address = address;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public String getUser_FName() {
        return user_FName;
    }

    public void setUser_FName(String user_FName) {
        this.user_FName = user_FName;
    }

    public String getUser_LName() {
        return user_LName;
    }

    public void setUser_LName(String user_LName) {
        this.user_LName = user_LName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_ID='" + user_ID + '\'' +
                ", user_FName='" + user_FName + '\'' +
                ", user_LName='" + user_LName + '\'' +
                ", address='" + address + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }

    protected User(Parcel in) {
        user_ID = in.readString();
        user_FName = in.readString();
        user_LName = in.readString();
        address = in.readString();
        userEmail = in.readString();
        userPassword = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_ID);
        dest.writeString(user_FName);
        dest.writeString(user_LName);
        dest.writeString(address);
        dest.writeString(userEmail);
        dest.writeString(userPassword);
    }

}
