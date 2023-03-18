package com.vedi.vedi_box.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class InvoiceRespone implements Parcelable {

//    @SerializedName("success")
    private String success;

    private Invoice[] order_details;

    protected InvoiceRespone(Parcel in) {
        success = in.readString();
        order_details = in.createTypedArray(Invoice.CREATOR);
    }

    public static final Creator<InvoiceRespone> CREATOR = new Creator<InvoiceRespone>() {
        @Override
        public InvoiceRespone createFromParcel(Parcel in) {
            return new InvoiceRespone(in);
        }

        @Override
        public InvoiceRespone[] newArray(int size) {
            return new InvoiceRespone[size];
        }
    };

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Invoice[] getOrder_details() {
        return order_details;
    }

    public void setOrder_details(Invoice[] order_details) {
        this.order_details = order_details;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(success);
        dest.writeTypedArray(order_details, flags);
    }
}
