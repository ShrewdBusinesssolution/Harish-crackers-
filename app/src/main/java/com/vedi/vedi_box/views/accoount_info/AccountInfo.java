package com.vedi.vedi_box.views.accoount_info;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vedi.vedi_box.R;
import com.vedi.vedi_box.databinding.ActivityAccountInfoBinding;
import com.vedi.vedi_box.models.Bank;
import com.vedi.vedi_box.views.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class AccountInfo extends AppCompatActivity {
    ActivityAccountInfoBinding binding;
    private MediaPlayer mp;
    private AccountInfoViewModel accountInfoViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mp = MediaPlayer.create(this, R.raw.single_shot);
        accountInfoViewModel = new ViewModelProvider(this).get(AccountInfoViewModel.class);

        binding.accountDetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        accountInfoViewModel.getBank().observe(this, new Observer<List<Bank>>() {
            @Override
            public void onChanged(List<Bank> banks) {
                for (Bank bank : banks) {
//                    Bank bankDetails = new Bank(bank.getBank_name(), bank.getAccount_name(), bank.getAccount_number(), bank.getIfsc_number(), bank.getBranch(), bank.getGpay_upi(), bank.getPhonepay_upi());
                    Bank bankDetails = new Bank("ICICI BANK", "Hirish Crackers", "617205015735", "ICICI0006172", "Sivakasi", "Hirishcrackers@okaxis", "Hirishcrackers@ypl");
                    binding.setAccount(bankDetails);


                    binding.accountDetailsGpay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(getApplicationContext(), "Upi Copied to ClipBoard", Toast.LENGTH_SHORT).show();
                            setClipboard(AccountInfo.this, bank.getGpay_upi());
                        }
                    });
                    binding.accountDetailsPhpay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(getApplicationContext(), "Upi Copied to ClipBoard", Toast.LENGTH_SHORT).show();
                            setClipboard(AccountInfo.this, bank.getPhonepay_upi());
                        }
                    });

                }
            }
        });

        binding.accountDetailsOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();

                Intent intent = new Intent(AccountInfo.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("TAG", "Order");
                startActivity(intent);
                finish();
            }
        });


    }

    private void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    private void googlepay() {
        String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";


        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "mjavahar56@okicici")
                .appendQueryParameter("pn", "Harikii testing")
                .appendQueryParameter("tn", "payment just for testing")
                .appendQueryParameter("am", "1")
                .appendQueryParameter("cu", "INR")
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        try {
            startActivityForResult(intent, 101);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "GooglePay Not Found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String value = data.getStringExtra("response");

                    //Collection of data from payment
                    ArrayList<String> list = new ArrayList<>();
                    list.add(value);
                    getStatus(list.get(0));
                }

            } else {
                Toast.makeText(getApplicationContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void getStatus(String data) {
        boolean isPaymentCancel = false;
        boolean isPaymentSuccess = false;

        String value[] = data.split("&");
        for (int i = 0; i < value.length; i++) {

            String checkString[] = value[i].split("=");
            if (checkString.length >= 2) {
                if (checkString[0].toLowerCase().equals("status")) {
                    if (checkString[1].equals("success")) {
                        isPaymentSuccess = true;
                    }
                }
            } else {
                isPaymentCancel = true;
            }
        }

        if (isPaymentSuccess) {
            Toast.makeText(getApplicationContext(), "Payment Successful", Toast.LENGTH_SHORT).show();
        } else if (isPaymentCancel) {
            Toast.makeText(getApplicationContext(), "Payment Canceled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            super.onBackPressed();
            finish();
        } else {
            Intent intent = new Intent(AccountInfo.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("TAG", "Order");
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();

        }

    }
}