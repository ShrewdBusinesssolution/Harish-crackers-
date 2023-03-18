package com.vedi.vedi_box.views.cart;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vedi.vedi_box.models.CartItem;
import com.vedi.vedi_box.repositories.CartRepo;

import java.util.List;

public class CartViewModel extends ViewModel {

    CartRepo cartRepo = new CartRepo();

    public LiveData<List<CartItem>> getCart(String userID, Context context) {
        return cartRepo.getCart(userID, context);
    }

    public LiveData<Integer> getTotalPrice() {
        return cartRepo.getTotalPrice();
    }

    public void removeItemFromCart(CartItem cartItem) {
        cartRepo.removeItemFromCart(cartItem);
    }
}

//   public void changeQuantity(int id, int quantity) {
//        cartRepo.changeQuantity(id,quantity);
//                }