package com.example.medicalgateway;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfilePatientBinding.inflate(inflater, container, false);

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