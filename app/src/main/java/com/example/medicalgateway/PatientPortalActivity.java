package com.example.medicalgateway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.medicalgateway.databinding.ActivityPatientportalBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PatientPortalActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ActivityPatientportalBinding binding;
    private static final String TAG = "PatientPortalActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientportalBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.bottomNavigationPatientView.setOnNavigationItemSelectedListener(this);
        binding.bottomNavigationPatientView.setSelectedItemId(R.id.navigation_home);

    }
    HomeFragment homeFragment = new HomeFragment();
    PharmacyPatientFragment pharmacyPatientFragment = new PharmacyPatientFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container , homeFragment).commit();
                return true;
            case R.id.navigation_pharmacy:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container , pharmacyPatientFragment).commit();
                return true;
            case R.id.navigation_profile:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container , profileFragment).commit();
                return true;
        }
        return false;
    }
}