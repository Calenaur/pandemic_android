package com.calenaur.pandemic;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.calenaur.pandemic.api.API;
import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.register.Registrar;

public class SharedGameDataViewModel extends ViewModel {

    public static final int BASE_RESEARCH_COST = 500;

    private MutableLiveData<Registrar> registrar = new MutableLiveData<>();
    private MutableLiveData<API> api = new MutableLiveData<>();
    private MutableLiveData<LocalUser> localUser = new MutableLiveData<>();
    private MutableLiveData<Long> balance = new MutableLiveData<>();
    private MutableLiveData<Medication> medication = new MutableLiveData<>();

    /*
    * Instantiates the value of balance if null.
    * Increment the balance variable using the value of the medication.
    * */
    public void incrementBalance(){
        if(balance.getValue() == null){
            balance.setValue(0L);
        }if(medication.getValue() == null){
            medication.setValue(new Medication(1, "Pain Killer", 10));
        }else{
            balance.setValue(balance.getValue() +  medication.getValue().getWorth());
        }
    }

    public void setApi(API api) {
        this.api.setValue(api);
    }

    public API getApi() {
        return api.getValue();
    }

    public void setLocalUser(LocalUser localUser) {
        this.localUser.setValue(localUser);
    }

    public LocalUser getLocalUser() {
        return localUser.getValue();
    }

    /*
    * Balance getter and setter.
    * */
    public void setBalance(Long input){
        balance.setValue(input);
    }
    public LiveData<Long> getBalance(){
        return balance;
    }

    /*
    * Medication getter ans setter.
    * */
    public void setMedication(MutableLiveData<Medication> medications) { this.medication = medications; }
    public MutableLiveData<Medication> getMedication() {
        return medication;
    }
    public int getWorth(){
        Medication med = medication.getValue();
        if (med == null){
            return 0;
        }
        else{
            return med.getWorth();
        }
    }

    public Registrar getRegistrar() {
        return registrar.getValue();
    }

    public void setRegistrar(Registrar registrar) {
        this.registrar.setValue(registrar);
    }

    /*
    * Generate the appendixes for balance depending on its cardinal number.
    * */
    @SuppressLint("DefaultLocale")
    public String getAppendix(){
        if (balance.getValue() == null){
            return "0";
        }
        String[] appendixes = {"k","m","b","t","q","Q","v"};
        Long value = balance.getValue();

        if ( value < Math.pow(10,3)){
            return value.toString();
        }else{
            for(int i = 1; i < appendixes.length; i++) {
                if (value >= Math.pow(10, i*3) && value < Math.pow(10, (i+1)*3)) {
                    return String.format("%.1f %s", value / Math.pow(10, i*3), appendixes[i]);
                }
            }
            return "-1";
        }
    }
}
