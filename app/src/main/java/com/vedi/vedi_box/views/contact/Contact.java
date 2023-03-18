package com.vedi.vedi_box.views.contact;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.vedi.vedi_box.R;
import com.vedi.vedi_box.databinding.ActivityContactBinding;
import com.vedi.vedi_box.views.accoount_info.AccountInfoViewModel;

public class Contact extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CALL = 121;

    ActivityContactBinding binding;
    private MediaPlayer mp;
    private AccountInfoViewModel accountInfoViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mp = MediaPlayer.create(this, R.raw.single_shot);
        accountInfoViewModel = new ViewModelProvider(this).get(AccountInfoViewModel.class);

        binding.conatctDetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.contactPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cell = "8608212581";
                if (ContextCompat.checkSelfPermission(Contact.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + cell));
                    startActivity(callIntent);
                } else {
                    ActivityCompat.requestPermissions(Contact.this, new String[]{Manifest.permission.CALL_PHONE},
                            PERMISSION_REQUEST_CALL);
                }
            }
        });
        binding.contactWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "8124512581";
                String url = "https://api.whatsapp.com/send?phone=" + "+91" + number;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}