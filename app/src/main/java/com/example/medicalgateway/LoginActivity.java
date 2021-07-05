package com.example.medicalgateway;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicalgateway.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static com.example.medicalgateway.MedicalUtils.getTextFromTextInputLayout;
import static com.example.medicalgateway.MedicalUtils.setTextInTextInputLayout;

public class LoginActivity extends AppCompatActivity {
    //TODO enable sign in via google,facebook,etc.

    public static final String EXTRA_PHONE_NUMBER = "com.example.medicalgateway.LoginActivity.EXTRA_PHONE_NUMBER";
    public static final String EXTRA_IS_PATIENT = "EXTRA_IS_PATIENT";
    private static final String TAG = "TAG";
    final boolean[] isPatient = {false};
    private ActivityLoginBinding mBinding;
    private FirebaseAuth mFirebaseAuth;
    private String mVerificationId;
    private String mPhoneNumber;
    private PhoneAuthProvider.ForceResendingToken mToken;
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
            Toast.makeText(LoginActivity.this, "Phone Number Verification Failed", Toast.LENGTH_SHORT)
                    .show();
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            disableViews(mBinding.progressBar);
            enableViews(mBinding.buttonChangeNumber, mBinding.buttonLogin);
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            displayLog("OTP Sent");
            Toast
                    .makeText(LoginActivity.this, "An OTP has been sent to " + mPhoneNumber, Toast.LENGTH_SHORT)
                    .show();
            mVerificationId = s;
            mToken = forceResendingToken;

            startOTPVerificationProcess();
        }
    };
    private DatabaseReference rootRef;
    private String CHILD_NAME_PATIENT = "patients_info";
    private String CHILD_NAME_DOCTOR = "doctors_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mFirebaseAuth = FirebaseAuth.getInstance();

        if (getIntent().hasExtra(RegisterActivity.EXTRA_PHONE_NUMBER)) {
            setTextInTextInputLayout(mBinding.textPhoneNumber, getIntent()
                    .getStringExtra(RegisterActivity.EXTRA_PHONE_NUMBER));
        }
    }

    /**
     * /Sign in the user to the Firebase using the provided {@link PhoneAuthCredential} instance
     *
     * @param phoneAuthCredential this will be used to sign in the user
     */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mFirebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, task -> {
            disableViews(mBinding.progressBar);
            if (task.isSuccessful()) {
                //Correct OTP Entered
                FirebaseUser firebaseUser = task.getResult().getUser();

                if (firebaseUser != null) {
                    Toast.makeText(LoginActivity.this, "Logged in as: " + firebaseUser
                            .getDisplayName(), Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this)
                            .edit();

                    editor.putBoolean(SharedPreferencesInfo.PREF_IS_USER_PATIENT, isPatient[0]);
                    editor.apply();

                    Intent intent = isPatient[0] ? new Intent(LoginActivity.this, PatientPortalActivity.class) : new Intent(LoginActivity.this, DoctorPortalActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            } else {
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    enableViews(mBinding.textOtpWarning);

                }
            }
        });
    }

    /**
     * Method that is executed after the OTP has been sent to the user's phone number
     */
    private void startOTPVerificationProcess() {
        enableViews(mBinding.textOtpHeading, mBinding.editOtp, mBinding.buttonResendOtp, mBinding.buttonChangeNumber, mBinding.buttonVerifyOtp);
        disableViews(mBinding.progressBar);
    }

    private void displayLog(String message) {
        Log.d(TAG, message);
    }

    private void enableViews(@NotNull View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void disableViews(@NotNull View... views) {
        for (View view : views) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public void verifyLogin(View view) {
        mPhoneNumber = getTextFromTextInputLayout(mBinding.textPhoneNumber);

        if (validatePhoneNumber()) {
            if (MedicalUtils.isOnline(this, true)) {

                enableViews(mBinding.progressBar);

                if (mBinding.textPhoneNumber.getEditText() != null) {
                    mBinding.textPhoneNumber.getEditText().setEnabled(false);
                }

                rootRef = FirebaseDatabase.getInstance().getReference();

                Query docQuery = rootRef.child(CHILD_NAME_DOCTOR).orderByChild("phone")
                        .equalTo(mPhoneNumber);

                Query patQuery = rootRef.child(CHILD_NAME_PATIENT).orderByChild("phone")
                        .equalTo(mPhoneNumber);

                patQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            //Check for Patient
                            isPatient[0] = true;

                            displayLog("Patient Detected");
                            verifyPhoneNumber();
                        } else {
                            docQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    //Check for Doctor
                                    if (snapshot.exists()) {
                                        displayLog("Doctor");
                                        verifyPhoneNumber();
                                    } else {
                                        displayLog("New User Detected");
                                        Toast
                                                .makeText(LoginActivity.this, "Redirecting you to the Register Page", Toast.LENGTH_SHORT)
                                                .show();
                                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        intent.putExtra(EXTRA_PHONE_NUMBER, mPhoneNumber);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                    displayLog(error.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        displayLog(error.getMessage());

                    }
                });

            }
        }

    }

    private boolean validatePhoneNumber() {
        String regexPhone = "[0-9]{10}";
        boolean checkNum = mPhoneNumber.matches(regexPhone);
        if (!checkNum) {
            mBinding.textPhoneNumber.setError("Invalid Mobile Number");
        }
        if (checkNum) {
            mBinding.textPhoneNumber.setError(null);
        }
        return checkNum;
    }

    private void showSnackbar(String message) {
        Snackbar.make(mBinding.getRoot(), message, BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    /**
     * Re-sends the OTP to the entered mobile number
     */
    public void resendOTP(View view) {
        showSnackbar("Resending OTP");

        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mFirebaseAuth)
                .setPhoneNumber(getString(R.string.country_code) + mPhoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setForceResendingToken(mToken)
                .setCallbacks(mCallBacks).build();

        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }

    public void changeNumber(View view) {
        mBinding.textPhoneNumber.getEditText().setEnabled(true);
        disableViews(mBinding.buttonVerifyOtp, mBinding.textOtpHeading, mBinding.editOtp, mBinding.buttonResendOtp, mBinding.buttonChangeNumber, mBinding.textOtpWarning);
        enableViews(mBinding.buttonLogin);
    }

    public void verifyOTP(View view) {
        String enteredOTP = "1";
        if (!mBinding.editOtp.getText().toString().isEmpty()) {
            enteredOTP = mBinding.editOtp.getText().toString();
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, enteredOTP);
        enableViews(mBinding.progressBar);

        signInWithPhoneAuthCredential(credential);
    }

    /**
     * Starts Verification of the Phone number entered by the user
     */
    private void verifyPhoneNumber() {
        showSnackbar("Verifying Phone Number");

        //Remove Social Sign-ins
//        disableViews(mBinding.imageButtonFacebook, mBinding.imageButtonGoogle);
        //Remove Login Button
        disableViews(mBinding.buttonLogin);

        mPhoneNumber = getTextFromTextInputLayout(mBinding.textPhoneNumber);
        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mFirebaseAuth)
                .setPhoneNumber(getString(R.string.country_code) + mPhoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(mCallBacks).build();

        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }

}