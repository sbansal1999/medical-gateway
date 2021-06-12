package com.example.medicalgateway;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class PharmacyAdapter extends FirebaseRecyclerAdapter<MedicineInfo, PharmacyAdapter.ViewHolder> {

    public PharmacyAdapter(@NonNull @NotNull FirebaseRecyclerOptions<MedicineInfo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull MedicineInfo model) {
        holder.getTextMedName()
              .setText(model.getName());
        holder.getTextMedPrice()
              .setText(model.getPrice());
        holder.getTextMedUnit()
              .setText(model.getUnit());
        holder.getTextMedMfgBy()
              .setText(model.getMfgBy());
        holder.getTextMedCtg()
              .setText(model.getCategory());

        Picasso.get()
               .load(model.getPhotoURL())
               .into(holder.getImageMedicine());
    }

    @NonNull
    @NotNull
    @Override
    public PharmacyAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.singlepharmacy_row, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageMedicine;
        private final TextView textMedName;
        private final TextView textMedPrice;
        private final TextView textMedUnit;
        private final TextView textMedMfgBy;
        private final TextView textMedCtg;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageMedicine = itemView.findViewById(R.id.image_medicine);
            textMedName = itemView.findViewById(R.id.text_medName);
            textMedPrice = itemView.findViewById(R.id.text_medPrice);
            textMedUnit = itemView.findViewById(R.id.text_medUnit);
            textMedMfgBy = itemView.findViewById(R.id.text_medMfgBy);
            textMedCtg = itemView.findViewById(R.id.text_medCtg);
        }

        public TextView getTextMedCtg() {
            return textMedCtg;
        }

        public ImageView getImageMedicine() {
            return imageMedicine;
        }

        public TextView getTextMedName() {
            return textMedName;
        }

        public TextView getTextMedPrice() {
            return textMedPrice;
        }

        public TextView getTextMedUnit() {
            return textMedUnit;
        }

        public TextView getTextMedMfgBy() {
            return textMedMfgBy;
        }
    }


}
