package com.vedi.vedi_box.adapters.testing;

import com.vedi.vedi_box.models.APIResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TestInterface {

    @GET("Home/product_api")
    Call<APIResponse> getAllProduct();
}
