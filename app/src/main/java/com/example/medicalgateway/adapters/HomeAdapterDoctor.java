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

import com.example.medicalgateway.CheckReportsDoctorActivity;
import com.example.medicalgateway.PatientAppointDoctorActivity;
import com.example.medicalgateway.R;
import com.example.medicalgateway.UpcomingAppointDoctorActivity;
import com.example.medicalgateway.datamodels.HomeDataModel;
import com.example.medicalgateway.datamodels.PatientAppointment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeAdapterDoctor extends RecyclerView.Adapter<HomeAdapterDoctor.ViewHolder> {
    ArrayList<HomeDataModel> data;
    Context context;

    public HomeAdapterDoctor(ArrayList<HomeDataModel> data, Context context) {
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
        final HomeDataModel temp = data.get(position);

        holder.getTextMedName()
              .setText(data.get(position)
                           .getMed_name());
        holder.getImageMed()
              .setImageResource(data.get(position)
                                    .getImg_name());
        holder.getWholeView()
              .setOnClickListener(v -> {
                  switch (position) {
                      case 0: {
                          Intent intent = new Intent(context, CheckReportsDoctorActivity.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                          context.startActivity(intent);
                          break;
                      }
                      case 1: {
                          Intent intent1 = new Intent(context, UpcomingAppointDoctorActivity.class);
                          intent1.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                          context.startActivity(intent1);
                          break;
                      }
                      case 2: {
                          Intent intent2 = new Intent(context, PatientAppointDoctorActivity.class);
                          intent2.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                          context.startActivity(intent2);
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
            super(itemView);

            imageMed = itemView.findViewById(R.id.image_medicine);
            textMedName = itemView.findViewById(R.id.textMedName);
            wholeView = itemView;
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
