package com.calenaur.pandemic.fragment;

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
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.model.user.UserDisease;
import com.calenaur.pandemic.api.model.user.UserEvent;
import com.calenaur.pandemic.view.DiseaseCardView;
import com.calenaur.pandemic.view.EventCardView;
import com.calenaur.pandemic.view.MedicineCardView;

public class DiseaseFragment extends Fragment {

    private LinearLayout diseaseLayout;
    private SharedGameDataViewModel data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_disease, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        data = ViewModelProviders.of(requireActivity()).get(SharedGameDataViewModel.class);
        diseaseLayout = view.findViewById(R.id.diseases);
        refresh();
    }

    public void refresh() {
        Bundle args = getArguments();
        if (args == null)
            return;

        LocalUser localUser = (LocalUser) args.get("local_user");
        if (localUser == null)
            return;

        UserDisease[] userDiseases = data.getRegistrar().getUserDiseaseRegistry().toArray(new UserDisease[]{});
        if (userDiseases.length > 0)
            diseaseLayout.removeAllViews();

        DiseaseCardView.LayoutParams layoutParams = new DiseaseCardView.LayoutParams(DiseaseCardView.LayoutParams.MATCH_PARENT, DiseaseCardView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);

        for (UserDisease disease : userDiseases)
            if (disease != null) {
                DiseaseCardView card = new DiseaseCardView(getContext(), data.getRegistrar().getDiseaseRegistry().get(disease.id));
                card.setLayoutParams(layoutParams);
                diseaseLayout.addView(card);
            }
    }
}