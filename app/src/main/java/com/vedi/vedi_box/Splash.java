package com.vedi.vedi_box;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.vedi.vedi_box.adapters.testing.TestClient;
import com.vedi.vedi_box.databinding.ActivitySplashBinding;
import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.utilities.Constants;
import com.vedi.vedi_box.utilities.PreferManager;
import com.vedi.vedi_box.views.Login;
import com.vedi.vedi_box.views.MainActivity;
import com.vedi.vedi_box.views.shop.ShopViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Splash extends AppCompatActivity {
    ActivitySplashBinding splashBinding;


    private PreferManager preferManager;
    private FirebaseAuth mAuth;
    private ShopViewModel shopViewModel;

    Handler handler = new Handler();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = splashBinding.getRoot();
        setContentView(view);

        preferManager = new PreferManager(this);
        final MediaPlayer mp = MediaPlayer.create(Splash.this, R.raw.mixkit_fireworks_bang_in_sky);
        float speed = 0.75f;
        mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed));

        splashBinding.splashAnimation.playAnimation();
        mp.start();
        mAuth = FirebaseAuth.getInstance();
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);


        FirebaseUser user = mAuth.getCurrentUser();


        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful() && task.getResult() != null) {

                    preferManager.putString(Constants.KEY_FCM_TOKEN, task.getResult().getToken());


                }
            }
        });


        Call<APIResponse> call = TestClient.getInstance().getMyApi().getAllProduct();
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.code() == 200) {
                    APIResponse apiResponse=response.body();
                    Toast.makeText(getApplicationContext(), apiResponse.getSuccess(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });

        shopViewModel.getProducts();

//        FirebaseDynamicLinks.getInstance()
//                .getDynamicLink(getIntent())
//                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
//                    @Override
//                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
//                        // Get deep link from result (may be null if no link is found)
//                        Uri deepLink = null;
//                        if (pendingDynamicLinkData != null) {
//                            deepLink = pendingDynamicLinkData.getLink();
//
//                            String referLink = deepLink.toString();
//                            try {
//
//
////                                referLink = referLink.substring(referLink.lastIndexOf("=") + 1);
////
////
////                                String[] split = referLink.split("/");
////
////
////
////                                Log.e("TAG", "my link::::" + split[1] + "\n" + split[2] + "\n" + split[3] + "\n" + split[4]);
//
//                                startActivity(new Intent(Splash.this, MainActivity.class));
//                                finish();
//
//                            } catch (Exception e) {
//
//                            }
//                        } else {
//
//                            Intent intent = new Intent(Splash.this, Login.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }
//                })
//                .addOnFailureListener(this, new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("TAG", "getDynamicLink:onFailure", e);
//                    }
//                });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mp.stop();
                if (user != null) {

                    preferManager.putBoolean(Constants.KEY_ALERT_STATUS, true);

                    preferManager.putBoolean(Constants.SAVED_RECYCLER_POSITION, false);

                    preferManager.ProductClear(Splash.this);

                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(Splash.this, Login.class));
                    finish();
                }
            }
        }, 3000);
    }

}