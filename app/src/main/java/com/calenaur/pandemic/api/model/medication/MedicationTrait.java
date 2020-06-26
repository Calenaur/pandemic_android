package com.calenaur.pandemic.api.model.medication;

public class MedicationTrait {

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
}
