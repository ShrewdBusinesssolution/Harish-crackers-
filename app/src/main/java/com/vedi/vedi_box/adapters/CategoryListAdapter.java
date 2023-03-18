package com.vedi.vedi_box.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vedi.vedi_box.R;
import com.vedi.vedi_box.databinding.CategoryItemBinding;
import com.vedi.vedi_box.models.Product;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {
    ShopInterface shopInterface;
    private final List<Product> productList;
    private final Context mcontext;
    private final LayoutInflater minflater;
    private String Type;


    public CategoryListAdapter(Context context, List<Product> products, String type, ShopInterface shopInterface) {
        mcontext = context;
        productList = products;
        minflater = LayoutInflater.from(context);
        Type = type;
        this.shopInterface = shopInterface;
    }


    @NonNull
    @NotNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        CategoryItemBinding categoryItemBinding = CategoryItemBinding.inflate(minflater, parent, false);
        return new CategoryViewHolder(categoryItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryViewHolder holder, int position) {

        Product product = productList.get(position);
        holder.categoryItemBinding.categoryName.setText(product.getSubcategory_name());

        Glide.with(mcontext).onLowMemory();
        Glide.with(mcontext).asBitmap().load(product.getCategory_image_url()).apply(new RequestOptions().override(500, 500)).into(holder.categoryItemBinding.categoryImageview);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public interface ShopInterface {
        void onItemClick(Product product);

    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        CategoryItemBinding categoryItemBinding;

        public CategoryViewHolder(CategoryItemBinding binding) {
            super(binding.getRoot());
            this.categoryItemBinding = binding;

            this.categoryItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    shopInterface.onItemClick(productList.get(getAdapterPosition()));


                }
            });

        }
    }
}
