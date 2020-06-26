package com.calenaur.pandemic.api.model.medication;

import com.calenaur.pandemic.api.model.Tier;

public class Medication {

    public int id;
    public String name;
    public String description;
    public int worth;
    public int research_cost;
    public int maximum_traits;
    public int tier;

    public Medication(int id, String name, int worth) {
        this.id = id;
        this.name = name;
        this.worth = worth;
    }

    public Medication() {

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

    public int getWorth() {
        return worth;
    }
}
