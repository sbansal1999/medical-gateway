package com.example.medicalgateway;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyViewHolder>
{
ArrayList<PharmacyDataModel> data;

    public PharmacyAdapter(ArrayList<PharmacyDataModel> data)
    {
        this.data = data;
    }

    @NonNull
    @Override
    public PharmacyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.singlepharmacy_row,parent,false);
        return new PharmacyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacyViewHolder holder, int position)
    {
        holder.textMedName.setText(data.get(position).getMed_name());
        holder.textMedQty.setText(data.get(position).getQty_change());
        holder.textMedPrice.setText(data.get(position).getPrice_change());
        holder.textMedMfg.setText(data.get(position).getMfg_change());
        holder.imageMed.setImageResource(data.get(position).getImg_name());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
