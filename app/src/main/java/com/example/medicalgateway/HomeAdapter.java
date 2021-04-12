package com.example.medicalgateway;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder>
{
    ArrayList<HomeDataModel> data;
    int PreviousPosition = 0;
    public HomeAdapter(ArrayList<HomeDataModel> data, Context context)
    {
        this.data = data;
    }
Context context;
    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.singlehome_row,parent,false);
        this.context=context;
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position)
    {
        final HomeDataModel temp = data.get(position);
        holder.textMedName.setText(data.get(position).getMed_name());
        holder.imageMed.setImageResource(data.get(position).getImg_name());
holder.textMedName.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(position==0)
        {
            Intent intent = new Intent(context, Doctors_Info.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }
});


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
