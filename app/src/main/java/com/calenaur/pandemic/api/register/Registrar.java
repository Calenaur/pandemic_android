package com.calenaur.pandemic.api.register;

import android.util.Log;

import com.calenaur.pandemic.api.API;
import com.calenaur.pandemic.api.model.Tier;
import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.medication.MedicationTrait;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.model.user.UserMedication;
import com.calenaur.pandemic.api.net.response.ErrorCode;
import com.calenaur.pandemic.api.net.response.user.UserMedicationResponse;
import com.calenaur.pandemic.api.store.PromiseHandler;

import java.util.ArrayList;

public class Registrar {

    private Registry<Medication> medicationRegistry;
    private Registry<MedicationTrait> medicationTraitRegistry;
    private Registry<UserMedication> userMedicationRegistry;

    public Registrar() {
        medicationRegistry = new Registry<>();
        medicationTraitRegistry = new Registry<>();
        userMedicationRegistry = new Registry<>();
    }

    public void updateAll(API api, LocalUser localUser) {
        updateMedicationRegistry(api, localUser);
        updateMedicationTierRegistry(api, localUser);
        updateUserMedicationRegistry(api, localUser);
    }

    public void updateMedicationRegistry(API api, LocalUser localUser) {
        api.getMedicineStore().medications(localUser, new PromiseHandler<Medication[]>() {
            @Override
            public void onDone(Medication[] object) {
                if (object == null)
                    return;

                medicationRegistry.clear();
                for (Medication medication : object) {
                    medicationRegistry.register(medication.getID(), medication);
                }
            }

            @Override
            public void onError(ErrorCode errorCode) {

            }
        });
    }

    public Registry<Medication> getMedicationRegistry() {
        return medicationRegistry;
    }

    public void updateMedicationTierRegistry(API api, LocalUser localUser) {
        api.getMedicineStore().medicationTraits(localUser, new PromiseHandler<MedicationTrait[]>() {
            @Override
            public void onDone(MedicationTrait[] object) {
                if (object == null)
                    return;

                medicationTraitRegistry.clear();
                for (MedicationTrait medicationTrait : object) {
                    medicationTraitRegistry.register(medicationTrait.getID(), medicationTrait);
                }
            }

            @Override
            public void onError(ErrorCode errorCode) {

            }
        });
    }

    public void updateUserMedicationRegistry(API api, LocalUser localUser) {
        api.getUserStore().userMedications(localUser, new PromiseHandler<UserMedicationResponse[]>() {
            @Override
            public void onDone(UserMedicationResponse[] object) {
                userMedicationRegistry.clear();
                if (object == null)
                    return;

                for (UserMedicationResponse userMedication : object) {
                    if (userMedication == null)
                        continue;

                    ArrayList<MedicationTrait> traitList = new ArrayList<>();
                    if (userMedication.traits != null)
                        if (userMedication.traits.length > 0)
                            for (int trait : userMedication.traits)
                                traitList.add(medicationTraitRegistry.get(trait));

                    Medication medication = medicationRegistry.get(userMedication.medication);
                    if (medication == null)
                        continue;

                    userMedicationRegistry.register(
                            userMedication.id,
                            new UserMedication(
                                    userMedication.id,
                                    medication,
                                    traitList.toArray(new MedicationTrait[]{})
                            )
                    );
                }
            }

            @Override
            public void onError(ErrorCode errorCode) {
                Log.e("TAG", "onError: "+errorCode);
            }
        });
    }

    public Registry<MedicationTrait> getMedicationTraitRegistry() {
        return medicationTraitRegistry;
    }

    public Registry<UserMedication> getUserMedicationRegistry() {
        return userMedicationRegistry;
    }
}
