package com.calenaur.pandemic.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import com.calenaur.pandemic.R;
import com.calenaur.pandemic.SharedGameDataViewModel;
import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.medication.MedicationTrait;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.view.MedicineCardView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ResearchChoiceFragment extends Fragment {

    private static final int RESEARCH_TIME_SECONDS = 5;

    private SharedGameDataViewModel data;
    private LinearLayout researchCandidates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_research_choice, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        data = ViewModelProviders.of(requireActivity()).get(SharedGameDataViewModel.class);
        researchCandidates = view.findViewById(R.id.research_candidates);
        View loader = view.findViewById(R.id.loader);
        View content = view.findViewById(R.id.content);
        new LoadTask(this, getActivity()).execute(loader, content);
    }

    private void generateCandidate() {
        LocalUser localUser = data.getLocalUser();
        Medication[] medications = data.getRegistrar().getMedicationRegistry().toArray(new Medication[]{});
        MedicationTrait[] traits = data.getRegistrar().getMedicationTraitRegistry().toArray(new MedicationTrait[]{});
        ArrayList<Medication> candidates = new ArrayList<>();

        for (Medication medication : medications) {
            if (-1 > localUser.getTier().getID())
                continue;

            candidates.add(medication);
        }

        Random random = new Random();
        Medication candidate = candidates.get(random.nextInt(candidates.size()));

        ArrayList<MedicationTrait> candidateTraits = new ArrayList<>();
        if (candidate.maximum_traits > 0)
            for (MedicationTrait trait : traits) {
                if (candidateTraits.size() >= candidate.maximum_traits)
                    break;

                if (random.nextFloat() < 0.5f)
                    continue;

                candidateTraits.add(trait);
            }

        appendResearchCandidate(candidate, candidateTraits.toArray(new MedicationTrait[]{}));
    }

    private void appendResearchCandidate(Medication medication, MedicationTrait[] traits) {
        if (getActivity() == null)
            return;

        if (getActivity().getApplicationContext() == null)
            return;

        MedicineCardView.LayoutParams layoutParams = new MedicineCardView.LayoutParams(MedicineCardView.LayoutParams.MATCH_PARENT, MedicineCardView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);

        MedicineCardView card = new MedicineCardView(getActivity(), medication, traits);
        card.setClickable(true);
        card.setOnClickListener(this::onCandidateSelect);
        card.setLayoutParams(layoutParams);
        researchCandidates.addView(card);
    }

    private void onCandidateSelect(View view) {
    }

    private static class LoadTask extends AsyncTask<View, Void, Void> {

        private WeakReference<ResearchChoiceFragment> researchChoiceFragment;
        private WeakReference<Activity> activityRef;

        LoadTask(ResearchChoiceFragment researchChoiceFragment, Activity activity) {
            this.researchChoiceFragment = new WeakReference<>(researchChoiceFragment);
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(View... views) {
            Activity activity = activityRef.get();
            if (activity == null) {
                return null;
            }

            if (views.length < 2) {
                return null;
            }

            View view = views[0];
            View content = views[1];

            activity.runOnUiThread(() -> {
                content.setVisibility(View.GONE);
                AlphaAnimation inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                view.setAnimation(inAnimation);
                view.setVisibility(View.VISIBLE);
                for (int i=0; i<10; i++) {
                    researchChoiceFragment.get().generateCandidate();
                }
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
                content.setVisibility(View.VISIBLE);
            });
            return null;
        }
    }

}