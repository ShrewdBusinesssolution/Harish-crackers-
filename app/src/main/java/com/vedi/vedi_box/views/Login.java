package com.vedi.vedi_box.views;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.vedi.vedi_box.R;
import com.vedi.vedi_box.databinding.ActivityLoginBinding;
import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.utilities.Constants;
import com.vedi.vedi_box.utilities.LoadingProgress;
import com.vedi.vedi_box.utilities.PreferManager;
import com.vedi.vedi_box.utilities.RetrofitClient;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    FirebaseAuth mAuth;
    ActivityLoginBinding binding;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PreferManager preferManager;
    private MediaPlayer mp;
    private String loginType = "Nothing";
    private LoadingProgress loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mp = MediaPlayer.create(Login.this, R.raw.single_shot);
        preferManager = new PreferManager(Login.this);
        loadingProgress = LoadingProgress.getInstance();
        mAuth = FirebaseAuth.getInstance();

        binding.loginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                if (!binding.phoneNumber.getText().toString().trim().isEmpty()) {
                    if (binding.phoneNumber.getText().toString().trim().length() == 10) {
                        if (loginType.equals("Something")) {
                            if (!binding.otp1.getText().toString().trim().isEmpty() && !binding.otp2.getText().toString().trim().isEmpty() && !binding.otp3.getText().toString().trim().isEmpty() && !binding.otp4.getText().toString().trim().isEmpty() && !binding.otp5.getText().toString().trim().isEmpty() && !binding.otp6.getText().toString().trim().isEmpty()) {
                                String enteredOtp = binding.otp1.getText().toString()
                                        + binding.otp2.getText().toString()
                                        + binding.otp3.getText().toString()
                                        + binding.otp4.getText().toString()
                                        + binding.otp5.getText().toString()
                                        + binding.otp6.getText().toString();

                                verifyPhoneNumberWithCode(mVerificationId, enteredOtp);
                            }
                        } else {
                            String Mobile = binding.phoneNumber.getText().toString().trim();

                            String first = Mobile.substring(0, 1);

                            if (first.equals("6") || first.equals("7") || first.equals("8") || first.equals("9")) {
                                Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().LoginIntoVB(Mobile, preferManager.getString(Constants.KEY_FCM_TOKEN));
                                call.enqueue(new Callback<APIResponse>() {
                                    @Override
                                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                        if (response.code() == 200) {
                                            if (response.body().getSuccess() != null && response.body().getSuccess().equals("Login successfully.")) {
                                                preferManager.putString(Constants.KEY_USER_ID, response.body().getUser_id());
                                                preferManager.putString(Constants.KEY_USER_MOBILE, Mobile);

//                                            loadingProgress.ShowProgress(Login.this, "Please wait", false);
//                                            startPhoneNumberVerification(Mobile);

                                                startActivity(new Intent(Login.this, MainActivity.class));
                                                finish();
                                            }
                                            Log.d(TAG, "RESPONSE::::" + preferManager.getString(Constants.KEY_USER_ID));
                                        } else if (response.code() == 500) {
                                            Toast.makeText(Login.this, "Internal Server error", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<APIResponse> call, Throwable t) {

                                    }
                                });

                            } else {
                                Toast.makeText(getApplicationContext(), "Please enter valid number", Toast.LENGTH_SHORT).show();
                            }


                        }
                    } else {
                        Toast.makeText(Login.this, "Please Enter Correct Number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.loginResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.phoneNumber.getText().toString().trim().isEmpty()) {
                    if (binding.phoneNumber.getText().toString().trim().length() == 10) {
                        if (!loginType.equals("Something")) {
                            resendVerification(binding.phoneNumber.getText().toString().trim());

                        }
                    }
                }

            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken ResendingToken) {
                super.onCodeSent(s, ResendingToken);
                mVerificationId = s;
                forceResendingToken = ResendingToken;
                binding.phoneLayout.setVisibility(View.GONE);
                binding.otpLayout.setVisibility(View.VISIBLE);
                binding.loginSubmit.setText("Submit");
                binding.otpPhoneNumber.setText("+91 " + binding.phoneNumber.getText().toString());
                loginType = "Something";
                if (loadingProgress != null) {
                    loadingProgress.hideProgress();
                }
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                Log.d("TAG", "CODE:::" + code);
                if (code != null)
                    signInUsingCredentilas(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "Message:::" + e.getMessage());
            }
        };

        otpNumberMove();
    }

    private void resendVerification(String number) {
//        PhoneAuthProvider.verifyPhoneNumber(
//                PhoneAuthOptions
//                        .newBuilder(FirebaseAuth.getInstance())
//                        .setActivity(Login.this)
//                        .setPhoneNumber("+91" + number)
//                        .setTimeout(60L, TimeUnit.SECONDS)
//                        .setCallbacks(mCallbacks)
//                        .setForceResendingToken(forceResendingToken)
//                        .build());

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + number,
                10,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks //Object of OnVerificationStateChangedCallbacks
        );

    }


    private void startPhoneNumberVerification(String number) {

//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(mAuth)
//                        .setPhoneNumber("+91" + number)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(Login.this)                 // Activity (for callback binding)
//                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks //Object of OnVerificationStateChangedCallbacks
        );

        new CountDownTimer(1000 * 60, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.loginResendOtp.setText("seconds remaining: " + millisUntilFinished / 1000);


                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                binding.loginResendOtp.setEnabled(true);
                binding.loginResendOtp.setText("RESEND");

                binding.loginResendOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String number = binding.phoneNumber.getText().toString();
                        if (TextUtils.isEmpty(number)) {
                            startPhoneNumberVerification(number);
                        }
                    }
                });


            }

        }.start();
    }

    private void verifyPhoneNumberWithCode(String mVerificationId, String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInUsingCredentilas(credential);
    }

    private void signInUsingCredentilas(PhoneAuthCredential credential) {

        loadingProgress.ShowProgress(Login.this, "Please wait", false);

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (loadingProgress != null) {
                        loadingProgress.hideProgress();
                    }
                    Toast.makeText(Login.this, "Login!", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                } else {
                    if (loadingProgress != null) {
                        loadingProgress.hideProgress();
                    }
                    Toast.makeText(Login.this, " please enter valid OTP & try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void otpNumberMove() {
        mp.start();
        binding.otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().trim().isEmpty()) {
                    binding.otp2.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    binding.otp2.requestFocus();
                } else {
                    binding.otp1.requestFocus();
                }
            }
        });
        binding.otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().trim().isEmpty()) {
                    binding.otp3.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    binding.otp3.requestFocus();
                } else {
                    binding.otp1.requestFocus();
                }
            }
        });
        binding.otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().trim().isEmpty()) {
                    binding.otp4.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    binding.otp4.requestFocus();
                } else {
                    binding.otp2.requestFocus();
                }
            }
        });
        binding.otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().trim().isEmpty()) {
                    binding.otp5.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    binding.otp5.requestFocus();
                } else {
                    binding.otp3.requestFocus();
                }
            }
        });
        binding.otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().trim().isEmpty()) {
                    binding.otp6.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    binding.otp6.requestFocus();
                } else {
                    binding.otp4.requestFocus();
                }
            }
        });
        binding.otp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    binding.otp5.requestFocus();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            finish();
//            startActivity(new Intent(Login.this, MainActivity.class));
//        }

        String userID = preferManager.getString(Constants.KEY_USER_ID);
        if (userID != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
    }
}

// PhoneAuthProvider.getInstance().verifyPhoneNumber(
//         "+91"+Mobile, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mcall);

//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcall = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        @Override
//        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//            String code = phoneAuthCredential.getSmsCode();
//            if (code != null) {
//                verifyCode(code);
//            }
//        }
//
//        @Override
//        public void onVerificationFailed(@NonNull FirebaseException e) {
//            binding.otpLayout.setVisibility(View.GONE);
//            binding.phoneLayout.setVisibility(View.VISIBLE);
//            Toast.makeText(Login.this, "Error Please Check Internet Connection", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//            codeBySystem = s;
//            binding.phoneLayout.setVisibility(View.GONE);
//            binding.otpLayout.setVisibility(View.VISIBLE);
//            binding.loginSubmit.setText("Submit");
//            binding.otpPhoneNumber.setText("+91 " + binding.phoneNumber.getText().toString());
//            loginType = "Something";
//        }
//    };
//
//    private void verifyCode(String code) {
//        Log.d(TAG, "VERIFY-CODE:::::" + code + "\n VERIFICATION-ID:::" + codeBySystem);
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
//        signInUsingCredentilas(credential);
//    }
//
//    private void signInUsingCredentilas(PhoneAuthCredential credential) {
//
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(Login.this, "Login!", Toast.LENGTH_SHORT).show();
//                            finish();
//                            startActivity(new Intent(Login.this, MainActivity.class));
//                        } else {
//
//                            Toast.makeText(Login.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//    }
