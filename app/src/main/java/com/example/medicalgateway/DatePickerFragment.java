package com.example.medicalgateway;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar  = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                                                 this,
                                                                 year,
                                                                 month,
                                                                 day);
        datePickerDialog.setCancelable(false);
        return datePickerDialog;
        //TODO remove cancel button smh
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        RegisterActivity registerActivity = (RegisterActivity) getActivity();
        if (registerActivity != null) {
            registerActivity.processDatePickerResult(year, month, dayOfMonth);
        }

    }
}
