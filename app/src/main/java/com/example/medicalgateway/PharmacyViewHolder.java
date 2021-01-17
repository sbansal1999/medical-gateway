package com.example.medicalgateway;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class PharmacyViewHolder extends RecyclerView.ViewHolder
{

    ImageView Img;
    TextView Med_name,Price_change,Mfg_change,Med_qty;
    public PharmacyViewHolder(@NotNull View itemView)
    {
        super((itemView));
        Img=(ImageView)itemView.findViewById(R.id.image_logo);
        Med_name=(TextView)itemView.findViewById(R.id.text_medicine_change);
        Price_change=(TextView)itemView.findViewById(R.id.text_price_change);
        Mfg_change=(TextView)itemView.findViewById(R.id.text_mfg_change);
        Med_qty=(TextView)itemView.findViewById(R.id.text_quantity_tablets);
    }
}
