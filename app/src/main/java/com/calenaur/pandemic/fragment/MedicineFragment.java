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
import android.widget.TextView;

import com.calenaur.pandemic.R;
import com.calenaur.pandemic.SharedGameDataViewModel;
import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.net.response.ErrorCode;
import com.calenaur.pandemic.api.store.PromiseHandler;

public class MedicineFragment extends Fragment {

    private SharedGameDataViewModel sharedGameDataViewModel;

    private LinearLayout medicineFragment;
    private LinearLayout container;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medicine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        medicineFragment = view.findViewById(R.id.medicineFragment);
        container = new LinearLayout(getContext());
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedGameDataViewModel = ViewModelProviders.of(requireActivity()).get(SharedGameDataViewModel.class);

        sharedGameDataViewModel.getRawApi().getMedicineStore().medications(sharedGameDataViewModel.getRawLocalUser(), new PromiseHandler<Medication[]>() {
            @Override
            public void onDone(Medication[] object) {
                for(Medication medication: object){
                    TextView textView = new TextView(getContext());
                    textView.setText(medication.toString());
                    container.addView(textView);
                }
                medicineFragment.addView(container);
            }

            @Override
            public void onError(ErrorCode errorCode) {

            }
        });
    }
}