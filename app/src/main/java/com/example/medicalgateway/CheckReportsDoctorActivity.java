package com.example.medicalgateway;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.medicalgateway.adapters.ReportsAdapter;
import com.example.medicalgateway.databinding.ActivityCheckReportsDoctorBinding;
import com.example.medicalgateway.datamodels.Reports;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class CheckReportsDoctorActivity extends AppCompatActivity {
    private final String CHILD_NAME = "patients_info";
    private final int DB_LIMIT = 20;
    private ActivityCheckReportsDoctorBinding mBinding;
    private ReportsAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityCheckReportsDoctorBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

    }

    public void searchReports(View view) {
        closeSoftKeyboard();
        mBinding.recyclerReports.setAdapter(null);

        String pID = mBinding.editSearchId.getEditText().getText().toString().trim();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        Query query = rootRef.child(CHILD_NAME).orderByChild("patientID").equalTo(pID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnap : snapshot.getChildren()) {
                        if (dataSnap.hasChild("reports")) {
                            showToast("Loading Reports");

                            Query newQuery = rootRef.child(CHILD_NAME)
                                    .child(dataSnap.getKey())
                                    .child("reports").limitToLast(DB_LIMIT);

                            FirebaseRecyclerOptions<Reports> options = new FirebaseRecyclerOptions.Builder<Reports>()
                                    .setQuery(newQuery, Reports.class)
                                    .build();

                            adapter = new ReportsAdapter(options, getApplicationContext());

                            mBinding.recyclerReports.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            mBinding.recyclerReports.setAdapter(adapter);
                            adapter.startListening();

                        } else {
                            showToast("No Reports Found");
                        }
                    }

                } else {
                    showToast("No Patient Found!");
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Method that closes any opened soft Keyboard
     */
    private void closeSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            View view = getCurrentFocus();
//            if (view == null) {
//                view = new View();
//            }
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (NullPointerException ignored) {

        }
    }

}