package com.vedi.vedi_box.views.rxjava;

import androidx.annotation.NonNull;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vedi.vedi_box.models.APIResponse;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RxClient {
//    private static final String BASE_URL = "http://www.vedibox.com/";
    private static final String BASE_URL = "http://hirishcrackers.com/";

    private static RxClient instance;
    private RxService rxService;

    private RxClient() {
        final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .protocols(Util.immutableListOf(Protocol.HTTP_1_1))
//                .protocols( Collections.singletonList(Protocol.HTTP_1_1) )
                .retryOnConnectionFailure(true)
                .build();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        rxService = retrofit.create(RxService.class);
    }

    public static RxClient getInstance() {
        if (instance == null) {
            instance = new RxClient();
        }
        return instance;
    }
    public Observable<APIResponse> getAllProducts(){
        return rxService.getProducts();
    }
    public Observable<APIResponse> getCart(@NonNull String userId){
        return rxService.getCart(userId);
    }
}
