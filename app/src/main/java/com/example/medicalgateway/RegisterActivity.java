package com.example.medicalgateway;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.medicalgateway.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String password = binding.textPassword.getEditText().getText().toString();
               String email = binding.textEmailAddress.getEditText().getText().toString();
               String phone = binding.textNumber.getEditText().getText().toString();
               String Password = binding.textPassword.getEditText().getText().toString();
               String lwcase = ".*[a-z].*";
               String upcase = ".*[A-Z].*";
               String num = ".*[0-9].*";
               String symbol = ".*[! @ # $ % ^ & *].*";
               String regex = "^[a-zA-Z0-9._]+[0-9._]+@[a-zA-Z]+[.]+[a-z]+$";
               String regexphone = "[0-9]{10}";
               //val passwordlayout =
               boolean checkpass = Password.matches(lwcase)&&Password.matches(upcase)&&Password.matches(num)&&Password.matches(symbol);
               boolean check=email.matches(regex);
               boolean checknum=phone.matches(regexphone);
                if(!checkpass||Password.length()<6)
                {
                    binding.textPassword.setError("Password must be - \n" +
                            "1 - Atleast 6 characters long \n" +
                            "2 - Must contain a uppercase alphabet \n" +
                            "3 - Must contain a lowercase alphabet \n" +
                            "4 - Must contain a number \n" +
                            "5 - Must contain a symbol - !,@,#,$,%,^,&,*");
                }
                if(checkpass)
                {
                    binding.textPassword.setError(null);
                }
               if(!checknum)
               {
                   binding.textNumber.setError("Invalid Number");
               }
                if(checknum)
                {
                    binding.textNumber.setError(null);
                }
               if(!check)
               {
                   binding.textEmailAddress.setError("Invalid Email");
               }
               if(check)
               {
                   binding.textEmailAddress.setError(null);
               }

            }
        });
    }
}