package com.calenaur.pandemic.api.model.user;

import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.medication.MedicationTrait;

public class UserMedication {
    public int id;
    public Medication medication;
    public MedicationTrait[] medicationTraits;

    public UserMedication(int id, Medication medication, MedicationTrait[] medicationTraits){
        this.id = id;
        this.medication = medication;
        this.medicationTraits = medicationTraits;
    }
}