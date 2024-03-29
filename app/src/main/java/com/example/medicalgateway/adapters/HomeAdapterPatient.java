package com.example.medicalgateway.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalgateway.AboutHospitalActivity;
import com.example.medicalgateway.AvailableBedsActivity;
import com.example.medicalgateway.CheckReportsPatientActivity;
import com.example.medicalgateway.DoctorInfoActivity;
import com.example.medicalgateway.OnlinePrescriptionActivity;
import com.example.medicalgateway.PathologyActivity;
import com.example.medicalgateway.PreviousAppointmentsActivity;
import com.example.medicalgateway.R;
import com.example.medicalgateway.datamodels.HomeDataModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeAdapterPatient extends RecyclerView.Adapter<HomeAdapterPatient.ViewHolder> {
    private ArrayList<HomeDataModel> data;
    private Context context;

    public HomeAdapterPatient(ArrayList<HomeDataModel> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.singlehome_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextMedName().setText(data.get(position).getMed_name());
        holder.getImageMed().setImageResource(data.get(position).getImg_name());

        holder.getWholeView().setOnClickListener(v -> {
            switch (position) {
                case 0: {
                    Intent intent = new Intent(context, DoctorInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent);
                    break;
                }
                case 1: {
                    Intent intent1 = new Intent(context, CheckReportsPatientActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent1);
                    break;
                }
                case 2: {
                    Intent intent4 = new Intent(context, PreviousAppointmentsActivity.class);
                    intent4.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent4);
                    break;
                }
                case 3: {
                    Intent intent2 = new Intent(context, AvailableBedsActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent2);
                    break;
                }
                case 4: {
                    Intent intent3 = new Intent(context, PathologyActivity.class);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent3);
                    break;
                }
                case 5: {
                    Intent intent5 = new Intent(context, OnlinePrescriptionActivity.class);
                    intent5.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent5);
                    break;
                }
                case 6: {
                    Intent intent6 = new Intent(context, AboutHospitalActivity.class);
                    intent6.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent6);
                    break;
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageMed;
        private TextView textMedName;
        private View wholeView;

        public ViewHolder(@NotNull View itemView) {
            super((itemView));

            wholeView = itemView;
            imageMed = itemView.findViewById(R.id.image_medicine);
            textMedName = itemView.findViewById(R.id.textMedName);
        }

        public ImageView getImageMed() {
            return imageMed;
        }

        public void setImageMed(ImageView imageMed) {
            this.imageMed = imageMed;
        }

        public TextView getTextMedName() {
            return textMedName;
        }

        public void setTextMedName(TextView textMedName) {
            this.textMedName = textMedName;
        }

        public View getWholeView() {
            return wholeView;
        }

        public void setWholeView(View wholeView) {
            this.wholeView = wholeView;
        }

    }
}
