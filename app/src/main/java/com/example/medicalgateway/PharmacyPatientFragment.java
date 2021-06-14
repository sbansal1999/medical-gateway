package com.example.medicalgateway;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.medicalgateway.databinding.FragmentPharmacyPatientBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class PharmacyPatientFragment extends Fragment {
    private final int DB_LIMIT = 20;
    private final String CHILD_NAME = "medicine_info";
    private FragmentPharmacyPatientBinding mBinding;
    private PharmacyAdapter adapter;
    private DatabaseReference rootRef;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPharmacyPatientBinding.inflate(inflater);

        mBinding.buttonSearchMed.setOnClickListener(view -> searchMed());

        rootRef = FirebaseDatabase.getInstance()
                                  .getReference();

        Query query = rootRef.child(CHILD_NAME)
                             .limitToLast(DB_LIMIT);

        FirebaseRecyclerOptions<MedicineInfo> options = new FirebaseRecyclerOptions.Builder<MedicineInfo>().setQuery(query, MedicineInfo.class)
                                                                                                           .build();

        adapter = new PharmacyAdapter(options);
        mBinding.recyclerPharmacy.setLayoutManager(new LinearLayoutManager(getContext()));

        mBinding.recyclerPharmacy.setAdapter(adapter);

        return mBinding.getRoot();

    }

    private void searchMed() {
        closeSoftKeyboard();

        final FirebaseRecyclerOptions[] options = new FirebaseRecyclerOptions[]{null};

        String searchText = mBinding.editSearch.getEditText()
                                               .getText()
                                               .toString()
                                               .toLowerCase();

        if (searchText.isEmpty()) {
            showToast("No Search Text Given");
            return;
        }

        searchText = Character.toUpperCase(searchText.charAt(0)) + searchText.substring(1);

        showToast("Searching");

        Query query = rootRef.child(CHILD_NAME)
                             .orderByChild("name")
                             .startAt(searchText)
                             .endAt(searchText + "\uf8ff")
                             .limitToFirst(DB_LIMIT);

        String finalSearchText = searchText;

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    options[0] = new FirebaseRecyclerOptions.Builder<MedicineInfo>().setQuery(query, MedicineInfo.class)
                                                                                    .build();

                } else {
                    Query q1 = rootRef.child(CHILD_NAME)
                                      .orderByChild("category")
                                      .startAt(finalSearchText)
                                      .endAt(finalSearchText + "\uf8ff")
                                      .limitToFirst(DB_LIMIT);
                    options[0] = new FirebaseRecyclerOptions.Builder<MedicineInfo>().setQuery(q1, MedicineInfo.class)
                                                                                    .build();
                }

                adapter = new PharmacyAdapter(options[0]);

                mBinding.recyclerPharmacy.setLayoutManager(new LinearLayoutManager(getContext()));

                mBinding.recyclerPharmacy.setAdapter(adapter);
                adapter.startListening();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    /**
     * Method that closes any opened soft Keyboard
     */
    private void closeSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            View view = getActivity().getCurrentFocus();
            if (view == null) {
                view = new View(getActivity());
            }
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (NullPointerException ignored) {

        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT)
             .show();

    }

}