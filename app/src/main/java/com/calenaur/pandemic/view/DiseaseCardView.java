package com.calenaur.pandemic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.calenaur.pandemic.R;
import com.calenaur.pandemic.api.model.disease.Disease;
import com.calenaur.pandemic.api.model.event.Event;

public class DiseaseCardView extends LinearLayout {

    private CardView card;
    private Disease disease;

    public DiseaseCardView(Context context, Disease disease) {
        this(context, (AttributeSet) null);
        this.disease = disease;
        updateInfo();
    }

    public DiseaseCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        initLayout();
    }

    private void initLayout() {
        View view = inflate(getContext(), R.layout.view_disease_card, this);
        card = view.findViewById(R.id.card);
        card.setMaxCardElevation(1f);
    }

    private void updateInfo() {
        if (disease == null)
            return;

        TextView name = card.findViewById(R.id.name);
        TextView tier = card.findViewById(R.id.tier);
        TextView description = card.findViewById(R.id.description);

        name.setText(disease.name);
        name.setTextColor(disease.getTier().getColor());
        tier.setText(disease.getTier().getName());
        tier.setTextColor(disease.getTier().getColor());
        description.setText(disease.description);
    }

}
