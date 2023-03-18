package com.vedi.vedi_box.repositories;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.Product;
import com.vedi.vedi_box.utilities.RetrofitClient;
import com.vedi.vedi_box.views.rxjava.RxClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopRepo {

    private static final String TAG = "ShopRepo";
    public final CompositeDisposable disposable = new CompositeDisposable();

    public static MutableLiveData<List<Product>> mutablePaginationProductList;
    public static MutableLiveData<List<Product>> mutableProductList;
    public static MutableLiveData<List<Product>> mutableBannerList;
    public static MutableLiveData<List<Product>> mutableCategoryList;

    public LiveData<List<Product>> getBanner() {
        if (mutableBannerList == null) {
            mutableBannerList = new MutableLiveData<>();

            loadBanner();
        }

        return mutableBannerList;
    }

    public LiveData<List<Product>> getCategory(String category) {
        if (mutableCategoryList == null) {
            mutableCategoryList = new MutableLiveData<>();

            loadCategory(category);
        }
        return mutableCategoryList;
    }


    public LiveData<List<Product>> getProducts() {
        if (mutableProductList == null) {
            mutableProductList = new MutableLiveData<>();

            loadProducts();
        }

        return mutableProductList;
    }

    public LiveData<List<Product>> getPaginationProducts(int page, Activity activity) {
        mutablePaginationProductList = new MutableLiveData<>();

        loadPaginationProducts(page, activity);

        return mutablePaginationProductList;
    }


    private void loadBanner() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().getBanners();

                call.enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        if (response.code() == 200) {
                            APIResponse apiResponse = response.body();

                            if (response.body().getSuccess() != null && response.body().getSuccess().equals("success")) {

                                final List<Product> imageList = new ArrayList(Arrays.asList(apiResponse.getBanner()));

                                mutableBannerList.postValue(imageList);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {

                    }
                });
            }
        });
        thread.start();


    }

    private void loadProducts() {

        RxClient.getInstance()
                .getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<APIResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull APIResponse apiResponses) {

                        List<Product> productList = new ArrayList(Arrays.asList(apiResponses.getAll_products()));

                        mutableProductList.postValue(productList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "In onError" + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().getProducts();
//
//                call.enqueue(new Callback<APIResponse>() {
//                    @Override
//                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
//                        if (response.code() == 200) {
//                            APIResponse apiResponse = response.body();
//                            if (response.body().getSuccess() != null && response.body().getSuccess().equals("success")) {
//                                List<Product> productList = new ArrayList(Arrays.asList(apiResponse.getAll_products()));
//
//                                for (Product product : productList) {
//                                    if (product.getId() == 34)
//                                        Log.e(TAG, "STATUS::::" + product.getProduct_name());
//                                }
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<APIResponse> call, Throwable t) {
//
//                    }
//                });
//            }
//        });
//        thread.start();


    }

    private void loadPaginationProducts(int page, Activity activity) {
        RxClient.getInstance()
                .getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<APIResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull APIResponse apiResponses) {

                        List<Product> productList = new ArrayList(Arrays.asList(apiResponses.getAll_products()));

                        mutablePaginationProductList.postValue(productList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "In onError" + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().getPaginationProducts(page);
//                call.enqueue(new Callback<APIResponse>() {
//                    @Override
//                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
//                        if (response.code() == 200) {
//                            APIResponse apiResponse = response.body();
//                            if (response.body().getSuccess() != null && response.body().getSuccess().equals("success")) {
//
//                                List<Product> productList = new ArrayList(Arrays.asList(apiResponse.getAll_products()));
//
//
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<APIResponse> call, Throwable t) {
//
//                    }
//                });
//            }
//        });
//        thread.start();


    }

    private void loadCategory(String category) {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Product> cate = new ArrayList<>();
//                cate.add(new Product("All", "https://www.myruppi.com/vedibox/assets/images/jk/WhatsApp%20Image%202021-08-05%20at%2011.51.38%20PM.jpeg"));
                Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().getCategory(category);
                call.enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        if (response.code() == 200) {
                            APIResponse apiResponse = response.body();
                            if (response.body().getSuccess() != null && response.body().getSuccess().equals("success")) {

                                List<Product> categoryList = new ArrayList(Arrays.asList(apiResponse.getProduct_category()));
                                for (Product product : categoryList) {
                                    cate.add(new Product(product.getSubcategory_name(), product.getCategory_image_url()));
                                }
                                mutableCategoryList.postValue(cate);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {

                    }
                });
            }
        });
        thread.start();


    }

}
