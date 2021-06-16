package com.example.medicalgateway;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.medicalgateway.adapters.CheckAppointmentsPatientAdapter;
import com.example.medicalgateway.databinding.ActivityPreviousAppointmentsBinding;
import com.example.medicalgateway.datamodels.PatientAppointment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class PreviousAppointmentsActivity extends AppCompatActivity {
    private static final int DB_LIMIT = 20;
    private final String CHILD_NAME = "appointment_info";
    private ActivityPreviousAppointmentsBinding mBinding;
    private DatabaseReference rootRef;
    private CheckAppointmentsPatientAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityPreviousAppointmentsBinding.inflate(getLayoutInflater());

        rootRef = FirebaseDatabase.getInstance()
                                  .getReference();

        String uid = FirebaseAuth.getInstance()
                                 .getUid();

        if (uid != null) {
            Query query = rootRef.child(CHILD_NAME)
                                 .child(uid)
                                 .limitToLast(DB_LIMIT);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        mBinding.textNoAppoint.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            FirebaseRecyclerOptions<PatientAppointment> options = new FirebaseRecyclerOptions.Builder<PatientAppointment>().setQuery(query, PatientAppointment.class)
                                                                                                                           .build();

            adapter = new CheckAppointmentsPatientAdapter(options);


            mBinding.recyclerAppointments.setLayoutManager(new LinearLayoutManager(this));

            mBinding.recyclerAppointments.setAdapter(adapter);
        } else {
            Log.d("test", "uid null");
        }
        setContentView(mBinding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}