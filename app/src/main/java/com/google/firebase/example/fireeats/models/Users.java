package com.google.firebase.example.fireeats.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {
    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };
    String name, mail, desg;

    protected Users(Parcel in) {
        name = in.readString();
        mail = in.readString();
        desg = in.readString();
    }

    public Users(String name, String mail, String desg) {

        this.name = name;
        this.mail = mail;
        this.desg = desg;
    }

    public Users() {
        this.name = name;
        this.mail = mail;
        this.desg = desg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDesg() {
        return desg;
    }

    public void setDesg(String desg) {
        this.desg = desg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(mail);
        parcel.writeString(desg);
    }
}
