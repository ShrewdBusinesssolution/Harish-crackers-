package com.vedi.vedi_box.repositories;

import android.app.Activity;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vedi.vedi_box.models.Invoice;
import com.vedi.vedi_box.models.InvoiceRespone;
import com.vedi.vedi_box.utilities.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceRepo {

    private static final String TAG = "InvoiceRepo";

    private final MutableLiveData<List<Invoice>> mutableTrackList = new MutableLiveData<>();

    public LiveData<List<Invoice>> getInvoiceList(Activity activity, String userID, String orderID) {
        getInvoiceTrackList(activity, userID, orderID);
        return mutableTrackList;
    }

    private void getInvoiceTrackList(Activity activity, String userID, String orderID) {

        Call<InvoiceRespone> call = RetrofitClient.getInstance().getMyApi().InvoiceTrackView(userID, orderID);
        call.enqueue(new Callback<InvoiceRespone>() {
            @Override
            public void onResponse(Call<InvoiceRespone> call, Response<InvoiceRespone> response) {
                if (response.code() == 200) {
                    InvoiceRespone invoiceRespone = response.body();
                    List<Invoice> trackList = new ArrayList(Arrays.asList(invoiceRespone.getOrder_details()));

                    mutableTrackList.setValue(trackList);
                } else if (response.code() == 500) {
                    Toast.makeText(activity, "Internal Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InvoiceRespone> call, Throwable t) {

            }
        });

    }
}
