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
import com.calenaur.pandemic.api.API;
import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.net.response.ErrorCode;
import com.calenaur.pandemic.api.store.PromiseHandler;
import com.tomer.fadingtextview.FadingTextView;

import java.util.Arrays;

public class ProductionFragment extends Fragment {

    private static final String balanceText = "$ :";
    private static final String[] fadetests = {"wow", "damn dude", "amazing", "good job"};

    private SharedGameDataViewModel sharedGameDataViewModel;

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
            sharedGameDataViewModel.incrementBalance();
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedGameDataViewModel = ViewModelProviders.of(requireActivity()).get(SharedGameDataViewModel.class);
        sharedGameDataViewModel.getBalance().observe(getViewLifecycleOwner(), balance -> {
            counter.setText(balanceText + sharedGameDataViewModel.getAppendix());
        });
        refresh();
    }

    public void refresh() {
        LocalUser localUser = sharedGameDataViewModel.getLocalUser();
        API api = sharedGameDataViewModel.getApi();
        api.getMedicineStore().medications(localUser, new PromiseHandler<Medication[]>() {
            @Override
            public void onDone(Medication[] object) {
                System.out.println(Arrays.toString(object));
            }

            @Override
            public void onError(ErrorCode errorCode) {
                System.out.println(errorCode);
            }
        });
    }
}