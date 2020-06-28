package com.calenaur.pandemic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.calenaur.pandemic.R;
import com.calenaur.pandemic.api.model.medication.MedicationTrait;

public class MedicineTraitView extends LinearLayout {

    private View layout;
    private MedicationTrait medicationTrait;

    public MedicineTraitView(Context context, MedicationTrait medicationTrait) {
        this(context, (AttributeSet) null);
        this.medicationTrait = medicationTrait;
        updateInfo();
    }

    public MedicineTraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        initLayout();
    }

    private void initLayout() {
        layout = inflate(getContext(), R.layout.view_medication_trait, this);
    }

    private void updateInfo() {
        TextView name = layout.findViewById(R.id.name);
        TextView tier = layout.findViewById(R.id.tier);
        TextView description = layout.findViewById(R.id.description);

        name.setText(medicationTrait.getName());
        //name.setTextColor(medicationTrait.getTier().getColor());
        tier.setText(medicationTrait.getTier().getName());
        tier.setTextColor(medicationTrait.getTier().getColor());
        description.setText(medicationTrait.getDescription());
    }

}
