package com.vedi.vedi_box.views.shop;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vedi.vedi_box.models.Product;
import com.vedi.vedi_box.repositories.ShopRepo;

import java.util.List;

public class ShopViewModel extends ViewModel {

    ShopRepo shopRepo = new ShopRepo();

    public LiveData<List<Product>> getBanners() {
        return shopRepo.getBanner();
    }

    public LiveData<List<Product>> getCategory(String category) {
        return shopRepo.getCategory(category);
    }

    public LiveData<List<Product>> getProducts() {
        return shopRepo.getProducts();
    }

    public LiveData<List<Product>> getPaginationProducts(int page, Activity activity) {
        return shopRepo.getPaginationProducts(page,activity);
    }


}

//    public boolean addItemToCart(Product product) {
//        return cartRepo.addItemToCart(product);
//    }
//

//
//    public void changeQuantity(CartItem cartItem, int quantity) {
//        cartRepo.changeQuantity(cartItem, quantity);
//    }