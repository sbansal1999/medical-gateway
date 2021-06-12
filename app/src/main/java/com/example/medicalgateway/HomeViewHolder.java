package com.example.medicalgateway;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class HomeViewHolder extends RecyclerView.ViewHolder
{

    ImageView imageMed;
    TextView textMedName;

    public HomeViewHolder(@NotNull View itemView)
    {
        super((itemView));

        imageMed= itemView.findViewById(R.id.image_medicine);
        textMedName= itemView.findViewById(R.id.textMedName);
    }

}
