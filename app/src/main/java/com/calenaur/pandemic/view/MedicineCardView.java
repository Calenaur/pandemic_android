package com.calenaur.pandemic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.calenaur.pandemic.R;
import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.medication.MedicationTrait;

public class MedicineCardView extends LinearLayout {

    private Medication medication;
    private MedicationTrait[] medicationTraits;
    private boolean clickable;
    private CardView card;

    public MedicineCardView(Context context, Medication medication, MedicationTrait[] medicationTraits) {
        this(context, null);
        this.medication = medication;
        this.medicationTraits = medicationTraits;
        updateInfo();
    }

    public MedicineCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MedicineCardView, 0, 0);
        try {
            if (a.hasValue(R.styleable.MedicineCardView_clickable)) {
                clickable = a.getBoolean(R.styleable.MedicineCardView_clickable, false);
            }
        } finally {
            a.recycle();
        }
        initLayout();
    }

    private void initLayout() {
        View view = inflate(getContext(), R.layout.view_medication_card, this);
        card = view.findViewById(R.id.card);
        card.setClickable(clickable);
        card.setMaxCardElevation(1f);
    }

    private void updateInfo() {
        TextView name = card.findViewById(R.id.name);
        TextView tier = card.findViewById(R.id.tier);
        TextView description = card.findViewById(R.id.description);
        LinearLayout traits = card.findViewById(R.id.traits);
        TextView traitsTitle = card.findViewById(R.id.traitsTitle);

        name.setText(medication.getName());
        name.setTextColor(medication.getTier().getColor());
        tier.setText(medication.getTier().getName());
        tier.setTextColor(medication.getTier().getColor());
        description.setText(medication.getDescription());
        if (medicationTraits.length < 1) {
            traitsTitle.setVisibility(GONE);
        } else {
            traitsTitle.setVisibility(VISIBLE);
        }

        for (MedicationTrait trait : medicationTraits) {
            MedicineTraitView traitView = new MedicineTraitView(getContext(), trait);
            traits.addView(traitView);
        }
    }

    public void setClickable(boolean clickable) {
        card.setClickable(clickable);
    }

    private Medication getMedication() {
        return medication;
    }

    private MedicationTrait[] getMedicationTier() {
        return medicationTraits;
    }
}
