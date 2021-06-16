package com.example.medicalgateway;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class AvailableBedsActivity extends AppCompatActivity {
    CardView cd1;
    Toolbar toolbar1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_available_beds_activity);
        toolbar1 = findViewById(R.id.toolbar);
        cd1 = findViewById(R.id.cardView1);
        toolbar1.setTitle("Available beds in Hospital");
        toolbar1.setTitleTextColor(Color.WHITE);
        toolbar1.setTitleMarginStart(10);


    }
}