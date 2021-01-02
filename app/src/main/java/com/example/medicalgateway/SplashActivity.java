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

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN_DURATION = 3000;

    //TODO decide in this activity what screen is to be shown next
    Animation topanim, bottomanim;
    ImageView image;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ConnectivityManager connectivitymanager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();

        topanim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomanim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        image = findViewById(R.id.splash_image);
        title = findViewById(R.id.textView2);
        image.setAnimation(topanim);
        title.setAnimation(bottomanim);

        new Handler().postDelayed(() -> {
            if (networkinfo == null || !networkinfo.isConnected() || !networkinfo.isAvailable()) {
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.alert_dialog);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow()
                      .setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow()
                      .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow()
                      .getAttributes().windowAnimations = android.R.style.Animation_Dialog;
                Button btTryagain = dialog.findViewById(R.id.try_again_button);
                btTryagain.setOnClickListener(view -> recreate());
                dialog.show();
            } else {
                Intent intent = checkIfUserIsAlreadySignedIn() ? new Intent(this, PatientPortalActivity.class) : new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
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