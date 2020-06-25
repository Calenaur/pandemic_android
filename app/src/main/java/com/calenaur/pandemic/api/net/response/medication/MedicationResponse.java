package com.calenaur.pandemic.api.net.response.medication;

import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.net.response.DefaultResponse;

public class MedicationResponse extends DefaultResponse {
    public Medication[] medications;
}
