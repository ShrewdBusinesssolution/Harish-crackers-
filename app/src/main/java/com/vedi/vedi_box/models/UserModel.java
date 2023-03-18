package com.vedi.vedi_box.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserModel implements Parcelable {

//    @SerializedName("user_id")
    private String user_id;

//    @SerializedName("user_Mobile")
    private String user_Mobile;

//    @SerializedName("user_name")
    private String user_name;

//    @SerializedName("user_email")
    private String user_email;

//    @SerializedName("house_or_flat")
    private String house_or_flat;

//    @SerializedName("area_street")
    private String area_street;

//    @SerializedName("state")
    private String state;

//    @SerializedName("city")
    private String city;

//    @SerializedName("taluk")
    private String taluk;

//    @SerializedName("pincode")
    private String pincode;

    private String address;


    public UserModel(String user_Mobile, String user_name, String user_email, String address) {
        this.user_Mobile = user_Mobile;
        this.user_name = user_name;
        this.user_email = user_email;
        this.address = address;
    }



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_Mobile() {
        return user_Mobile;
    }

    public void setUser_Mobile(String user_Mobile) {
        this.user_Mobile = user_Mobile;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getHouse_or_flat() {
        return house_or_flat;
    }

    public void setHouse_or_flat(String house_or_flat) {
        this.house_or_flat = house_or_flat;
    }

    public String getArea_street() {
        return area_street;
    }

    public void setArea_street(String area_street) {
        this.area_street = area_street;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTaluk() {
        return taluk;
    }

    public void setTaluk(String taluk) {
        this.taluk = taluk;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    protected UserModel(Parcel in) {
        user_id = in.readString();
        user_Mobile = in.readString();
        user_name = in.readString();
        user_email = in.readString();
        house_or_flat = in.readString();
        area_street = in.readString();
        state = in.readString();
        city = in.readString();
        taluk = in.readString();
        pincode = in.readString();
        address = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(user_Mobile);
        dest.writeString(user_name);
        dest.writeString(user_email);
        dest.writeString(house_or_flat);
        dest.writeString(area_street);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(taluk);
        dest.writeString(pincode);
        dest.writeString(address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
}
