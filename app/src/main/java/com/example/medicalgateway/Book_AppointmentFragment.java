package com.example.medicalgateway;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.medicalgateway.databinding.ActivityRegisterBinding;
import com.example.medicalgateway.databinding.FragmentBookappointmentBinding;
import com.example.medicalgateway.databinding.FragmentBookappointmentBinding;
import com.example.medicalgateway.databinding.FragmentProfilePatientBinding;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class Book_AppointmentFragment extends Fragment {

    private static final int IMAGE_DIMEN = 1000;
    private FragmentBookappointmentBinding binding;
    private Calendar todaydate = Calendar.getInstance();


    ArrayAdapter<CharSequence> adapter;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBookappointmentBinding.inflate(inflater, container, false);
        adapter = ArrayAdapter.createFromResource(getContext(),R.array.Doctors,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDoctor.setAdapter(adapter);
        List<String> spinnerArray =  new ArrayList<>();
        todaydate.add(Calendar.DATE,1);
        String currentdate = DateFormat.getDateInstance(DateFormat.FULL).format(todaydate.getTime());
        spinnerArray.add(currentdate);
        todaydate.add(Calendar.DATE,1);
        String currentdate2 = DateFormat.getDateInstance(DateFormat.FULL).format(todaydate.getTime());
        spinnerArray.add(currentdate2);
        todaydate.add(Calendar.DATE,1);
        String currentdate3 = DateFormat.getDateInstance(DateFormat.FULL).format(todaydate.getTime());
        spinnerArray.add(currentdate3);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDate.setAdapter(adapter2);
        return binding.getRoot();
    }





}