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
import android.widget.Toast;

import com.example.medicalgateway.databinding.ActivityRegisterBinding;
import com.example.medicalgateway.databinding.FragmentBookappointmentBinding;
import com.example.medicalgateway.databinding.FragmentBookappointmentBinding;
import com.example.medicalgateway.databinding.FragmentProfilePatientBinding;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;


public class Book_AppointmentFragment extends Fragment {

    private static final int IMAGE_DIMEN = 1000;
    private FragmentBookappointmentBinding binding;

    ArrayAdapter<CharSequence> adapter;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBookappointmentBinding.inflate(inflater, container, false);
        adapter = ArrayAdapter.createFromResource(getContext(),R.array.Doctors,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDoctor.setAdapter(adapter);
        return binding.getRoot();
    }


    /**
     * Performs Uploading of the Image
     */


    public void openDOB(View view) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();

    }
}