package com.vedi.vedi_box.views.ViewCategory;

import static com.vedi.vedi_box.adapters.PaginationListener.PAGE_START;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.vedi.vedi_box.R;
import com.vedi.vedi_box.adapters.CategoryListAdapter;
import com.vedi.vedi_box.adapters.PaginationListener;
import com.vedi.vedi_box.adapters.PostRecyclerAdapter;
import com.vedi.vedi_box.adapters.ViewShopListAdapter;
import com.vedi.vedi_box.databinding.ActivityViewCategoryBinding;
import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.CartItem;
import com.vedi.vedi_box.models.Product;
import com.vedi.vedi_box.utilities.Constants;
import com.vedi.vedi_box.utilities.LoadingProgress;
import com.vedi.vedi_box.utilities.PreferManager;
import com.vedi.vedi_box.utilities.RetrofitClient;
import com.vedi.vedi_box.views.ProductDetails;
import com.vedi.vedi_box.views.cart.Cart;
import com.vedi.vedi_box.views.cart.CartViewModel;
import com.vedi.vedi_box.views.shop.ShopViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCategory extends AppCompatActivity {

    private static final String TAG = "Category";

    private LinearLayoutManager layoutManager;
    ActivityViewCategoryBinding binding;
    private List<Product> categoryList;
    private PreferManager preferManager;
    private CategoryListAdapter categoryListAdapter;
    private ViewShopListAdapter shopListAdapter;
    private ShopViewModel shopViewModel;
    private CartViewModel cartViewModel;
    private MediaPlayer mp;
    private String UserID;

    private final Handler handler = new Handler();
    private Runnable runnable;
    private LoadingProgress loadingProgress;

//    private int page_number = 0;
//    private boolean isLoading = true;


    private PostRecyclerAdapter adapter;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewCategoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mp = MediaPlayer.create(this, R.raw.single_shot);
        preferManager = new PreferManager(this);
        UserID = preferManager.getString(Constants.KEY_USER_ID);
        loadingProgress = LoadingProgress.getInstance();

        loadingProgress.ShowProgress(ViewCategory.this, "Please wait", false);

        //Shop
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        //Cart
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);


        binding.subCategoryRecyclerview.setLayoutManager(layoutManager);
        binding.subCategoryRecyclerview.setHasFixedSize(true);

        binding.viewCategoryRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        binding.viewCategoryRecycler.setHasFixedSize(true);


        binding.viewCategoryBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.mainCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Intent intent = new Intent(ViewCategory.this, Cart.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            String category = bundle.getString(Constants.KEY_CATEGORY_TYPE);
            String subcategory = bundle.getString(Constants.KEY_SUB_CATEGORY_TYPE);


//            String gifUrl = "https://img1.picmix.com/output/stamp/normal/6/2/3/4/534326_39fd0.gif";
//            Glide
//                    .with(this)
//                    .load(gifUrl)
//                    .into(binding.viewCategoryImage);

//            runnable = new Runnable() {
//                @Override
//                public void run() {


            if (category != null) {

                binding.viewProgress.setVisibility(View.GONE);

                getCategoryRecycler(ViewCategory.this, category, "Sparklers");

                shopViewModel.getCategory(category).observe(ViewCategory.this, new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {

                        categoryListAdapter = new CategoryListAdapter(ViewCategory.this, products, "View", new CategoryListAdapter.ShopInterface() {
                            @Override
                            public void onItemClick(Product product) {
                                mp.start();
                                getCategoryRecycler(ViewCategory.this, category, product.getSubcategory_name());

                            }
                        });
                        categoryListAdapter.notifyDataSetChanged();
                        binding.subCategoryRecyclerview.setAdapter(categoryListAdapter);

                    }
                });
            } else if (subcategory != null) {

                binding.categoryTypeTitle.setText(subcategory);
                binding.subCategoryRecyclerview.setVisibility(View.GONE);
                binding.sortByLayout.setPadding(0, 5, 0, 5);


                getSubCategoryRecycler(ViewCategory.this, subcategory);

//                doApiCall();
//
//                adapter = new PostRecyclerAdapter(new ArrayList<>());
//                binding.viewCategoryRecycler.setAdapter(adapter);
//
//                binding.viewCategoryRecycler.addOnScrollListener(new PaginationListener(layoutManager) {
//                    @Override
//                    protected void loadMoreItems() {
//                        isLoading = true;
//                        currentPage++;
//
//                        Toast.makeText(getApplicationContext(), "panalam", Toast.LENGTH_SHORT).show();
////                                getSubCategoryRecycler(ViewCategory.this, subcategory);
//
//                    }
//
//                    @Override
//                    public boolean isLastPage() {
//                        Toast.makeText(getApplicationContext(), "panalam", Toast.LENGTH_SHORT).show();
//                        return isLastPage;
//
//                    }
//
//                    @Override
//                    public boolean isLoading() {
//                        Toast.makeText(getApplicationContext(), "panalam", Toast.LENGTH_SHORT).show();
//                        return isLoading;
//                    }
//                });
            }
//                }
//            };
//            handler.postDelayed(runnable, 1000);


            getCartData();


        }
    }

    private void doApiCall() {
        final ArrayList<Product> items = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; i++) {
                    itemCount++;
                    Product postItem = new Product();
                    postItem.setProduct_name("Name" + itemCount);
                    postItem.setProduct_rate(120);
                    items.add(postItem);
                }
                /**
                 * manage progress view
                 */
                if (currentPage != PAGE_START) adapter.removeLoading();
                adapter.addItems(items);
                // check weather is last page or not
                if (currentPage < totalPage) {
                    adapter.addLoading();
                } else {
                    isLastPage = true;
                }
                isLoading = false;
            }
        }, 1500);

    }

    private void getCartData() {
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);//60 second delay

                cartViewModel.getCart(UserID, ViewCategory.this).observe(ViewCategory.this, new Observer<List<CartItem>>() {
                    @Override
                    public void onChanged(List<CartItem> cartItems) {
                        if (cartItems.size() > 0) {
                            binding.mainCartCount.setVisibility(View.VISIBLE);
                            binding.mainCartCount.setText(String.valueOf(cartItems.size()));
                        } else {
                            binding.mainCartCount.setVisibility(View.GONE);

                        }
                    }
                });
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void performPagination(int page_number) {


        shopViewModel.getPaginationProducts(page_number, ViewCategory.this).observe(ViewCategory.this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                shopListAdapter.addProducts(products);
                shopListAdapter.getRefreshData();
            }
        });

    }

    private void getCategoryRecycler(Activity activity, String category, String type) {

        binding.categoryTypeTitle.setText(category + "\n\n" + type);

        categoryList = new ArrayList<>();


        shopViewModel.getProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                categoryList.clear();
                for (Product product : products) {

                    if (product.getProduct_category().equals(category)) {

                        if (product.getProduct_subcategory().equals(type) || type.equals("All")) {


                            categoryList.add(new Product(product.getId(), product.getProduct_name(), product.getProduct_imageurl(), product.getProduct_category(), product.getProduct_subcategory(), product.getProduct_mrp(), product.getProduct_additional_rate(), product.getProduct_rate(), product.getProduct_pieces(), product.getProduct_stock()));


                            shopListAdapter = new ViewShopListAdapter(ViewCategory.this, categoryList, new ViewShopListAdapter.ShopInterface() {
                                @Override
                                public void onItemClick(Product product) {
                                    mp.start();
                                    if (product.getProduct_stock().equals("")) {
                                        Intent intent = new Intent(activity, ProductDetails.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        intent.putExtra(Constants.KEY_PRODUCT_ID, product.getId());
                                        intent.putExtra(Constants.KEY_PRODUCT_SUB_CATEGORY, product.getProduct_subcategory());
                                        intent.putExtra(Constants.KEY_PRODUCT_NAME, product.getProduct_name());
                                        intent.putExtra(Constants.KEY_PRODUCT_RATE, product.getProduct_rate());
                                        intent.putExtra(Constants.KEY_PRODUCT_IMAGE, product.getProduct_imageurl());
                                        intent.putExtra(Constants.KEY_PRODUCT_PIECES, product.getProduct_pieces());
                                        intent.putExtra(Constants.KEY_PRODUCT_STOCK, product.getProduct_stock());
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(activity, "out of stock", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void Add_to_Cart(Product product) {
                                    mp = MediaPlayer.create(getApplicationContext(), R.raw.single_shot);
                                    mp.start();

                                    productAddToCart(product, activity);

                                }

                                @Override
                                public void Add_Cart(Product product, int quantity) {
                                    mp = MediaPlayer.create(activity, R.raw.single_shot);
                                    mp.start();

                                    set_add_sub_quantity(product, quantity, "add", activity);

                                }

                                @Override
                                public void Sub_Cart(Product product, int quantity) {

                                    mp = MediaPlayer.create(activity, R.raw.single_shot);
                                    mp.start();

                                    set_add_sub_quantity(product, quantity, "sub", activity);
                                }

                                @Override
                                public void Remove_Cart(Product product) {
                                    mp = MediaPlayer.create(activity, R.raw.single_shot);
                                    mp.start();

                                    Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().RemoveCart(String.valueOf(product.getId()), "0", UserID);
                                    call.enqueue(new Callback<APIResponse>() {
                                        @Override
                                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                            if (response.code() == 200) {
                                                if (response.body().getSuccess() != null) {

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
                            binding.viewCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position == 0) {
                                        Collections.sort(categoryList, Product.LOW_TO_HIGH);
                                        shopListAdapter.notifyDataSetChanged();
                                    } else if (position == 1) {
                                        Collections.sort(categoryList, Product.LOW_TO_HIGH);
                                        shopListAdapter.notifyDataSetChanged();
                                    } else if (position == 2) {
                                        Collections.sort(categoryList, Product.HIGH_TO_LOW);
                                        shopListAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            Collections.sort(categoryList, Product.LOW_TO_HIGH);


                            shopListAdapter.notifyDataSetChanged();
                            binding.viewCategoryRecycler.setAdapter(shopListAdapter);

                        }


                    }

                }
                if (categoryList.size() > 0) {

                    binding.emptyLayout.setVisibility(View.GONE);
                    binding.categoryAnimation.cancelAnimation();
                    binding.viewScrollView.setVisibility(View.VISIBLE);
                } else {
                    if (loadingProgress!=null){
                        loadingProgress.hideProgress();
                    }

                    binding.viewScrollView.setVisibility(View.GONE);
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                    binding.categoryAnimation.playAnimation();
                }
            }
        });

    }

    private void getSubCategoryRecycler(Activity activity, String subcategory) {


        if (subcategory.equals("All")) {

//            binding.viewScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//                @Override
//                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
//
//                        page_number++;
//                        performPagination(page_number);
//
//                        binding.viewCategoryFilter.setSelection(0);
//                    }
//                }
//            });

            shopViewModel.getPaginationProducts(0, ViewCategory.this).observe(ViewCategory.this, new Observer<List<Product>>() {
                @Override
                public void onChanged(List<Product> products) {
                    shopListAdapter = new ViewShopListAdapter(ViewCategory.this, products, new ViewShopListAdapter.ShopInterface() {
                        @Override
                        public void onItemClick(Product product) {
                            mp.start();
                            if (product.getProduct_stock().equals("")) {
                                Intent intent = new Intent(activity, ProductDetails.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.putExtra(Constants.KEY_PRODUCT_ID, product.getId());
                                intent.putExtra(Constants.KEY_PRODUCT_SUB_CATEGORY, product.getProduct_subcategory());
                                intent.putExtra(Constants.KEY_PRODUCT_NAME, product.getProduct_name());
                                intent.putExtra(Constants.KEY_PRODUCT_RATE, product.getProduct_rate());
                                intent.putExtra(Constants.KEY_PRODUCT_IMAGE, product.getProduct_imageurl());
                                intent.putExtra(Constants.KEY_PRODUCT_PIECES, product.getProduct_pieces());
                                intent.putExtra(Constants.KEY_PRODUCT_STOCK, product.getProduct_stock());
                                startActivity(intent);
                            } else {
                                Toast.makeText(activity, "out of stock", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void Add_to_Cart(Product product) {
                            mp = MediaPlayer.create(getApplicationContext(), R.raw.single_shot);
                            mp.start();

                            productAddToCart(product, activity);

                        }

                        @Override
                        public void Add_Cart(Product product, int quantity) {
                            mp = MediaPlayer.create(activity, R.raw.single_shot);
                            mp.start();

                            set_add_sub_quantity(product, quantity, "add", activity);

                        }

                        @Override
                        public void Sub_Cart(Product product, int quantity) {

                            mp = MediaPlayer.create(activity, R.raw.single_shot);
                            mp.start();

                            set_add_sub_quantity(product, quantity, "sub", activity);
                        }

                        @Override
                        public void Remove_Cart(Product product) {
                            mp = MediaPlayer.create(activity, R.raw.single_shot);
                            mp.start();

                            Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().RemoveCart(String.valueOf(product.getId()), "0", UserID);
                            call.enqueue(new Callback<APIResponse>() {
                                @Override
                                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().getSuccess() != null) {

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
                    binding.viewCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                Collections.sort(products, Product.LOW_TO_HIGH);
                                shopListAdapter.notifyDataSetChanged();
                            } else if (position == 1) {
                                Collections.sort(products, Product.LOW_TO_HIGH);
                                shopListAdapter.notifyDataSetChanged();
                            } else if (position == 2) {
                                Collections.sort(products, Product.HIGH_TO_LOW);
                                shopListAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    Collections.sort(products, Product.LOW_TO_HIGH);


                    shopListAdapter.notifyDataSetChanged();
                    binding.viewCategoryRecycler.setAdapter(shopListAdapter);
                }
            });
        } else {

            binding.viewProgress.setVisibility(View.GONE);

            categoryList = new ArrayList<>();
            shopViewModel.getProducts().observe(ViewCategory.this, new Observer<List<Product>>() {
                @Override
                public void onChanged(List<Product> products) {
                    categoryList.clear();

                    for (Product product : products) {

                        if (product.getProduct_subcategory().equals(subcategory)) {


                            categoryList.add(new Product(product.getId(), product.getProduct_name(), product.getProduct_imageurl(), product.getProduct_category(), product.getProduct_subcategory(), product.getProduct_mrp(), product.getProduct_additional_rate(), product.getProduct_rate(), product.getProduct_pieces(), product.getProduct_stock()));


                            shopListAdapter = new ViewShopListAdapter(ViewCategory.this, categoryList, new ViewShopListAdapter.ShopInterface() {
                                @Override
                                public void onItemClick(Product product) {
                                    mp.start();
                                    if (product.getProduct_stock().equals("")) {
                                        Intent intent = new Intent(activity, ProductDetails.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        intent.putExtra(Constants.KEY_PRODUCT_ID, product.getId());
                                        intent.putExtra(Constants.KEY_PRODUCT_SUB_CATEGORY, product.getProduct_subcategory());
                                        intent.putExtra(Constants.KEY_PRODUCT_NAME, product.getProduct_name());
                                        intent.putExtra(Constants.KEY_PRODUCT_RATE, product.getProduct_rate());
                                        intent.putExtra(Constants.KEY_PRODUCT_IMAGE, product.getProduct_imageurl());
                                        intent.putExtra(Constants.KEY_PRODUCT_PIECES, product.getProduct_pieces());
                                        intent.putExtra(Constants.KEY_PRODUCT_STOCK, product.getProduct_stock());
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(activity, "out of stock", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void Add_to_Cart(Product product) {
                                    mp = MediaPlayer.create(getApplicationContext(), R.raw.single_shot);
                                    mp.start();

                                    productAddToCart(product, activity);

                                }

                                @Override
                                public void Add_Cart(Product product, int quantity) {
                                    mp = MediaPlayer.create(activity, R.raw.single_shot);
                                    mp.start();

                                    set_add_sub_quantity(product, quantity, "add", activity);

                                }

                                @Override
                                public void Sub_Cart(Product product, int quantity) {

                                    mp = MediaPlayer.create(activity, R.raw.single_shot);
                                    mp.start();

                                    set_add_sub_quantity(product, quantity, "sub", activity);
                                }

                                @Override
                                public void Remove_Cart(Product product) {
                                    mp = MediaPlayer.create(activity, R.raw.single_shot);
                                    mp.start();

                                    Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().RemoveCart(String.valueOf(product.getId()), "0", UserID);
                                    call.enqueue(new Callback<APIResponse>() {
                                        @Override
                                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                            if (response.code() == 200) {
                                                if (response.body().getSuccess() != null) {

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
                            binding.viewCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position == 0) {
                                        Collections.sort(categoryList, Product.LOW_TO_HIGH);
                                        shopListAdapter.notifyDataSetChanged();
                                    } else if (position == 1) {
                                        Collections.sort(categoryList, Product.LOW_TO_HIGH);
                                        shopListAdapter.notifyDataSetChanged();
                                    } else if (position == 2) {
                                        Collections.sort(categoryList, Product.HIGH_TO_LOW);
                                        shopListAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            binding.viewCategoryFilter.setSelection(2);
                            Collections.sort(categoryList, Product.HIGH_TO_LOW);


                            shopListAdapter.notifyDataSetChanged();
                            binding.viewCategoryRecycler.setAdapter(shopListAdapter);

                        }


                    }
                    if (categoryList.size() > 0) {

                        binding.emptyLayout.setVisibility(View.GONE);
                        binding.categoryAnimation.cancelAnimation();
                        binding.viewScrollView.setVisibility(View.VISIBLE);
                    } else {
                        if (loadingProgress!=null){
                            loadingProgress.hideProgress();
                        }

                        binding.viewScrollView.setVisibility(View.GONE);
                        binding.emptyLayout.setVisibility(View.VISIBLE);
                        binding.categoryAnimation.playAnimation();
                    }

                }
            });


        }

    }


    private void set_add_sub_quantity(Product product, int quantity, String type, Activity activity) {
        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().Add_Sub(String.valueOf(product.getId()), String.valueOf(quantity), type, UserID);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getSuccess() != null) {

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


    private void productAddToCart(Product product, Activity activity) {

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
                                        Intent intent = new Intent(activity, Cart.class);
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

    @Override
    public void onResume() {
        super.onResume();

        if (shopListAdapter != null) {
            shopListAdapter.getRefreshData();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
        handler.removeCallbacks(runnable);
    }
}
