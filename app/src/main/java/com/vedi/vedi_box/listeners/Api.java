package com.vedi.vedi_box.listeners;

import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.InvoiceRespone;
import com.vedi.vedi_box.models.Order;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {
//    String BASE_URL = "http://www.vedibox.com/";
    String BASE_URL = "http://hirishcrackers.com/";

    @FormUrlEncoded
    @POST("home/login_api")
    Call<APIResponse> LoginIntoVB(@Field("user_mobile") String user_mobile, @Field("fcm_token") String fcm_token);

    @GET("Home/banner_view_api")
    Call<APIResponse> getBanners();

    @FormUrlEncoded
    @POST("Home/category_view_api")
    Call<APIResponse> getCategory(@Field("category_name") String category_name);


    @GET("Home/product_view_api")
    Call<APIResponse> getProducts();

    @GET("Home/version_api")
    Call<APIResponse> getVersion();

    @FormUrlEncoded
    @POST("home/search_api")
    Call<APIResponse> getSearch(@Field("name_search") String search);

    @FormUrlEncoded
    @POST("home/pagination_api")
    Call<APIResponse> getPaginationProducts(@Field("page_number") int page);

    @GET("Home/admin_bank_details_api")
    Call<APIResponse> getBankDetails();

    @FormUrlEncoded
    @POST("home/addtocart_api")
    Call<APIResponse> AddToCart(@Field("product_id") String product_id, @Field("product_quantity") String product_quantity, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("home/add_sub_api")
    Call<APIResponse> Add_Sub(@Field("product_id") String product_id, @Field("product_quantity") String product_quantity, @Field("product_quantity_type") String product_quantity_type, @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("Home/remove_cart_api")
    Call<APIResponse> RemoveCart(@Field("product_id") String product_id, @Field("product_quantity") String product_quantity, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Home/order_details_api")
    Call<Order> OrdersDetails(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("home/order_insert_api")
    Call<APIResponse> OrdersInsert(@Field("user_id") String user_id, @Field("order_id") String order_id, @Field("order_date") String order_date, @Field("expected_date") String expected_date, @Field("total_price") String total_price,@Field("user_name") String user_name,@Field("user_mobile") String user_mobile, @Field("payment_method") String payment_method, @Field("online_id") String online_id);

    @FormUrlEncoded
    @POST("home/order_view_api")
    Call<APIResponse> OrdersView(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("home/order_track_view")
    Call<APIResponse> OrderTrackView(@Field("user_id") String user_id, @Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("home/order_count_api")
    Call<APIResponse> OrderCount(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("home/order_track_view")
    Call<InvoiceRespone> InvoiceTrackView(@Field("user_id") String user_id, @Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("home/proof_upload")
    Call<APIResponse> ProofUpload(@Field("image") String image, @Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("Home/user_profile_api")
    Call<APIResponse> AddProfileDetails(@Field("user_id") String user_id, @Field("user_email") String user_email, @Field("house_or_flat") String house_or_flat, @Field("area_street") String area_street, @Field("state") String state, @Field("city") String city, @Field("taluk") String taluk, @Field("pincode") String pincode, @Field("user_name") String user_name);

    @FormUrlEncoded
    @POST("Home/user_profile_view_api")
    Call<APIResponse> ViewProfile(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Home/user_details_view_api")
    Call<APIResponse> ViewUserDetails(@Field("user_id") String user_id);

    @GET("home/notification_view_api")
    Call<APIResponse> getNotification();
}
