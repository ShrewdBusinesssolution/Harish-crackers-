package com.vedi.vedi_box.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vedi.vedi_box.databinding.CartItemBinding;
import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.CartItem;
import com.vedi.vedi_box.utilities.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.CartViewHolder> {
    CartInterface cartInterface;
    private final List<CartItem> mlist;
    private final Context mcontext;
    private final LayoutInflater minflater;


    public CartListAdapter(Context context, List<CartItem> list, CartInterface cartInterface) {
        mcontext = context;
        mlist = list;
        minflater = LayoutInflater.from(context);
        this.cartInterface = cartInterface;
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

        CartItem product = mlist.get(position);

        int quantity = product.getProduct_quantity() * product.getProduct_rate();

        holder.cartItemBinding.cartItemName.setText(product.getProduct_name());
        holder.cartItemBinding.cartItemActualPrice.setText("₹ " + product.getProduct_rate());
        holder.cartItemBinding.cartItemCalculatedPrice.setText("₹ " + quantity);
        holder.cartItemBinding.cartItemCount.setText(String.valueOf(product.getProduct_quantity()));
        holder.cartItemBinding.cartItemQuantity.setText("One Box ( " + product.getProduct_pieces() + " Pieces )");

        Glide.with(mcontext).onLowMemory();
        Glide.with(mcontext).load(product.getProduct_imageurl()).into(holder.cartItemBinding.cartItemImage);


    }

    public CartItem onItemDelete(int position, String userID) {
        CartItem cartItem = mlist.get(position);

        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().RemoveCart(String.valueOf(cartItem.getProduct_id()), "0", userID);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getSuccess() != null) {
                        notifyDataSetChanged();
                    }

                } else if (response.code() == 500) {
                    Toast.makeText(mcontext, "Internal Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
        return mlist.get(position);
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public interface CartInterface {
        void onItemAddClick(CartItem cartItem, int quantity);

        void onItemSubClick(CartItem cartItem, int quantity);

        void Remove_Cart(int position);

    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        CartItemBinding cartItemBinding;

        public CartViewHolder(CartItemBinding binding) {
            super(binding.getRoot());
            this.cartItemBinding = binding;
            cartItemBinding.cartItemAddCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(cartItemBinding.cartItemCount.getText().toString()) + 1;

                    if (quantity >= 1) {
                        cartItemBinding.cartItemCount.setText(String.valueOf(quantity));
                        cartInterface.onItemAddClick(mlist.get(getAdapterPosition()), quantity);
                    }
                }
            });
            cartItemBinding.cartItemSubCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(cartItemBinding.cartItemCount.getText().toString()) - 1;

                    if (quantity >= 1) {
                        cartItemBinding.cartItemCount.setText(String.valueOf(quantity));
                        cartInterface.onItemSubClick(mlist.get(getAdapterPosition()), quantity);
                    } else {
                        int position = getAdapterPosition();
                        cartInterface.Remove_Cart(position);
                    }


                }
            });


        }
    }
}


