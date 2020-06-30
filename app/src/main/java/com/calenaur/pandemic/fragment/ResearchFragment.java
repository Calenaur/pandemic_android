package com.calenaur.pandemic.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.calenaur.pandemic.R;
import com.calenaur.pandemic.SharedGameDataViewModel;
import com.calenaur.pandemic.api.model.user.UserMedication;
import com.calenaur.pandemic.view.MedicineCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ResearchFragment extends Fragment {

    private LinearLayout medicationLayout;
    private FloatingActionButton fab;
    private SharedGameDataViewModel data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_research, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        data = ViewModelProviders.of(requireActivity()).get(SharedGameDataViewModel.class);
        medicationLayout = view.findViewById(R.id.medications);
        fab = view.findViewById(R.id.add);
        fab.setOnClickListener(this::onFABClick);

        Bundle args = getArguments();
        if (args != null)
            if (args.containsKey("user_medication")) {
                NavController navController = Navigation.findNavController(medicationLayout);
                navController.popBackStack();
                UserMedication userMedication = (UserMedication) args.getSerializable("user_medication");
                //TODO::This will only work with ID from the database, make a PUT request first
                data.getRegistrar().getUserMedicationRegistry().register(-1, userMedication);
            }

        UserMedication[] medications = data.getMedications();
        if (medications == null)
            return;

        if (medications.length < 1)
            return;

        medicationLayout.removeAllViews();
        for (UserMedication userMedication : medications) {
            MedicineCardView.LayoutParams layoutParams = new MedicineCardView.LayoutParams(MedicineCardView.LayoutParams.MATCH_PARENT, MedicineCardView.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 10);
            MedicineCardView card = new MedicineCardView(
                    getContext(),
                    userMedication,
                    userMedication.getMedication(data.getRegistrar().getMedicationRegistry()),
                    userMedication.getMedicationTraits(data.getRegistrar().getMedicationTraitRegistry())
            );
            card.setLayoutParams(layoutParams);
            card.setClickable(true);
            card.setOnClickListener(this::onMedicineCardClick);
            UserMedication currentMedication = data.getCurrentMedication();
            if (currentMedication != null)
                if (currentMedication.id == userMedication.id)
                    card.setSelected(true);

            medicationLayout.addView(card);
        }
    }

    private void onMedicineCardClick(View view) {
        if (!(view instanceof MedicineCardView))
            return;

        MedicineCardView card = (MedicineCardView) view;
        if (card.getUserMedication() == null)
            return;

        for (int i=0; i<medicationLayout.getChildCount(); i++)
            if (medicationLayout.getChildAt(i) instanceof MedicineCardView) {
                MedicineCardView child = (MedicineCardView) medicationLayout.getChildAt(i);
                child.setSelected(card.getMedication().id == child.getMedication().id);
            }
        data.setCurrentUserMedicationID(card.getUserMedication().id);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        data = ViewModelProviders.of(requireActivity()).get(SharedGameDataViewModel.class);
    }

    public void onFABClick(View v) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.research_new)
                .setCancelable(true)
                .setMessage(getResources().getString(R.string.research_new_cost, 500))
                .setPositiveButton(getResources().getString(R.string.research_new_pay, 500), this::onResearchNew)
                .setNegativeButton(R.string.cancel, this::onFABCancel)
                .create();
        dialog.show();
    }

    public void onResearchNew(DialogInterface dialog, int which) {
        if (data.getLocalUser().pay(SharedGameDataViewModel.BASE_RESEARCH_COST)) {
            dialog.dismiss();
            Navigation.findNavController(fab).navigate(R.id.researchChoiceFragment);
            return;
        }

        Toast.makeText(getContext(), getResources().getString(R.string.insufficient_balance), Toast.LENGTH_LONG).show();
    }

    public void onFABCancel(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}