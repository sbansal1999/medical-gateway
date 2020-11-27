package com.example.medicalgateway;

import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String TAG = "Log";
    private final static String COUNTRY_CODE = "+91";
    private final static String CHILD_NAME = "patients";
    private final static String LINK_TO_TERMS_AND_CONDITIONS = "https://firebasestorage.googleapis.com/v0/b/medical-gateway-296507.appspot.com/o/terms_and_conditions.txt?alt=media&token=618eaa58-dea0-4966-bd4e-d8f16048e1d8;";
    private final static String KEY_NAME = "KEY_NAME";
    private final static String KEY_PHONE_NUMBER = "KEY_PHONE_NUMBER";
    private final static String KEY_DOB = "KEY_DOB";
    private final static String KEY_EMAIL_ADDRESS = "KEY_EMAIL_ADDRESS";
    private final static String KEY_RESIDENTIAL_ADDRESS = "KEY_RESIDENTIAL_ADDRESS";
    private static final String KEY_ALERT_DIALOG = "KEY_ALERT_DIALOG";
    private static final String KEY_OTP = "KEY_OTP";
    private boolean isAlertDialogDisplayed = false;
    private ActivityRegisterBinding binding;
    private UserInfo userInfo;
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
            Toast.makeText(RegisterActivity.this, "An OTP has been sent to " + getTextFromTextInputLayout(binding.textPhoneNumber), Toast.LENGTH_SHORT)
                 .show();
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

    /**
     * /Sign in the user to the Firebase using the provided {@link PhoneAuthCredential} instance
     *
     * @param phoneAuthCredential this will be used to sign in the user
     */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        displayLog("signing in");

        mFirebaseAuth.signInWithCredential(phoneAuthCredential)
                     .addOnCompleteListener(this, task -> {
                         if (task.isSuccessful()) {
                             alertDialog.dismiss();
                             //Correct OTP entered
                             FirebaseUser firebaseUser = task.getResult()
                                                             .getUser();
                             DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                                                                   .getReference();
                             databaseReference.child(CHILD_NAME)
                                              .child(firebaseUser.getUid())
                                              .setValue(userInfo);

                             Intent intent = new Intent(this, PatientPortalActivity.class);

                             Bundle bundle = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? ActivityOptions.makeSceneTransitionAnimation(this)
                                                                                                                    .toBundle() : null;
                             intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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

        binding.editTextDob.setOnClickListener(this);

        //Retrieve data from savedInstanceState if any
        if (savedInstanceState != null) {
            //Set strings in Edit Texts
            setEditTextValueFromInstanceState(savedInstanceState, binding.textName, KEY_NAME);
            setEditTextValueFromInstanceState(savedInstanceState, binding.textPhoneNumber, KEY_PHONE_NUMBER);
            setEditTextValueFromInstanceState(savedInstanceState, binding.textDob, KEY_DOB);
            setEditTextValueFromInstanceState(savedInstanceState, binding.textEmailAddress, KEY_EMAIL_ADDRESS);
            setEditTextValueFromInstanceState(savedInstanceState, binding.textResidentialAddress, KEY_RESIDENTIAL_ADDRESS);

            //Display PopUp and the entered OTP if it was already displayed
            isAlertDialogDisplayed = savedInstanceState.getBoolean(KEY_ALERT_DIALOG);
            if (isAlertDialogDisplayed) {
                showPopUp();
                EditText editTextOTP = alertDialog.findViewById(R.id.edit_otp);
                if (editTextOTP != null) {
                    editTextOTP.setText(savedInstanceState.getString(KEY_OTP));
                }
            }
        }
    }

    /**
     * Sets the entered text to {@link TextInputLayout} in {@link Bundle} with the given key
     *
     * @param savedInstanceState the {@link Bundle} in which the String is stored
     * @param textInputLayout    the {@link TextInputLayout} in which the text is to entered
     * @param key                the key will be used to extract value from savedInstanceState
     */
    private void setEditTextValueFromInstanceState(Bundle savedInstanceState, TextInputLayout textInputLayout, String key) {
        setTextInTextInputLayout(textInputLayout, savedInstanceState.getString(key));

    }

    /**
     * Method which performs the registration process
     * @param view The Button that was clicked
     */
    public void doRegister(View view) {
        showPopUp();
        if (performValidation()) {
            closeSoftKeyboard();
            if (isOnline()) {
                binding.progressCircular.setVisibility(View.VISIBLE);
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
        performFirebaseOperations();
    }

    /**
     * Method to perform Firebase Operations using the passed instance
     */
    private void performFirebaseOperations() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        verifyPhoneNumber();
    }

    /**
     * Displays an {@link AlertDialog} which asks the user to enter the OTP
     */
    private void showPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(getLayoutInflater().inflate(R.layout.dialog_verify_phone, null))
               .setCancelable(false)
               .setPositiveButton("Verify OTP", null)
               .setNeutralButton("Resend OTP", null)
               .setNegativeButton("Change number", null);

        alertDialog = builder.create();

        binding.progressCircular.setVisibility(View.INVISIBLE);

        isAlertDialogDisplayed = true;
        alertDialog.show();

        //To avoid closing the dialog after the button is clicked.
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                   .setOnClickListener(v -> verifyOTP());
        alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL)
                   .setOnClickListener(v -> resendOTP());

        editTextOTP = alertDialog.findViewById(R.id.edit_otp);

//        int[] dimens = getScreenDimens();

    }

    /**
     * Starts Verification of the Phone number entered by the user
     */
    private void verifyPhoneNumber() {
        displayLog("Verifying Phone Number");
        showSnackbar("Verifying Phone Number");

        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mFirebaseAuth)
                                                       .setPhoneNumber(COUNTRY_CODE + userInfo.getPhone())
                                                       .setTimeout(60L, TimeUnit.SECONDS)
                                                       .setActivity(this)
                                                       .setCallbacks(mCallBacks)
                                                       .build();

        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }

    /**
     * Shows a {@link Snackbar} that has text sent in message
     *
     * @param message the text to be displayed
     */
    private void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, BaseTransientBottomBar.LENGTH_SHORT)
                .show();
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
        if (textInputLayout.getEditText() != null) {
            return textInputLayout.getEditText()
                                  .getText()
                                  .toString();
        }
        return null;
    }

    /**
     * Called when "VERIFY" is tapped in the pop-up displayed by on the screen
     */
    public void verifyOTP() {
        String enteredOTP = "1";
        if (!editTextOTP.getText()
                        .toString()
                        .isEmpty()) {
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
        alertDialog.dismiss();
        showSnackbar("Resending OTP");

        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mFirebaseAuth)
                                                       .setPhoneNumber(COUNTRY_CODE + userInfo.getPhone())
                                                       .setTimeout(60L, TimeUnit.SECONDS)
                                                       .setActivity(this)
                                                       .setForceResendingToken(mToken)
                                                       .setCallbacks(mCallBacks)
                                                       .build();

        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }

    /**
     * Method that opens the Terms & Conditions in a browser
     *
     * @param view the button that was clicked
     */
    public void openTC(View view) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(LINK_TO_TERMS_AND_CONDITIONS));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            showSnackbar("No Browser Detected");
        }

    }

    @Override
    public void onClick(View v) {
        openDOB(v);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        //Retrieve texts in EditTexts if it's not empty
        storeStringInOutState(outState, binding.textName, KEY_NAME);
        storeStringInOutState(outState, binding.textPhoneNumber, KEY_PHONE_NUMBER);
        storeStringInOutState(outState, binding.textDob, KEY_DOB);
        storeStringInOutState(outState, binding.textEmailAddress, KEY_EMAIL_ADDRESS);
        storeStringInOutState(outState, binding.textResidentialAddress, KEY_RESIDENTIAL_ADDRESS);

        if (isAlertDialogDisplayed) {
            outState.putBoolean(KEY_ALERT_DIALOG, true);
            EditText editTextOTP = alertDialog.findViewById(R.id.edit_otp);
            if (editTextOTP != null) {
                outState.putString(KEY_OTP, editTextOTP.getText()
                                                       .toString());
            }
        }
    }

    /**
     * Stores the entered text in {@link TextInputLayout} in {@link Bundle} with the given key
     *
     * @param outState        the {@link Bundle} in which the String is to be stored
     * @param textInputLayout the {@link TextInputLayout} to which the text is to be extracted
     * @param key             the key will be used to extract value from outState
     */
    private void storeStringInOutState(@NonNull Bundle outState, TextInputLayout textInputLayout, String key) {
        if (!getTextFromTextInputLayout(textInputLayout).isEmpty()) {
            outState.putString(key, getTextFromTextInputLayout(textInputLayout));
        }
    }

    private void setTextInTextInputLayout(@NotNull TextInputLayout textInputLayout, String string) {
        if (textInputLayout.getEditText() != null) {
            textInputLayout.getEditText()
                           .setText(string);
        }
    }

}