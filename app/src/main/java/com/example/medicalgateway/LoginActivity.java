package com.example.medicalgateway;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.medicalgateway.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    //TODO enable sign in via google,facebook,etc.

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void verifyLogin(View view) {
    }
}