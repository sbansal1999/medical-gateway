package com.example.medicalgateway;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.medicalgateway.databinding.FragmentPathologyBinding;

public class PathologyFragment extends Fragment {

    private FragmentPathologyBinding mBinding;

    public PathologyFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPathologyBinding.inflate(inflater, container, false);
        return mBinding.getRoot();

    }
}