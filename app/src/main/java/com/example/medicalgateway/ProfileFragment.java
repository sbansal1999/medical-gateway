package com.example.medicalgateway;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.medicalgateway.databinding.FragmentProfilePatientBinding;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;

//TODO include apache license

public class ProfileFragment extends Fragment {

    private static final int IMAGE_DIMEN = 1000;
    private FragmentProfilePatientBinding mBinding;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentProfilePatientBinding.inflate(inflater, container, false);

        UserInfo userInfo = getUserInfoFromSharedPreferences();
        setValuesFromUserInfo(userInfo);

        adapter = ArrayAdapter.createFromResource(getContext(), R.array.blood_groups, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mBinding.spinnerBloodGroup.setAdapter(adapter);
        mBinding.buttonChangeAddress.setOnClickListener((View.OnClickListener) view -> {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View promptsView = layoutInflater.inflate(R.layout.prompts, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setView(promptsView);

            EditText userInput = promptsView.findViewById(R.id.editTextDialogUserInput);

            // set dialog message
            alertDialogBuilder.setCancelable(false)
                              .setPositiveButton("Change Address", (dialog, id) -> {
                                  // get user input and set it to result
                                  mBinding.textAddressPatientValue.setText(userInput.getText());
                              })
                              .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
        mBinding.buttonUploadImage.setOnClickListener(v -> uploadImage());

        return mBinding.getRoot();
    }

    private void setValuesFromUserInfo(UserInfo userInfo) {
        mBinding.textNamePatientValue.setText(userInfo.getName());
        mBinding.textDobPatientValue.setText(userInfo.getDOB());
        mBinding.textPhonePatientValue.setText(userInfo.getPhone());
        mBinding.textEmailPatientValue.setText(userInfo.getEmailAddress());
        mBinding.textAddressPatientValue.setText(userInfo.getResidentialAddress());
    }

    private UserInfo getUserInfoFromSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String json = sharedPreferences.getString(SharedPreferencesInfo.PREF_CURRENT_USER_INFO, null);

        Gson gson = new Gson();
        return gson.fromJson(json, UserInfo.class);
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
                    mBinding.circularImageView.setImageURI(resultUri);
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
        mBinding = null;
    }
}
