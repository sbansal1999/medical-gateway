package com.example.medicalgateway;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicalgateway.databinding.ActivityRegisterBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    //TODO add OTP verification
    public final static String TAG = "Log";
    private final static String COUNTRY_CODE = "+91";
    private final static String CHILD_NAME = "patients";
    private UserInfo userInfo;
    private ActivityRegisterBinding binding;
    private FirebaseAuth mFirebaseAuth;
    private String mVerificationId;
    private EditText editTextOTP;
    private PhoneAuthProvider.ForceResendingToken mToken;
    private AlertDialog alertDialog;
    //Callback for PhoneAuthProvider
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            displayLog("Automatically Verified");
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            displayLog(e.getMessage());
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                binding.textPhoneNumber.setError("Invalid phone number");
            }
            Toast.makeText(RegisterActivity.this, "Phone Number Verification Failed", Toast.LENGTH_SHORT)
                 .show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            displayLog("OTP Sent");

            mVerificationId = s;
            mToken = forceResendingToken;
            showPopUp();
        }
    };

    /**
     * Returns month NAME for the provided month NUMBER
     *
     * @param monthNumber the number pf the month
     * @return the name of the month
     */
    public static String getMonthName(int monthNumber) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[monthNumber];
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        displayLog("signing in");

        mFirebaseAuth.signInWithCredential(phoneAuthCredential)
                     .addOnCompleteListener(this, task -> {
                         if (task.isSuccessful()) {
                             //Correct OTP entered
                             FirebaseUser firebaseUser = task.getResult()
                                                             .getUser();
                             DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                                                                   .getReference();
                             databaseReference.child(CHILD_NAME)
                                              .child(userInfo.getPhone())
                                              .setValue(userInfo);

                             Intent intent = new Intent(this, PatientPortalActivity.class);

                             Bundle bundle = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? ActivityOptions.makeSceneTransitionAnimation(this)
                                                                                                                    .toBundle() : null;

                             startActivity(intent, bundle);
                         } else {
                             //Incorrect OTP entered
                             alertDialog.findViewById(R.id.text_otp_warning)
                                        .setVisibility(View.VISIBLE);
                         }
                     });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    /**
     * Method which performs the registration process
     *
     * @param view The Button that was clicked
     */
    public void doRegister(View view) {
        if (performValidation()) {
            if (isOnline()) {
                storeData();
            } else {
                Toast.makeText(this, "Connect to Internet to continue", Toast.LENGTH_SHORT)
                     .show();
            }
        }

    }

    /**
     * Method to validate user's input
     *
     * @return true, if validation is successful or else
     * false
     */
    private boolean performValidation() {
        String name = getTextFromTextInputLayout(binding.textName);
        String phone = getTextFromTextInputLayout(binding.textPhoneNumber);
        String DOB = getTextFromTextInputLayout(binding.textDob);
        String emailAddress = getTextFromTextInputLayout(binding.textEmailAddress);
        String residentialAddress = getTextFromTextInputLayout(binding.textResidentialAddress);

        String regexEMail = "^[a-zA-Z0-9._]+@[a-zA-Z]+[.]+[a-z]+$";
        String regexPhone = "[0-9]{10}";


        boolean checkEMail = emailAddress.matches(regexEMail);
        boolean checkNum = phone.matches(regexPhone);
        boolean checkAddress = !residentialAddress.isEmpty();
        boolean checkName = !name.isEmpty();
        boolean checkDOB = !DOB.isEmpty();
        boolean checkTC = binding.checkBoxTc.isChecked();

        if (!checkNum) {
            binding.textPhoneNumber.setError("Invalid phone number");
        }

        if (checkNum) {
            binding.textPhoneNumber.setError(null);
        }

        if (!checkEMail) {
            binding.textEmailAddress.setError("Invalid Email");
        }

        if (checkEMail) {
            binding.textEmailAddress.setError(null);
        }

        if (!checkTC) {
            Toast.makeText(this, "Please Accept Terms & Conditions to continue", Toast.LENGTH_SHORT)
                 .show();
        }

        if (!checkAddress) {
            binding.textResidentialAddress.setError("Invalid Address");
        }

        if (checkAddress) {
            binding.textResidentialAddress.setError(null);
        }

        if (!checkName) {
            binding.textName.setError("Invalid Name");
        }

        if (checkName) {
            binding.textName.setError(null);
        }

        if (!checkDOB) {
            binding.textDob.setError("Invalid Date of Birth");
        }

        if (checkDOB) {
            binding.textDob.setError(null);
        }
        return checkNum && checkEMail && checkTC && checkAddress;
    }

    /**
     * Method to create an instance of {@link UserInfo}
     */
    private void storeData() {
        String name = getTextFromTextInputLayout(binding.textName);
        String phone = getTextFromTextInputLayout(binding.textPhoneNumber);
        String DOB = getTextFromTextInputLayout(binding.textDob);
        String emailAddress = getTextFromTextInputLayout(binding.textEmailAddress);
        String residentialAddress = getTextFromTextInputLayout(binding.textResidentialAddress);

        userInfo = new UserInfo(name, phone, DOB, emailAddress, residentialAddress);
        performFirebaseOperations(userInfo);
    }

    /**
     * Method to perform Firebase Operations using the passed instance
     *
     * @param userInfo the instance used to create user as well as feed data into the database
     */
    private void performFirebaseOperations(@NotNull UserInfo userInfo) {
        mFirebaseAuth = FirebaseAuth.getInstance();

        verifyPhoneNumber();

    }

    private void showPopUp() {
        Toast.makeText(this, "An OTP has been sent to " + getTextFromTextInputLayout(binding.textPhoneNumber), Toast.LENGTH_SHORT)
             .show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(getLayoutInflater().inflate(R.layout.dialog_verify_phone, null))
               .setCancelable(false)
               .setPositiveButton("Verify OTP", null)
               .setNeutralButton("Resend OTP", null);

        alertDialog = builder.create();
        alertDialog.show();

        //To avoid closing the dialog after the button is clicked.
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                   .setOnClickListener(v -> verifyOTP());
        alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL)
                   .setOnClickListener(v -> resendOTP());

        editTextOTP = alertDialog.findViewById(R.id.edit_otp);

//        int[] dimens = getScreenDimens();

    }

    private void verifyPhoneNumber() {
        displayLog("Verifying Phone Number");
        Toast.makeText(this, "Verifying Phone Number", Toast.LENGTH_SHORT)
             .show();

        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mFirebaseAuth)
                                                       .setPhoneNumber(COUNTRY_CODE + userInfo.getPhone())
                                                       .setTimeout(60L, TimeUnit.SECONDS)
                                                       .setActivity(this)
                                                       .setCallbacks(mCallBacks)
                                                       .build();

        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }

    /**
     * Method to retrieve Screen Dimensions
     *
     * @return an int array with first value as width, second as height (in px.)
     */
    private int[] getScreenDimens() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                          .getMetrics(displayMetrics);
        return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
    }

    private void displayLog(String message) {
        Log.d(TAG, message);
    }

    /**
     * Method to show {@link DatePickerFragment}
     *
     * @param view the button that was clicked
     */
    public void openDOB(View view) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setCancelable(false);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Method that processes the input given from {@link DatePickerFragment}
     *
     * @param year  the year selected by the user
     * @param month the month selected by the user
     * @param day   the date selected by the user
     */
    public void processDatePickerResult(int year, int month, int day) {
        closeSoftKeyboard();

        String inputYear = String.valueOf(year);
        String inputMonth = getMonthName(month);
        String inputDay = String.valueOf(day);

        String message = inputDay + "/ " + inputMonth + "/ " + inputYear;

        if (binding.textDob.getEditText() != null) {
            binding.textDob.getEditText()
                           .setText(message);
        }
    }

    /**
     * Method that closes any opened soft Keyboard
     */
    private void closeSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            View view = getCurrentFocus();
            if (view == null) {
                view = new View(this);
            }
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (NullPointerException ignored) {

        }
    }

    /**
     * Retrieves text from the {@link android.widget.EditText} in the passed {@code textInputLayout}
     *
     * @param textInputLayout the view from which the text is to be retrieved
     * @return the retrieved text
     */
    public String getTextFromTextInputLayout(@NotNull TextInputLayout textInputLayout) {
        return textInputLayout.getEditText()
                              .getText()
                              .toString();
    }

    /**
     * Called when "VERIFY" is tapped in the pop-up displayed by on the screen
     */
    public void verifyOTP() {
        String enteredOTP = "1";
        if(!editTextOTP.getText().toString().isEmpty()) {
            enteredOTP = editTextOTP.getText()
                                           .toString();
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, enteredOTP);

        signInWithPhoneAuthCredential(credential);
    }

    /**
     * Method that checks if the device is connected to internet.
     *
     * @return true if online else false
     */
    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();


    }

    /**
     * Resend the OTP to the entered mobile number
     */
    public void resendOTP() {
        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mFirebaseAuth)
                                                       .setPhoneNumber(COUNTRY_CODE + userInfo.getPhone())
                                                       .setTimeout(60L, TimeUnit.SECONDS)
                                                       .setActivity(this)
                                                       .setForceResendingToken(mToken)
                                                       .setCallbacks(mCallBacks)
                                                       .build();

        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }
}