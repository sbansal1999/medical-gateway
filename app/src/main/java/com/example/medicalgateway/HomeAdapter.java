package com.example.medicalgateway;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {
    ArrayList<HomeDataModel> data;
    int PreviousPosition = 0;
    Context context;

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
        final HomeDataModel temp = data.get(position);
        holder.textMedName.setText(data.get(position)
                                       .getMed_name());
        holder.imageMed.setImageResource(data.get(position)
                                             .getImg_name());
        holder.textMedName.setOnClickListener(v -> {
//            if (position == 0) {
//                Intent intent = new Intent(context, Doctors_Info.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }

            Log.d("TAG", "applied listener " + position);
            AppCompatActivity activity = (AppCompatActivity) context;
            switch (position) {
                case 0:
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, new DoctorsInfoFragment())
                            .addToBackStack("Doctors")
                            .commit();

                    Log.d("TAG", "first");
                    break;
                case 1:
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, new PathologyFragment())
                            .addToBackStack("Pathology")
                            .commit();
                    Log.d("TAG", "second");
                    break;

            }

        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
