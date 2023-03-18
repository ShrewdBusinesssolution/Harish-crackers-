package com.vedi.vedi_box.views.rxjava;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.vedi.vedi_box.databinding.ActivityRxBinding;
import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.Product;
import com.vedi.vedi_box.utilities.LoadingProgress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Rx_Activity extends AppCompatActivity {
    private static final String TAG = Rx_Activity.class.getSimpleName();

    ActivityRxBinding binding;
    private RxListAdapter adapter;
    public final CompositeDisposable disposable = new CompositeDisposable();
    private LoadingProgress loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRxBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        final GridLayoutManager gridLayoutManager = new GridLayoutManager(Rx_Activity.this, 2);

        binding.rxRecycler.setLayoutManager(gridLayoutManager);
        binding.rxRecycler.setHasFixedSize(true);

        loadingProgress = LoadingProgress.getInstance();

        loadingProgress.ShowProgress(Rx_Activity.this, "Rx Loading", false);
        getRxData();
    }

    private void getRxData() {
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
                        loadingProgress.hideProgress();
                        List<Product> productList = new ArrayList(Arrays.asList(apiResponses.getAll_products()));


                        Log.d(TAG, "RxDATA:::" + productList.size());

                        adapter = new RxListAdapter(Rx_Activity.this, productList);
                        adapter.notifyDataSetChanged();
                        binding.rxRecycler.setAdapter(adapter);
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
    }
}