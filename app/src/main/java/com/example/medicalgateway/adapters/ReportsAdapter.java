package com.example.medicalgateway.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalgateway.R;
import com.example.medicalgateway.datamodels.Reports;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.jetbrains.annotations.NotNull;

public class ReportsAdapter extends FirebaseRecyclerAdapter<Reports, ReportsAdapter.ViewHolder> {
    private final Context mContext;

    public ReportsAdapter(@NonNull @NotNull FirebaseRecyclerOptions<Reports> options,
                          Context context) {
        super(options);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position,
                                    @NonNull @NotNull Reports model) {
        holder.getTextReportDate()
              .setText(model.getReportDate());

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(model.getReportURL()), "application/pdf");

        Intent chooser = Intent.createChooser(intent, "Select the application to use");

        holder.getButtonViewReport()
              .setOnClickListener(v -> {
                  if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                      mContext.startActivity(chooser);
                  } else {
                      Toast.makeText(mContext, "Kindly Download a PDF Viewer First", Toast.LENGTH_SHORT)
                           .show();
                  }
              });
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.report_info_item, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textReportDate;
        private final Button buttonViewReport;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            textReportDate = itemView.findViewById(R.id.text_reportDate);
            buttonViewReport = itemView.findViewById(R.id.button_viewReport);
        }

        public TextView getTextReportDate() {
            return textReportDate;
        }

        public Button getButtonViewReport() {
            return buttonViewReport;
        }
    }
}
