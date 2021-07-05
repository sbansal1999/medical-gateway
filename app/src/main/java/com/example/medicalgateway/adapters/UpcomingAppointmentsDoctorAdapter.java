package com.example.medicalgateway.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalgateway.R;
import com.example.medicalgateway.datamodels.DoctorAppointment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UpcomingAppointmentsDoctorAdapter extends RecyclerView.Adapter<UpcomingAppointmentsDoctorAdapter.ViewHolder> {
    private ArrayList<DoctorAppointment> data;
    private Context context;

    public UpcomingAppointmentsDoctorAdapter(ArrayList<DoctorAppointment> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.upcoming_appoint_doctor_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        DoctorAppointment currentItem = data.get(position);
        holder.getTextAppointDate().setText(currentItem.getDateAppoint());
        holder.getTextProblemDesc().setText(currentItem.getProblemDesc());

        String status = currentItem.isAppointmentFulfilled() ? "Fulfilled" : "Not Yet Fulfilled";
        holder.getTextAppStatus().setText(status);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textAppointDate;
        private final TextView textProblemDesc;
        private final TextView textAppStatus;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            textAppointDate = itemView.findViewById(R.id.text_appointDateDoc);
            textProblemDesc = itemView.findViewById(R.id.text_problemDescDoc);
            textAppStatus = itemView.findViewById(R.id.text_appStatusDoc);
        }

        public TextView getTextAppointDate() {
            return textAppointDate;
        }

        public TextView getTextProblemDesc() {
            return textProblemDesc;
        }

        public TextView getTextAppStatus() {
            return textAppStatus;
        }
    }
}
