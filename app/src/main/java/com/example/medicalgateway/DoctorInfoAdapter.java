package com.example.medicalgateway;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class DoctorInfoAdapter extends FirebaseRecyclerAdapter<DoctorInfo, DoctorInfoAdapter.ViewHolder> {

    public DoctorInfoAdapter(@NonNull @NotNull FirebaseRecyclerOptions<DoctorInfo> options) {
        super(options);
    }

    @NonNull
    @NotNull
    @Override
    public DoctorInfoAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent,
                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.doctor_info_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull DoctorInfoAdapter.ViewHolder holder,
                                    int position, @NonNull @NotNull DoctorInfo model) {
        holder.getTextDocName()
              .setText(model.getName());
        holder.getTextDocSpec()
              .setText(model.getSpeciality());

        Picasso.get()
               .load(model.getPhotoURL())
               .into(holder.getImageDoctor(), new Callback.EmptyCallback() {
                   @Override
                   public void onSuccess() {
                       holder.getProgressLoading()
                             .setVisibility(View.INVISIBLE);

                   }
               });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageDoctor;
        private TextView textDocName;
        private TextView textDocSpec;
        private ProgressBar progressLoading;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageDoctor = itemView.findViewById(R.id.image_doctor);
            textDocName = itemView.findViewById(R.id.text_docName);
            textDocSpec = itemView.findViewById(R.id.text_docSpec);
            progressLoading = itemView.findViewById(R.id.progress_image);
        }

        public ProgressBar getProgressLoading() {
            return progressLoading;
        }

        public void setProgressLoading(ProgressBar progressLoading) {
            this.progressLoading = progressLoading;
        }

        public ImageView getImageDoctor() {
            return imageDoctor;
        }

        public void setImageDoctor(ImageView imageDoctor) {
            this.imageDoctor = imageDoctor;
        }

        public TextView getTextDocName() {
            return textDocName;
        }

        public void setTextDocName(TextView textDocName) {
            this.textDocName = textDocName;
        }

        public TextView getTextDocSpec() {
            return textDocSpec;
        }

        public void setTextDocSpec(TextView textDocSpec) {
            this.textDocSpec = textDocSpec;
        }
    }
}
