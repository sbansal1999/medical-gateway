package com.example.medicalgateway;

import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

public class Available_beds_activity extends AppCompatActivity {
CardView cd1;
Toolbar toolbar1;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_available_beds_activity);
toolbar1 = (Toolbar)findViewById(R.id.toolbar1);
        cd1 = (CardView)findViewById(R.id.cardView1) ;
        toolbar1.setTitle("Available beds in Hospital");
        toolbar1.setTitleTextColor(Color.WHITE);
        toolbar1.setTitleMarginStart(10);



    }
}