package com.example.medicalgateway;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
Animation topAnim,bottomAnim;
Context conText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ConnectivityManager connectivitymanager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        binding.splashImage.setAnimation(topAnim);
        binding.title.setAnimation(bottomAnim);
        conText = this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(networkinfo==null || !networkinfo.isConnected() || !networkinfo.isAvailable())
                {
                    Dialog diaLog = new Dialog(conText);
                    diaLog.setContentView(R.layout.alert_dialog);
                    diaLog.setCanceledOnTouchOutside(false);
                    diaLog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.WRAP_CONTENT);
                    diaLog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    diaLog.getWindow().getAttributes().windowAnimations =
                            android.R.style.Animation_Dialog;
                    Button btTryagain = diaLog.findViewById(R.id.try_again_button);
                    btTryagain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            recreate();
                        }
                    });
                    diaLog.show();
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_SCREEN_DURATION);
    }

    private boolean checkIfUserIsAlreadySignedIn() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.contains(SharedPreferencesInfo.PREF_IS_USER_SIGNED_IN)) {
            return sharedPreferences.getBoolean(SharedPreferencesInfo.PREF_IS_USER_SIGNED_IN, false);
        }
        return false;
    }
}