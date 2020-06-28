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

    public double getMultiplier() {
        switch (id) {
            case 1:
                return 1.05;
            case 2:
                return 1.10;
            case 3:
                return 1.50;
            case 4:
                return 0.95;
            case 5:
                return 0.9;
            case 6:
                return 0.85;
            case 7:
                return 0.6;
            case 8:
                return 1;
        }
        return 1;
    }
}
