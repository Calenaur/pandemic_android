package com.calenaur.pandemic.api;

import android.content.Context;

import com.calenaur.pandemic.api.net.HTTPClient;
import com.calenaur.pandemic.api.store.DiseaseStore;
import com.calenaur.pandemic.api.store.EventStore;
import com.calenaur.pandemic.api.store.MedicationStore;
import com.calenaur.pandemic.api.store.UserStore;

public class API {

    private HTTPClient httpClient;
    private UserStore userStore;
    private MedicationStore medicineStore;
    private EventStore eventStore;
    private DiseaseStore diseaseStore;

    public API(Context context) {
        this.httpClient = new HTTPClient("https://api.calenaur.com", context);
        this.userStore = new UserStore(httpClient);
        this.medicineStore = new MedicationStore(httpClient);
        this.eventStore = new EventStore(httpClient);
        this.diseaseStore = new DiseaseStore(httpClient);
    }

    public UserStore getUserStore() {
        return userStore;
    }

    public MedicationStore getMedicineStore() {
        return medicineStore;
    }

    public EventStore getEventStore() {
        return eventStore;
    }

    public DiseaseStore getDiseaseStore() {
        return diseaseStore;
    }
}
