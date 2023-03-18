package com.vedi.vedi_box.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vedi.vedi_box.R;
import com.vedi.vedi_box.databinding.OrderItemBinding;
import com.vedi.vedi_box.models.Order;
import com.vedi.vedi_box.models.Product;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {
    OrderInterface orderInterface;
    private final List<Order> orderList;
    private final List<Product> productList;
    private final Context mcontext;
    private final LayoutInflater minflater;


    public OrderListAdapter(Context context, List<Order> orders, List<Product> products, OrderInterface orderInterface) {
        mcontext = context;
        orderList = orders;
        productList = products;
        minflater = LayoutInflater.from(context);
        this.orderInterface = orderInterface;
    }


    @NonNull
    @NotNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        OrderItemBinding orderItemBinding = OrderItemBinding.inflate(minflater, parent, false);
        return new OrderViewHolder(orderItemBinding);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.orderItemBinding.orderItemId.setText(order.getOrder_id());
        holder.orderItemBinding.orderItemDate.setText("Ordered On " + order.getOrder_date());
        holder.orderItemBinding.orderItemProgressOrderDate.setText(order.getOrder_date());
        holder.orderItemBinding.orderItemProgressExpectedDate.setText(order.getExpected_date());
        holder.orderItemBinding.orderItemStatus.setText(order.getOrder_status());

        holder.orderItemBinding.orderItemPaymentDocument.setText(order.getProof_upload());

        if (order.getPayment_method().equals("manual")) {
            holder.orderItemBinding.orderItemPaymentMethod.setText("Manual");
        } else if (order.getPayment_method().equals("online")) {
            holder.orderItemBinding.orderItemPaymentMethod.setText("Online");
        }

        if (order.getPayment_method().equals("manual") && order.getOrder_status().equals("Completed")) {
            holder.orderItemBinding.orderItemProgress.setMax(1000);
            holder.orderItemBinding.orderItemProgress.setProgress(1000);

            holder.orderItemBinding.orderItemStatus.setTextColor(ContextCompat.getColor(mcontext, R.color.status_green));
            holder.orderItemBinding.orderItemUploadTitle.setText(order.getProof_upload());
            holder.orderItemBinding.orderItemUploadDescription.setVisibility(View.GONE);
            holder.orderItemBinding.orderItemUploadImage.setEnabled(false);


//            holder.orderItemBinding.orderItemUploadImage.setImageResource(R.drawable.check);
//            holder.orderItemBinding.orderItemUploadImageLayout.setBackgroundResource(R.drawable.round_outline_layout);
//            holder.orderItemBinding.orderItemUploadImage.setBackgroundTintList(ColorStateList.valueOf(mcontext.getColor(R.color.status_green)));
//            holder.orderItemBinding.orderItemUploadImage.setColorFilter(ContextCompat.getColor(mcontext, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.orderItemBinding.orderUploadLayout.setVisibility(View.GONE);
            holder.orderItemBinding.orderPaymentMethodLayout.setVisibility(View.GONE);

        } else if (order.getPayment_method().equals("online") && order.getOrder_status().equals("Completed")) {
            holder.orderItemBinding.orderItemProgress.setMax(1000);
            holder.orderItemBinding.orderItemProgress.setProgress(1000);

            holder.orderItemBinding.orderItemStatus.setTextColor(ContextCompat.getColor(mcontext, R.color.status_green));
            holder.orderItemBinding.orderUploadLayout.setVisibility(View.GONE);
            holder.orderItemBinding.orderPaymentMethodLayout.setVisibility(View.GONE);

        } else {
            holder.orderItemBinding.orderItemProgress.setMax(1000);
            holder.orderItemBinding.orderItemProgress.setProgress(500);

            holder.orderItemBinding.orderItemStatus.setTextColor(ContextCompat.getColor(mcontext, R.color.status_app_color));
            holder.orderItemBinding.orderUploadLayout.setVisibility(View.GONE);
            holder.orderItemBinding.orderPaymentMethodLayout.setVisibility(View.GONE);
        }
        holder.orderItemBinding.orderItemRecyclerCount.setText("X " + order.getItem_count());

        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);

            if (order.getOrder_id().equals(product.getOrder_id())) {

                List<Product> trackList = new ArrayList<>();
                trackList.add(new Product(order.getOrder_id(), product.getProduct_name(), product.getProduct_imageurl(), product.getProduct_rate(), product.getProduct_quantity(), product.getProduct_pieces()));

                if (product.getId() == 1) {
                    Glide.with(mcontext).onLowMemory();
                    Glide.with(mcontext).load(product.getProduct_imageurl()).into(holder.orderItemBinding.orderItemImageOne);
                }

                if (product.getId() == 2) {
                    holder.orderItemBinding.orderItemImageLayoutTwo.setVisibility(View.VISIBLE);
                    Glide.with(mcontext).onLowMemory();
                    Glide.with(mcontext).load(product.getProduct_imageurl()).into(holder.orderItemBinding.orderItemImageTwo);
                }


            }
        }


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public interface OrderInterface {
        void onItemClick(Order order);

        void ImageUpload(Order order);
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        OrderItemBinding orderItemBinding;

        public OrderViewHolder(OrderItemBinding binding) {
            super(binding.getRoot());
            this.orderItemBinding = binding;
            this.orderItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            orderItemBinding.orderItemUploadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderInterface.ImageUpload(orderList.get(getAdapterPosition()));
                }
            });
            orderItemBinding.orderItemPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderInterface.onItemClick(orderList.get(getAdapterPosition()));

                }
            });
        }
    }
}
//            if (positionSelected == -1) {
//                shopRowBinding.productQuantity.setVisibility(View.GONE);
//            } else {
//                if (positionSelected == getAdapterPosition()) {
//                    shopRowBinding.productQuantity.setVisibility(View.VISIBLE);
//                } else {
//                    shopRowBinding.productQuantity.setVisibility(View.GONE);
//                }
//            }
//            shopRowBinding.productAdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    shopRowBinding.productQuantity.setVisibility(View.VISIBLE);
//                    if (positionSelected != getAdapterPosition()) {
//                        notifyItemChanged(positionSelected);
//                        positionSelected = getAdapterPosition();
//                    }
//                }
//            });

