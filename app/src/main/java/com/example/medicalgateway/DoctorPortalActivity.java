package com.example.medicalgateway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import com.example.medicalgateway.databinding.ActivityDoctorPortalBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DoctorPortalActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "DoctorPortalActivity";


    private ActivityDoctorPortalBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDoctorPortalBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        //Set Default Shared Pref on user sign in
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(SharedPreferencesInfo.PREF_IS_USER_SIGNED_IN, true)
                .apply();

        mBinding.bottomNavigationDoctorView.setOnNavigationItemSelectedListener(this);
        mBinding.bottomNavigationDoctorView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_home) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.container, new HomeFragment_Doctor())
                    .commit();
            return true;
        } else if (itemId == R.id.navigation_pharmacy) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.container, new PharmacyPatientFragment())
                    .commit();
            return true;
        } else if (itemId == R.id.navigation_profile) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.container, new ProfileFragment())
                    .commit();
            return true;
        }
        return false;
    }
}