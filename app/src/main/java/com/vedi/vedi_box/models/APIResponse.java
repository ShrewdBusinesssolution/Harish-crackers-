package com.vedi.vedi_box.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class APIResponse implements Parcelable {

//    @SerializedName("success")
    private String success;

    private String link;

//    @SerializedName("user_id")
    private String user_id;

//    @SerializedName("minimum_purchase")
    private String minimum_purchase;

//    @SerializedName("delivery_cost")
    private String delivery_cost;

//    @SerializedName("total_orders")
    private String total_orders;

//    @SerializedName("purchase_amount")
    private String purchase_amount;


    private Product[] all_products;
    private Product[] search_products;
    private Product[] banner;
    private Product[] product_category;
    private CartItem[] products;
    private UserModel[] user_details;
    private Bank[] bank_details;
    private Order[] my_orders;
    private Product[] order_details;
    private Notify[] notification_details;
    private Notify[] version;

    public APIResponse(String total_orders, String purchase_amount) {
        this.total_orders = total_orders;
        this.purchase_amount = purchase_amount;
    }


    protected APIResponse(Parcel in) {
        success = in.readString();
        link = in.readString();
        user_id = in.readString();
        minimum_purchase = in.readString();
        delivery_cost = in.readString();
        total_orders = in.readString();
        purchase_amount = in.readString();
        all_products = in.createTypedArray(Product.CREATOR);
        search_products = in.createTypedArray(Product.CREATOR);
        banner = in.createTypedArray(Product.CREATOR);
        product_category = in.createTypedArray(Product.CREATOR);
        products = in.createTypedArray(CartItem.CREATOR);
        user_details = in.createTypedArray(UserModel.CREATOR);
        bank_details = in.createTypedArray(Bank.CREATOR);
        my_orders = in.createTypedArray(Order.CREATOR);
        order_details = in.createTypedArray(Product.CREATOR);
        notification_details = in.createTypedArray(Notify.CREATOR);
        version = in.createTypedArray(Notify.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(success);
        dest.writeString(link);
        dest.writeString(user_id);
        dest.writeString(minimum_purchase);
        dest.writeString(delivery_cost);
        dest.writeString(total_orders);
        dest.writeString(purchase_amount);
        dest.writeTypedArray(all_products, flags);
        dest.writeTypedArray(search_products, flags);
        dest.writeTypedArray(banner, flags);
        dest.writeTypedArray(product_category, flags);
        dest.writeTypedArray(products, flags);
        dest.writeTypedArray(user_details, flags);
        dest.writeTypedArray(bank_details, flags);
        dest.writeTypedArray(my_orders, flags);
        dest.writeTypedArray(order_details, flags);
        dest.writeTypedArray(notification_details, flags);
        dest.writeTypedArray(version, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<APIResponse> CREATOR = new Creator<APIResponse>() {
        @Override
        public APIResponse createFromParcel(Parcel in) {
            return new APIResponse(in);
        }

        @Override
        public APIResponse[] newArray(int size) {
            return new APIResponse[size];
        }
    };

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMinimum_purchase() {
        return minimum_purchase;
    }

    public void setMinimum_purchase(String minimum_purchase) {
        this.minimum_purchase = minimum_purchase;
    }

    public String getDelivery_cost() {
        return delivery_cost;
    }

    public void setDelivery_cost(String delivery_cost) {
        this.delivery_cost = delivery_cost;
    }


    public String getTotal_orders() {
        return total_orders;
    }

    public void setTotal_orders(String total_orders) {
        this.total_orders = total_orders;
    }

    public String getPurchase_amount() {
        return purchase_amount;
    }

    public void setPurchase_amount(String purchase_amount) {
        this.purchase_amount = purchase_amount;
    }

    public Product[] getAll_products() {
        return all_products;
    }

    public void setAll_products(Product[] all_products) {
        this.all_products = all_products;
    }

    public Product[] getSearch_products() {
        return search_products;
    }

    public void setSearch_products(Product[] search_products) {
        this.search_products = search_products;
    }

    public Product[] getBanner() {
        return banner;
    }

    public void setBanner(Product[] banner) {
        this.banner = banner;
    }

    public Product[] getProduct_category() {
        return product_category;
    }

    public void setProduct_category(Product[] product_category) {
        this.product_category = product_category;
    }

    public CartItem[] getProducts() {
        return products;
    }

    public void setProducts(CartItem[] products) {
        this.products = products;
    }

    public UserModel[] getUser_details() {
        return user_details;
    }

    public void setUser_details(UserModel[] user_details) {
        this.user_details = user_details;
    }

    public Bank[] getBank_details() {
        return bank_details;
    }

    public void setBank_details(Bank[] bank_details) {
        this.bank_details = bank_details;
    }

    public Order[] getMy_orders() {
        return my_orders;
    }

    public void setMy_orders(Order[] my_orders) {
        this.my_orders = my_orders;
    }

    public Notify[] getNotification_details() {
        return notification_details;
    }

    public void setNotification_details(Notify[] notification_details) {
        this.notification_details = notification_details;
    }

    public Product[] getOrder_details() {
        return order_details;
    }

    public void setOrder_details(Product[] order_details) {
        this.order_details = order_details;
    }


    public Notify[] getVersion() {
        return version;
    }

    public void setVersion(Notify[] version) {
        this.version = version;
    }


}

