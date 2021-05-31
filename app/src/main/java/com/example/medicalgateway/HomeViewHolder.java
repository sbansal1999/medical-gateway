package com.example.medicalgateway;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class HomeViewHolder extends RecyclerView.ViewHolder
{

    ImageView imageMed;
    TextView textMedName;

    public HomeViewHolder(@NotNull View itemView)
    {
        super((itemView));

        imageMed=(ImageView)itemView.findViewById(R.id.image_logo);
        textMedName=(TextView)itemView.findViewById(R.id.textMedName);
    }

}
