package com.vedi.vedi_box.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Invoice implements Parcelable {

//    @SerializedName("id")
    private int id;

//    @SerializedName("order_id")
    private String order_id;

//    @SerializedName("order_date")
    private String order_date;

//    @SerializedName("product_id")
    private String product_id;

//    @SerializedName("product_name")
    private String product_name;

//    @SerializedName("product_quantity")
    private String product_quantity;

//    @SerializedName("product_pieces")
    private String product_pieces;

//    @SerializedName("product_rate")
    private String product_rate;

//    @SerializedName("total_price")
    private String total_price;

//    @SerializedName("without_gst")
    private String without_gst;

//    @SerializedName("house_or_flat")
    private String house_or_flat;

//    @SerializedName("area_street")
    private String area_street;

//    @SerializedName("city")
    private String city;

//    @SerializedName("state")
    private String state;

//    @SerializedName("taluk")
    private String taluk;

//    @SerializedName("pincode")
    private String pincode;

//    @SerializedName("payment_method")
    private String payment_method;

    private String address;

    public Invoice(String order_id, String order_date, String product_name, String product_quantity, String product_pieces, String product_rate, String total_price, String payment_method, String address) {
        this.order_id = order_id;
        this.order_date = order_date;
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.product_pieces = product_pieces;
        this.product_rate = product_rate;
        this.total_price = total_price;
        this.payment_method = payment_method;
        this.address = address;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_pieces() {
        return product_pieces;
    }

    public void setProduct_pieces(String product_pieces) {
        this.product_pieces = product_pieces;
    }

    public String getProduct_rate() {
        return product_rate;
    }

    public void setProduct_rate(String product_rate) {
        this.product_rate = product_rate;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getWithout_gst() {
        return without_gst;
    }

    public void setWithout_gst(String without_gst) {
        this.without_gst = without_gst;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    protected Invoice(Parcel in) {
        id = in.readInt();
        order_id = in.readString();
        order_date = in.readString();
        product_id = in.readString();
        product_name = in.readString();
        product_quantity = in.readString();
        product_pieces = in.readString();
        product_rate = in.readString();
        total_price = in.readString();
        without_gst = in.readString();
        house_or_flat = in.readString();
        area_street = in.readString();
        city = in.readString();
        state = in.readString();
        taluk = in.readString();
        pincode = in.readString();
        payment_method = in.readString();
        address = in.readString();
    }

    public static final Creator<Invoice> CREATOR = new Creator<Invoice>() {
        @Override
        public Invoice createFromParcel(Parcel in) {
            return new Invoice(in);
        }

        @Override
        public Invoice[] newArray(int size) {
            return new Invoice[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(order_id);
        dest.writeString(order_date);
        dest.writeString(product_id);
        dest.writeString(product_name);
        dest.writeString(product_quantity);
        dest.writeString(product_pieces);
        dest.writeString(product_rate);
        dest.writeString(total_price);
        dest.writeString(without_gst);
        dest.writeString(house_or_flat);
        dest.writeString(area_street);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(taluk);
        dest.writeString(pincode);
        dest.writeString(payment_method);
        dest.writeString(address);
    }
}
