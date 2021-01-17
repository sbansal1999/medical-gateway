package com.example.medicalgateway;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.medicalgateway.databinding.FragmentPharmacyPatientBinding;

import java.util.ArrayList;

public class PharmacyPatientFragment extends Fragment {
    private FragmentPharmacyPatientBinding binding;
    PharmacyAdapter Adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding= FragmentPharmacyPatientBinding.inflate(inflater);
        binding.recylerviewPharmacy.setLayoutManager(new LinearLayoutManager(getContext()));
        Adapter=new PharmacyAdapter(dataqueue());
        binding.recylerviewPharmacy.setAdapter(Adapter);
        return binding.getRoot();
    }
    public ArrayList<PharmacyDataModel> dataqueue()
    {
        ArrayList<PharmacyDataModel> holder= new ArrayList<>();
        PharmacyDataModel obj1= new PharmacyDataModel();
        obj1.setMed_name("Revital");
        obj1.setMfg_change("HealthBio Pharmaceuticals");
        obj1.setPrice_change("100Rs.");
        obj1.setQty_change("20 tablets");
        obj1.setImg_name(R.drawable.hospital_logo);
        holder.add(obj1);
        PharmacyDataModel obj2= new PharmacyDataModel();
        obj2.setMed_name("Multivitamins");
        obj2.setMfg_change("HealthBio Pharmaceuticals");
        obj2.setPrice_change("200Rs.");
        obj2.setQty_change("30 tablets");
        obj2.setImg_name(R.drawable.hospital_logo);
        holder.add(obj2);
        PharmacyDataModel obj3= new PharmacyDataModel();
        obj3.setMed_name("Revital");
        obj3.setMfg_change("HealthBio Pharmaceuticals");
        obj3.setPrice_change("100Rs.");
        obj3.setQty_change("20 tablets");
        obj3.setImg_name(R.drawable.hospital_logo);
        holder.add(obj3);
        PharmacyDataModel obj4= new PharmacyDataModel();
        obj4.setMed_name("Revital");
        obj4.setMfg_change("HealthBio Pharmaceuticals");
        obj4.setPrice_change("100Rs.");
        obj4.setQty_change("20 tablets");
        obj4.setImg_name(R.drawable.hospital_logo);
        holder.add(obj4);
        PharmacyDataModel obj5= new PharmacyDataModel();
        obj5.setMed_name("Revital");
        obj5.setMfg_change("HealthBio Pharmaceuticals");
        obj5.setPrice_change("100Rs.");
        obj5.setQty_change("20 tablets");
        obj5.setImg_name(R.drawable.hospital_logo);
        holder.add(obj5);
        PharmacyDataModel obj6= new PharmacyDataModel();
        obj6.setMed_name("Revital");
        obj6.setMfg_change("HealthBio Pharmaceuticals");
        obj6.setPrice_change("100Rs.");
        obj6.setQty_change("20 tablets");
        obj6.setImg_name(R.drawable.hospital_logo);
        holder.add(obj6);

        return holder;
    }
}