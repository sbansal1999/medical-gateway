package com.example.medicalgateway;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.medicalgateway.adapters.ReportsAdapter;
import com.example.medicalgateway.databinding.ActivityCheckReportsBinding;
import com.example.medicalgateway.datamodels.Reports;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class CheckReportsPatientActivity extends AppCompatActivity {
    private static final int DB_LIMIT = 20;
    private final String CHILD_NAME = "patients_info";
    private ActivityCheckReportsBinding mBinding;
    private ReportsAdapter adapter;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityCheckReportsBinding.inflate(getLayoutInflater());
        rootRef = FirebaseDatabase.getInstance().getReference();

        String uid = FirebaseAuth.getInstance().getUid();

        if (uid != null) {
            Query query = rootRef.child(CHILD_NAME).child(uid).child("reports").limitToLast(DB_LIMIT);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        mBinding.textNoReports.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            FirebaseRecyclerOptions<Reports> options = new FirebaseRecyclerOptions.Builder<Reports>()
                    .setQuery(query, Reports.class).build();

            adapter = new ReportsAdapter(options, this);

            mBinding.recyclerReports.setLayoutManager(new LinearLayoutManager(this));

            mBinding.recyclerReports.setAdapter(adapter);
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