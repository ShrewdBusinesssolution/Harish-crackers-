package com.vedi.vedi_box.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Bank implements Parcelable {

//    @SerializedName("id")
    private int id;

//    @SerializedName("bank_name")
    private String bank_name;

//    @SerializedName("account_name")
    private String account_name;

//    @SerializedName("account_number")
    private String account_number;

//    @SerializedName("ifsc_number")
    private String ifsc_number;

//    @SerializedName("branch")
    private String branch;

//    @SerializedName("gpay_upi")
    private String gpay_upi;

//    @SerializedName("phonepay_upi")
    private String phonepay_upi;


    public Bank(String bank_name, String account_name, String account_number, String ifsc_number, String branch, String gpay_upi, String phonepay_upi) {
        this.bank_name = bank_name;
        this.account_name = account_name;
        this.account_number = account_number;
        this.ifsc_number = ifsc_number;
        this.branch = branch;
        this.gpay_upi = gpay_upi;
        this.phonepay_upi = phonepay_upi;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getIfsc_number() {
        return ifsc_number;
    }

    public void setIfsc_number(String ifsc_number) {
        this.ifsc_number = ifsc_number;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getGpay_upi() {
        return gpay_upi;
    }

    public void setGpay_upi(String gpay_upi) {
        this.gpay_upi = gpay_upi;
    }

    public String getPhonepay_upi() {
        return phonepay_upi;
    }

    public void setPhonepay_upi(String phonepay_upi) {
        this.phonepay_upi = phonepay_upi;
    }


    protected Bank(Parcel in) {
        id = in.readInt();
        bank_name = in.readString();
        account_name = in.readString();
        account_number = in.readString();
        ifsc_number = in.readString();
        branch = in.readString();
        gpay_upi = in.readString();
        phonepay_upi = in.readString();
    }

    public static final Creator<Bank> CREATOR = new Creator<Bank>() {
        @Override
        public Bank createFromParcel(Parcel in) {
            return new Bank(in);
        }

        @Override
        public Bank[] newArray(int size) {
            return new Bank[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(bank_name);
        dest.writeString(account_name);
        dest.writeString(account_number);
        dest.writeString(ifsc_number);
        dest.writeString(branch);
        dest.writeString(gpay_upi);
        dest.writeString(phonepay_upi);
    }
}
