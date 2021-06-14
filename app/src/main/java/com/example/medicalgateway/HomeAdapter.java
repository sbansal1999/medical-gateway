package com.example.medicalgateway;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {
    private ArrayList<HomeDataModel> data;
    private Context context;

    public HomeAdapter(ArrayList<HomeDataModel> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.singlehome_row, parent, false);

        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.textMedName.setText(data.get(position)
                                       .getMed_name());
        holder.imageMed.setImageResource(data.get(position)
                                             .getImg_name());

        holder.wholeView.setOnClickListener(v -> {
            switch (position) {
                case 0: {
                    Intent intent = new Intent(context, DoctorInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                }
                case 1: {
                    Intent intent1 = new Intent(context, CheckReportsActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent1);
                    break;
                }
                case 2: {
                    Intent intent2 = new Intent(context, AvailableBedsActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent2);
                    break;
                }
                case 3: {
                    Intent intent3 = new Intent(context, PathologyActivity.class);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent3);
                    break;
                }
                case 4: {
                    Intent intent4 = new Intent(context, PreviousAppointmentsActivity.class);
                    intent4.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent4);
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
}
