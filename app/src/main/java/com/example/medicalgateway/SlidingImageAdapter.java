package com.example.medicalgateway;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SlidingImageAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

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
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
