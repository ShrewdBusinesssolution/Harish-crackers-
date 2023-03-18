package com.vedi.vedi_box.views.payment;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.vedi.vedi_box.R;
import com.vedi.vedi_box.databinding.ActivityPaymentBinding;
import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.Order;
import com.vedi.vedi_box.utilities.Constants;
import com.vedi.vedi_box.utilities.LoadingProgress;
import com.vedi.vedi_box.utilities.PreferManager;
import com.vedi.vedi_box.utilities.RetrofitClient;
import com.vedi.vedi_box.views.MainActivity;
import com.vedi.vedi_box.views.accoount_info.AccountInfo;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Payment extends AppCompatActivity implements PaymentResultWithDataListener {

    private static final String TAG = "Payment";

    ActivityPaymentBinding binding;
    private PreferManager preferManager;
    private MediaPlayer mp;
    private PaymentViewModel paymentViewModel;
    private String UserID, UserName, UserMobile;
    private String paymentMethod = "Manual";
    private LoadingProgress loadingProgress;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Uri shortLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mp = MediaPlayer.create(this, R.raw.single_shot);
        preferManager = new PreferManager(this);
        loadingProgress = LoadingProgress.getInstance();

        UserID = preferManager.getString(Constants.KEY_USER_ID);
        UserName = preferManager.getString(Constants.KEY_USER_FULL_NAME);
        UserMobile=preferManager.getString(Constants.KEY_USER_MOBILE);
//        UserMobile = mUser.getPhoneNumber();

        loadingProgress.ShowProgress(Payment.this, "sit tight", false);

        //Payment
        paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);

        binding.paymentDetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                onBackPressed();
            }
        });

        binding.paymentManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "Manual";

                binding.paymentManualStatus.setText("Selected");
                binding.paymentOnlineStatus.setText("Not Selected");

                binding.paymentManual.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_outline_appcolor_edge));
                binding.paymentManual.setPadding(8, 8, 8, 8);
                binding.paymentOnline.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_outline_layout));
                binding.paymentOnline.setPadding(8, 8, 8, 8);

                binding.paymentManualRadio.setChecked(true);
                binding.paymentOnlineRadio.setChecked(false);
            }
        });

        binding.paymentOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "Online";

                binding.paymentOnlineStatus.setText("Selected");
                binding.paymentManualStatus.setText("Not Selected");

                binding.paymentOnline.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_outline_appcolor_edge));
                binding.paymentOnline.setPadding(8, 8, 8, 8);
                binding.paymentManual.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_outline_layout));
                binding.paymentManual.setPadding(8, 8, 8, 8);

                binding.paymentOnlineRadio.setChecked(true);
                binding.paymentManualRadio.setChecked(false);
            }
        });
        binding.paymentManualRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "Manual";

                binding.paymentManualStatus.setText("Selected");
                binding.paymentOnlineStatus.setText("Not Selected");

                binding.paymentManual.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_outline_appcolor_edge));
                binding.paymentManual.setPadding(8, 8, 8, 8);
                binding.paymentOnline.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_outline_layout));
                binding.paymentOnline.setPadding(8, 8, 8, 8);

                binding.paymentManualRadio.setChecked(true);
                binding.paymentOnlineRadio.setChecked(false);
            }
        });

        binding.paymentOnlineRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "Online";

                binding.paymentOnlineStatus.setText("Selected");
                binding.paymentManualStatus.setText("Not Selected");

                binding.paymentOnline.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_outline_appcolor_edge));
                binding.paymentOnline.setPadding(8, 8, 8, 8);
                binding.paymentManual.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_outline_layout));
                binding.paymentManual.setPadding(8, 8, 8, 8);

                binding.paymentOnlineRadio.setChecked(true);
                binding.paymentManualRadio.setChecked(false);
            }
        });

        paymentViewModel.getOrderDetails(UserID).observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                if (loadingProgress != null) {
                    loadingProgress.hideProgress();
                }
                for (Order order : orders) {

                    String total = order.getTotal_price();
                    total = total.replaceAll("[^0-9]", "");


                    Order orderView = new Order(order.getOrder_id(), order.getOrder_date(), order.getExpected_date(), "₹ " + total);
                    binding.setOrder(orderView);

                    ShareView(order.getOrder_id(), order.getOrder_date(), order.getExpected_date(), "₹ " + total);

                    int finalSum = Integer.parseInt(total);
                    binding.paymentDetailsContinue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadingProgress.ShowProgress(Payment.this, "Please Wait", false);
                            if (paymentMethod.equals("Manual")) {
                                mp.start();
                                Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().OrdersInsert(UserID, order.getOrder_id(), order.getOrder_date(), order.getExpected_date(), String.valueOf(finalSum), UserName, UserMobile, "manual", "");
                                call.enqueue(new Callback<APIResponse>() {
                                    @Override
                                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                        if (response.code() == 200) {
                                            if (loadingProgress != null) {
                                                loadingProgress.hideProgress();
                                            }
                                            if (response.body().getSuccess() != null && response.body().getSuccess().equals("Order Placed")) {

                                                Toast.makeText(Payment.this, response.body().getSuccess(), Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(Payment.this, AccountInfo.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(Payment.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        } else if (response.code() == 500) {
                                            if (loadingProgress != null) {
                                                loadingProgress.hideProgress();
                                            }
                                            Toast.makeText(Payment.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<APIResponse> call, Throwable t) {

                                    }
                                });
                            } else {
                                Log.e(TAG,"URL:::"+shortLink);


                                Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().OrdersInsert(UserID, order.getOrder_id(), order.getOrder_date(), order.getExpected_date(), String.valueOf(finalSum), UserName, UserMobile, "online", "");
                                call.enqueue(new Callback<APIResponse>() {
                                    @Override
                                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                        if (response.code() == 200) {
                                            if (loadingProgress != null) {
                                                loadingProgress.hideProgress();
                                            }
                                            if (response.body().getSuccess() != null) {

                                                Uri uri = Uri.parse(response.body().getLink()); // missing 'http://' will cause crashed
                                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(Payment.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        } else if (response.code() == 500) {
                                            if (loadingProgress != null) {
                                                loadingProgress.hideProgress();
                                            }
                                            Toast.makeText(Payment.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<APIResponse> call, Throwable t) {

                                    }
                                });

                               // startPayment(Integer.parseInt(total));
                            }

                        }
                    });
                }
            }
        });
    }

    private void ShareView(String order_id, String order_date, String expected_date, String total) {

//        order_id=order_id.replaceAll("#","");
//        "id=/" + order_id +"/"+order_date+"/"+expected_date+"/"+total+

        String sharelinktext = "https://vedi.page.link?" +
                "link=https://www.vedibox.com/" +
                "&apn=" + getPackageName() ;

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(sharelinktext))
                // Set parameters
                // ...
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {

                            // Short link created
                             shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                        } else {
                            // Error
                            // ...
                        }
                    }
                });
    }

    public void startPayment(int sub) {

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_6LGN4ZzyHaMqfO");
        /**
         * Set your logo here
         */

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "Vedi BOX");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     InvoiceFragment Payment
             *     etc.
             */

            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */

            int amount = (sub * 100);
            options.put("amount", amount);
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {

        Log.d(TAG, "PAYMENT-DATA:::" + paymentData);

        String OrderId = binding.paymentOrderId.getText().toString();
        String OrderDate = binding.paymentOrderDate.getText().toString();
        String OrderExpectDate = binding.paymentOrderExpectedDate.getText().toString();
        String total = binding.paymentOrderTotal.getText().toString();


        total = total.replaceAll("[^0-9]", "");

        loadingProgress.ShowProgress(Payment.this, "Please Wait", false);

        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().OrdersInsert(UserID, OrderId, OrderDate, OrderExpectDate, total, UserName, UserMobile, "online", s);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.code() == 200) {
                    if (loadingProgress != null) {
                        loadingProgress.hideProgress();
                    }

                    if (response.body().getSuccess() != null && response.body().getSuccess().equals("Order Placed")) {

                        Log.d("PAYMENT", "ORDER-STATUS:::::: true");

                        Toast.makeText(Payment.this, response.body().getSuccess(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Payment.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("TAG", "Order");
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Payment.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 500) {
                    if (loadingProgress != null) {
                        loadingProgress.hideProgress();
                    }
                    Toast.makeText(Payment.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

        if (loadingProgress != null) {
            loadingProgress.hideProgress();
        }

        Log.e(TAG, "Payment-Error::::" + s + "\n" + paymentData);

        Toast.makeText(getApplicationContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
    }
}