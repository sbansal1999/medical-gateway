package com.example.medicalgateway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                int flag=0;
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
                    flag=1;
                }
                if(checkpass)
                {
                    binding.textPassword.setError(null);
                    flag=0;
                }
               if(!checknum)
               {
                   binding.textNumber.setError("Invalid Number");
                   flag=1;
               }
                if(checknum)
                {
                    binding.textNumber.setError(null);
                    flag=0;
                }
               if(!check)
               {
                   binding.textEmailAddress.setError("Invalid Email");
                   flag=1;
               }
               if(check)
               {
                   binding.textEmailAddress.setError(null);
                   flag=0;
               }
               if(binding.checkBoxTc.isChecked()==false)
               {
                   Toast.makeText(RegisterActivity.this,"Please accept the terms and conditions",Toast.LENGTH_LONG).show();
                   flag=1;
               }
               else
               {
                   flag=0;
               }
               if(flag==0)
               {
                   Intent myIntent = new Intent(RegisterActivity.this, PatientPortalActivity.class);
                   RegisterActivity.this.startActivity(myIntent);
               }

            }
        });
    }
}