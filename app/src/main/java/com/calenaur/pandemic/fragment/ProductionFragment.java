package com.calenaur.pandemic.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.calenaur.pandemic.R;
import com.calenaur.pandemic.SharedGameDataViewModel;
import com.calenaur.pandemic.api.API;
import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.net.response.ErrorCode;
import com.calenaur.pandemic.api.store.PromiseHandler;

import java.util.Arrays;

public class ProductionFragment extends Fragment {

    private static final String balanceText = "$ :";

    private SharedGameDataViewModel sharedGameDataViewModel;

    private ImageView generator;
    private TextView counter;
    private RelativeLayout clickContainer;

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
        clickContainer = view.findViewById(R.id.clickContainer);
        counter.setText(balanceText+ 0);
        generator.setOnTouchListener((v, e) -> {
            if (e.getAction() != MotionEvent.ACTION_UP){
                return true;
            }
            sharedGameDataViewModel.incrementBalance();
            TextView indicator = new TextView(getContext());

            final Animation out = new AlphaAnimation(1.0f, 0.0f);
            out.setDuration(1000);

            out.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    indicator.setText("+"+sharedGameDataViewModel.getWorth());
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    clickContainer.removeView(indicator);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            indicator.startAnimation(out);
            indicator.setX(e.getX());
            indicator.setY(e.getY());
            clickContainer.addView(indicator);
            return true;
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