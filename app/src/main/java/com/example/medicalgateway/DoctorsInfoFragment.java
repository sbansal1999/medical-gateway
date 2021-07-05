package com.example.medicalgateway;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.medicalgateway.databinding.FragmentDoctorsInfoBinding;

public class DoctorsInfoFragment extends Fragment {

    private FragmentDoctorsInfoBinding mBinding;

    public DoctorsInfoFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentDoctorsInfoBinding.inflate(inflater, container, false);
        return mBinding.getRoot();

    }
}