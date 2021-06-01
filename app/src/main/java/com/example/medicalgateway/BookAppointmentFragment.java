package com.example.medicalgateway;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.medicalgateway.databinding.FragmentBookappointmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookAppointmentFragment extends Fragment {

    private static final int IMAGE_DIMEN = 1000;
    private static final String CHILD_NAME = "appointment_info";
    ArrayAdapter<CharSequence> dateAdapter;
    private FragmentBookappointmentBinding mBinding;
    private Calendar currentDate = Calendar.getInstance();


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentBookappointmentBinding.inflate(inflater, container, false);

        dateAdapter = ArrayAdapter.createFromResource(getContext(), R.array.doctors, android.R.layout.simple_spinner_item);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mBinding.spinnerDoctor.setAdapter(dateAdapter);

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
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinnerDate.setAdapter(adapter2);

        //TODO add Date Picker Dialog to select date

        mBinding.buttonBookAppointmentConfirm.setOnClickListener(e -> bookAppointment());

        return mBinding.getRoot();
    }

    private void bookAppointment() {
        String problemDesc = mBinding.edittextProblem.getText()
                                                     .toString();

        String prefDoc = mBinding.spinnerDoctor.getSelectedItem()
                                               .toString();

        String dateAppoint = mBinding.spinnerDate.getSelectedItem()
                                                 .toString();


        if (problemDesc.equals("")) {
            Toast.makeText(getActivity(), "Kindly Describe Your Problem First", Toast.LENGTH_SHORT)
                 .show();
        } else {

            if (prefDoc.equals("Choose your Doctor")) {
                Toast.makeText(getActivity(), "Kindly Select a Doctor First", Toast.LENGTH_SHORT)
                     .show();
            } else {
                if (dateAppoint.equals("Select")) {
                    Toast.makeText(getActivity(), "Kindly Select your preferred date", Toast.LENGTH_SHORT)
                         .show();
                } else {
                    //Book Appointment
                    String uid = FirebaseAuth.getInstance()
                                             .getUid();

                    DatabaseReference rootRef = FirebaseDatabase.getInstance()
                                                                .getReference();

                    if (uid != null) {

                        PatientAppointment patientAppointment = new PatientAppointment(problemDesc, prefDoc, dateAppoint);


                        rootRef.child(CHILD_NAME)
                               .child(uid)
                               .setValue(patientAppointment)
                               .addOnSuccessListener(e -> Toast.makeText(getActivity(), "Appointment Booking Request Sent. We will contact you shortly", Toast.LENGTH_SHORT)
                                                               .show());

                    } else {
                        Toast.makeText(getActivity(), "Some Error Occurred", Toast.LENGTH_SHORT)
                             .show();

                    }

                }
            }
        }


    }


}