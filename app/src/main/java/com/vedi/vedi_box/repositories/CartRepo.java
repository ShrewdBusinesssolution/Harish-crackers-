package com.vedi.vedi_box.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.CartItem;
import com.vedi.vedi_box.utilities.Constants;
import com.vedi.vedi_box.utilities.PreferManager;
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

public class CartRepo {

    private static final String TAG = "CartRepo";
    public final CompositeDisposable disposable = new CompositeDisposable();

    private final MutableLiveData<List<CartItem>> mutableCart = new MutableLiveData<>();
    private final MutableLiveData<Integer> mutableTotalPrice = new MutableLiveData<>();
    private PreferManager preferManager;


    public LiveData<List<CartItem>> getCart(String userID, Context context) {
        initCart(userID, context);
        return mutableCart;
    }


    public void initCart(String userID, Context context) {

        preferManager = new PreferManager(context);

        RxClient.getInstance()
                .getCart(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<APIResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull APIResponse apiResponse) {
                        if (apiResponse.getSuccess() != null && apiResponse.getSuccess().equals("success")) {
                        List<CartItem> productList = new ArrayList(Arrays.asList(apiResponse.getProducts()));

                        mutableCart.setValue(productList);
                        calculateCartTotal();

                        preferManager.putString(Constants.KEY_MINIMUM_PURCHASE, apiResponse.getMinimum_purchase());
                        preferManager.putString(Constants.KEY_DELIVERY_COST, apiResponse.getDelivery_cost());

                        PreferManager.CartWriteInPref(context, productList);

                    } else {
                        List productList = new ArrayList();
                        mutableCart.setValue(productList);
                        PreferManager.CartClear(context);
                    }
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
//        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().getCart(userID);
//        call.enqueue(new Callback<APIResponse>() {
//            @Override
//            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
//                if (response.code() == 200) {
//                    APIResponse apiResponse = response.body();
//                    if (apiResponse.getSuccess() != null && apiResponse.getSuccess().equals("success")) {
//                        List<CartItem> productList = new ArrayList(Arrays.asList(apiResponse.getProducts()));
//
//                        mutableCart.setValue(productList);
//                        calculateCartTotal();
//
//                        preferManager.putString(Constants.KEY_MINIMUM_PURCHASE, apiResponse.getMinimum_purchase());
//                        preferManager.putString(Constants.KEY_DELIVERY_COST, apiResponse.getDelivery_cost());
//
//                        PreferManager.CartWriteInPref(context, productList);
//
//                    } else {
//                        List productList = new ArrayList();
//                        mutableCart.setValue(productList);
//                        PreferManager.CartClear(context);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<APIResponse> call, Throwable t) {
//
//            }
//        });

    }


    private void calculateCartTotal() {
        if (mutableCart.getValue() == null) return;

        int total = 0;
        List<CartItem> cartItemList = mutableCart.getValue();
        for (CartItem cartItem : cartItemList) {

            total += cartItem.getProduct_rate() * cartItem.getProduct_quantity();
        }
        mutableTotalPrice.setValue(total);
    }

    public LiveData<Integer> getTotalPrice() {
        if (mutableTotalPrice.getValue() == null) {
            mutableTotalPrice.setValue(0);
        }
        return mutableTotalPrice;
    }

    public void removeItemFromCart(CartItem cartItem) {
        if (mutableCart.getValue() == null) {
            return;
        }
        List<CartItem> cartItemList = new ArrayList<>(mutableCart.getValue());
        cartItemList.remove(cartItem);
        mutableCart.setValue(cartItemList);

        calculateCartTotal();
    }
}
//    public boolean addItemToCart(Product product) {
//        if (mutableCart.getValue() == null) {
//            initCart();
//        }
//        List<CartItem> cartItemList = new ArrayList<>(mutableCart.getValue());
//
//        for (CartItem cartItem : cartItemList) {
//            if (cartItem.getProduct().getId().equals(product.getId())) {
//                int index = cartItemList.indexOf(cartItem);
//                cartItem.setQuantity(cartItem.getQuantity() + 1);
//                cartItemList.set(index, cartItem);
//
//                mutableCart.setValue(cartItemList);
//
//                calculateCartTotal();
//
//                return true;
//            }
//        }
//
//        CartItem cartItem = new CartItem(product, 1);
//        cartItemList.add(cartItem);
//        mutableCart.setValue(cartItemList);
//
//        calculateCartTotal();
//
//        return true;
//    }

//    public void changeQuantity(int id, int quantity) {
//        if (mutableCart.getValue() == null) return;
//        List<CartItem> cartItemList = new ArrayList<>(mutableCart.getValue());
//
//        CartItem updatedItem = new CartItem(id, quantity);
//        cartItemList.set(id, updatedItem);
//
//        mutableCart.setValue(cartItemList);
//
//        calculateCartTotal();
//    }

