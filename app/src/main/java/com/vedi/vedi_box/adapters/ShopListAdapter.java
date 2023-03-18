package com.vedi.vedi_box.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vedi.vedi_box.databinding.ShopRowBinding;
import com.vedi.vedi_box.models.CartItem;
import com.vedi.vedi_box.models.Product;
import com.vedi.vedi_box.utilities.PreferManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ShopViewHolder> implements Filterable {
    ShopInterface shopInterface;
    private List<Product> productList;
    public List<Product> mproductList;
    private List<CartItem> cartList;
    private final Context mcontext;
    private final LayoutInflater minflater;



    public ShopListAdapter(Context context, List<Product> products, ShopInterface shopInterface) {
        mcontext = context;
        productList = products;
        mproductList = productList;
        minflater = LayoutInflater.from(context);
        this.shopInterface = shopInterface;

        cartList = PreferManager.CartReadList(mcontext);
        if (cartList == null) {
            cartList = new ArrayList<>();
            cartList.clear();
        }
    }
    public void getRefreshData() {
        cartList = PreferManager.CartReadList(mcontext);
        if (cartList == null) {
            cartList = new ArrayList<>();
            cartList.clear();
        }
        notifyDataSetChanged();
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
        Glide.with(mcontext).asBitmap().load(product.getProduct_imageurl()).apply(new RequestOptions().override(600, 200)).into(holder.shopRowBinding.productImageview);

        if (!product.getProduct_stock().equals("")){
            holder.shopRowBinding.productAdd.setVisibility(View.GONE);
            holder.shopRowBinding.productOutStock.setVisibility(View.VISIBLE);
        }else {
            holder.shopRowBinding.productOutStock.setVisibility(View.GONE);
            holder.shopRowBinding.productAdd.setVisibility(View.VISIBLE);
        }

        Log.e("ShopAdapter","DATA:::"+cartList.size());

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

//        Shimmer shimmer=new Shimmer.ColorHighlightBuilder()
//                .setBaseColor(Color.parseColor("#F3F3F3"))
//                .setBaseAlpha(1)
//                .setHighlightColor(Color.parseColor("#E7E7E7"))
//                .setHighlightAlpha(1)
//                .setDropoff(50)
//                .build();
//
//        ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
//        shimmerDrawable.setShimmer(shimmer);


    }



    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = mproductList.size();
                    filterResults.values = mproductList;
                } else {
                    String search = constraint.toString().toLowerCase();
                    List<Product> resultsdata = new ArrayList<>();
                    for (Product model : mproductList) {
                        if (model.getProduct_name().toLowerCase().contains(search)) {
                            resultsdata.add(model);
                        }
                    }
                    filterResults.count = resultsdata.size();
                    filterResults.values = resultsdata;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productList = (List<Product>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    @Override
    public int getItemCount() {
        if (mproductList.size() > 10) {
            return 10;
        }
        return mproductList.size();
    }

    public void addProducts(List<Product> products) {
        productList.addAll(products);
        notifyDataSetChanged();
    }

    public interface ShopInterface {
        void onItemClick(Product product);

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
                    shopInterface.onItemClick(productList.get(getAdapterPosition()));
                }
            });
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
