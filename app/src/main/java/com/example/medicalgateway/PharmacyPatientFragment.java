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
    Pharmacy_Adapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding= FragmentPharmacyPatientBinding.inflate(inflater);
       binding.recylerviewPharmacy.setLayoutManager(new LinearLayoutManager(getContext()));
       adapter=new Pharmacy_Adapter(dataqueue());
       binding.recylerviewPharmacy.setAdapter(adapter);
        return binding.getRoot();
    }
    public ArrayList<PharmacyData_Model> dataqueue()
    {
        ArrayList<PharmacyData_Model> holder= new ArrayList<>();
        PharmacyData_Model obj1= new PharmacyData_Model();
        obj1.setMed_name("Revital");
        obj1.setMfg_change("HealthBio Pharmaceuticals");
        obj1.setPrice_change("100 Rs.");
        obj1.setQty_change("20 tablets");
        obj1.setImg_name(R.drawable.hospital_logo);
        holder.add(obj1);
        PharmacyData_Model obj2= new PharmacyData_Model();
        obj2.setMed_name("Revital");
        obj2.setMfg_change("HealthBio Pharmaceuticals");
        obj2.setPrice_change("100 Rs.");
        obj2.setQty_change("20 tablets");
        obj2.setImg_name(R.drawable.hospital_logo);
        holder.add(obj2);
        PharmacyData_Model obj3= new PharmacyData_Model();
        obj3.setMed_name("Revital");
        obj3.setMfg_change("HealthBio Pharmaceuticals");
        obj3.setPrice_change("100 Rs.");
        obj3.setQty_change("20 tablets");
        obj3.setImg_name(R.drawable.hospital_logo);
        holder.add(obj3);
        PharmacyData_Model obj4= new PharmacyData_Model();
        obj4.setMed_name("Revital");
        obj4.setMfg_change("HealthBio Pharmaceuticals");
        obj4.setPrice_change("100 Rs.");
        obj4.setQty_change("20 tablets");
        obj4.setImg_name(R.drawable.hospital_logo);
        holder.add(obj4);
        PharmacyData_Model obj5= new PharmacyData_Model();
        obj5.setMed_name("Revital");
        obj5.setMfg_change("HealthBio Pharmaceuticals");
        obj5.setPrice_change("100 Rs.");
        obj5.setQty_change("20 tablets");
        obj5.setImg_name(R.drawable.hospital_logo);
        holder.add(obj5);
        PharmacyData_Model obj6= new PharmacyData_Model();
        obj6.setMed_name("Revital");
        obj6.setMfg_change("HealthBio Pharmaceuticals");
        obj6.setPrice_change("100 Rs.");
        obj6.setQty_change("20 tablets");
        obj6.setImg_name(R.drawable.hospital_logo);
        holder.add(obj6);

        return holder;
    }
}