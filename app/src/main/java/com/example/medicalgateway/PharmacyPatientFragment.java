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
        obj1.setMed_name("Remdesiver");
        obj1.setMfg_change("HealthBio Pharmaceuticals");
        obj1.setPrice_change("10000Rs.");        obj1.setQty_change("1 unit");
        obj1.setImg_name(R.drawable.remdesiver);
        holder.add(obj1);
        PharmacyDataModel obj2= new PharmacyDataModel();
        obj2.setMed_name("PPE Kits");
        obj2.setMfg_change("HealthBio Pharmaceuticals");
        obj2.setPrice_change("800Rs.");
        obj2.setQty_change("1 unit");
        obj2.setImg_name(R.drawable.ppe);
        holder.add(obj2);
        PharmacyDataModel obj3= new PharmacyDataModel();
        obj3.setMed_name("Masks");
        obj3.setMfg_change("HealthBio Pharmaceuticals");
        obj3.setPrice_change("100Rs.");
        obj3.setQty_change("1 unit");
        obj3.setImg_name(R.drawable.masks);
        holder.add(obj3);
        PharmacyDataModel obj4= new PharmacyDataModel();
        obj4.setMed_name("Gloves");
        obj4.setMfg_change("HealthBio Pharmaceuticals");
        obj4.setPrice_change("50Rs.");
        obj4.setQty_change("1 unit");
        obj4.setImg_name(R.drawable.gloves);
        holder.add(obj4);
        PharmacyDataModel obj5= new PharmacyDataModel();
        obj5.setMed_name("Gluconate tablets");
        obj5.setMfg_change("HealthBio Pharmaceuticals");
        obj5.setPrice_change("100Rs.");
        obj5.setQty_change("20 tablets");
        obj5.setImg_name(R.drawable.gluconate);
        holder.add(obj5);
        PharmacyDataModel obj6= new PharmacyDataModel();
        obj6.setMed_name("Sugar Free");
        obj6.setMfg_change("HealthBio Pharmaceuticals");
        obj6.setPrice_change("250Rs.");
        obj6.setQty_change("1 unit");
        obj6.setImg_name(R.drawable.sugarfree);
        holder.add(obj6);
        PharmacyDataModel obj7= new PharmacyDataModel();
        obj7.setMed_name("Zincovit");
        obj7.setMfg_change("HealthBio Pharmaceuticals");
        obj7.setPrice_change("150Rs.");
        obj7.setQty_change("1 unit");
        obj7.setImg_name(R.drawable.zincovit);
        holder.add(obj7);

        return holder;
    }
}