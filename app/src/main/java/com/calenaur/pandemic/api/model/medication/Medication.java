package com.calenaur.pandemic.api.model.medication;

import com.calenaur.pandemic.api.model.Tier;


public class Medication extends Tier.Tiered {

    public int id;
    public String name;
    public String description;
    public int base_value;
    public int research_cost;
    public int maximum_traits;
    public int tier;
    private Tier innerTier;

    public Medication(){
    }

    public Medication(int id, String name, String description, int base_value, int research_cost, int maximum_traits, int tier, Tier innerTier) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.base_value = base_value;
        this.research_cost = research_cost;
        this.maximum_traits = maximum_traits;
        this.tier = tier;
        this.innerTier = innerTier;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getBaseValue() {
        return base_value;
    }

    @Override
    public Tier getTier() {
        return innerTier;
    }
}
