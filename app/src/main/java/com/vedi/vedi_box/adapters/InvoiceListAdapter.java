package com.vedi.vedi_box.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vedi.vedi_box.databinding.CartItemBinding;
import com.vedi.vedi_box.models.Invoice;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InvoiceListAdapter extends RecyclerView.Adapter<InvoiceListAdapter.CartViewHolder> {
    private final List<Invoice> mlist;
    private final Context mcontext;
    private final LayoutInflater minflater;


    public InvoiceListAdapter(Context context, List<Invoice> list) {
        mcontext = context;
        mlist = list;
        minflater = LayoutInflater.from(context);
    }


    @NonNull
    @NotNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        CartItemBinding cartItemBinding = CartItemBinding.inflate(minflater, parent, false);
        return new CartViewHolder(cartItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartViewHolder holder, int position) {

        Invoice product = mlist.get(position);

        int quantity = Integer.parseInt(product.getProduct_quantity());
        int rate = Integer.parseInt(product.getProduct_rate());

        holder.cartItemBinding.cartItemName.setText(product.getProduct_name());
        holder.cartItemBinding.cartItemActualPrice.setText("₹ " + product.getProduct_rate());
        holder.cartItemBinding.cartItemCalculatedPrice.setText("₹ " + rate * quantity);


        holder.cartItemBinding.cartItemQuantityLayout.setVisibility(View.GONE);
        holder.cartItemBinding.cartItemImageLayout.setVisibility(View.GONE);
        holder.cartItemBinding.cartItemTrackQuantity.setVisibility(View.VISIBLE);
        holder.cartItemBinding.cartItemTrackQuantity.setText(product.getProduct_quantity());
        holder.cartItemBinding.cartItemQuantity.setText("One Box ( " + product.getProduct_pieces() + " Pieces )");

    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }


    class CartViewHolder extends RecyclerView.ViewHolder {
        CartItemBinding cartItemBinding;

        public CartViewHolder(CartItemBinding binding) {
            super(binding.getRoot());
            this.cartItemBinding = binding;
        }
    }
}


