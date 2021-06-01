package com.example.medicalgateway;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.medicalgateway.databinding.FragmentBookAppointmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookAppointmentFragment extends Fragment {

    private static final int IMAGE_DIMEN = 1000;
    private static final String CHILD_NAME = "appointment_info";
    private FragmentBookAppointmentBinding mBinding;
    private Calendar currentDate = Calendar.getInstance();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentBookAppointmentBinding.inflate(inflater, container, false);

        ArrayAdapter<CharSequence> doctorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.doctors, android.R.layout.simple_spinner_item);
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinnerDoctor.setAdapter(doctorAdapter);

        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Select");

        currentDate.add(Calendar.DATE, 1);
        String currentDate1 = DateFormat.getDateInstance(DateFormat.FULL)
                                        .format(currentDate.getTime());
        spinnerArray.add(currentDate1);
        currentDate.add(Calendar.DATE, 1);
        String currentDate2 = DateFormat.getDateInstance(DateFormat.FULL)
                                        .format(currentDate.getTime());
        spinnerArray.add(currentDate2);
        currentDate.add(Calendar.DATE, 1);
        String currentDate3 = DateFormat.getDateInstance(DateFormat.FULL)
                                        .format(currentDate.getTime());
        spinnerArray.add(currentDate3);

        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinnerDate.setAdapter(dateAdapter);

        //TODO add Date Picker Dialog to select date

        mBinding.buttonBookAppointmentConfirm.setOnClickListener(e -> bookAppointment());

        return mBinding.getRoot();
    }

    private void bookAppointment() {
        String problemDesc = mBinding.edittextProblem.getEditText()
                                                     .getText()
                                                     .toString();

        String prefDoc = mBinding.spinnerDoctor.getSelectedItem()
                                               .toString();

        String dateAppoint = mBinding.spinnerDate.getSelectedItem()
                                                 .toString();

        if (problemDesc.equals("")) {
            showToast("Kindly Describe Your Problem First");
        } else {

            if (prefDoc.equals("Choose your Doctor")) {
                showToast("Kindly Select a Doctor First");
            } else {
                if (dateAppoint.equals("Select")) {
                    showToast("Kindly Select your preferred date");
                } else {
                    //Book Appointment
                    showToast("Booking Appointment");

                    String uid = FirebaseAuth.getInstance()
                                             .getUid();

                    DatabaseReference rootRef = FirebaseDatabase.getInstance()
                                                                .getReference();

                    if (uid != null) {
                        PatientAppointment patientAppointment = new PatientAppointment(problemDesc, prefDoc, dateAppoint, false);

                        rootRef.child(CHILD_NAME)
                               .child(uid)
                               .addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                       if (snapshot.exists()) {
                                           long num = snapshot.getChildrenCount();

                                           String currentStat = String.valueOf(snapshot.child(num + "")
                                                                                       .child("appointmentFulfilled")
                                                                                       .getValue());

                                           if (currentStat.equals("false")) {
                                               showToast("You already have one upcoming appointment scheduled at : " + snapshot.child(num + "")
                                                                                                                               .child("dateAppoint")
                                                                                                                               .getValue()
                                                                                                                               .toString());
                                           } else {
                                               num++;

                                               //Previous Appointment Fulfilled
                                               rootRef.child(CHILD_NAME)
                                                      .child(uid)
                                                      .child(num + "")
                                                      .setValue(patientAppointment)
                                                      .addOnSuccessListener(e -> showToast("Appointment Booking Request Sent. We will contact you shortly"));
                                           }
                                       } else {
                                           rootRef.child(CHILD_NAME)
                                                  .child(uid)
                                                  .child("1")
                                                  .setValue(patientAppointment)
                                                  .addOnSuccessListener(e -> showToast("Appointment Booking Request Sent. We will contact you shortly"));
                                       }
                                   }

                                   @Override
                                   public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                   }
                               });


                    } else {
                        showToast("Some Error Occurred");

                    }
                }
            }
        }
    }


    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)
             .show();
    }


}