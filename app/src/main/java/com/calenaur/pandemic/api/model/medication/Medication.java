package com.calenaur.pandemic.api.model.medication;

import com.calenaur.pandemic.api.model.Tier;

public class Medication {

    private int id;
    private String name;
    private String description;
    private int worth;
    private int researchCost;
    private int maximumTraits;
    private int rarity;
    private Tier tier;

    public Medication(int id, String name, int worth) {
        this.id = id;
        this.name = name;
        this.worth = worth;
    }

    public int getWorth() {
        return worth;
    }
}
