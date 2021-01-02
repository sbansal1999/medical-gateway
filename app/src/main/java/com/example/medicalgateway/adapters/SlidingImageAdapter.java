package com.example.medicalgateway.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.medicalgateway.slidingimagesfragment.sliding_activity.SlidingImageFirstFragment;
import com.example.medicalgateway.slidingimagesfragment.sliding_activity.SlidingImageSecondFragment;
import com.example.medicalgateway.slidingimagesfragment.sliding_activity.SlidingImageThirdFragment;

public class SlidingImageAdapter extends FragmentStatePagerAdapter {
    private final int mNumOfTabs;

    public SlidingImageAdapter(@NonNull FragmentManager fm, int behavior, int mNumOfTabs) {
        super(fm, behavior);
        this.mNumOfTabs = mNumOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SlidingImageFirstFragment();
            case 1:
                return new SlidingImageSecondFragment();
            case 2:
                return new SlidingImageThirdFragment();
            default:
                Log.d("TAG", "Something unexpected");
        }
        return new SlidingImageFirstFragment();
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
