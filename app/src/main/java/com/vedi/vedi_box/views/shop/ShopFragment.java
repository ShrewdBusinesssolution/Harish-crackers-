package com.vedi.vedi_box.views.shop;

import static com.vedi.vedi_box.repositories.ShopRepo.mutableBannerList;
import static com.vedi.vedi_box.repositories.ShopRepo.mutableCategoryList;
import static com.vedi.vedi_box.repositories.ShopRepo.mutableProductList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.vedi.vedi_box.R;
import com.vedi.vedi_box.adapters.CategoryListAdapter;
import com.vedi.vedi_box.adapters.ShopListAdapter;
import com.vedi.vedi_box.adapters.SliderListAdapter;
import com.vedi.vedi_box.databinding.ShopFragmentBinding;
import com.vedi.vedi_box.listeners.BackPressListener;
import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.Product;
import com.vedi.vedi_box.utilities.Constants;
import com.vedi.vedi_box.utilities.PreferManager;
import com.vedi.vedi_box.utilities.RetrofitClient;
import com.vedi.vedi_box.views.ProductDetails;
import com.vedi.vedi_box.views.ViewCategory.ViewCategory;
import com.vedi.vedi_box.views.cart.Cart;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopFragment extends Fragment implements BackPressListener {

    private static final String TAG = "ShopFragment";
    ShopFragmentBinding binding;
    private PreferManager preferManager;
    private SliderListAdapter sliderAdapter;
    private CategoryListAdapter categoryAdapter;
    private ShopListAdapter shopListAdapter;
    private ShopViewModel shopViewModel;
    private MediaPlayer mp;
    private String UserID;


    private String layoutTransaction = "Nothing";


    public static BackPressListener listener;

    public static ShopFragment newInstance() {
        return new ShopFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ShopFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            mp = MediaPlayer.create(activity, R.raw.single_shot);
            preferManager = new PreferManager(activity);
            UserID = preferManager.getString(Constants.KEY_USER_ID);

            binding.shopShimmerLayout.startShimmer();

            //Shop
            shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);


            binding.homeBanner.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            binding.homeBanner.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            binding.homeBanner.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
            binding.homeBanner.setIndicatorSelectedColor(Color.WHITE);
            binding.homeBanner.setIndicatorUnselectedColor(Color.GRAY);
            binding.homeBanner.setScrollTimeInSec(3); //set scroll delay in seconds :
            binding.homeBanner.startAutoCycle();


            final GridLayoutManager layoutManager = new GridLayoutManager(activity, 3);

            binding.shopCategoryRecyclerview.setLayoutManager(layoutManager);
            binding.shopCategoryRecyclerview.setHasFixedSize(true);


            final GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);

            binding.shopRecyclerview.setLayoutManager(gridLayoutManager);
            binding.shopRecyclerview.setHasFixedSize(true);


            //Banner
            getBanner();

            //Category
            getCategory(activity);

            //Products
            getProductRecycler(activity);


            binding.shopSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

//                shopListAdapter.getFilter().filter(s);
//                shopListAdapter.notifyDataSetChanged();
                    if (s.toString().isEmpty() || s.toString().length() <= 3) {
                        binding.shopNormalLayout.setVisibility(View.VISIBLE);
                        getProductRecycler(activity);
                        layoutTransaction = "Nothing";
                    } else {
                        binding.shopNormalLayout.setVisibility(View.GONE);
                        getSearchRecycler(activity, s.toString());
                        layoutTransaction = "Something";
                    }

                }
            });


            binding.shopRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    //dy is the change in the vertical scroll position
                    if (dy > 0) {
                        //scroll down
                        binding.homeScrollUp.setVisibility(View.GONE);
                    } else if (dy < 0) {
                        //scroll up
                        binding.homeScrollUp.setVisibility(View.VISIBLE);
                    }
                }
            });

            binding.homeScrollUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.shopNestedScrollview.fullScroll(ScrollView.FOCUS_UP);
                }
            });

            binding.homeSwipeToRefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(activity, R.color.app_color));
            binding.homeSwipeToRefresh.setColorSchemeColors(Color.WHITE);

            binding.homeSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mutableBannerList = null;
                    mutableCategoryList = null;
                    mutableProductList = null;

                    shopViewModel.getBanners();

                    shopViewModel.getCategory("");

                    shopViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
                        @Override
                        public void onChanged(List<Product> products) {
                            getBanner();
                            getCategory(activity);
                            getProductRecycler(activity);

                            binding.homeSwipeToRefresh.setRefreshing(false);
                        }
                    });
                }
            });

        }


    }


    private void getBanner() {
        shopViewModel.getBanners().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                sliderAdapter = new SliderListAdapter(requireActivity(), products);
                sliderAdapter.notifyDataSetChanged();
                binding.homeBanner.setSliderAdapter(sliderAdapter);

            }
        });
    }

    private void getCategory(Activity activity) {
        shopViewModel.getCategory("").observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                categoryAdapter = new CategoryListAdapter(activity, products, "Shop", new CategoryListAdapter.ShopInterface() {
                    @Override
                    public void onItemClick(Product product) {
                        mp.start();

                        Intent intent = new Intent(activity, ViewCategory.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra(Constants.KEY_SUB_CATEGORY_TYPE, product.getSubcategory_name());
                        startActivity(intent);
                    }
                });
                categoryAdapter.notifyDataSetChanged();
                binding.shopCategoryRecyclerview.setAdapter(categoryAdapter);

            }
        });
    }

    private void getProductRecycler(Activity activity) {

        shopViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {

                if (products != null) {

                    binding.shopShimmerLayout.stopShimmer();
                    binding.shopShimmerLayout.setVisibility(View.GONE);
                    binding.shopNestedScrollview.setVisibility(View.VISIBLE);


                    shopListAdapter = new ShopListAdapter(requireActivity(), products, new ShopListAdapter.ShopInterface() {
                        @Override
                        public void onItemClick(Product product) {
                            mp.start();
                            if (product.getProduct_stock().equals("")){
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
                            }else {
                                Toast.makeText(activity, "out of stock", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void Add_to_Cart(Product product) {
                            mp = MediaPlayer.create(requireActivity(), R.raw.single_shot);
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
                                        Toast.makeText(requireContext(), "Internal Server error", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<APIResponse> call, Throwable t) {

                                }
                            });
                        }
                    });
                    binding.shopFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                Collections.sort(products, Product.LOW_TO_HIGH);
                                shopListAdapter.notifyDataSetChanged();
                            } else if (position == 1) {
                                Collections.sort(products, Product.LOW_TO_HIGH);
                                shopListAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    preferManager.putBoolean(Constants.SAVED_RECYCLER_POSITION, true);
                    Collections.sort(products, Product.LOW_TO_HIGH);
                    shopListAdapter.notifyDataSetChanged();
                    binding.shopRecyclerview.setAdapter(shopListAdapter);
                }
            }
        });
    }

    private void getSearchRecycler(Activity activity, String s) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (s.length() > 3) {

                    Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().getSearch(s);
                    call.enqueue(new Callback<APIResponse>() {
                        @Override
                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                            if (response.code() == 200) {
                                APIResponse apiResponse = response.body();
                                if (response.body().getSuccess() != null && response.body().getSuccess().equals("success")) {

                                    List<Product> products = new ArrayList(Arrays.asList(apiResponse.getSearch_products()));

                                    shopListAdapter = new ShopListAdapter(requireActivity(), products, new ShopListAdapter.ShopInterface() {
                                        @Override
                                        public void onItemClick(Product product) {
                                            mp.start();
                                            if (product.getProduct_stock().equals("")){
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
                                            }else {
                                                Toast.makeText(activity, "out of stock", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void Add_to_Cart(Product product) {
                                            mp = MediaPlayer.create(requireActivity(), R.raw.single_shot);
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
                                                        Toast.makeText(requireContext(), "Internal Server error", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<APIResponse> call, Throwable t) {

                                                }
                                            });
                                        }
                                    });
                                    binding.shopFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if (position == 1) {
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
                                    preferManager.putBoolean(Constants.SAVED_RECYCLER_POSITION, true);
                                    Collections.sort(products, Product.LOW_TO_HIGH);
                                    shopListAdapter.notifyDataSetChanged();
                                    binding.shopRecyclerview.setAdapter(shopListAdapter);

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<APIResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });
        thread.start();

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
                    Toast.makeText(requireContext(), "Internal Server error", Toast.LENGTH_SHORT).show();
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
                        Snackbar.make(requireView(), product.getProduct_name() + " added to cart.", Snackbar.LENGTH_LONG)
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
                    Toast.makeText(requireContext(), "Internal Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onPause() {
        listener = null;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        listener = this;
        if (shopListAdapter != null) {
            shopListAdapter.getRefreshData();
        }

    }

    @Override
    public void onBackPressed() {
        if (!layoutTransaction.equals("Nothing")) {
            binding.shopSearch.setText("");
            binding.shopNormalLayout.setVisibility(View.VISIBLE);
            getProductRecycler(getActivity());
        } else {
            listener = null;
        }


    }
}

//        binding.shopNestedScrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//@Override
//public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//        if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
//
//
////                    if (isLoading) {
////
////                        page_number++;
////                        binding.shopProgress.setVisibility(View.VISIBLE);
////                        performPagination();
////                        isLoading = false;
////                    }
//        } else {
//
//        }
//        }
//        });