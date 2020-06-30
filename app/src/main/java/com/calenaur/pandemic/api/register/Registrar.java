package com.calenaur.pandemic.api.register;

import android.util.Log;

import com.calenaur.pandemic.api.API;
import com.calenaur.pandemic.api.model.event.Event;
import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.medication.MedicationTrait;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.model.user.UserEvent;
import com.calenaur.pandemic.api.model.user.UserMedication;
import com.calenaur.pandemic.api.net.response.ErrorCode;
import com.calenaur.pandemic.api.store.PromiseHandler;

import java.util.LinkedList;

public class Registrar {

    private int storeUpdatedCount;
    private boolean updated;
    private LinkedList<Registry<?>> registries;
    private Registry<Medication> medicationRegistry;
    private Registry<MedicationTrait> medicationTraitRegistry;
    private Registry<UserMedication> userMedicationRegistry;
    private Registry<Event> eventRegistry;
    private Registry<UserEvent> userEventRegistry;

    public Registrar() {
        registries = new LinkedList<>();

        medicationRegistry = new Registry<>();
        registries.add(medicationRegistry);

        medicationTraitRegistry = new Registry<>();
        registries.add(medicationTraitRegistry);

        userMedicationRegistry = new Registry<>();
        registries.add(userMedicationRegistry);

        eventRegistry = new Registry<>();
        registries.add(eventRegistry);

        userEventRegistry = new Registry<>();
        registries.add(userEventRegistry);
    }

    public void updateAll(API api, LocalUser localUser) {
        storeUpdatedCount = 0;
        updated = false;
        updateMedicationRegistry(api, localUser);
        updateMedicationTierRegistry(api, localUser);
        updateUserMedicationRegistry(api, localUser);
        updateEventRegistry(api, localUser);
        updateUserEventRegistry(api, localUser);
    }

    public void updateMedicationRegistry(API api, LocalUser localUser) {
        api.getMedicineStore().medications(localUser, new PromiseHandler<Medication[]>() {
            @Override
            public void onDone(Medication[] object) {
                if (object == null)
                    return;

                medicationRegistry.clear();
                for (Medication medication : object)
                    medicationRegistry.register(medication.getID(), medication);

                updateDone();
            }

            @Override
            public void onError(ErrorCode errorCode) {

            }
        });
    }

    public void updateMedicationTierRegistry(API api, LocalUser localUser) {
        api.getMedicineStore().medicationTraits(localUser, new PromiseHandler<MedicationTrait[]>() {
            @Override
            public void onDone(MedicationTrait[] object) {
                if (object == null)
                    return;

                medicationTraitRegistry.clear();
                for (MedicationTrait medicationTrait : object)
                    medicationTraitRegistry.register(medicationTrait.getID(), medicationTrait);

                updateDone();
            }

            @Override
            public void onError(ErrorCode errorCode) {

            }
        });
    }

    public void updateUserMedicationRegistry(API api, LocalUser localUser) {
        api.getUserStore().getUserMedications(localUser, new PromiseHandler<UserMedication[]>() {
            @Override
            public void onDone(UserMedication[] object) {
                userMedicationRegistry.clear();
                if (object == null)
                    return;

                for (UserMedication userMedication : object)
                    if (userMedication != null)
                        userMedicationRegistry.register(userMedication.id, userMedication);

                updateDone();
            }

            @Override
            public void onError(ErrorCode errorCode) {
                Log.e("TAG", "onError: "+errorCode);
            }
        });
    }

    private void updateEventRegistry(API api, LocalUser localUser) {
        api.getEventStore().events(localUser, new PromiseHandler<Event[]>() {
            @Override
            public void onDone(Event[] object) {
                eventRegistry.clear();
                if (object == null)
                    return;

                for (Event event : object)
                    if (event != null)
                        eventRegistry.register(event.id, event);

                updateDone();
            }

            @Override
            public void onError(ErrorCode errorCode) {
                Log.e("TAG", "onError: "+errorCode);
            }
        });
    }

    private void updateUserEventRegistry(API api, LocalUser localUser) {
        api.getUserStore().getUserEvents(localUser, new PromiseHandler<UserEvent[]>() {
            @Override
            public void onDone(UserEvent[] object) {
                userEventRegistry.clear();
                if (object == null)
                    return;

                for (UserEvent event : object)
                    if (event != null)
                        userEventRegistry.register(event.id, event);

                updateDone();
            }

            @Override
            public void onError(ErrorCode errorCode) {
                Log.e("TAG", "onError: "+errorCode);
            }
        });
    }

    public Registry<Medication> getMedicationRegistry() {
        return medicationRegistry;
    }

    public Registry<MedicationTrait> getMedicationTraitRegistry() {
        return medicationTraitRegistry;
    }

    public Registry<UserMedication> getUserMedicationRegistry() {
        return userMedicationRegistry;
    }

    public Registry<Event> getEventRegistry() {
        return eventRegistry;
    }

    public Registry<UserEvent> getUserEventRegistry() {
        return userEventRegistry;
    }

    private void updateDone() {
        storeUpdatedCount++;
        updated = storeUpdatedCount >= registries.size();
    }

    public boolean isUpdated() {
        return updated;
    }
}
