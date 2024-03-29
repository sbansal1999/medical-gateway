package com.example.medicalgateway;

import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicalgateway.databinding.ActivityRegisterBinding;
import com.example.medicalgateway.datamodels.UserInfo;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static com.example.medicalgateway.MedicalUtils.getTextFromTextInputLayout;
import static com.example.medicalgateway.MedicalUtils.setTextInTextInputLayout;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    //TODO add onTextChanged in all fields and retrieve phone number automatically
    public static final String EXTRA_PHONE_NUMBER = "com.example.medicalgateway.RegisterActivity.EXTRA_PHONE_NUMBER";
    public final static String TAG = "Log";
    public final static String CHILD_NAME = "patients_info";
    private final static String LINK_TO_TERMS_AND_CONDITIONS = "https://firebasestorage.googleapis.com/v0/b/medical-gateway-296507.appspot.com/o/terms_and_conditions.txt?alt=media&token=618eaa58-dea0-4966-bd4e-d8f16048e1d8;";
    //Bundle Variables for Configuration Changes
    private final static String BUNDLE_NAME = "BUNDLE_NAME";
    private final static String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";
    private final static String BUNDLE_DOB = "BUNDLE_DOB";
    private final static String BUNDLE_EMAIL_ADDRESS = "BUNDLE_EMAIL_ADDRESS";
    private final static String BUNDLE_RESIDENTIAL_ADDRESS = "BUNDLE_RESIDENTIAL_ADDRESS";
    private static final String BUNDLE_ALERT_DIALOG = "BUNDLE_ALERT_DIALOG";
    private static final String BUNDLE_OTP = "BUNDLE_OTP";
    private boolean isAlertDialogDisplayed = false;
    private ActivityRegisterBinding mBinding;
    private UserInfo userInfo;
    private FirebaseAuth mFirebaseAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mToken;
    private EditText editTextOTP;
    private AlertDialog alertDialog;
    private String enteredPhoneNumber;
    private DatabaseReference rootRef;
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
                mBinding.textPhoneNumber.setError("Invalid mobile number");
            }
            Toast.makeText(RegisterActivity.this, "Phone Number Verification Failed", Toast.LENGTH_SHORT)
                    .show();
            mBinding.progressCircular.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            displayLog("OTP Sent");
            Toast
                    .makeText(RegisterActivity.this, "An OTP has been sent to " + getTextFromTextInputLayout(mBinding.textPhoneNumber), Toast.LENGTH_SHORT)
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
    //TODO make the change in the DOB on the client side
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

        mFirebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                alertDialog.dismiss();
                //Correct OTP entered
                addIntoDatabase(task);
                storeDataInSharedPreferences();

                Intent intent = new Intent(this, PatientPortalActivity.class);

                Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show();

                Bundle bundle = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? ActivityOptions
                        .makeSceneTransitionAnimation(this).toBundle() : null;
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent, bundle);
                finish();
            } else {
                //Incorrect OTP entered
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    alertDialog.findViewById(R.id.text_otp_warning).setVisibility(View.VISIBLE);
                }

            }
        });

    }

    /**
     * Sets user data into the Default {@link SharedPreferences} using {@link Gson}
     */
    private void storeDataInSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(userInfo);
        editor.putString(SharedPreferencesInfo.PREF_CURRENT_USER_INFO, json);

        editor.putBoolean(SharedPreferencesInfo.PREF_IS_USER_PATIENT, true);

        editor.apply();
    }

    private void addIntoDatabase(@NotNull Task<AuthResult> task) {
        if (task.getResult() != null) {
            FirebaseUser firebaseUser = task.getResult().getUser();

            if (firebaseUser != null) {
                DatabaseReference childReference = rootRef.child(CHILD_NAME).child(firebaseUser.getUid());
                childReference.setValue(userInfo);

                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(userInfo.getName()).build();

                firebaseUser.updateProfile(userProfileChangeRequest);
            }
        } else {
            Toast.makeText(this, "Something wrong happened", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        mBinding.editTextDob.setOnClickListener(this);

        rootRef = FirebaseDatabase.getInstance().getReference();

        //Retrieve data from savedInstanceState if any
        if (savedInstanceState != null) {
            //Set strings in Edit Texts
            setEditTextValueFromInstanceState(savedInstanceState, mBinding.textName, BUNDLE_NAME);
            setEditTextValueFromInstanceState(savedInstanceState, mBinding.textPhoneNumber, BUNDLE_PHONE_NUMBER);
            setEditTextValueFromInstanceState(savedInstanceState, mBinding.textDob, BUNDLE_DOB);
            setEditTextValueFromInstanceState(savedInstanceState, mBinding.textEmailAddress, BUNDLE_EMAIL_ADDRESS);
            setEditTextValueFromInstanceState(savedInstanceState, mBinding.textResidentialAddress, BUNDLE_RESIDENTIAL_ADDRESS);

            //Display PopUp and the entered OTP if it was already displayed
            isAlertDialogDisplayed = savedInstanceState.getBoolean(BUNDLE_ALERT_DIALOG);
            if (isAlertDialogDisplayed) {
                showPopUp();
                EditText editTextOTP = alertDialog.findViewById(R.id.edit_otp);
                if (editTextOTP != null) {
                    editTextOTP.setText(savedInstanceState.getString(BUNDLE_OTP));
                }
            }
        }

        if (getIntent().hasExtra(LoginActivity.EXTRA_PHONE_NUMBER)) {
            mBinding.textPhoneNumber.setEndIconMode(TextInputLayout.END_ICON_NONE);
            String phoneNumber = getIntent().getStringExtra(LoginActivity.EXTRA_PHONE_NUMBER);
            EditText editText = mBinding.textPhoneNumber.getEditText();
            if (editText != null) {
                editText.setText(phoneNumber);
            }
        }

    }

    /**
     * Method which performs the registration process
     *
     * @param view The Button that was clicked
     */
    public void doRegister(View view) {
        if (doValidation()) {
            closeSoftKeyboard();
            if (MedicalUtils.isOnline(this, true)) {
                doUserAlreadyRegisteredCheck();
            }
        }

    }

    /**
     * Checks if the user is already registered and continues accordingly
     */
    private void doUserAlreadyRegisteredCheck() {
        enteredPhoneNumber = getTextFromTextInputLayout(mBinding.textPhoneNumber);

        rootRef.child(CHILD_NAME).orderByChild("phone").equalTo(enteredPhoneNumber)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            //Old User
                            Toast
                                    .makeText(RegisterActivity.this, "You are Already Registered. Redirecting to Login", Toast.LENGTH_SHORT)
                                    .show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra(EXTRA_PHONE_NUMBER, enteredPhoneNumber);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            finish();
                        } else {
                            //New User
                            mBinding.progressCircular.setVisibility(View.VISIBLE);
                            createUserInfo();
                            doFirebaseOperations();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        displayLog(error.getMessage());
                    }
                });
    }

    /**
     * Method to validate user's input
     *
     * @return true, if validation is successful or else
     * false
     */
    private boolean doValidation() {
        String name = getTextFromTextInputLayout(mBinding.textName);
        String phone = getTextFromTextInputLayout(mBinding.textPhoneNumber);
        String DOB = getTextFromTextInputLayout(mBinding.textDob);
        String emailAddress = getTextFromTextInputLayout(mBinding.textEmailAddress);
        String residentialAddress = getTextFromTextInputLayout(mBinding.textResidentialAddress);

        String regexEMail = "^[a-zA-Z0-9._]+@[a-zA-Z]+[.]+[a-z]+$";
        String regexPhone = "[0-9]{10}";

        boolean checkEMail = emailAddress.matches(regexEMail);
        boolean checkNum = phone.matches(regexPhone);
        boolean checkAddress = !residentialAddress.isEmpty();
        boolean checkName = !name.isEmpty();
        boolean checkDOB = !DOB.isEmpty();
        boolean checkTC = mBinding.checkBoxTc.isChecked();

        if (!checkNum) {
            mBinding.textPhoneNumber.setError("Invalid phone number");
        } else {
            mBinding.textPhoneNumber.setError(null);
        }

        if (!checkEMail) {
            mBinding.textEmailAddress.setError("Invalid Email");
        } else {
            mBinding.textEmailAddress.setError(null);
        }

        if (!checkAddress) {
            mBinding.textResidentialAddress.setError("Invalid Address");
        } else {
            mBinding.textResidentialAddress.setError(null);
        }

        if (!checkName) {
            mBinding.textName.setError("Invalid Name");
        } else {
            mBinding.textName.setError(null);
        }

        if (!checkDOB) {
            mBinding.textDob.setError("Invalid Date of Birth");
        } else {
            mBinding.textDob.setError(null);
        }

        if (!checkTC) {
            Toast.makeText(this, "Please Accept Terms & Conditions to continue", Toast.LENGTH_SHORT)
                    .show();
        }

        return checkNum && checkEMail && checkTC && checkAddress;
    }

    /**
     * Method to create an instance of {@link UserInfo}
     */
    private void createUserInfo() {
        String name = getTextFromTextInputLayout(mBinding.textName);
        String phone = getTextFromTextInputLayout(mBinding.textPhoneNumber);
        String DOB = getTextFromTextInputLayout(mBinding.textDob);
        String emailAddress = getTextFromTextInputLayout(mBinding.textEmailAddress);
        String residentialAddress = getTextFromTextInputLayout(mBinding.textResidentialAddress);

        userInfo = new UserInfo(name, phone, DOB, emailAddress, residentialAddress, getID());
    }

    @NotNull
    private String getID() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd/HHmmssSSS", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(new Date());
    }

    /**
     * Method to perform Firebase Operations using the passed instance
     */
    private void doFirebaseOperations() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        verifyPhoneNumber();
    }

    /**
     * Displays an {@link AlertDialog} which asks the user to enter the OTP
     */
    private void showPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setView(getLayoutInflater().inflate(R.layout.dialog_verify_phone, mBinding.getRoot(), false))
                .setCancelable(false).setPositiveButton("Verify OTP", null)
                .setNeutralButton("Resend OTP", null)
                .setNegativeButton("Change number", (dialog, which) -> mBinding.textPhoneNumber
                        .setEnabled(true));

        alertDialog = builder.create();

        mBinding.progressCircular.setVisibility(View.INVISIBLE);

        isAlertDialogDisplayed = true;
        alertDialog.show();

        //To avoid closing the dialog after the button is clicked.
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> verifyOTP());
        alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(v -> resendOTP());

        editTextOTP = alertDialog.findViewById(R.id.edit_otp);
    }

    /**
     * Starts Verification of the Phone number entered by the user
     */
    private void verifyPhoneNumber() {
        displayLog("Verifying Phone Number");
        showSnackbar("Verifying Phone Number");

        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mFirebaseAuth)
                .setPhoneNumber(getString(R.string.country_code) + userInfo.getPhone())
                .setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(mCallBacks).build();

        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }

    /**
     * Shows a {@link Snackbar} that has text sent in message
     *
     * @param message the text to be displayed
     */
    private void showSnackbar(String message) {
        Snackbar.make(mBinding.getRoot(), message, BaseTransientBottomBar.LENGTH_SHORT).show();
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
        String inputMonth = String.valueOf(++month);
        String inputDay = String.valueOf(day);

        String message = inputDay + "-" + inputMonth + "-" + inputYear;

        //Sample Date  - 05-05-2021
        if (mBinding.textDob.getEditText() != null) {
            mBinding.textDob.getEditText().setText(message);
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
     * Called when "VERIFY" is tapped in the pop-up displayed by on the screen
     */
    public void verifyOTP() {
        String enteredOTP = "1";
        if (!editTextOTP.getText().toString().isEmpty()) {
            enteredOTP = editTextOTP.getText().toString();
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, enteredOTP);

        signInWithPhoneAuthCredential(credential);
    }

    /**
     * Resend the OTP to the entered mobile number
     */
    public void resendOTP() {
        alertDialog.dismiss();
        showSnackbar("Resending OTP");

        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mFirebaseAuth)
                .setPhoneNumber(getString(R.string.country_code) + userInfo.getPhone())
                .setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setForceResendingToken(mToken)
                .setCallbacks(mCallBacks).build();

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
        storeStringInOutState(outState, mBinding.textName, BUNDLE_NAME);
        storeStringInOutState(outState, mBinding.textPhoneNumber, BUNDLE_PHONE_NUMBER);
        storeStringInOutState(outState, mBinding.textDob, BUNDLE_DOB);
        storeStringInOutState(outState, mBinding.textEmailAddress, BUNDLE_EMAIL_ADDRESS);
        storeStringInOutState(outState, mBinding.textResidentialAddress, BUNDLE_RESIDENTIAL_ADDRESS);

        if (isAlertDialogDisplayed) {
            outState.putBoolean(BUNDLE_ALERT_DIALOG, true);
            EditText editTextOTP = alertDialog.findViewById(R.id.edit_otp);
            if (editTextOTP != null) {
                outState.putString(BUNDLE_OTP, editTextOTP.getText().toString());
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
    private void storeStringInOutState(@NonNull Bundle outState, TextInputLayout textInputLayout,
                                       String key) {
        if (!getTextFromTextInputLayout(textInputLayout).isEmpty()) {
            outState.putString(key, getTextFromTextInputLayout(textInputLayout));
        }
    }

    /**
     * Sets the entered text to {@link TextInputLayout} in {@link Bundle} with the given key
     *
     * @param savedInstanceState the {@link Bundle} in which the String is stored
     * @param textInputLayout    the {@link TextInputLayout} in which the text is to entered
     * @param key                the key will be used to extract value from savedInstanceState
     */
    private void setEditTextValueFromInstanceState(Bundle savedInstanceState,
                                                   TextInputLayout textInputLayout, String key) {
        setTextInTextInputLayout(textInputLayout, savedInstanceState.getString(key));

    }

}