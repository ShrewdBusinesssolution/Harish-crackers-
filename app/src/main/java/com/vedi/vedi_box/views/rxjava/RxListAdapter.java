package com.vedi.vedi_box.views.rxjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vedi.vedi_box.adapters.ShopListAdapter;
import com.vedi.vedi_box.databinding.ShopRowBinding;
import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.Product;

import java.util.ArrayList;
import java.util.List;


public class RxListAdapter extends RecyclerView.Adapter<RxListAdapter.RxViewHolder> {

    private List<Product> getRx = new ArrayList<>();
    private final Context mcontext;
    private final LayoutInflater minflater;

    public RxListAdapter(Context context, List<Product> products) {
        mcontext = context;
        minflater = LayoutInflater.from(context);
        getRx = products;

    }

    @NonNull
    @Override
    public RxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ShopRowBinding shopRowBinding = ShopRowBinding.inflate(minflater, parent, false);
        return new RxListAdapter.RxViewHolder(shopRowBinding);
    }

    @Override
    public int getItemCount() {
        return getRx.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RxViewHolder holder, int position) {
        Product product = getRx.get(position);

        holder.shopRowBinding.productName.setText(product.getProduct_name());
        holder.shopRowBinding.productPrice.setText("₹ " + product.getProduct_rate());
        Glide.with(mcontext).onLowMemory();
        Glide.with(mcontext).asBitmap().load(product.getProduct_imageurl()).apply(new RequestOptions().override(600, 200)).into(holder.shopRowBinding.productImageview);

        if (!product.getProduct_stock().equals("")) {
            holder.shopRowBinding.productAdd.setVisibility(View.GONE);
            holder.shopRowBinding.productOutStock.setVisibility(View.VISIBLE);
        } else {
            holder.shopRowBinding.productOutStock.setVisibility(View.GONE);
            holder.shopRowBinding.productAdd.setVisibility(View.VISIBLE);
        }


    }

    public static class RxViewHolder extends RecyclerView.ViewHolder {
        ShopRowBinding shopRowBinding;

        public RxViewHolder(ShopRowBinding binding) {
            super(binding.getRoot());
            this.shopRowBinding = binding;
        }
    }
}
