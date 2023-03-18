package com.vedi.vedi_box.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import java.util.Comparator;

public class Product implements Parcelable {
//    @SerializedName("id")
    private int id;

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

    //Banner
//    @SerializedName("banner_imageurl")
    private String banner_imageurl;

    //Category
//    @SerializedName("category_name")
    private String category_name;

//    @SerializedName("subcategory_name")
    private String subcategory_name;

//    @SerializedName("category_image_url")
    private String category_image_url;

    //Order_id
//    @SerializedName("order_id")
    private String order_id;

//    @SerializedName("product_quantity")
    private String product_quantity;


    private String product_stock;

    public Product(int id, String product_name, String product_imageurl, String product_category, String product_subcategory, String product_mrp, String product_additional_rate, int product_rate, String product_pieces,String product_stock) {
        this.id = id;
        this.product_name = product_name;
        this.product_imageurl = product_imageurl;
        this.product_category = product_category;
        this.product_subcategory = product_subcategory;
        this.product_mrp = product_mrp;
        this.product_additional_rate = product_additional_rate;
        this.product_rate = product_rate;
        this.product_pieces = product_pieces;
        this.product_stock = product_stock;
    }

    public Product(String product_name, String product_image, int product_rate, String product_pieces) {
        this.product_name = product_name;
        this.product_imageurl = product_image;
        this.product_rate = product_rate;
        this.product_pieces = product_pieces;

    }

    public Product(int id, String order_id, String product_name, String product_quantity, String product_imageurl) {
        this.id = id;
        this.order_id = order_id;
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.product_imageurl = product_imageurl;
    }

    public Product(String order_id, String product_name, String product_imageurl, int product_rate, String product_quantity, String product_pieces) {
        this.order_id = order_id;
        this.product_name = product_name;
        this.product_imageurl = product_imageurl;
        this.product_rate = product_rate;
        this.product_quantity = product_quantity;
        this.product_pieces = product_pieces;

    }

    public static final Comparator<Product> LOW_TO_HIGH = new Comparator<Product>() {
        @Override
        public int compare(Product o1, Product o2) {
            return o1.getProduct_rate() - o2.getProduct_rate();
        }
    };
    public static final Comparator<Product> HIGH_TO_LOW = new Comparator<Product>() {
        @Override
        public int compare(Product o1, Product o2) {
            return o2.getProduct_rate() - o1.getProduct_rate();
        }
    };

    public Product(String product_subcategory) {
       this.product_subcategory=product_subcategory;
    }

    public Product(String subcategory_name, String subcategory_imageUrl) {
        this.subcategory_name=subcategory_name;
        this.category_image_url=subcategory_imageUrl;
    }

    protected Product(Parcel in) {
        id = in.readInt();
        product_name = in.readString();
        product_imageurl = in.readString();
        product_category = in.readString();
        product_subcategory = in.readString();
        product_mrp = in.readString();
        product_additional_rate = in.readString();
        product_rate = in.readInt();
        product_pieces = in.readString();
        banner_imageurl = in.readString();
        category_name = in.readString();
        subcategory_name = in.readString();
        category_image_url = in.readString();
        order_id = in.readString();
        product_quantity = in.readString();
        product_stock = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public Product() {

    }

    @BindingAdapter("android:productImage")
    public static void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView).onLowMemory();
        Glide.with(imageView).load(imageUrl).fitCenter().into(imageView);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getProduct_stock() {
        return product_stock;
    }

    public void setProduct_stock(String product_stock) {
        this.product_stock = product_stock;
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

    public String getBanner_imageurl() {
        return banner_imageurl;
    }

    public void setBanner_imageurl(String banner_imageurl) {
        this.banner_imageurl = banner_imageurl;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getSubcategory_name() {
        return subcategory_name;
    }

    public void setSubcategory_name(String subcategory_name) {
        this.subcategory_name = subcategory_name;
    }

    public String getCategory_image_url() {
        return category_image_url;
    }

    public void setCategory_image_url(String category_image_url) {
        this.category_image_url = category_image_url;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(product_name);
        dest.writeString(product_imageurl);
        dest.writeString(product_category);
        dest.writeString(product_subcategory);
        dest.writeString(product_mrp);
        dest.writeString(product_additional_rate);
        dest.writeInt(product_rate);
        dest.writeString(product_pieces);
        dest.writeString(banner_imageurl);
        dest.writeString(category_name);
        dest.writeString(subcategory_name);
        dest.writeString(category_image_url);
        dest.writeString(order_id);
        dest.writeString(product_quantity);
        dest.writeString(product_stock);
    }
}
