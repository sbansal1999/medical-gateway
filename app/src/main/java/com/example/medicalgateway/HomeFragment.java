package com.example.medicalgateway;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.example.medicalgateway.adapters.HomeAdapterPatient;
import com.example.medicalgateway.adapters.SlidingImageHomeAdapter;
import com.example.medicalgateway.databinding.FragmentHomePatientBinding;
import com.example.medicalgateway.datamodels.HomeDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.medicalgateway.MedicalUtils.checkIfPatient;

public class HomeFragment extends Fragment {

    private final static int NUMBER_OF_IMAGES = 3;
    private static final long SCROLL_DELAY = 5000;
    private HomeAdapterPatient adapter;
    private FragmentHomePatientBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomePatientBinding.inflate(inflater);
        binding.recyclerHome.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HomeAdapterPatient(dataQueue(), getContext());
        binding.recyclerHome.setAdapter(adapter);

        //Set Default Image Level
        binding.imageDotFirst.setImageLevel(1);
        binding.buttonBookAppointment.setOnClickListener(view -> {
            Fragment book_appointment_fragment = new BookAppointmentFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(((ViewGroup) getView().getParent()).getId(), book_appointment_fragment); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to back-stack
            transaction.commit();
        });
        SlidingImageHomeAdapter imageHomeAdapter = new SlidingImageHomeAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, NUMBER_OF_IMAGES);

        binding.viewPagerImages.setAdapter(imageHomeAdapter);

        binding.viewPagerImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
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


        if (checkIfPatient(getContext())) {
            insertPID();
        }

        return binding.getRoot();

    }



    private void insertPID() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance()
                                                    .getReference();

        FirebaseAuth user = FirebaseAuth.getInstance();

        if (user.getUid() != null) {
            rootRef.child("patients_info")
                   .child(user.getUid())
                   .child("patientID")
                   .addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                           SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity())
                                                                              .edit();

                           String pID = snapshot.getValue()
                                                .toString();

                           editor.putString(SharedPreferencesInfo.PREF_CURRENT_USER_PID, pID);
                           editor.apply();
                       }

                       @Override
                       public void onCancelled(@NonNull @NotNull DatabaseError error) {

                       }
                   });
        }
    }

    public ArrayList<HomeDataModel> dataQueue() {
        ArrayList<HomeDataModel> holder = new ArrayList<>();

        HomeDataModel obj1 = new HomeDataModel();
        obj1.setMed_name("Our Specialized Doctors");
        obj1.setImg_name(R.drawable.hospital_logo);
        holder.add(obj1);

        HomeDataModel obj2 = new HomeDataModel();
        obj2.setMed_name("Check Reports");
        obj2.setImg_name(R.drawable.hospital_logo);
        holder.add(obj2);

        HomeDataModel obj3 = new HomeDataModel();
        obj3.setMed_name("Previous Appointments");
        obj3.setImg_name(R.drawable.hospital_logo);
        holder.add(obj3);

        HomeDataModel obj4 = new HomeDataModel();
        obj4.setMed_name("Available Beds");
        obj4.setImg_name(R.drawable.hospital_logo);
        holder.add(obj4);

        HomeDataModel obj5 = new HomeDataModel();
        obj5.setMed_name("Pathology");
        obj5.setImg_name(R.drawable.hospital_logo);
        holder.add(obj5);

        HomeDataModel obj6 = new HomeDataModel();
        obj6.setMed_name("Online Prescription");
        obj6.setImg_name(R.drawable.hospital_logo);
        holder.add(obj6);

        HomeDataModel obj7 = new HomeDataModel();
        obj7.setMed_name("About Hospital");
        obj7.setImg_name(R.drawable.hospital_logo);
        holder.add(obj7);

        return holder;
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