package com.calenaur.pandemic.api.register;

import android.util.Log;

import com.calenaur.pandemic.api.API;
import com.calenaur.pandemic.api.model.disease.Disease;
import com.calenaur.pandemic.api.model.event.Event;
import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.medication.MedicationDisease;
import com.calenaur.pandemic.api.model.medication.MedicationTrait;
import com.calenaur.pandemic.api.model.user.Friend;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.model.user.UserDisease;
import com.calenaur.pandemic.api.model.user.UserEvent;
import com.calenaur.pandemic.api.model.user.UserMedication;
import com.calenaur.pandemic.api.net.response.ErrorCode;

import com.calenaur.pandemic.api.store.PromiseHandler;

import java.util.LinkedList;

public class Registrar {

    private int storeUpdatedCount;
    private boolean updated;
    private LinkedList<Object> registries;
    private Registry<Medication> medicationRegistry;
    private Registry<MedicationTrait> medicationTraitRegistry;
    private PairRegistry<MedicationDisease> medicationDiseaseRegistry;
    private Registry<UserMedication> userMedicationRegistry;

    private Registry<Event> eventRegistry;
    private Registry<UserEvent> userEventRegistry;
    private Registry<Disease> diseaseRegistry;
    private Registry<UserDisease> userDiseaseRegistry;
    private Registry<Friend> friendRegistry;
    private Registry<Friend> requestRegistry;


    public Registrar() {
        registries = new LinkedList<>();

        medicationRegistry = new Registry<>();
        registries.add(medicationRegistry);

        medicationTraitRegistry = new Registry<>();
        registries.add(medicationTraitRegistry);

        medicationDiseaseRegistry = new PairRegistry<>();
        registries.add(medicationDiseaseRegistry);

        userMedicationRegistry = new Registry<>();
        registries.add(userMedicationRegistry);

        eventRegistry = new Registry<>();
        registries.add(eventRegistry);

        userEventRegistry = new Registry<>();
        registries.add(userEventRegistry);

        diseaseRegistry = new Registry<>();
        registries.add(diseaseRegistry);

        userDiseaseRegistry = new Registry<>();
        registries.add(userDiseaseRegistry);

        friendRegistry = new Registry<>();
        registries.add(friendRegistry);

        requestRegistry = new Registry<>();
        registries.add(requestRegistry);

    }

    public void updateAll(API api, LocalUser localUser) {
        storeUpdatedCount = 0;
        updated = false;
        updateMedicationRegistry(api, localUser);
        updateMedicationTraitRegistry(api, localUser);
        updateMedicationDiseaseRegistry(api, localUser);
        updateUserMedicationRegistry(api, localUser);
        updateEventRegistry(api, localUser);
        updateUserEventRegistry(api, localUser);
        updateDiseaseRegistry(api, localUser);
        updateUserDiseaseRegistry(api, localUser);
        updateFriendRegistry(api, localUser);
        updateRequestRegistry(api, localUser);
    }

    public void updateMedicationRegistry(API api, LocalUser localUser) {
        api.getMedicineStore().getMedications(localUser, new PromiseHandler<Medication[]>() {
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
                Log.e("ERROR", "updateMedicationRegistry: "+errorCode);
            }
        });
    }

    public void updateMedicationTraitRegistry(API api, LocalUser localUser) {
        api.getMedicineStore().getMedicationTraits(localUser, new PromiseHandler<MedicationTrait[]>() {
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
                Log.e("ERROR", "updateMedicationTraitRegistry: "+errorCode);
            }
        });
    }

    public void updateMedicationDiseaseRegistry(API api, LocalUser localUser) {
        api.getMedicineStore().getMedicationDiseases(localUser, new PromiseHandler<MedicationDisease[]>() {
            @Override
            public void onDone(MedicationDisease[] object) {
                if (object == null)
                    return;

                medicationDiseaseRegistry.clear();
                for (MedicationDisease medicationDisease : object)
                    medicationDiseaseRegistry.register(new KeyPair(medicationDisease.medication, medicationDisease.disease), medicationDisease);

                updateDone();
            }

            @Override
            public void onError(ErrorCode errorCode) {
                Log.e("ERROR", "updateMedicationDiseaseRegistry: "+errorCode);
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
                Log.e("ERROR", "updateUserMedicationRegistry: "+errorCode);
            }
        });
    }

    private void updateEventRegistry(API api, LocalUser localUser) {
        api.getEventStore().getEvents(localUser, new PromiseHandler<Event[]>() {
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
                Log.e("ERROR", "updateEventRegistry: "+errorCode);
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
                Log.e("ERROR", "updateUserEventRegistry: "+errorCode);
            }
        });
    }

    private void updateDiseaseRegistry(API api, LocalUser localUser) {
        api.getDiseaseStore().getDiseases(localUser, new PromiseHandler<Disease[]>() {
            @Override
            public void onDone(Disease[] object) {
                diseaseRegistry.clear();
                if (object == null)
                    return;

                for (Disease disease : object)
                    if (disease != null)
                        diseaseRegistry.register(disease.id, disease);

                updateDone();
            }

            @Override
            public void onError(ErrorCode errorCode) {
                Log.e("ERROR", "updateDiseaseRegistry: "+errorCode);
            }
        });
    }

    private void updateUserDiseaseRegistry(API api, LocalUser localUser) {
        api.getUserStore().getUserDiseases(localUser, new PromiseHandler<UserDisease[]>() {
            @Override
            public void onDone(UserDisease[] object) {
                userDiseaseRegistry.clear();
                if (object == null)
                    return;

                for (UserDisease userDisease : object)
                    if (userDisease != null) {
                        userDisease.user = localUser.getUid();
                        userDiseaseRegistry.register(userDisease.id, userDisease);
                    }

                updateDone();
            }

            @Override
            public void onError(ErrorCode errorCode) {
                Log.e("ERROR", "updateUserDiseaseRegistry: "+errorCode);

            }
        });
    }

    public void updateFriendRegistry(API api, LocalUser localUser) {
        api.getUserStore().friends(localUser, new PromiseHandler<Friend[]>() {
            @Override
            public void onDone(Friend[] object) {
                friendRegistry.clear();
                if (object == null)
                    return;

                for (int i = 0; i < object.length; i++) {
                    if (object[i] == null)
                        continue;
                    friendRegistry.register(
                            i, object[i]
                    );
                }
            }

            @Override
            public void onError(ErrorCode errorCode) {
                Log.e("TAG", "onError: " + errorCode);
            }
        });
    }

    public void updateRequestRegistry(API api, LocalUser localUser) {
        api.getUserStore().friendRequests(localUser, new PromiseHandler<Friend[]>() {
            @Override
            public void onDone(Friend[] object) {
                requestRegistry.clear();
                if (object == null)
                    return;

                for (int i = 0; i < object.length; i++) {
                    if (object[i] == null)
                        continue;
                    requestRegistry.register(
                            i, object[i]
                    );
                }
            }

            @Override
            public void onError(ErrorCode errorCode) {
                Log.e("TAG", "onError: " + errorCode);
            }
        });
    }


    public Registry<Medication> getMedicationRegistry() {
        return medicationRegistry;
    }

    public Registry<MedicationTrait> getMedicationTraitRegistry() {
        return medicationTraitRegistry;
    }

    public PairRegistry<MedicationDisease> getMedicationDiseaseRegistry() {
        return medicationDiseaseRegistry;
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

    public Registry<Disease> getDiseaseRegistry() {
        return diseaseRegistry;
    }

    public Registry<UserDisease> getUserDiseaseRegistry() {
        return userDiseaseRegistry;
    }

    public Registry<Friend> getFriendRegistry() {
        return friendRegistry;
    }
    public Registry<Friend> getRequestRegistry() {
        return requestRegistry;
    }

    private void updateDone() {
        storeUpdatedCount++;
        updated = storeUpdatedCount >= registries.size();
    }

    public boolean isUpdated() {
        return updated;
    }
}
