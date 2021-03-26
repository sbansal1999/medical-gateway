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
    private FragmentBookappointmentBinding binding;
    private Calendar CurrentDate = Calendar.getInstance();


    ArrayAdapter<CharSequence> DateAdapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBookappointmentBinding.inflate(inflater, container, false);
        DateAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Doctors, android.R.layout.simple_spinner_item);
        DateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDoctor.setAdapter(DateAdapter);
        List<String> spinnerArray = new ArrayList<>();
        CurrentDate.add(Calendar.DATE, 1);
        String CurrentDate1 = DateFormat.getDateInstance(DateFormat.FULL).format(CurrentDate.getTime());
        spinnerArray.add(CurrentDate1);
        CurrentDate.add(Calendar.DATE, 1);
        String CurrentDate2 = DateFormat.getDateInstance(DateFormat.FULL).format(CurrentDate.getTime());
        spinnerArray.add(CurrentDate2);
        CurrentDate.add(Calendar.DATE, 1);
        String CurrentDate3 = DateFormat.getDateInstance(DateFormat.FULL).format(CurrentDate.getTime());
        spinnerArray.add(CurrentDate3);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        DateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDate.setAdapter(adapter2);
        return binding.getRoot();
    }





}