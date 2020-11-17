package com.example.medicalgateway;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.medicalgateway.adapters.SlidingImageHomeAdapter;
import com.example.medicalgateway.databinding.FragmentHomePatientBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private final static int NUMBER_OF_IMAGES = 3;
    private static final long SCROLL_DELAY = 5000;
    private FragmentHomePatientBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomePatientBinding.inflate(inflater);

        //Set Default Image Level
        binding.imageDotFirst.setImageLevel(1);

        SlidingImageHomeAdapter imageHomeAdapter = new SlidingImageHomeAdapter(
                getChildFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                NUMBER_OF_IMAGES);

        binding.viewPagerImages.setAdapter(imageHomeAdapter);

        binding.viewPagerImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                highlightDot(position);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //TODO add something to make sure to stop on user touch

        Handler handler = new Handler();
        Runnable update = () -> {
            int current = binding.viewPagerImages.getCurrentItem();
            if (current == 2) {
                binding.viewPagerImages.setCurrentItem(0, true);
            } else {
                binding.viewPagerImages.setCurrentItem(++current, true);
            }
        };

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, SCROLL_DELAY, SCROLL_DELAY);

        return binding.getRoot();

    }

    /**
     * Method to change the dots below the viewPager according to the {@code position}
     *
     * @param position the index of the image currently on the screen
     */
    private void highlightDot(int position) {
        binding.imageDotFirst.setImageLevel(0);
        binding.imageDotSecond.setImageLevel(0);
        binding.imageDotThird.setImageLevel(0);
        switch (position) {
            case 0:
                binding.imageDotFirst.setImageLevel(1);
                break;
            case 1:
                binding.imageDotSecond.setImageLevel(1);
                break;
            case 2:
                binding.imageDotThird.setImageLevel(1);
                break;
        }
    }
}