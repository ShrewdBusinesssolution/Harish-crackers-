package com.vedi.vedi_box.adapters;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vedi.vedi_box.databinding.ShopRowBinding;
import com.vedi.vedi_box.models.CartItem;
import com.vedi.vedi_box.models.Product;
import com.vedi.vedi_box.utilities.PreferManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ShopViewHolder> {
    ShopInterface shopInterface;
    int Id;
    int positionSelected = 0;
    private final List<Product> productList;
    private final PreferManager preferManager;
    private List<CartItem> cartList;
    private final Context mcontext;
    private final LayoutInflater minflater;

    public ProductListAdapter(Context context, List<Product> products, int id, ShopInterface shopInterface) {
        mcontext = context;
        productList = products;
        preferManager = new PreferManager(mcontext);
        Id = id;
        minflater = LayoutInflater.from(context);
        this.shopInterface = shopInterface;
    }

    public void ItemChange(int position, int productId) {
        if (productList.get(position).getId() == productId) {
            positionSelected = productId;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @NotNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ShopRowBinding shopRowBinding = ShopRowBinding.inflate(minflater, parent, false);
        return new ShopViewHolder(shopRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ShopViewHolder holder, int position) {

        Product product = productList.get(position);
        holder.shopRowBinding.productName.setText(product.getProduct_name());
        holder.shopRowBinding.productPrice.setText("₹ " + product.getProduct_rate());
        Glide.with(mcontext).onLowMemory();
        Glide.with(mcontext).load(product.getProduct_imageurl()).into(holder.shopRowBinding.productImageview);

        if (!product.getProduct_stock().equals("")){
            holder.shopRowBinding.productAdd.setVisibility(View.GONE);
            holder.shopRowBinding.productOutStock.setVisibility(View.VISIBLE);
        }else {
            holder.shopRowBinding.productOutStock.setVisibility(View.GONE);
            holder.shopRowBinding.productAdd.setVisibility(View.VISIBLE);
        }

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(200, 200);
        imageParams.setMargins(10, 10, 10, 10);
        imageParams.gravity = Gravity.CENTER;
        holder.shopRowBinding.productImageview.setLayoutParams(imageParams);


        if (String.valueOf(Id) != null && Id == product.getId() || product.getId() == positionSelected) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = 0;
            layoutParams.width = 0;
            holder.itemView.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.width = 450;
            holder.itemView.setLayoutParams(layoutParams);
        }

        cartList = PreferManager.CartReadList(mcontext);
        if (cartList == null) {
            cartList = new ArrayList<>();
            cartList.clear();
        }
        for (int i = 0; i < cartList.size(); i++) {

            CartItem cartItem = cartList.get(i);

            if (cartItem.getProduct_id() == product.getId()) {
                holder.shopRowBinding.productQuantityCount.setText(String.valueOf(cartItem.getProduct_quantity()));
                if (cartItem.getProduct_quantity() != 0) {
                    holder.shopRowBinding.productAdd.setVisibility(View.GONE);
                    holder.shopRowBinding.productQuantityLayout.setVisibility(View.VISIBLE);
                } else {
                    holder.shopRowBinding.productQuantityLayout.setVisibility(View.GONE);
                    holder.shopRowBinding.productAdd.setVisibility(View.VISIBLE);
                }

            }
        }


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public interface ShopInterface {
        void onItemClick(int position, Product product);

        void Add_to_Cart(Product product);

        void Add_Cart(Product product, int quantity);

        void Sub_Cart(Product product, int quantity);

        void Remove_Cart(Product product);
    }

    class ShopViewHolder extends RecyclerView.ViewHolder {
        ShopRowBinding shopRowBinding;

        public ShopViewHolder(ShopRowBinding binding) {
            super(binding.getRoot());
            this.shopRowBinding = binding;
            this.shopRowBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    shopInterface.onItemClick(position, productList.get(getAdapterPosition()));
                }
            });
            this.shopRowBinding.productName.setTextSize(10);
            this.shopRowBinding.productPrice.setTextSize(10);
            this.shopRowBinding.productQuantityCount.setTextSize(10);

            shopRowBinding.productAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopInterface.Add_to_Cart(productList.get(getAdapterPosition()));
                    shopRowBinding.productAdd.setVisibility(View.GONE);
                    shopRowBinding.productQuantityLayout.setVisibility(View.VISIBLE);
                }
            });
            shopRowBinding.productQuantityAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(shopRowBinding.productQuantityCount.getText().toString()) + 1;

                    if (quantity >= 1) {
                        shopRowBinding.productQuantityCount.setText(String.valueOf(quantity));
                        shopInterface.Add_Cart(productList.get(getAdapterPosition()), quantity);
                    }


                }
            });
            shopRowBinding.productQuantitySub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(shopRowBinding.productQuantityCount.getText().toString()) - 1;

                    if (quantity >= 1) {
                        shopRowBinding.productQuantityCount.setText(String.valueOf(quantity));
                        shopInterface.Sub_Cart(productList.get(getAdapterPosition()), quantity);
                    } else {
                        shopInterface.Remove_Cart(productList.get(getAdapterPosition()));

                        shopRowBinding.productQuantityCount.setText("1");
                        shopRowBinding.productQuantityLayout.setVisibility(View.GONE);
                        shopRowBinding.productAdd.setVisibility(View.VISIBLE);
                    }

                }
            });

        }
    }
}


