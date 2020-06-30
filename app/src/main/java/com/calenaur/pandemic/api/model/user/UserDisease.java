package com.calenaur.pandemic.api.model.user;

import com.calenaur.pandemic.api.model.disease.Disease;
import com.calenaur.pandemic.api.register.Registry;

public class UserDisease {
    public String user;
    public int id;

    public UserDisease() {

    }

    public UserDisease(String user, int disease) {
        this.user = user;
        this.id = disease;
    }

    public Disease getDisease(Registry<Disease> medicationRegistry) {
        return medicationRegistry.get(id);
    }
}
