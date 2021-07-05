package com.example.medicalgateway;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutHospitalActivity extends AppCompatActivity {
    Toolbar toolbar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_hospital_activity);
        toolbar1 = findViewById(R.id.include);
        toolbar1.setTitle("About Hospital");
        toolbar1.setTitleTextColor(Color.WHITE);
        toolbar1.setTitleMarginStart(10);
    }
}