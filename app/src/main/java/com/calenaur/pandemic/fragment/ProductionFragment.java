package com.calenaur.pandemic.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calenaur.pandemic.R;


public class ProductionFragment extends Fragment {

    private ImageView generator;
    private TextView counter;
    private int count;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_production, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        generator = view.findViewById(R.id.generator);
        counter = view.findViewById(R.id.counter);
        count = -1;
        generator.setOnClickListener(this::onGenerate);
        generator.callOnClick();
    }

    private void onGenerate(View v) {
        counter.setText("Click count: " + (++count));
    }

}