package com.calenaur.pandemic.api.register;

import com.calenaur.pandemic.api.API;
import com.calenaur.pandemic.api.model.Tier;
import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.medication.MedicationTrait;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.net.response.ErrorCode;
import com.calenaur.pandemic.api.store.PromiseHandler;

public class Registrar {

    private Registry<Medication> medicationRegistry;
    private Registry<MedicationTrait> medicationTraitRegistry;
    private Registry<Tier> tierRegistry;

    public Registrar() {
        medicationRegistry = new Registry<>();
        medicationTraitRegistry = new Registry<>();
        tierRegistry = new Registry<>();
    }

    public void updateAll(API api, LocalUser localUser) {
        updateMedicationRegistry(api, localUser);
        updateMedicationTierRegistry(api, localUser);
        updateTierRegistry(api, localUser);
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

    public Registry<MedicationTrait> getMedicationTraitRegistry() {
        return medicationTraitRegistry;
    }

    public void updateTierRegistry(API api, LocalUser localUser) {
        //TODO replace with API call
        /*
        tierRegistry.clear();
        tierRegistry.register(1, new Tier(1, "Common", ""));
        tierRegistry.register(2, new Tier(2, "Uncommon", ""));
        tierRegistry.register(3, new Tier(3, "Rare", ""));
        tierRegistry.register(4, new Tier(4, "Epic", ""));
        tierRegistry.register(5, new Tier(5, "Legendary", ""));
         */
    }

    public Registry<Tier> getTierRegistry() {
        return tierRegistry;
    }
}
