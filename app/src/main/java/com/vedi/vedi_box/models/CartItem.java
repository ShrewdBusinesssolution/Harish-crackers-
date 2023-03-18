package com.vedi.vedi_box.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CartItem implements Parcelable {

//    @SerializedName("product_id")
    private int product_id;

//    @SerializedName("product_name")
    private String product_name;

//    @SerializedName("product_imageurl")
    private String product_imageurl;

//    @SerializedName("product_category")
    private String product_category;

//    @SerializedName("product_subcategory")
    private String product_subcategory;

//    @SerializedName("product_mrp")
    private String product_mrp;

//    @SerializedName("product_additional_rate")
    private String product_additional_rate;

//    @SerializedName("product_rate")
    private int product_rate;

//    @SerializedName("product_pieces")
    private String product_pieces;

//    @SerializedName("product_quantity")
    private int product_quantity;

    private CartItem cartItem;

    public CartItem(int productid, String product_name, String product_imageurl, String product_category, String product_subcategory, String product_mrp, String product_additional_rate, int product_rate, String product_pieces, int product_quantity) {
        this.product_id = productid;
        this.product_name = product_name;
        this.product_imageurl = product_imageurl;
        this.product_category = product_category;
        this.product_subcategory = product_subcategory;
        this.product_mrp = product_mrp;
        this.product_additional_rate = product_additional_rate;
        this.product_rate = product_rate;
        this.product_pieces = product_pieces;
        this.product_quantity = product_quantity;
    }

    public CartItem(int id, int product_quantity) {
        this.product_id = id;
        this.product_quantity = product_quantity;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_imageurl() {
        return product_imageurl;
    }

    public void setProduct_imageurl(String product_imageurl) {
        this.product_imageurl = product_imageurl;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getProduct_subcategory() {
        return product_subcategory;
    }

    public void setProduct_subcategory(String product_subcategory) {
        this.product_subcategory = product_subcategory;
    }

    public String getProduct_mrp() {
        return product_mrp;
    }

    public void setProduct_mrp(String product_mrp) {
        this.product_mrp = product_mrp;
    }

    public String getProduct_additional_rate() {
        return product_additional_rate;
    }

    public void setProduct_additional_rate(String product_additional_rate) {
        this.product_additional_rate = product_additional_rate;
    }

    public int getProduct_rate() {
        return product_rate;
    }

    public void setProduct_rate(int product_rate) {
        this.product_rate = product_rate;
    }

    public String getProduct_pieces() {
        return product_pieces;
    }

    public void setProduct_pieces(String product_pieces) {
        this.product_pieces = product_pieces;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }


    protected CartItem(Parcel in) {
        product_id = in.readInt();
        product_name = in.readString();
        product_imageurl = in.readString();
        product_category = in.readString();
        product_subcategory = in.readString();
        product_mrp = in.readString();
        product_additional_rate = in.readString();
        product_rate = in.readInt();
        product_pieces = in.readString();
        product_quantity = in.readInt();
        cartItem = in.readParcelable(CartItem.class.getClassLoader());
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(product_id);
        dest.writeString(product_name);
        dest.writeString(product_imageurl);
        dest.writeString(product_category);
        dest.writeString(product_subcategory);
        dest.writeString(product_mrp);
        dest.writeString(product_additional_rate);
        dest.writeInt(product_rate);
        dest.writeString(product_pieces);
        dest.writeInt(product_quantity);
        dest.writeParcelable(cartItem, flags);
    }
}
