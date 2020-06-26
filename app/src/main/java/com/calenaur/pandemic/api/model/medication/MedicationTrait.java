package com.calenaur.pandemic.api.model.medication;

import com.calenaur.pandemic.api.model.Tier;

public class MedicationTrait extends Tier.Tiered {

    public int id;
    public int tier;
    public String name;
    public String description;

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Tier getTier() {
        return getTier(tier);
    }
}
