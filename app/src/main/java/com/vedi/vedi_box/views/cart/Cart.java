package com.vedi.vedi_box.views.cart;

import android.content.Intent;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vedi.vedi_box.R;
import com.vedi.vedi_box.adapters.CartListAdapter;
import com.vedi.vedi_box.databinding.ActivityCartBinding;
import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.CartItem;
import com.vedi.vedi_box.utilities.Constants;
import com.vedi.vedi_box.utilities.PreferManager;
import com.vedi.vedi_box.utilities.RetrofitClient;
import com.vedi.vedi_box.views.delivery.Delivery;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart extends AppCompatActivity {

    private static final String TAG = "CartFragment";

    ActivityCartBinding binding;
    private PreferManager preferManager;
    private CartViewModel cartViewModel;
    private CartListAdapter cartListAdapter;
    private MediaPlayer mp;
    private String UserID, MinimumPurchase, DeliveryCost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        mp = MediaPlayer.create(this, R.raw.single_shot);
        preferManager = new PreferManager(this);
        UserID = preferManager.getString(Constants.KEY_USER_ID);
        MinimumPurchase = preferManager.getString(Constants.KEY_MINIMUM_PURCHASE);
        DeliveryCost = preferManager.getString(Constants.KEY_DELIVERY_COST);


        //Cart
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        binding.orderDetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                onBackPressed();
            }
        });
        binding.cartShimmerLayout.startShimmer();
        binding.orderDetailsCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();

                String total = binding.orderDetailsSubTotal.getText().toString();
                total = total.replaceAll("[^0-9]", "");
                int cost = Integer.parseInt(total);
                int min_cost = Integer.parseInt(MinimumPurchase);
                if (cost > min_cost) {
                    Intent intent = new Intent(Cart.this, Delivery.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Minimum Purchase ₹ " + MinimumPurchase, Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        cartViewModel.getCart(UserID, Cart.this).observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                if (cartItems.size() > 0) {
                    binding.cartShimmerLayout.stopShimmer();
                    binding.cartShimmerLayout.setVisibility(View.GONE);
                    binding.cartRecyclerView.setVisibility(View.VISIBLE);
                    binding.mainLayout.setVisibility(View.VISIBLE);
                    binding.emptyLayout.setVisibility(View.GONE);
                    binding.cartAnimation.cancelAnimation();
                    binding.orderDetailsCheckout.setVisibility(View.VISIBLE);


                    cartViewModel.getTotalPrice().observe(Cart.this, new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            binding.orderDetailsSubTotal.setText("₹ " + integer);
                            binding.orderDetailsDeliveryCost.setText("₹ " + DeliveryCost);

                            String total = binding.orderDetailsSubTotal.getText().toString();
                            total = total.replaceAll("[^0-9]", "");


                            binding.orderDetailsTotalCost.setText("₹ " + total);


                        }
                    });

                } else {
                    binding.mainLayout.setVisibility(View.GONE);
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                    binding.cartAnimation.playAnimation();
                    binding.orderDetailsCheckout.setVisibility(View.GONE);
                }


                cartListAdapter = new CartListAdapter(Cart.this, cartItems, new CartListAdapter.CartInterface() {
                    @Override
                    public void onItemAddClick(CartItem cartItem, int quantity) {
                        mp = MediaPlayer.create(Cart.this, R.raw.single_shot);
                        mp.start();

                        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().Add_Sub(String.valueOf(cartItem.getProduct_id()), String.valueOf(quantity), "add", UserID);
                        call.enqueue(new Callback<APIResponse>() {
                            @Override
                            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                if (response.code() == 200) {
                                    if (response.body().getSuccess() != null) {
                                        getCartCount();

                                    }

                                } else if (response.code() == 500) {
                                    Toast.makeText(getApplicationContext(), "Internal Server error", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<APIResponse> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onItemSubClick(CartItem cartItem, int quantity) {
                        mp = MediaPlayer.create(Cart.this, R.raw.single_shot);
                        mp.start();

                        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().Add_Sub(String.valueOf(cartItem.getProduct_id()), String.valueOf(quantity), "sub", UserID);
                        call.enqueue(new Callback<APIResponse>() {
                            @Override
                            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                if (response.code() == 200) {
                                    if (response.body().getSuccess() != null) {
                                        getCartCount();
                                    }

                                } else if (response.code() == 500) {
                                    Toast.makeText(getApplicationContext(), "Internal Server error", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<APIResponse> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void Remove_Cart(int position) {
                        mp = MediaPlayer.create(Cart.this, R.raw.single_shot);
                        mp.start();

                        cartListAdapter.onItemDelete(position, UserID);
                        cartViewModel.removeItemFromCart(cartListAdapter.onItemDelete(position, UserID));

                        getCartCount();

                    }
                });
                cartListAdapter.notifyDataSetChanged();
                binding.cartRecyclerView.setAdapter(cartListAdapter);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                mp.start();
                cartListAdapter.onItemDelete(viewHolder.getAdapterPosition(), UserID);
                cartViewModel.removeItemFromCart(cartListAdapter.onItemDelete(viewHolder.getAdapterPosition(), UserID));

                getCartCount();
            }

            @Override
            public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//                View itemView = viewHolder.itemView;
//                ColorDrawable bg = new ColorDrawable();
//                bg.setColor(Color.RED);
//                bg.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom());
//                bg.draw(c);

            }
        }).attachToRecyclerView(binding.cartRecyclerView);


    }

    private void getCartCount() {
        cartViewModel.getCart(UserID, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCartCount();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getCartCount();
    }
}