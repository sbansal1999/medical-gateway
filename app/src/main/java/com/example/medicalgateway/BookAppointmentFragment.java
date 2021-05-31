package com.example.medicalgateway;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import com.example.medicalgateway.databinding.FragmentBookappointmentBinding;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class BookAppointmentFragment extends Fragment {

    private static final int IMAGE_DIMEN = 1000;
    ArrayAdapter<CharSequence> DateAdapter;
    private FragmentBookappointmentBinding binding;
    private Calendar currentDate = Calendar.getInstance();



    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBookappointmentBinding.inflate(inflater, container, false);
        DateAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Doctors, android.R.layout.simple_spinner_item);
        DateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDoctor.setAdapter(DateAdapter);
        List<String> spinnerArray = new ArrayList<>();
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
        DateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDate.setAdapter(adapter2);
        return binding.getRoot();
    }
}