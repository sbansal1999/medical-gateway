package com.example.medicalgateway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medicalgateway.databinding.ActivityRegisterBinding;
import com.example.medicalgateway.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN =  3000;
    private ActivitySplashBinding binding;

    //TODO decide in this activity what screen is to be shown next
Animation topanim,bottomanim;
Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ConnectivityManager connectivitymanager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();

        topanim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomanim = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        binding.splashImage.setAnimation(topanim);
        binding.title.setAnimation(bottomanim);
        context = this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(networkinfo==null || !networkinfo.isConnected() || !networkinfo.isAvailable())
                {
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.alert_dialog);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().getAttributes().windowAnimations =
                            android.R.style.Animation_Dialog;
                    Button btTryagain = dialog.findViewById(R.id.try_again_button);
                    btTryagain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            recreate();
                        }
                    });
                    dialog.show();
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_SCREEN);
    }
}