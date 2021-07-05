package com.example.medicalgateway;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new CustomDatePickerDialog(getActivity(), this, year, month, day);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        RegisterActivity registerActivity = (RegisterActivity) getActivity();
        if (registerActivity != null) {
            registerActivity.processDatePickerResult(year, month, dayOfMonth);
        }

    }

    private static class CustomDatePickerDialog extends DatePickerDialog {

        public CustomDatePickerDialog(@NonNull Context context, @Nullable OnDateSetListener listener,
                                      int year, int month, int dayOfMonth) {
            super(context, listener, year, month, dayOfMonth);
            this.setButton(BUTTON_POSITIVE, "OK", this);
            this.setButton(BUTTON_NEGATIVE, "", this);
        }
    }
}
