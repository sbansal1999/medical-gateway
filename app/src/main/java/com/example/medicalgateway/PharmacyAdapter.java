package com.example.medicalgateway;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyViewHolder> implements Filterable {
    ArrayList<PharmacyDataModel> data;
    ArrayList<PharmacyDataModel> datacopy;
    private final Filter dataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<PharmacyDataModel> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(datacopy);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (PharmacyDataModel item : datacopy) {
                    if (item.getMed_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            data.clear();
            data.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public PharmacyAdapter(ArrayList<PharmacyDataModel> data) {
        this.data = data;
        datacopy = new ArrayList<>(data);
    }

    @NonNull
    @Override
    public PharmacyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.singlepharmacy_row, parent, false);
        return new PharmacyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacyViewHolder holder, int position) {
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

    @Override
    public Filter getFilter() {
        return dataFilter;
    }

}
