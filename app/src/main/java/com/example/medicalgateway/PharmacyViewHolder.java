package com.example.medicalgateway;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class PharmacyViewHolder extends RecyclerView.ViewHolder
{

    ImageView imageMed;
    TextView textMedName,textMedPrice,textMedMfg,textMedQty;
    public PharmacyViewHolder(@NotNull View itemView)
    {
        super((itemView));
        imageMed=(ImageView)itemView.findViewById(R.id.image_logo);
        textMedName=(TextView)itemView.findViewById(R.id.textMedName);
        textMedPrice=(TextView)itemView.findViewById(R.id.textMedPriceChange);
        textMedMfg=(TextView)itemView.findViewById(R.id.textMedMfgChange);
        textMedQty=(TextView)itemView.findViewById(R.id.textMedQty);
    }
}
