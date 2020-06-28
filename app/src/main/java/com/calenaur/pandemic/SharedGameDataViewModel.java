package com.calenaur.pandemic;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.calenaur.pandemic.api.API;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.model.user.UserMedication;
import com.calenaur.pandemic.api.register.Registrar;

import java.util.ArrayList;
import java.util.Arrays;

public class SharedGameDataViewModel extends ViewModel {

    public static final int BASE_RESEARCH_COST = 500;

    private Registrar registrar;
    private API api;

    private LocalUser localUser;

    private int currentMedicationIndex = 0;
    private ArrayList<UserMedication> medicationList = new ArrayList<>();

    private MutableLiveData<Long> balance = new MutableLiveData<>();

    //API Getters/Setters
    public void setApi(API api) {
        this.api = api;
    }
    public API getApi() {
        return api;
    }

    //LocalUser Getters/Setters
    public void setLocalUser(LocalUser localUser) {
        this.localUser = localUser;
    }
    public LocalUser getLocalUser() {
        return localUser;
    }

    //CurrentMedicationIndex getter ans setter.
    public void setCurrentMedicationIndex(int medicationIndex) { this.currentMedicationIndex = medicationIndex; }
    public int getCurrentMedicationIndex() { return currentMedicationIndex; }

    public ArrayList<UserMedication> getMedicationList() {
        return medicationList;
    }

    public UserMedication getCurrentMedication(){
        return medicationList.get(currentMedicationIndex);
    }

    //Medications getter ans setter.
    public UserMedication[] getMedications() { return medicationList.toArray(new UserMedication[]{}); }

    //Registrar Getters and Setters
    public Registrar getRegistrar() { return registrar; }
    public void setRegistrar(Registrar registrar) { this.registrar = registrar; }

    //Balance Getter and Setter
    public MutableLiveData<Long> getBalance() { return balance; }

    public void incrementBalance(){
        if(balance.getValue() == null && localUser != null){
            balance.setValue(localUser.getBalance());
        }
        UserMedication currentMedication = medicationList.get(currentMedicationIndex);
        if(localUser != null &&  currentMedication != null){
            int incrementValue = currentMedication.medication.base_value;
            localUser.incrementBalance(incrementValue);
            balance.setValue(balance.getValue() + incrementValue);
        }
    }

    public String getBalanceAppendix() {
        return getAppendix(localUser.getBalance());
    }

    /*
    * Generate the appendixes for balance depending on its cardinal number.
    * */
    @SuppressLint("DefaultLocale")
    public String getAppendix(long value){
        String[] appendixes = {"k","m","b","t","q","Q","v"};

        if ( value < Math.pow(10,3)){
            return "" + value;
        }else{
            for(int i = 1; i < appendixes.length; i++) {
                if (value >= Math.pow(10, i*3) && value < Math.pow(10, (i+1)*3)) {
                    return String.format("%.1f %s", value / Math.pow(10, i*3), appendixes[i]);
                }
            }
            return "-1";
        }
    }

    public void setMedications(UserMedication[] medications) {
        if (medications == null)
            return;

        medicationList.addAll(Arrays.asList(medications));
    }
}
