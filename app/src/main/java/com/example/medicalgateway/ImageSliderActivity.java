package com.example.medicalgateway;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.Objects;

public class ImageSliderActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ImageView image_dot_first, image_dot_second, image_dot_third;
    private final static int NUMBER_OF_IMAGES = 3;
    private Button button_skip,button_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_image_slider);

        viewPager = findViewById(R.id.viewPager_images);

        image_dot_first = findViewById(R.id.image_dot_first);
        image_dot_second = findViewById(R.id.image_dot_second);
        image_dot_third = findViewById(R.id.image_dot_third);

        button_skip = findViewById(R.id.button_skip);
        button_next = findViewById(R.id.button_next);

        //Set Default Image Level
        image_dot_first.setImageLevel(1);



        SlidingImageAdapter imageAdapter = new SlidingImageAdapter(getSupportFragmentManager(),
                                                                   FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                                                                   NUMBER_OF_IMAGES);
        viewPager.setAdapter(imageAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
     * @param position the index of the image currently on the screen
     */
    private void highlightDot(int position) {
        button_skip.setVisibility(View.VISIBLE);
        button_next.setVisibility(View.INVISIBLE);

        image_dot_first.setImageLevel(0);
        image_dot_second.setImageLevel(0);
        image_dot_third.setImageLevel(0);
        switch (position) {
            case 0:
                image_dot_first.setImageLevel(1);
                break;
            case 1:
                image_dot_second.setImageLevel(1);
                break;
            case 2:
                image_dot_third.setImageLevel(1);
                button_skip.setVisibility(View.INVISIBLE);
                button_next.setVisibility(View.VISIBLE);
                break;
        }
    }
}