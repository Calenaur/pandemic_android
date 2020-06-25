package com.calenaur.pandemic.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.calenaur.pandemic.R;
import com.calenaur.pandemic.SharedGameDataViewModel;
import com.calenaur.pandemic.api.model.medication.Medication;

public class MedicineFragment extends Fragment {

    private SharedGameDataViewModel balanceViewModel;

    private LinearLayout medicineFragment;
    private LinearLayout container;

    private static final Medication[] medications = {new Medication(1,"paper", 2), new Medication(2,"pil", 5)};


    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medicine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        medicineFragment = view.findViewById(R.id.medicineFragment);
        container = new LinearLayout(getContext());

        for(Medication medication: medications){
            

            container.addView();
        }


        medicineFragment.addView(container);
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        balanceViewModel = ViewModelProviders.of(requireActivity()).get(SharedGameDataViewModel.class);
    }
}