package com.example.medicalgateway;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class HomeViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageMed;
    private TextView textMedName;
    private View wholeView;

    public HomeViewHolder(@NotNull View itemView) {
        super((itemView));

        wholeView = itemView;
        imageMed = itemView.findViewById(R.id.image_medicine);
        textMedName = itemView.findViewById(R.id.textMedName);
    }

    public ImageView getImageMed() {
        return imageMed;
    }

    public void setImageMed(ImageView imageMed) {
        this.imageMed = imageMed;
    }

    public TextView getTextMedName() {
        return textMedName;
    }

    public void setTextMedName(TextView textMedName) {
        this.textMedName = textMedName;
    }

    public View getWholeView() {
        return wholeView;
    }

    public void setWholeView(View wholeView) {
        this.wholeView = wholeView;
    }

}
