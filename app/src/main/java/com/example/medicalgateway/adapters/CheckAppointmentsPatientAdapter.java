package com.example.medicalgateway.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalgateway.R;
import com.example.medicalgateway.datamodels.PatientAppointment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CheckAppointmentsPatientAdapter extends FirebaseRecyclerAdapter<PatientAppointment, CheckAppointmentsPatientAdapter.ViewHolder> {


    public CheckAppointmentsPatientAdapter(
            @NonNull @NotNull FirebaseRecyclerOptions<PatientAppointment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(
            @NonNull @NotNull CheckAppointmentsPatientAdapter.ViewHolder holder, int position,
            @NonNull @NotNull PatientAppointment model) {
        String appointDate = model.getDateAppoint();

        holder.getTextAppointDate().setText(convertDate(appointDate));

        holder.getTextPrefDoc().setText(model.getPrefDoctor());
        holder.getTextProblemDesc().setText(model.getProblemDesc());

        boolean appStatus = model.isAppointmentFulfilled();

        if (appStatus) {
            holder.getTextAppStatus().setText(R.string.fulfilled);
        } else {
            holder.getTextAppStatus().setText(R.string.not_fulfilled);
        }

    }

    @NonNull
    @NotNull
    @Override
    public CheckAppointmentsPatientAdapter.ViewHolder onCreateViewHolder(
            @NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_info_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Method to convert date from DD-MM-YYYY to DD MonthName YYYY
     *
     * @param inputDate the date to be converted
     * @return the converted date
     */
    private String convertDate(String inputDate) {
        SimpleDateFormat fromFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        SimpleDateFormat toFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

        try {
            return toFormat.format(fromFormat.parse(inputDate));

        } catch (ParseException e) {
            Log.d("test", e.getLocalizedMessage());
        }
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textAppointDate;
        private final TextView textPrefDoc;
        private final TextView textProblemDesc;
        private final TextView textAppStatus;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            textAppointDate = itemView.findViewById(R.id.text_appointDate);
            textPrefDoc = itemView.findViewById(R.id.text_prefDoc);
            textProblemDesc = itemView.findViewById(R.id.text_problemDesc);
            textAppStatus = itemView.findViewById(R.id.text_appStatus);
        }

        public TextView getTextAppointDate() {
            return textAppointDate;
        }

        public TextView getTextPrefDoc() {
            return textPrefDoc;
        }

        public TextView getTextProblemDesc() {
            return textProblemDesc;
        }

        public TextView getTextAppStatus() {
            return textAppStatus;
        }
    }
}
