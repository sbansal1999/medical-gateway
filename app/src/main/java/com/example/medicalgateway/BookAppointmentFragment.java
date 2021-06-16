package com.example.medicalgateway;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.medicalgateway.databinding.FragmentBookAppointmentBinding;
import com.example.medicalgateway.datamodels.PatientAppointment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BookAppointmentFragment extends Fragment {

    private static final int IMAGE_DIMEN = 1000;
    private static final String CHILD_NAME_APPOINT = "appointment_info";
    private static final String CHILD_NAME_DOCTOR = "doctors_info";
    private final Calendar currentDate = Calendar.getInstance();
    private FragmentBookAppointmentBinding mBinding;
    private List<String> docIDList;
    private List<String> docNameList;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentBookAppointmentBinding.inflate(inflater, container, false);

//        ArrayAdapter<CharSequence> doctorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.doctors, android.R.layout.simple_spinner_item);
//        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mBinding.spinnerDoctor.setAdapter(doctorAdapter);

        mBinding.spinnerDoctor.setTitle("Select your Preferred Doctor");

        DatabaseReference rootRef = FirebaseDatabase.getInstance()
                                                    .getReference();

        rootRef.child(CHILD_NAME_DOCTOR)
               .addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                       docNameList = new ArrayList<>();
                       docNameList.add("Choose your Doctor");

                       docIDList = new ArrayList<>();
                       docIDList.add("BLANK ENTRY");

                       for (DataSnapshot snap : snapshot.getChildren()) {
                           String docName = snap.child("name")
                                                .getValue(String.class);
                           String docID = snap.child("doctorID")
                                              .getValue(String.class);

                           docNameList.add(docName);
                           docIDList.add(docID);

                       }

                       ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, docNameList);
                       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                       mBinding.spinnerDoctor.setAdapter(adapter);
                   }

                   @Override
                   public void onCancelled(@NonNull @NotNull DatabaseError error) {

                   }
               });

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
                    dateAppoint = convertDate(dateAppoint);

                    //Book Appointment
                    showToast("Booking Appointment");

                    String docID = docIDList.get(docNameList.indexOf(prefDoc));

                    String uid = FirebaseAuth.getInstance()
                                             .getUid();

                    DatabaseReference rootRef = FirebaseDatabase.getInstance()
                                                                .getReference();

                    if (uid != null) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        String result = sharedPreferences.getString(SharedPreferencesInfo.PREF_CURRENT_USER_PID, "hi");

                        PatientAppointment patientAppointment = new PatientAppointment(result, problemDesc, prefDoc, docID, dateAppoint, false);

                        rootRef.child(CHILD_NAME_APPOINT)
                               .child(uid)
                               .addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(
                                           @NonNull @NotNull DataSnapshot snapshot) {
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
                                               rootRef.child(CHILD_NAME_APPOINT)
                                                      .child(uid)
                                                      .child(num + "")
                                                      .setValue(patientAppointment)
                                                      .addOnSuccessListener(e -> showToast("Appointment Booking Request Sent. We will contact you shortly"));
                                           }
                                       } else {
                                           rootRef.child(CHILD_NAME_APPOINT)
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

    private String convertDate(String inputDate) {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("E, MMMMM dd, yyyy", Locale.ENGLISH);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {
            return simpleDateFormat.format(simpleDateFormat1.parse(inputDate));

        } catch (ParseException e) {
            Log.d("test", "convertDate: " + e.getLocalizedMessage());
        }

        return null;
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)
             .show();
    }


}