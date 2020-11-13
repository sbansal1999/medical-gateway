package com.example.medicalgateway;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medicalgateway.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

    }

    /**
     * Method which performs the registration process
     * @param view The Button that was clicked
     */
    public void doRegister(View view) {
        {
            String email = binding.textEmailAddress.getEditText().getText().toString();
            String phone = binding.textNumber.getEditText().getText().toString();
            String password = binding.textPassword.getEditText().getText().toString();
            String regexLowerCase = ".*[a-z].*";
            String regexUpperCase = ".*[A-Z].*";
            String regexNum = ".*[0-9].*";
            String regexSymbol = ".*[! @#$%^&*].*";
            String regexEMail = "^[a-zA-Z0-9._]+[0-9._]+@[a-zA-Z]+[.]+[a-z]+$";
            String regexPhone = "[0-9]{10}";


            boolean checkPass = password.matches(regexLowerCase) && password.matches(regexUpperCase) && password
                    .matches(regexNum) && password.matches(regexSymbol);
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
                binding.textNumber.setError("Invalid Number");
            }

            if (checkNum) {
                binding.textNumber.setError(null);
            }

            if (!checkEMail) {
                binding.textEmailAddress.setError("Invalid Email");
            }

            if (checkEMail) {
                binding.textEmailAddress.setError(null);
            }

        }
    }
}