package com.example.medicalgateway;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.medicalgateway.adapters.SlidingImageAdapter;
import com.example.medicalgateway.databinding.ActivityImageSliderBinding;

public class ImageSliderActivity extends AppCompatActivity {

    private final static int NUMBER_OF_IMAGES = 3;
    private ActivityImageSliderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityImageSliderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Hide Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //Set Default Image Level
        binding.imageDotFirst.setImageLevel(1);

        SlidingImageAdapter imageAdapter = new SlidingImageAdapter(getSupportFragmentManager(),
                                                                   FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                                                                   NUMBER_OF_IMAGES);
        binding.viewPagerImages.setAdapter(imageAdapter);

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
    }

    /**
     * Method to change the dots below the viewPager according to the {@code position}
     *
     * @param position the index of the image currently on the screen
     */
    private void highlightDot(int position) {
        binding.buttonSkip.setVisibility(View.VISIBLE);
        binding.buttonNext.setVisibility(View.INVISIBLE);

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
                binding.buttonSkip.setVisibility(View.INVISIBLE);
                binding.buttonNext.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void nextActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}