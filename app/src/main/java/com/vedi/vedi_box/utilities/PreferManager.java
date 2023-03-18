package com.vedi.vedi_box.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vedi.vedi_box.models.CartItem;
import com.vedi.vedi_box.models.Product;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PreferManager {
    private static final String ProductList = "ProductList";
    private static final String CartList = "CartList";
    private static final String TrackList = "TrackList";
    private final SharedPreferences sharedPreferences;

    public PreferManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static void ProductWriteInPref(Context context, List<Product> list) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);

        SharedPreferences pref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ProductList, jsonString);
        editor.apply();
    }

    public static List<Product> ProductReadList(Context context) {
        SharedPreferences pref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString(ProductList, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Product>>() {
        }.getType();
        List<Product> list = gson.fromJson(jsonString, type);
        return list;
    }

    public static void CartWriteInPref(Context context, List<CartItem> list) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);

        SharedPreferences pref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(CartList, jsonString);
        editor.apply();
    }

    public static List<CartItem> CartReadList(Context context) {
        SharedPreferences pref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString(CartList, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CartItem>>() {
        }.getType();
        List<CartItem> list = gson.fromJson(jsonString, type);
        return list;
    }

    public static void ProductClear(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(ProductList);
        editor.commit();
    }

    public static void CartClear(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(CartList);
        editor.commit();
    }

    public static void TrackWriteInPref(Context context, List<Product> list) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);

        SharedPreferences pref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(TrackList, jsonString);
        editor.apply();
    }

    public static List<Product> TrackReadList(Context context) {
        SharedPreferences pref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString(TrackList, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Product>>() {
        }.getType();
        List<Product> list = gson.fromJson(jsonString, type);
        return list;
    }


    public static void TrackClear(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(TrackList);
        editor.commit();
    }


    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {

        return sharedPreferences.getString(key, null);
    }

    public void clearPreference() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void clearUserId() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.KEY_USER_ID);
        editor.commit();
    }


}
