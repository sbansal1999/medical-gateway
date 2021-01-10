package com.example.medicalgateway;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class Pharmacy_viewHolder extends RecyclerView.ViewHolder
{
    ImageView img;
    TextView med_name,price_change,mfg_change,med_qty;
    public Pharmacy_viewHolder(@NotNull View itemView)
    {
        super((itemView));
        img=(ImageView)itemView.findViewById(R.id.image_logo);
        med_name=(TextView)itemView.findViewById(R.id.text_medicine_change);
        price_change=(TextView)itemView.findViewById(R.id.text_price_change);
        mfg_change=(TextView)itemView.findViewById(R.id.text_mfg_change);
        med_qty=(TextView)itemView.findViewById(R.id.text_quantity_tablets);
    }
}
