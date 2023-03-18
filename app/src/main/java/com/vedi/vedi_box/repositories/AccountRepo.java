package com.vedi.vedi_box.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.Bank;
import com.vedi.vedi_box.utilities.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountRepo {

    private static final String TAG = "AccountRepo";

    private final MutableLiveData<List<Bank>> mutableBank = new MutableLiveData<>();

    public LiveData<List<Bank>> getBank() {
        initBankDetails();
        return mutableBank;
    }

    private void initBankDetails() {
        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().getBankDetails();
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.code() == 200) {
                    APIResponse apiResponse = response.body();
                    if (apiResponse.getSuccess() != null && apiResponse.getSuccess().equals("success")) {
                        List<Bank> bankList = new ArrayList(Arrays.asList(apiResponse.getBank_details()));

                        mutableBank.setValue(bankList);
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }

}
