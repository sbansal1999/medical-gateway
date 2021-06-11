package com.example.medicalgateway;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class HomeViewHolder_Doctor extends RecyclerView.ViewHolder {
    ImageView imageMed;
    TextView textMedName;

    public HomeViewHolder_Doctor(@NotNull View itemView)
    {
        super(itemView);

        imageMed=(ImageView)itemView.findViewById(R.id.image_logo);
        textMedName=(TextView)itemView.findViewById(R.id.textMedName);
    }
}
