package com.calenaur.pandemic.api.model.user;

import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.medication.MedicationTrait;

public class UserMedication {
    public int id;
    public Medication medication;
    public MedicationTrait[] medicationTraits;
}
