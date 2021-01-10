package com.example.medicalgateway;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Pharmacy_Adapter extends RecyclerView.Adapter<Pharmacy_viewHolder>
{
ArrayList<PharmacyData_Model> data;

    public Pharmacy_Adapter(ArrayList<PharmacyData_Model> data)
    {
        this.data = data;
    }

    @NonNull
    @Override
    public Pharmacy_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.singlepharmacy_row,parent,false);
        return new Pharmacy_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Pharmacy_viewHolder holder, int position)
    {
        holder.med_name.setText(data.get(position).getMed_name());
        holder.med_qty.setText(data.get(position).getQty_change());
        holder.price_change.setText(data.get(position).getPrice_change());
        holder.mfg_change.setText(data.get(position).getMfg_change());
        holder.img.setImageResource(data.get(position).getImg_name());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
