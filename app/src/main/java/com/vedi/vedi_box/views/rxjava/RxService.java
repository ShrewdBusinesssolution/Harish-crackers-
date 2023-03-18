package com.vedi.vedi_box.views.rxjava;


import com.vedi.vedi_box.models.APIResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RxService {
    @GET("Home/product_view_api")
    Observable<APIResponse> getProducts();

    @FormUrlEncoded
    @POST("Home/viewcart_api")
    Observable<APIResponse> getCart(@Field("user_id") String user_id);
}
