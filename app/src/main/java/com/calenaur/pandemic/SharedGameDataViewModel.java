package com.calenaur.pandemic;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.calenaur.pandemic.api.API;
import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.model.user.UserMedication;
import com.calenaur.pandemic.api.register.Registrar;

import java.util.Objects;

public class SharedGameDataViewModel extends ViewModel {

    public static final int BASE_RESEARCH_COST = 500;

    private MutableLiveData<Registrar> registrar = new MutableLiveData<>();
    private MutableLiveData<API> api = new MutableLiveData<>();

    private MutableLiveData<LocalUser> localUser = new MutableLiveData<>();

    private MutableLiveData<UserMedication> medication = new MutableLiveData<>();
    private MutableLiveData<Long> balance = new MutableLiveData<>();

    //API Getters/Setters
    public void setApi(API api) {
        this.api.setValue(api);
    }

    public API getRawApi() {
        return api.getValue();
    }

    public MutableLiveData<API> getApi() {
        return api;
    }

    //LocalUser Getters/Setters
    public void setLocalUser(LocalUser localUser) {
        this.localUser.setValue(localUser);
    }

    public LocalUser getRawLocalUser() {
        return localUser.getValue();
    }

    public MutableLiveData<LocalUser> getLocalUser() {
        return localUser;
    }

    /*
    * Medication getter ans setter.
    * */
    public void setMedication(UserMedication medication) { this.medication.setValue(medication); }

    public UserMedication getRawMedication() {
        return medication.getValue();
    }

    public MutableLiveData<UserMedication> getMedication() {
        return medication;
    }

    public Registrar getRawRegistrar() {
        return registrar.getValue();
    }
    public void setRegistrar(Registrar registrar) {
        this.registrar.setValue(registrar);
    }

    public MutableLiveData<Registrar> getRegistrar() {
        return registrar;
    }

    public MutableLiveData<Long> getBalance() {
        return balance;
    }

    public void incrementBalance(){
        LocalUser lou = localUser.getValue();
        if(balance.getValue() == null && lou != null){
            balance.setValue(lou.getBalance());
        }
        if(lou != null && medication.getValue() != null){
            int incrementValue = medication.getValue().medication.base_value;
            lou.incrementBalance(incrementValue);
            balance.setValue(balance.getValue() + incrementValue);
        }
    }

    public String getBalanceAppendix(){
        System.out.println("increment");
        return getAppendix(Objects.requireNonNull(localUser.getValue()).getBalance());
    }
    /*
    * Generate the appendixes for balance depending on its cardinal number.
    * */
    @SuppressLint("DefaultLocale")
    public String getAppendix(long value){
        String[] appendixes = {"k","m","b","t","q","Q","v"};

        if ( value < Math.pow(10,3)){
            return ""+value;
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
