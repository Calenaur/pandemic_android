package com.calenaur.pandemic.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import com.calenaur.pandemic.R;
import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.medication.MedicationTrait;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class ResearchChoiceFragment extends Fragment {

    private static final int RESEARCH_TIME_SECONDS = 5;

    private LinearLayout researchCandidates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_research_choice, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        View loader = view.findViewById(R.id.loader);
        researchCandidates = view.findViewById(R.id.research_candidates);
        new LoadTask(getActivity()).execute(loader);
    }

    private void appendResearchCandidate(Medication medication, MedicationTrait[] traits) {
        if (getActivity() == null)
            return;

        if (getActivity().getApplicationContext() == null)
            return;

        TypedValue background = new TypedValue();
        getActivity().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, background, true);

        CardView card = new CardView(getActivity().getApplicationContext());
        card.setClickable(true);
        card.setOnClickListener(this::onCandidateSelect);
        card.setMaxCardElevation(1f);
        card.setForeground(getResources().getDrawable(background.resourceId, getActivity().getTheme()));
        card.set
    }

    private void onCandidateSelect(View view) {
    }

    private static class LoadTask extends AsyncTask<View, Void, Void> {

        private WeakReference<Activity> activityRef;

        LoadTask(Activity activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(View... views) {
            Activity activity = activityRef.get();
            if (activity == null) {
                return null;
            }

            if (views.length < 1) {
                return null;
            }

            View view = views[0];

            activity.runOnUiThread(() -> {
                AlphaAnimation inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                view.setAnimation(inAnimation);
                view.setVisibility(View.VISIBLE);
            });

            try {
                for (int i = 0; i < RESEARCH_TIME_SECONDS; i++) {
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            activity.runOnUiThread(() -> {
                AlphaAnimation outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                view.setAnimation(outAnimation);
                view.setVisibility(View.GONE);
            });
            return null;
        }
    }

}