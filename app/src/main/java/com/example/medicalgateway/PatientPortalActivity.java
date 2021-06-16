package com.example.medicalgateway;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicalgateway.databinding.ActivityPatientPortalBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PatientPortalActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityPatientPortalBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityPatientPortalBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        //Set Default Shared Pref on user sign in
        PreferenceManager.getDefaultSharedPreferences(this)
                         .edit()
                         .putBoolean(SharedPreferencesInfo.PREF_IS_USER_SIGNED_IN, true)
                         .apply();

        mBinding.bottomNavigationPatientView.setOnNavigationItemSelectedListener(this);
        mBinding.bottomNavigationPatientView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_home) {
            getSupportFragmentManager().beginTransaction()
                                       .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                       .replace(R.id.container, new HomeFragment())
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