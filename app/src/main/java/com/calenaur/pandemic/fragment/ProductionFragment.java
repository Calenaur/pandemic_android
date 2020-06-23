package com.calenaur.pandemic.fragment;

import android.animation.ValueAnimator;
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
import com.tomer.fadingtextview.FadingTextView;

public class ProductionFragment extends Fragment {

    private static final String balanceText = "$ :";
    private static final String[] fadetests = {"wow", "damn dude", "amazing", "good job"};

    private SharedGameDataViewModel balanceViewModel;

    private ImageView generator;
    private TextView counter;
    private FadingTextView collectionIndicator;

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
        collectionIndicator = view.findViewById(R.id.collectionIndicator);
        counter.setText(balanceText+ 0);
        collectionIndicator.setTexts(fadetests);
        generator.setOnClickListener((v) -> {
            balanceViewModel.incrementBalance();
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        balanceViewModel = ViewModelProviders.of(requireActivity()).get(SharedGameDataViewModel.class);
        balanceViewModel.getBalance().observe(getViewLifecycleOwner(), balance -> {
            counter.setText(balanceText + balanceViewModel.getAppendix());
        });
    }
}