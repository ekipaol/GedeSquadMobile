package com.bsi.gede_squad_mobile.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.bsi.gede_squad_mobile.Utils.AppUtil;
import com.bsi.gede_squad_mobile.databinding.ActivityInputAbsensiBinding;
import com.bsi.gede_squad_mobile.databinding.ActivityMenuHomeGedeSquadBinding;

public class InputAbsensiActivity extends AppCompatActivity {

    ActivityInputAbsensiBinding binding;
    private int exitCounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInputAbsensiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setSupportActionBar(binding.toolbar.toolbarChild);
        binding.toolbar.tvTitle.setText("Input Absensi");


        allOnClick();
    }

    private void allOnClick(){

        binding.toolbar.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputAbsensiActivity.super.onBackPressed();
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        AppUtil.logSecure("exitcounter",Integer.toString(exitCounter));
//        exitCounter++;
//        if(binding.svMenuGedeSquad.getVisibility()== View.GONE){
//            binding.svMenuGedeSquad.setVisibility(View.VISIBLE);
//            binding.toolbar.getRoot().setVisibility(View.VISIBLE);
//            exitCounter=0;
//        }
//        else{
//            if(exitCounter>=2){
//                finishAffinity();
//            }
//            else{
//                exitCounter++;
//                Toast.makeText(this, "Tekan Back Sekali Lagi Untuk Keluar Aplikasi", Toast.LENGTH_SHORT).show();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        exitCounter=0;
//                    }
//                }, 2000);
//
//
//
//            }}
//    }

}