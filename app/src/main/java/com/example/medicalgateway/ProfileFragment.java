package com.example.medicalgateway;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.canhub.cropper.CropImage;
import com.example.medicalgateway.databinding.FragmentProfilePatientBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

//TODO include apache license

public class ProfileFragment extends Fragment {

    public final static String CHILD_NAME = "patients_info";
    private static final int IMAGE_DIMEN = 1000;
    private static final String CHILD_NAME_BLOOD_GRP = "blood_group";
    private FragmentProfilePatientBinding mBinding;
    private ArrayAdapter<CharSequence> adapter;
    private DatabaseReference rootRef;
    private boolean imageChanged = false;

    public ProfileFragment() {

    }

    public static String getMonthName(int monthNumber) {
        monthNumber--;
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[monthNumber];
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentProfilePatientBinding.inflate(inflater, container, false);

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

        mBinding.buttonSave.setOnClickListener(v -> saveChanges());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPreferences.contains(SharedPreferencesInfo.PREF_CURRENT_USER_INFO)) {
            UserInfo userInfo = getUserInfoFromSharedPreferences();
            setValuesFromUserInfo(userInfo);
//            changeDOBFormat();
        } else {
            fetchDataFromFirebase();
        }

        return mBinding.getRoot();
    }

    private void changeDOBFormat() {
        TextView dobPatientValue = mBinding.textDobPatientValue;

        String current = dobPatientValue.getText()
                                        .toString();

        int indexFirst = current.indexOf("-");
        int indexSecond = current.indexOf("-", indexFirst + 1);

        String result = current.substring(0, indexFirst) + " ";

        int month = indexSecond - indexFirst == 2 ? Integer.parseInt(current.charAt(indexFirst + 1) + "") : Integer.parseInt(current.substring(indexFirst + 1, indexSecond));

        String mName = getMonthName(month);
        result += mName + " ";

        result += current.substring(indexSecond + 1);

        dobPatientValue.setText(result);
    }

    /**
     * Fetches Data from Firebase DB and updates it as well
     */
    private void fetchDataFromFirebase() {
        String uid = FirebaseAuth.getInstance()
                                 .getUid();

        rootRef = FirebaseDatabase.getInstance()
                                  .getReference();

        if (uid != null) {
            rootRef.child(CHILD_NAME)
                   .child(uid)
                   .addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                           if (snapshot.exists()) {
                               String json = snapshot.getValue()
                                                     .toString();

                               UserInfo userInfo = snapshot.getValue(UserInfo.class);

                               setValuesFromUserInfo(userInfo);

                               DatabaseReference childRef = rootRef.child(CHILD_NAME_BLOOD_GRP)
                                                                   .child(uid);


                               childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                       if (snapshot.getValue() == null) {
                                           mBinding.spinnerBloodGroup.setSelection(0);
                                       } else {
                                           String bloodGrp = snapshot.getValue()
                                                                     .toString();

                                           setBloodGrpSpinner(bloodGrp);

                                           SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity())
                                                                                              .edit();

                                           editor.putString(SharedPreferencesInfo.PREF_CURRENT_USER_BLOOD_GROUP, bloodGrp);
                                           editor.apply();
                                       }
                                   }

                                   @Override
                                   public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                   }
                               });
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull @NotNull DatabaseError error) {

                       }
                   });

            Picasso.get()
                   .load(FirebaseAuth.getInstance()
                                     .getCurrentUser()
                                     .getPhotoUrl())
                   .into(mBinding.circularImageView);

        }
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)
             .show();
    }

    /**
     * Takes blood group as param and sets the spinner accordingly
     *
     * @param s The Blood Group String
     */
    private void setBloodGrpSpinner(String s) {
        List<String> bloodGrpList = Arrays.asList(getResources().getStringArray(R.array.blood_groups));
        int index = bloodGrpList.indexOf(s);
        mBinding.spinnerBloodGroup.setSelection(index);

    }

    /**
     * Updates the changes made by the user to the Firebase DB
     */
    public void saveChanges() {
        String address = mBinding.textAddressPatientValue.getText()
                                                         .toString();
        String bloodGroup = mBinding.spinnerBloodGroup.getSelectedItem()
                                                      .toString();

        boolean uploadGroup = !bloodGroup.equals(getResources().getStringArray(R.array.blood_groups)[0]);

        rootRef = FirebaseDatabase.getInstance()
                                  .getReference();

        String uid = FirebaseAuth.getInstance()
                                 .getUid();

        if (uid != null) {
            showToast("Saving Changes");

            final boolean[] status = {false};

            rootRef.child(CHILD_NAME)
                   .child(uid)
                   .child("residentialAddress")
                   .setValue(address)
                   .addOnSuccessListener(e -> status[0] = true);


            if (uploadGroup) {
                rootRef.child(CHILD_NAME_BLOOD_GRP)
                       .child(uid)
                       .setValue(bloodGroup)
                       .addOnSuccessListener(e -> status[0] = true);

                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity())
                                                                   .edit();

                editor.putString(SharedPreferencesInfo.PREF_CURRENT_USER_BLOOD_GROUP, bloodGroup);
                editor.apply();
            }

            if (imageChanged) {
                StorageReference strRef = FirebaseStorage.getInstance()
                                                         .getReference()
                                                         .child(uid)
                                                         .child("profile_pic.jpg");

                BitmapDrawable drawable = (BitmapDrawable) mBinding.circularImageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] data = byteArrayOutputStream.toByteArray();

                UploadTask uploadTask = strRef.putBytes(data);
                uploadTask.addOnFailureListener(e -> showToast("Some Error Occurred During Saving Data"))
                          .addOnSuccessListener(taskSnapshot -> strRef.getDownloadUrl()
                                                                      .addOnSuccessListener(uri -> {
                                                                          UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri)
                                                                                                                                                   .build();
                                                                          FirebaseAuth.getInstance()
                                                                                      .getCurrentUser()
                                                                                      .updateProfile(request)
                                                                                      .addOnSuccessListener(unused -> status[0] = true);
                                                                      }));
            }

            if (status[0] = true) {
                showToast("Profile Updated Successfully");
            }
        } else {
            showToast("Some Error Occurred");
        }
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

        //Check if blood grp exists
        if (sharedPreferences.contains(SharedPreferencesInfo.PREF_CURRENT_USER_BLOOD_GROUP)) {
            setBloodGrpSpinner(sharedPreferences.getString(SharedPreferencesInfo.PREF_CURRENT_USER_BLOOD_GROUP, "--"));
        }

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
        imageChanged = true;
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
                    showToast("ERROR");
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
