package com.example.medicalgateway;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicalgateway.databinding.ActivityRegisterBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class RegisterActivity extends AppCompatActivity {
    //TODO add OTP verification
    public final static String TAG = "LogTag";
    private UserInfo userInfo;
    private ActivityRegisterBinding binding;


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
            storeData();
        }
    }

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

        return checkPass || password.length() >= 6 && checkNum && checkEMail;
    }

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

    private void performFirebaseOperations(@NotNull UserInfo userInfo) {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

        //create account on firebase for a particular user
        mFirebaseAuth.createUserWithEmailAndPassword(userInfo.getEmailAddress(),
                                                     userInfo.getPassword())
                     .addOnCompleteListener(this, task -> {
                         if (task.isSuccessful()) {
                             Log.d(TAG, "User Created Successfully");
                             showPopUp();

                         } else {
                             Log.d(TAG, "Failure");
                             displayMessage("Registration Failure");
                         }
                     });
    }

    private void showPopUp() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);

    }

    private void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
             .show();
    }

    public void openDOB(View view) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

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

    public String getTextFromTextInputLayout(@NotNull TextInputLayout textInputLayout) {
        return textInputLayout.getEditText()
                              .getText()
                              .toString();
    }
}