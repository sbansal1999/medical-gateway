package com.example.medicalgateway;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.medicalgateway.adapters.UpcomingAppointmentsDoctorAdapter;
import com.example.medicalgateway.databinding.ActivityUpcomingAppointDoctorBinding;
import com.example.medicalgateway.datamodels.DoctorAppointment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class UpcomingAppointDoctorActivity extends AppCompatActivity {
    private static final String CHILD_NAME = "appointment_info";
    private ActivityUpcomingAppointDoctorBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String pattern = "dd-MM-yyyy";
        String dateInString = new SimpleDateFormat(pattern, Locale.ENGLISH).format(new Date());

        ArrayList<DoctorAppointment> doctorAppointments = new ArrayList<>();

        mBinding = ActivityUpcomingAppointDoctorBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        String docUserID = FirebaseAuth.getInstance().getUid();


        FirebaseDatabase.getInstance()
                .getReference()
                .child("doctors_info")
                .child(docUserID)
                .child("doctorID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        String docID = snapshot.getValue().toString();

                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child(CHILD_NAME)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            for (DataSnapshot mainSnap : dataSnapshot.getChildren()) {
                                                if (mainSnap.child("prefDoctorID")
                                                        .getValue()
                                                        .toString()
                                                        .equals(docID)) {

                                                    mBinding.textNoAppointDoc.setVisibility(View.INVISIBLE);

                                                    DoctorAppointment value = mainSnap.getValue(DoctorAppointment.class);

                                                    if (value != null) {
                                                        String dateString = value.getDateAppoint();
                                                        try {
                                                            Date date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
                                                                    .parse(dateString);

                                                            int result = date.compareTo(new Date(System.currentTimeMillis()));

                                                            if (result >= 0) {
                                                                doctorAppointments.add(value);
                                                            }

                                                        } catch (ParseException e) {
                                                            Log.d("TAG", "onDataChange: " + "date error");
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        if (!doctorAppointments.isEmpty()) {
                                            UpcomingAppointmentsDoctorAdapter adapter = new UpcomingAppointmentsDoctorAdapter(doctorAppointments, getApplicationContext());

                                            mBinding.recyclerUpcoming.setAdapter(adapter);
                                            mBinding.recyclerUpcoming.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


    }
}