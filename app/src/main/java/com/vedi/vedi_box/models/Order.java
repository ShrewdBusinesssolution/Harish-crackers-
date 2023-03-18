package com.vedi.vedi_box.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Order implements Parcelable {
//    @SerializedName("order_id")
    private String order_id;

//    @SerializedName("order_date")
    private String order_date;

//    @SerializedName("expected_date")
    private String expected_date;

//    @SerializedName("order_status")
    private String order_status;

//    @SerializedName("payment_method")
    private String payment_method;

//    @SerializedName("proof_upload")
    private String proof_upload;

//    @SerializedName("total_price")
    private String total_price;

//    @SerializedName("item_count")
    private String item_count;

    private Product[] items;


    public Order(String order_id, String order_date, String expected_date, String total_price) {
        this.order_id = order_id;
        this.order_date = order_date;
        this.expected_date = expected_date;
        this.total_price = total_price;
    }



    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getExpected_date() {
        return expected_date;
    }

    public void setExpected_date(String expected_date) {
        this.expected_date = expected_date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getProof_upload() {
        return proof_upload;
    }

    public void setProof_upload(String proof_upload) {
        this.proof_upload = proof_upload;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getItem_count() {
        return item_count;
    }

    public void setItem_count(String item_count) {
        this.item_count = item_count;
    }

    public Product[] getItems() {
        return items;
    }

    public void setItems(Product[] items) {
        this.items = items;
    }


    protected Order(Parcel in) {
        order_id = in.readString();
        order_date = in.readString();
        expected_date = in.readString();
        order_status = in.readString();
        payment_method = in.readString();
        proof_upload = in.readString();
        total_price = in.readString();
        item_count = in.readString();
        items = in.createTypedArray(Product.CREATOR);
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(order_id);
        dest.writeString(order_date);
        dest.writeString(expected_date);
        dest.writeString(order_status);
        dest.writeString(payment_method);
        dest.writeString(proof_upload);
        dest.writeString(total_price);
        dest.writeString(item_count);
        dest.writeTypedArray(items, flags);
    }
}
