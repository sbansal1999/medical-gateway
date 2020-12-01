package com.example.medicalgateway;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicalgateway.databinding.ActivityLoginBinding;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;

public class LoginActivity extends AppCompatActivity {
    //TODO enable sign in via google,facebook,etc.

    public static final String EXTRA_PHONE_NUMBER = "com.example.medicalgateway.EXTRA_PHONE_NUMBER";
    private static final String TAG = "TAG";
    private ActivityLoginBinding mBinding;
    private FirebaseAuth mFirebaseAuth;
    private String mVerificationId;
    private String mPhoneNumber;
    private PhoneAuthProvider.ForceResendingToken mToken;
    private CallbackManager mCallbackManager;

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
            disableViews(mBinding.progressBar);
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            displayLog("OTP Sent");
            Toast.makeText(LoginActivity.this, "An OTP has been sent to " + mPhoneNumber, Toast.LENGTH_SHORT)
                 .show();
            mVerificationId = s;
            mToken = forceResendingToken;

            startOTPVerificationProcess();
        }
    };

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
                             //Correct OTP Entered
                             FirebaseUser firebaseUser = task.getResult()
                                                             .getUser();
                             if (firebaseUser != null) {
                                 Toast.makeText(LoginActivity.this, "Logged in as: " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT)
                                      .show();
                                 Intent intent = new Intent(LoginActivity.this, PatientPortalActivity.class);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        performFacebookLoginInitialization();

    }

    private void performFacebookLoginInitialization() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = mBinding.facebookLogin;
        loginButton.setReadPermissions("email");

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    public void verifyLogin(View view) {
        mPhoneNumber = getTextFromTextInputLayout(mBinding.textPhoneNumber);
        if (validatePhoneNumber()) {
            enableViews(mBinding.progressBar);
            if (mBinding.textPhoneNumber.getEditText() != null) {
                mBinding.textPhoneNumber.getEditText()
                                        .setEnabled(false);
            }


            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                                                  .getReference();
            Query query = databaseReference.child(RegisterActivity.CHILD_NAME)
                                           .orderByChild("phone")
                                           .equalTo(mPhoneNumber);
            displayLog("query");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        displayLog("Old User Detected");
                        verifyPhoneNumber();
                    } else {
                        displayLog("New User Detected");
                        Toast.makeText(LoginActivity.this, "Redirecting you to the Register Page", Toast.LENGTH_SHORT)
                             .show();
                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        intent.putExtra(EXTRA_PHONE_NUMBER, mPhoneNumber);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    displayLog(error.getMessage());

                }
            });

            displayLog("end");
        }

    }

    private boolean validatePhoneNumber() {
        String regexPhone = "[0-9]{10}";
        boolean checkNum = mPhoneNumber.matches(regexPhone);
        if (!checkNum) {
            mBinding.textPhoneNumber.setError("Invalid mobile number");
        }
        if (checkNum) {
            mBinding.textPhoneNumber.setError(null);
        }
        return checkNum;
    }

    private void showSnackbar(String message) {
        Snackbar.make(mBinding.getRoot(), message, BaseTransientBottomBar.LENGTH_SHORT)
                .show();
    }

    /**
     * Resend the OTP to the entered mobile number
     */
    public void resendOTP(View view) {
        showSnackbar("Resending OTP");

        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mFirebaseAuth)
                                                       .setPhoneNumber(getString(R.string.country_code) + mPhoneNumber)
                                                       .setTimeout(60L, TimeUnit.SECONDS)
                                                       .setActivity(this)
                                                       .setForceResendingToken(mToken)
                                                       .setCallbacks(mCallBacks)
                                                       .build();

        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }

    public void changeNumber(View view) {
        mBinding.textPhoneNumber.getEditText()
                                .setEnabled(true);
        disableViews(mBinding.buttonVerifyOtp);
        enableViews(mBinding.buttonLogin);
    }

    public void verifyOTP(View view) {
        String enteredOTP = "1";
        if (!mBinding.editOtp.getText()
                             .toString()
                             .isEmpty()) {
            enteredOTP = mBinding.editOtp.getText()
                                         .toString();
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, enteredOTP);

        signInWithPhoneAuthCredential(credential);
    }

    /**
     * Starts Verification of the Phone number entered by the user
     */
    private void verifyPhoneNumber() {
        showSnackbar("Verifying Phone Number");
        mFirebaseAuth = FirebaseAuth.getInstance();

        //Remove Social Sign-ins
//        disableViews(mBinding.imageButtonFacebook, mBinding.imageButtonGoogle);
        //Remove Login Button
        disableViews(mBinding.buttonLogin);

        mPhoneNumber = getTextFromTextInputLayout(mBinding.textPhoneNumber);
        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mFirebaseAuth)
                                                       .setPhoneNumber(getString(R.string.country_code) + mPhoneNumber)
                                                       .setTimeout(60L, TimeUnit.SECONDS)
                                                       .setActivity(this)
                                                       .setCallbacks(mCallBacks)
                                                       .build();

        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }

    /**
     * Retrieves text from the {@link android.widget.EditText} in the passed {@code textInputLayout}
     *
     * @param textInputLayout the view from which the text is to be retrieved
     * @return the retrieved text
     */
    private String getTextFromTextInputLayout(@NotNull TextInputLayout textInputLayout) {
        if (textInputLayout.getEditText() != null) {
            return textInputLayout.getEditText()
                                  .getText()
                                  .toString();
        }
        return null;
    }
}