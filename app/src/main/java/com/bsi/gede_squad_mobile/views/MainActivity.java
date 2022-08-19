package com.bsi.gede_squad_mobile.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.bsi.gede_squad_mobile.Utils.AppUtil;
import com.bsi.gede_squad_mobile.databinding.ActivityMenuHomeGedeSquadBinding;
import com.bsi.gede_squad_mobile.databinding.ActivityWebviewGedeBinding;
import com.google.android.material.internal.VisibilityAwareImageButton;

public class MainActivity extends AppCompatActivity {

    ActivityMenuHomeGedeSquadBinding binding;
    private int exitCounter=0;
    private boolean clickedSomething=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuHomeGedeSquadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.webview.getSettings().setLoadsImagesAutomatically(true);
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setDomStorageEnabled(true);

        // Tiga baris di bawah ini agar laman yang dimuat dapat
        // melakukan zoom.
        binding.webview.getSettings().setSupportZoom(true);
        binding.webview.getSettings().setBuiltInZoomControls(true);
        binding.webview.getSettings().setDisplayZoomControls(false);
        // Baris di bawah untuk menambahkan scrollbar di dalam WebView-nya
        binding.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.webview.setWebViewClient(new WebViewClient());
        binding.webview.loadUrl("https://gedesquad.com/absensi-input");

        allOnClick();
    }

    private void allOnClick(){
        binding.toolbar.tvPageTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent=new Intent(MainActivity.this,InputAbsensiActivity.class);
                startActivity(intent);
                return false;
            }
        });

        binding.cvInputAbsensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.loading.progressbarLoading.setVisibility(View.VISIBLE);
                if(clickedSomething){
                    binding.webview.loadUrl("https://gedesquad.com/absensi-input");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.loading.progressbarLoading.setVisibility(View.GONE);
                            clickedSomething=false;
                            binding.webview.setVisibility(View.VISIBLE);
                            binding.svMenuGedeSquad.setVisibility(View.GONE);
                            binding.toolbar.getRoot().setVisibility(View.GONE);
                            binding.tvFooter.setVisibility(View.GONE);
                        }
                    }, 2000);
                }
                else{
                    binding.loading.progressbarLoading.setVisibility(View.GONE);
                    clickedSomething=false;
                    binding.webview.setVisibility(View.VISIBLE);
                    binding.svMenuGedeSquad.setVisibility(View.GONE);
                    binding.toolbar.getRoot().setVisibility(View.GONE);
                    binding.tvFooter.setVisibility(View.GONE);
                }
            }
        });
        binding.cvReportAbsensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.loading.progressbarLoading.setVisibility(View.VISIBLE);
                binding.webview.loadUrl("https://gedesquad.com/absensi-report");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.loading.progressbarLoading.setVisibility(View.GONE);
                        clickedSomething=true;
                        binding.webview.setVisibility(View.VISIBLE);
                        binding.svMenuGedeSquad.setVisibility(View.GONE);
                        binding.toolbar.getRoot().setVisibility(View.GONE);
                        binding.tvFooter.setVisibility(View.GONE);
                    }
                }, 2000);


            }
        });
        binding.cvDashboardAbsensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.loading.progressbarLoading.setVisibility(View.VISIBLE);
                binding.webview.loadUrl("https://gedesquad.com/dashboard");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.loading.progressbarLoading.setVisibility(View.GONE);
                        clickedSomething=true;
                        binding.webview.setVisibility(View.VISIBLE);
                        binding.svMenuGedeSquad.setVisibility(View.GONE);
                        binding.toolbar.getRoot().setVisibility(View.GONE);
                        binding.tvFooter.setVisibility(View.GONE);
                    }
                }, 2000);

            }
        });

    }

    @Override
    public void onBackPressed() {
        AppUtil.logSecure("exitcounter",Integer.toString(exitCounter));
        exitCounter++;
        if(binding.svMenuGedeSquad.getVisibility()== View.GONE){
            binding.svMenuGedeSquad.setVisibility(View.VISIBLE);
            binding.toolbar.getRoot().setVisibility(View.VISIBLE);
            binding.webview.setVisibility(View.GONE);
            binding.tvFooter.setVisibility(View.VISIBLE);
            exitCounter=0;
        }
        else{
            if(exitCounter>=2){
                finishAffinity();
            }
            else{
                exitCounter++;
                Toast.makeText(this, "Tekan Back Sekali Lagi Untuk Keluar Aplikasi", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exitCounter=0;
                    }
                }, 2000);



            }}
            }

        }

