package com.vedi.vedi_box.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Notify implements Parcelable {
//    @SerializedName("id")
    private String id;

//    @SerializedName("notification_title")
    private String notification_title;

//    @SerializedName("notification_body")
    private String notification_body;

//    @SerializedName("notification_date")
    private String notification_date;

//    @SerializedName("image_url")
    private String image_url;


    private String version;


    protected Notify(Parcel in) {
        id = in.readString();
        notification_title = in.readString();
        notification_body = in.readString();
        notification_date = in.readString();
        image_url = in.readString();
        version = in.readString();
    }

    public static final Creator<Notify> CREATOR = new Creator<Notify>() {
        @Override
        public Notify createFromParcel(Parcel in) {
            return new Notify(in);
        }

        @Override
        public Notify[] newArray(int size) {
            return new Notify[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public void setNotification_title(String notification_title) {
        this.notification_title = notification_title;
    }

    public String getNotification_body() {
        return notification_body;
    }

    public void setNotification_body(String notification_body) {
        this.notification_body = notification_body;
    }

    public String getNotification_date() {
        return notification_date;
    }

    public void setNotification_date(String notification_date) {
        this.notification_date = notification_date;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(notification_title);
        dest.writeString(notification_body);
        dest.writeString(notification_date);
        dest.writeString(image_url);
        dest.writeString(version);
    }
}
