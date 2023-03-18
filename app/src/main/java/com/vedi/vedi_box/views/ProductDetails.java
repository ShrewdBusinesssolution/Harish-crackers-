package com.vedi.vedi_box.views;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.vedi.vedi_box.R;
import com.vedi.vedi_box.adapters.ProductListAdapter;
import com.vedi.vedi_box.databinding.ActivityProductDetailsBinding;
import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.CartItem;
import com.vedi.vedi_box.models.Product;
import com.vedi.vedi_box.utilities.Constants;
import com.vedi.vedi_box.utilities.PreferManager;
import com.vedi.vedi_box.utilities.RetrofitClient;
import com.vedi.vedi_box.views.cart.Cart;
import com.vedi.vedi_box.views.cart.CartViewModel;
import com.vedi.vedi_box.views.shop.ShopViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetails extends AppCompatActivity {
    private static final String TAG = "ProductDetailsFragment";

    ActivityProductDetailsBinding binding;
    private PreferManager preferManager;
    private MediaPlayer mp;
    private ShopViewModel shopViewModel;
    private CartViewModel cartViewModel;
    private ProductListAdapter productListAdapter;
    private String UserID, product_sub_category;
    private int ProID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mp = MediaPlayer.create(this, R.raw.single_shot);
        preferManager = new PreferManager(this);
        UserID = preferManager.getString(Constants.KEY_USER_ID);


        //Shop
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        //Cart
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);


        binding.productDetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                onBackPressed();
            }
        });
        binding.productShimmerLayout.startShimmer();

        binding.productDetailsRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.productDetailsRecycler.setHasFixedSize(true);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ProID = bundle.getInt(Constants.KEY_PRODUCT_ID);
            product_sub_category = bundle.getString(Constants.KEY_PRODUCT_SUB_CATEGORY);
            String product_name = bundle.getString(Constants.KEY_PRODUCT_NAME);
            String product_image = bundle.getString(Constants.KEY_PRODUCT_IMAGE);
            String product_pieces = bundle.getString(Constants.KEY_PRODUCT_PIECES);
            String product_stock = bundle.getString(Constants.KEY_PRODUCT_STOCK);
            int product_rate = bundle.getInt(Constants.KEY_PRODUCT_RATE);


            Log.d(TAG, "IMAGE-URL,," + product_image);


            Product productView = new Product(product_name, product_image, product_rate, "One Box ( " + product_pieces + " Pieces )");
            binding.setProduct(productView);


            getProductRecycler(ProID, product_sub_category);

            getCartDetails(ProID);

//            productDetailsBinding.productDetailsCart.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    addToCartByCartItem(product_id, product_name);
//
//                    getCartDetails(product_id);
//                }
//            });
            if (binding.productCount.getText().toString().equals("0")) {
                binding.productDetailsAddQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addToCartByCartItem(ProID, product_name);

                        getCartDetails(ProID);

                    }
                });
            }
            binding.productDetailsBuyMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.start();
                    Intent intent = new Intent(ProductDetails.this, Cart.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            });
        }


    }

    private void getCartDetails(int product_id) {


        cartViewModel.getCart(UserID, this).observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                for (CartItem cartItem : cartItems) {
                    if (cartItem.getProduct_id() == product_id) {


                        binding.productCount.setText(String.valueOf(cartItem.getProduct_quantity()));


                        binding.productDetailsAddQuantity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int quantity = Integer.parseInt(binding.productCount.getText().toString()) + 1;

                                binding.productCount.setText(String.valueOf(quantity));

                                set_add_sub_quantityByCartItem(cartItem, quantity, "add");

                            }
                        });
                        binding.productDetailsSubQuantity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int quantity = Integer.parseInt(binding.productCount.getText().toString()) - 1;
                                if (quantity >= 1) {
                                    set_add_sub_quantityByCartItem(cartItem, quantity, "sub");

                                    binding.productCount.setText(String.valueOf(quantity));

                                } else {
                                    binding.productCount.setText("0");

                                    Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().RemoveCart(String.valueOf(cartItem.getProduct_id()), "0", UserID);
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

                            }
                        });
                        break;
                    } else {

                        shopViewModel.getProducts().observe(ProductDetails.this, new Observer<List<Product>>() {
                            @Override
                            public void onChanged(List<Product> products) {
                                for (Product product : products) {
                                    if (product.getId() == product_id) {

                                        binding.productCount.setText("0");

                                        if (binding.productCount.getText().toString().equals("0")) {
                                            binding.productDetailsAddQuantity.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    addToCartByCartItem(product_id, product.getProduct_name());
                                                    getCartDetails(product_id);

                                                }
                                            });
                                        }
                                        break;
                                    }
                                }
                            }
                        });

                    }
                }
            }
        });

    }

    private void getProductRecycler(int product_id, String product_sub_category) {
        List<Product> productItems = new ArrayList<>();
        shopViewModel.getProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                binding.productShimmerLayout.stopShimmer();
                binding.productShimmerLayout.setVisibility(View.GONE);
                binding.productDetailsRecycler.setVisibility(View.VISIBLE);

                for (Product product : products) {
                    if (product.getProduct_subcategory().equals(product_sub_category)) {

                        productItems.add(new Product(product.getId(), product.getProduct_name(), product.getProduct_imageurl(), product.getProduct_category(), product.getProduct_subcategory(), product.getProduct_mrp(), product.getProduct_additional_rate(), product.getProduct_rate(), product.getProduct_pieces(),product.getProduct_stock()));

                        productListAdapter = new ProductListAdapter(ProductDetails.this, productItems, product_id, new ProductListAdapter.ShopInterface() {
                            @Override
                            public void onItemClick(int position, Product product) {

                                mp.start();
                                if (product.getProduct_stock().equals("")){
                                    Product productClick = new Product(product.getProduct_name(), product.getProduct_imageurl(), product.getProduct_rate(), "One Box ( " + product.getProduct_pieces() + " Pieces )");
                                    binding.setProduct(productClick);

                                    productListAdapter.ItemChange(position, product.getId());


                                }else {
                                    Toast.makeText(getApplicationContext(), "out of stock", Toast.LENGTH_SHORT).show();
                                }

                                getCartDetails(product.getId());
                            }

                            @Override
                            public void Add_to_Cart(Product product) {
                                mp = MediaPlayer.create(ProductDetails.this, R.raw.single_shot);
                                mp.start();

                                addToCartByProduct(product);
                            }

                            @Override
                            public void Add_Cart(Product product, int quantity) {
                                mp = MediaPlayer.create(ProductDetails.this, R.raw.single_shot);
                                mp.start();

                                set_add_sub_quantityByProduct(product, quantity, "add");
                            }

                            @Override
                            public void Sub_Cart(Product product, int quantity) {
                                mp = MediaPlayer.create(ProductDetails.this, R.raw.single_shot);
                                mp.start();

                                set_add_sub_quantityByProduct(product, quantity, "sub");
                            }

                            @Override
                            public void Remove_Cart(Product product) {
                                mp = MediaPlayer.create(ProductDetails.this, R.raw.single_shot);
                                mp.start();

                                Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().RemoveCart(String.valueOf(product.getId()), "0", UserID);
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
                        });
                        productListAdapter.notifyDataSetChanged();
                        binding.productDetailsRecycler.setAdapter(productListAdapter);
                    }
                }
            }
        });
    }

    private void set_add_sub_quantityByProduct(Product product, int quantity, String type) {
        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().Add_Sub(String.valueOf(product.getId()), String.valueOf(quantity), type, UserID);
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


    private void addToCartByCartItem(int id, String product_name) {
        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().AddToCart(String.valueOf(id), "1", UserID);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getSuccess() != null) {

                        int quantity = Integer.parseInt(binding.productCount.getText().toString()) + 1;
                        binding.productCount.setText(String.valueOf(quantity));

                        Snackbar.make(binding.getRoot(), product_name + " added to cart.", Snackbar.LENGTH_LONG)
                                .setAction("Checkout", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mp.start();
                                        Intent intent = new Intent(ProductDetails.this, Cart.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                    }
                                }).show();
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

    private void addToCartByProduct(Product product) {
        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().AddToCart(String.valueOf(product.getId()), "1", UserID);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getSuccess() != null) {

                        Snackbar.make(binding.getRoot(), product.getProduct_name() + " added to cart.", Snackbar.LENGTH_LONG)
                                .setAction("Checkout", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mp.start();
                                        Intent intent = new Intent(ProductDetails.this, Cart.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                    }
                                }).show();
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

    private void set_add_sub_quantityByCartItem(CartItem cartItem, int quantity, String type) {

        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().Add_Sub(String.valueOf(cartItem.getProduct_id()), String.valueOf(quantity), type, UserID);
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

    private void getCartCount() {
        cartViewModel.getCart(UserID, this);
    }

}

