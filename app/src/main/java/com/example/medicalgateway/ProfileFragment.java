package com.example.medicalgateway;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.medicalgateway.databinding.FragmentProfilePatientBinding;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;

//TODO include apache license

public class ProfileFragment extends Fragment {
    //TODO add something so that when user clicks the circular image it shows its picture in an enlarged view

    private static final int IMAGE_DIMEN = 1000;
    private FragmentProfilePatientBinding binding;

    ArrayAdapter<CharSequence> adapter;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfilePatientBinding.inflate(inflater, container, false);
        adapter = ArrayAdapter.createFromResource(getContext(),R.array.blood_groups,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinner.setAdapter(adapter);
        binding.changeAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.prompts, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Change Address",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        binding.textAddressPatient.setText(userInput.getText());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();


            }
        });
        binding.buttonUploadImage.setOnClickListener(v -> uploadImage());

        return binding.getRoot();
    }


    /**
     * Performs Uploading of the Image
     */
    private void uploadImage() {
        if (getContext() != null) {
            CropImage.activity()
                     .setMinCropResultSize(IMAGE_DIMEN / 2, IMAGE_DIMEN / 2)
                     .setMaxCropResultSize(IMAGE_DIMEN, IMAGE_DIMEN)
                     .start(getContext(), this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getContext() != null) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = null;
                    if (activityResult != null) {
                        resultUri = activityResult.getUri();
                    }
                    binding.circularImageView.setImageURI(resultUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT)
                         .show();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}