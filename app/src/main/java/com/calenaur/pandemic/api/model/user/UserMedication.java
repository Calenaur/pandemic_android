package com.calenaur.pandemic.api.model.user;

import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.medication.MedicationTrait;
import com.calenaur.pandemic.api.register.Registry;

import java.io.Serializable;
import java.util.ArrayList;

public class UserMedication implements Serializable {
    public int id;
    public int medication;
    public Integer[] traits;

    public UserMedication() {

    }

    public UserMedication(int id, int medication, Integer[] traits) {
        this.id = id;
        this.medication = medication;
        this.traits = traits;
    }

    public Medication getMedication(Registry<Medication> medicationRegistry) {
        return medicationRegistry.get(medication);
    }

    public MedicationTrait[] getMedicationTraits(Registry<MedicationTrait> medicationTraitRegistry) {
        ArrayList<MedicationTrait> traitList = new ArrayList<>();
        if (traits != null)
            if (traits.length > 0)
                for (int trait : traits)
                    traitList.add(medicationTraitRegistry.get(trait));

        return traitList.toArray(new MedicationTrait[]{});
    }

}