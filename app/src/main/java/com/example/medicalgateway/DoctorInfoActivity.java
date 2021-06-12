package com.example.medicalgateway;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.medicalgateway.databinding.ActivityDoctorsInfoBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import kotlin.jvm.internal.Ref;

public class DoctorInfoActivity extends AppCompatActivity {
    private static final String dbChild = "doctors_info";
    private final int dbLimit = 20;
    private ActivityDoctorsInfoBinding mBinding;
    private DoctorInfoAdapter adapter;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDoctorsInfoBinding.inflate(getLayoutInflater());

        rootRef = FirebaseDatabase.getInstance()
                                  .getReference();

        Query query = rootRef.child(dbChild)
                             .limitToLast(dbLimit);


        FirebaseRecyclerOptions<DoctorInfo> options = new FirebaseRecyclerOptions.Builder<DoctorInfo>().setQuery(query, DoctorInfo.class)
                                                                                                           .build();

        adapter = new DoctorInfoAdapter(options);
        mBinding.recyclerDoctor.setLayoutManager(new GridLayoutManager(this, 2));

        mBinding.recyclerDoctor.setAdapter(adapter);

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