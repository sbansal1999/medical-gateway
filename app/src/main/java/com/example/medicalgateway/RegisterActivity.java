package com.example.medicalgateway;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicalgateway.databinding.ActivityRegisterBinding;
import com.example.medicalgateway.databinding.DialogVerifyPhoneBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    //TODO add OTP verification
    public final static String TAG = "Log";
    private final static String COUNTRY_CODE = "+91";
    private UserInfo userInfo;
    private ActivityRegisterBinding binding;
    private FirebaseAuth mFirebaseAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mToken;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG, "Automatic Verification Success");
            displayMessage("automatically verified");

            signInWithPhoneAuthCredential(phoneAuthCredential);

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.d(TAG, e.getMessage());
            displayMessage("verification failed");
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.d(TAG, "Code is sent");
            displayMessage("otp sent");

            mVerificationId = s;
            mToken = forceResendingToken;

            showPopUp();

        }
    };

    /**
     * Returns month NAME for the provided month NUMBER
     *
     * @param month the number pf the month
     * @return the name of the month
     */
    public static String theMonth(int month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mFirebaseAuth.signInWithCredential(phoneAuthCredential)
                     .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful()) {
                                 Log.d(TAG, "sign in successful");
                                 FirebaseUser firebaseUser = task.getResult()
                                                                 .getUser();
                                 displayMessage("login success");
                             } else {
                                 Log.d(TAG, "sign in unsuccessful");
                                 displayMessage("login failure");
                             }
                         }
                     });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //safety net
        

    }

    /**
     * Method which performs the registration process
     *
     * @param view The Button that was clicked
     */
    public void doRegister(View view) {
//        if (performValidation()) {
//            storeData();
//        }
        storeData();
    }

    /**
     * Method to validate user's input
     *
     * @return true, if validation is successful or else
     * false
     */
    private boolean performValidation() {
        //TODO add something on password field to show exactly what's wrong with it
        String email = getTextFromTextInputLayout(binding.textEmailAddress);
        String phone = getTextFromTextInputLayout(binding.textPhoneNumber);
        String password = getTextFromTextInputLayout(binding.textPassword);

        String regexLowerCase = ".*[a-z].*";
        String regexUpperCase = ".*[A-Z].*";
        String regexNum = ".*[0-9].*";
        String regexSymbol = ".*[!@#$%^&*].*";
        String regexEMail = "^[a-zA-Z0-9._]+@[a-zA-Z]+[.]+[a-z]+$";
        String regexPhone = "[0-9]{10}";


        boolean checkPass = password.matches(regexLowerCase) &&
                            password.matches(regexUpperCase) &&
                            password.matches(regexNum) &&
                            password.matches(regexSymbol);
        boolean checkEMail = email.matches(regexEMail);
        boolean checkNum = phone.matches(regexPhone);
        boolean checkTC = binding.checkBoxTc.isChecked();

        if (!checkPass || password.length() < 6) {
            binding.textPassword.setError("Password must be - \n" +
                                          "1 - At Least 6 characters long \n" +
                                          "2 - Must contain a uppercase alphabet \n" +
                                          "3 - Must contain a lowercase alphabet \n" +
                                          "4 - Must contain a number \n" +
                                          "5 - Must contain a symbol - !,@,#,$,%,^,&,*");
        }

        if (checkPass) {
            binding.textPassword.setError(null);
        }

        if (!checkNum) {
            binding.textPhoneNumber.setError("Invalid Number");
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

        return checkPass || password.length() >= 6 && checkNum && checkEMail && checkTC;
    }

    /**
     * Method to create an instance of {@link UserInfo}
     */
    private void storeData() {
        String name = getTextFromTextInputLayout(binding.textName);
        String phone = getTextFromTextInputLayout(binding.textPhoneNumber);
        String DOB = getTextFromTextInputLayout(binding.textDob);
        String password = getTextFromTextInputLayout(binding.textPassword);
        String emailAddress = getTextFromTextInputLayout(binding.textEmailAddress);
        String residentialAddress = getTextFromTextInputLayout(binding.textResidentialAddress);

        userInfo = new UserInfo(name, phone, DOB, password, emailAddress, residentialAddress);
        performFirebaseOperations(userInfo);
    }

    /**
     * Method to perform Firebase Operations using the passed instance
     *
     * @param userInfo the instance used to create user as well as feed data into the database
     */
    private void performFirebaseOperations(@NotNull UserInfo userInfo) {
        mFirebaseAuth = FirebaseAuth.getInstance();

        //create account on firebase for a particular user
//        mFirebaseAuth.createUserWithEmailAndPassword(userInfo.getEmailAddress(),
//                                                     userInfo.getPassword())
//                     .addOnCompleteListener(this, task -> {
//                         if (task.isSuccessful()) {
//                             Log.d(TAG, "User Created Successfully");
//                             showPopUp();
//                         } else {
//                             Log.d(TAG, "Failure");
//                             displayMessage("Registration Failure");
//                         }
//                     });

        verifyPhoneNumber();

    }

    private void showPopUp() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(getLayoutInflater().inflate(R.layout.dialog_verify_phone, null));
        dialog.setCancelable(false);

        dialog.show();

        int[] dimens = getScreenDimens();
        dialog.getWindow()
              .setLayout(dimens[0] / 2, dimens[0] / 2);
    }

    private void verifyPhoneNumber() {
        displayMessage("Verifying Phone");

        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mFirebaseAuth)
                                                       .setPhoneNumber(COUNTRY_CODE + "7906097190")
                                                                       //userInfo.getPhone())
                                                       .setTimeout(30L, TimeUnit.SECONDS)
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

    private void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
             .show();
    }

    /**
     * Method to show {@link DatePickerFragment}
     *
     * @param view the button that was clicked
     */
    public void openDOB(View view) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
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
        String inputYear = String.valueOf(year);
        String inputMonth = theMonth(month);
        String inputDay = String.valueOf(day);

        String message = inputDay + "/" + inputMonth + "/" + inputYear;

        if (binding.textDob.getEditText() != null) {
            binding.textDob.getEditText()
                           .setText(message);
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


    public void verifyOTP(View view) {
        DialogVerifyPhoneBinding binding = DialogVerifyPhoneBinding.inflate(getLayoutInflater());
        String enteredOTP = binding.editOtp.getText()
                                           .toString();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,
                                                                         enteredOTP);

        signInWithPhoneAuthCredential(credential);

    }
}