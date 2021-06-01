package com.example.medicalgateway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Doctors_Info_activity extends AppCompatActivity {
    Toolbar toolbar1;
    ImageView doc1,doc2,doc3,doc4,doc5,doc6,doc7,doc8,doc9,doc10;
    TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7,txt8,txt9,txt10;
    TextView prof1,prof2,prof3,prof4,prof5,prof6,prof7,prof10,prof8,prof9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors__info);
        toolbar1 = (Toolbar)findViewById(R.id.toolbar1);
        toolbar1.setTitle("Specialized Doctors in Hospital");
        toolbar1.setTitleTextColor(Color.WHITE);
        toolbar1.setTitleMarginStart(10);
        doc1=(ImageView)findViewById(R.id.doc1);
        doc1.setImageResource(R.drawable.doc1);
        txt1=(TextView)findViewById((R.id.cardt1));
        txt1.setText("Dr.PK DAVE");
        prof1=(TextView)findViewById(R.id.cardp1);
        prof1.setText("Neurologist");
        doc2=(ImageView)findViewById(R.id.doc2);
        doc2.setImageResource(R.drawable.doc2);
        txt2=(TextView)findViewById((R.id.cardt2));
        txt2.setText("Dr.Megha Singhla");
        prof2=(TextView)findViewById(R.id.cardp2);
        prof2.setText("Orthopedics");
        doc3=(ImageView)findViewById(R.id.doc3);
        doc3.setImageResource(R.drawable.doc3);
        txt3=(TextView)findViewById((R.id.cardt3));
        txt3.setText("Dr.Prakash M Doshi");
        prof3=(TextView)findViewById(R.id.cardp3);
        prof3.setText("Cardiologist");
        doc4=(ImageView)findViewById(R.id.doc4);
        doc4.setImageResource(R.drawable.doc4);
        txt4=(TextView)findViewById((R.id.cardt4));
        txt4.setText("Dr.Ashok Seth");
        prof4=(TextView)findViewById(R.id.cardp4);
        prof4.setText("Physician");
        doc5=(ImageView)findViewById(R.id.doc5);
        doc5.setImageResource(R.drawable.doc5);
        txt5=(TextView)findViewById((R.id.cardt5));
        txt5.setText("Dr.Anil Behl");
        prof5=(TextView)findViewById(R.id.cardp5);
        prof5.setText("Plastic and Reconstructive Surgery");
        doc6=(ImageView)findViewById(R.id.doc6);
        doc6.setImageResource(R.drawable.doc6);
        txt6=(TextView)findViewById((R.id.cardt6));
        txt6.setText("Dr.Sanjeev Baksh");
        prof6=(TextView)findViewById(R.id.cardp6);
        prof6.setText("Diabetologist");
        doc7=(ImageView)findViewById(R.id.doc7);
        doc7.setImageResource(R.drawable.doc7);
        txt7=(TextView)findViewById((R.id.cardt7));
        txt7.setText("Dr.Anoop Mishra");
        prof7=(TextView)findViewById(R.id.cardp7);
        prof7.setText("Dentist");
        doc8=(ImageView)findViewById(R.id.doc8);
        doc8.setImageResource(R.drawable.doc8);
        txt8=(TextView)findViewById((R.id.cardt8));
        txt8.setText("Dr.Suryanarayana");
        prof8=(TextView)findViewById(R.id.cardp8);
        prof8.setText("Endocrinologist");
        doc9=(ImageView)findViewById(R.id.doc9);
        doc9.setImageResource(R.drawable.doc9);
        txt9=(TextView)findViewById((R.id.cardt9));
        txt9.setText("Dr.Anoop Mishra");
        prof9=(TextView)findViewById(R.id.cardp9);
        prof9.setText("Gastreologist");
        doc10=(ImageView)findViewById(R.id.doc10);
        doc10.setImageResource(R.drawable.doc10);
        txt10=(TextView)findViewById((R.id.cardt10));
        txt10.setText("Dr.Rameshwaram ");
        prof10=(TextView)findViewById(R.id.cardp10);
        prof10.setText("ENT Specialist");



    }

}