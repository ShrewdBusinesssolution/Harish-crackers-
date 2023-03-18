package com.vedi.vedi_box.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.vedi.vedi_box.R;
import com.vedi.vedi_box.databinding.ActivityMainBinding;
import com.vedi.vedi_box.databinding.PendingAlertBinding;
import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.CartItem;
import com.vedi.vedi_box.models.Notify;
import com.vedi.vedi_box.models.Order;
import com.vedi.vedi_box.utilities.ConnectivityReceiver;
import com.vedi.vedi_box.utilities.Constants;
import com.vedi.vedi_box.utilities.PreferManager;
import com.vedi.vedi_box.utilities.RetrofitClient;
import com.vedi.vedi_box.utilities.Utils;
import com.vedi.vedi_box.views.ViewCategory.ViewCategory;
import com.vedi.vedi_box.views.accoount_info.AccountInfo;
import com.vedi.vedi_box.views.cart.Cart;
import com.vedi.vedi_box.views.cart.CartViewModel;
import com.vedi.vedi_box.views.contact.Contact;
import com.vedi.vedi_box.views.invoice.InvoiceFragment;
import com.vedi.vedi_box.views.notification.NotificationFragment;
import com.vedi.vedi_box.views.order.OrdersFragment;
import com.vedi.vedi_box.views.order.OrdersViewModel;
import com.vedi.vedi_box.views.profile.ProfileFragment;
import com.vedi.vedi_box.views.rxjava.Rx_Activity;
import com.vedi.vedi_box.views.shop.ShopFragment;
import com.vedi.vedi_box.views.track_order.TrackFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = "MainActivity";
    public static String BroadCastStringForAction = "CONNECTIVITY::::";
    private final Handler handler = new Handler();

    ActivityMainBinding binding;
    private OrdersViewModel ordersViewModel;
    float speed = 1f;

    private FirebaseAuth mAuth;
    private PreferManager preferManager;
    private CartViewModel cartViewModel;
    private Dialog dialogView;
    private MediaPlayer mp;
    private String UserID;
    private Runnable runnable;

    String current_version = "";
    @SuppressLint("NewApi")
    private final BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed));
                    mp.start();
                    fragment = new ShopFragment();
                    break;

                case R.id.bottom_notification:
                    mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed));
                    mp.start();
                    fragment = new NotificationFragment();
                    break;

                case R.id.bottom_orders:
                    mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed));
                    mp.start();
                    fragment = new OrdersFragment();
                    break;
                case R.id.bottom_profile:
                    mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed));
                    mp.start();
                    fragment = new ProfileFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.fragment_container, fragment).commit();

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mAuth = FirebaseAuth.getInstance();
        preferManager = new PreferManager(this);
        mp = MediaPlayer.create(MainActivity.this, R.raw.single_shot);
        dialogView = new Dialog(MainActivity.this, R.style.FullHeightDialog);
        dialogView.setCancelable(false);
        UserID = preferManager.getString(Constants.KEY_USER_ID);

        //Cart
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        //Order
        ordersViewModel = new ViewModelProvider(this).get(OrdersViewModel.class);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShopFragment()).commit();
            binding.bottomNavigation.setSelectedItemId(R.id.bottom_home);


            checkInterConnection();

            getVersionUpdate();

        }
        binding.bottomNavigation.setOnNavigationItemSelectedListener(navlistener);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString("TAG") != null && bundle.getString("TAG").equals("Order")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrdersFragment()).commit();
            binding.bottomNavigation.setSelectedItemId(R.id.bottom_orders);
        }
        binding.toolbar.mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        binding.toolbar.mainCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Intent intent = new Intent(MainActivity.this, Cart.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);//60 second delay
                cartViewModel.getCart(UserID, MainActivity.this).observe(MainActivity.this, new Observer<List<CartItem>>() {
                    @Override
                    public void onChanged(List<CartItem> cartItems) {
                        if (cartItems.size() > 0) {
                            binding.toolbar.mainCartCount.setVisibility(View.VISIBLE);
                            binding.toolbar.mainCartCount.setText(String.valueOf(cartItems.size()));
                        } else {
                            binding.toolbar.mainCartCount.setVisibility(View.GONE);

                        }
                    }
                });
            }
        };
        handler.postDelayed(runnable, 1000);

        binding.sideNavigation.getHeaderView(0).findViewById(R.id.header_link_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://shrewdbs.com/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        binding.sideNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);

                Fragment fragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.side_home:
                        fragment = new ShopFragment();
                        break;

                    case R.id.side_bank:
                        Intent account = new Intent(MainActivity.this, AccountInfo.class);
                        account.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        account.putExtra(Constants.KEY_CLASS_TYPE, "normal");
                        startActivity(account);

                        return true;


                    case R.id.side_kids:
                        Intent kids = new Intent(MainActivity.this, ViewCategory.class);
                        kids.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        kids.putExtra(Constants.KEY_CATEGORY_TYPE, "kids");
                        startActivity(kids);

                        return true;

                    case R.id.side_elder:
                        Intent elders = new Intent(MainActivity.this, ViewCategory.class);
                        elders.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        elders.putExtra(Constants.KEY_CATEGORY_TYPE, "elders");
                        startActivity(elders);

                        return true;

                    case R.id.side_Adults:
                        Intent adults = new Intent(MainActivity.this, ViewCategory.class);
                        adults.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        adults.putExtra(Constants.KEY_CATEGORY_TYPE, "adults");
                        startActivity(adults);

                        return true;


                    case R.id.side_share:
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        String shareBody = "Download Vedi Box:-https://play.google.com/store/apps/details?id=" + getPackageName();
                        String shareSub = "Vedi Box";
                        share.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                        share.putExtra(Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(share, "Share Via"));
                        return true;

                    case R.id.side_contact:
                        Intent contact = new Intent(MainActivity.this, Contact.class);
                        contact.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(contact);
                        return true;
                    case R.id.side_logout:
//                        mAuth.signOut();
                        preferManager.clearUserId();
                        startActivity(new Intent(MainActivity.this, Login.class));
                        finish();
                        return true;

                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                return true;
            }
        });


    }

    private void getVersionUpdate() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        current_version = String.valueOf(info.versionCode);

        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().getVersion();
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.code() == 200) {
                    APIResponse apiResponse = response.body();
                    if (apiResponse.getSuccess() != null && apiResponse.getSuccess().equals("success")) {
                        List<Notify> notify = new ArrayList(Arrays.asList(apiResponse.getVersion()));

                        androidx.appcompat.app.AlertDialog.Builder normalBuilder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
                        normalBuilder.setTitle("Update Available")
                                .setMessage("Please update to access new Features")
                                .setCancelable(false)
                                .setPositiveButton("Update",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                final String appPackageName = getPackageName(); // package name of the app
                                                try {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                } catch (android.content.ActivityNotFoundException anfe) {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                }
                                            }
                                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        androidx.appcompat.app.AlertDialog normalAlert = normalBuilder.create();

                        for (Notify version : notify) {
                            if (version.getVersion() != null && !version.getVersion().equals(current_version)) {

                                normalAlert.show();
                            } else {

                                normalAlert.dismiss();
                            }


                        }


                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });


    }

    private void checkInterConnection() {

        boolean isConnected = ConnectivityReceiver.isConnected();

        showSnackBar(isConnected);

    }

    private void showSnackBar(boolean isConnected) {


        if (!isConnected) {
            Snackbar snackbar = Snackbar.make(binding.getRoot(), "You' re Offline \n Please Enable Internet & Swipe down to Load Data", Snackbar.LENGTH_LONG);

            View view = snackbar.getView();

            TextView textView = view.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            } else {
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
            }
            snackbar.show();
        } else {

        }


    }

    private void getOrderAlert() {
        ordersViewModel.getOrders(UserID, MainActivity.this).observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                for (Order order : orders) {
                    if (order.getOrder_status().equals("Pending")) {
                        getPendingAlert();
                    }
                }
            }
        });
    }

    private void getPendingAlert() {

        PendingAlertBinding alertBinding = PendingAlertBinding.inflate(LayoutInflater.from(MainActivity.this));
        dialogView.setContentView(alertBinding.getRoot());


        if (dialogView.getWindow() != null) {
            dialogView.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialogView.getWindow().setGravity(Gravity.CENTER);

        }

        alertBinding.pendingAlertClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();

                preferManager.putBoolean(Constants.KEY_ALERT_STATUS, false);

            }
        });
        alertBinding.pendingUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();

                preferManager.putBoolean(Constants.KEY_ALERT_STATUS, false);

                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.fragment_container, new OrdersFragment()).commit();
                binding.bottomNavigation.setSelectedItemId(R.id.bottom_orders);

            }
        });

        if (preferManager.getBoolean(Constants.KEY_ALERT_STATUS)) {
            dialogView.show();
        } else {
            dialogView.dismiss();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
        registerReceiver(connectivityReceiver, intentFilter);


        Utils.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onBackPressed() {
        mp.start();

        if (dialogView != null && dialogView.isShowing()) {
            preferManager.putBoolean(Constants.KEY_ALERT_STATUS, false);
            dialogView.dismiss();
        }

        if (ShopFragment.listener != null) {
            ShopFragment.listener.onBackPressed();
        } else if (OrdersFragment.listener != null) {
            OrdersFragment.listener.onBackPressed();
            binding.bottomNavigation.setSelectedItemId(R.id.bottom_home);
        } else if (NotificationFragment.listener != null) {
            NotificationFragment.listener.onBackPressed();
            binding.bottomNavigation.setSelectedItemId(R.id.bottom_home);
        } else if (ProfileFragment.listener != null) {
            ProfileFragment.listener.onBackPressed();
            binding.bottomNavigation.setSelectedItemId(R.id.bottom_home);
        } else if (TrackFragment.listener != null) {
            TrackFragment.listener.onBackPressed();
        } else if (InvoiceFragment.listener != null) {
            InvoiceFragment.listener.onBackPressed();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
        handler.removeCallbacks(runnable);

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnackBar(isConnected);
    }
}
