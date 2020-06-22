package com.calenaur.pandemic.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calenaur.pandemic.R;
import com.calenaur.pandemic.SharedGameDataViewModel;

public class ProductionFragment extends Fragment {

    private static final String balanceText = "$ :";

    private SharedGameDataViewModel balanceViewModel;

    private ImageView generator;
    private TextView counter;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_production, container, false);
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        generator = view.findViewById(R.id.generator);
        counter = view.findViewById(R.id.counter);
        counter.setText(balanceText+ 0);
        generator.setOnClickListener((v) -> balanceViewModel.incrementBalance(100));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        balanceViewModel = ViewModelProviders.of(requireActivity()).get(SharedGameDataViewModel.class);
        balanceViewModel.getBalance().observe(getViewLifecycleOwner(), balance -> {
            counter.setText(balanceText + getAppendix(balance));
        });
    }

    public String getAppendix(long value){
        //TODO optimise the conversion check

        if (value < 1000){
            return ""+value;
        }else if(value > 1000 && value < 1000000){
            return ""+(int)value/1000+"K";
        }else if(value > 1000000 && value < 1000000000){
            return ""+(int)value/1000000+"M";
        }
        
        return "";
    }
}