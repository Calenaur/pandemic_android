package com.calenaur.pandemic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.calenaur.pandemic.api.API;
import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.medication.MedicationDisease;
import com.calenaur.pandemic.api.model.medication.MedicationTrait;
import com.calenaur.pandemic.api.model.user.Friend;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.model.user.UserDisease;
import com.calenaur.pandemic.api.model.user.UserMedication;
import com.calenaur.pandemic.api.register.KeyPair;
import com.calenaur.pandemic.api.register.PairRegistry;
import com.calenaur.pandemic.api.net.response.ErrorCode;
import com.calenaur.pandemic.api.register.Registrar;
import com.calenaur.pandemic.api.store.PromiseHandler;


public class SharedGameDataViewModel extends ViewModel {

    public static final int BASE_CLICK_VALUE = 1;
    public static final int BASE_RESEARCH_COST = 500;

    private Registrar registrar;
    private API api;
    private LocalUser localUser;
    private int currentUserMedicationID = -1;
    private MutableLiveData<Long> balance = new MutableLiveData<>();
    private int clickValue = BASE_CLICK_VALUE;

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
        balance.setValue(localUser.getBalance());
    }
    public LocalUser getLocalUser() {
        return localUser;
    }

    //CurrentMedicationIndex getter ans setter.
    public UserMedication getCurrentMedication(){
        return registrar.getUserMedicationRegistry().get(currentUserMedicationID);
    }

    //Medications getter ans setter.
    public UserMedication[] getMedications() { return registrar.getUserMedicationRegistry().toArray(new UserMedication[]{}); }

    //Registrar Getters and Setters
    public Registrar getRegistrar() { return registrar; }
    public void setRegistrar(Registrar registrar) { this.registrar = registrar; }

    //Balance Getter and Setter
    public MutableLiveData<Long> getBalanceMutable() { return balance; }

    public long getBalance() {
        if (balance.getValue() == null)
            return 0;

        return balance.getValue();
    }

    //Friend getter
    public Friend[] getFriends() { return registrar.getFriendRegistry().toArray(new Friend[]{}); }

    public Friend[] getRequests() { return registrar.getRequestRegistry().toArray(new Friend[]{}); }

    public void sendFriendResponse(String friend, int response){
        api.getUserStore().sendFriendResponse(localUser, friend, response, new PromiseHandler<Object>() {
            @Override
            public void onDone(Object object) {}
            @Override
            public void onError(ErrorCode errorCode) {}
        });
        registrar.updateRequestRegistry(api, localUser);
        registrar.updateFriendRegistry(api, localUser);
    }

    public void addFriend(String friend){
        api.getUserStore().sendFriendRequest(localUser, friend, new PromiseHandler<Object>() {
            @Override
            public void onDone(Object object) {}
            @Override
            public void onError(ErrorCode errorCode) {}
        });
    }

    public void removeFriend(String friend){
        api.getUserStore().removeFriend(localUser, friend, new PromiseHandler<Object>() {
            @Override
            public void onDone(Object object) {}
            @Override
            public void onError(ErrorCode errorCode) {}
        });
        registrar.updateRequestRegistry(api, localUser);
        registrar.updateFriendRegistry(api, localUser);
    }

    public void incrementBalance(){
        if (localUser == null)
            return;

        if (balance.getValue() == null && localUser != null)
            balance.setValue(localUser.getBalance());

        assert localUser != null;
        localUser.incrementBalance(clickValue);
        balance.setValue(balance.getValue() + clickValue);
    }

    public void pay(int amount){
        if (localUser == null)
            return;

        if(balance.getValue() == null && localUser != null)
            balance.setValue(localUser.getBalance());

        localUser.incrementBalance(-amount);
        balance.setValue(balance.getValue() + amount);
    }

    public String getClickValue() {
        return getAppendix(clickValue);
    }

    public String getBalanceAppendix() {
        return getAppendix(localUser.getBalance());
    }

    /*
    * Generate the appendixes for balance depending on its cardinal number.
    * */
    @SuppressLint("DefaultLocale")
    public String getAppendix(long value){
        String[] appendixes = {"K","M","B","T","q","Q","V"};

        if ( value < Math.pow(10,3)){
            return "" + value;
        }else{
            for(int i = 1; i < appendixes.length; i++) {
                if (value >= Math.pow(10, i*3) && value < Math.pow(10, (i+1)*3)) {
                    return String.format("%.1f %s", value / Math.pow(10, i*3), appendixes[i-1]);
                }
            }
            return "-1";
        }
    }

    public void calcClickValue() {
        UserMedication userMedication = getCurrentMedication();
        if (userMedication != null) {
            Medication medication = userMedication.getMedication(registrar.getMedicationRegistry());
            MedicationTrait[] medicationTraits = userMedication.getMedicationTraits(registrar.getMedicationTraitRegistry());
            //Medication traits effectiveness
            if (medication != null) {
                double value = medication.base_value;
                if (medicationTraits != null)
                    for (MedicationTrait trait : medicationTraits)
                        value *= trait.getMultiplier();

                clickValue = (int) Math.floor(value);
            }

            UserDisease[] userDiseases = getRegistrar().getUserDiseaseRegistry().toArray(new UserDisease[]{});
            PairRegistry<MedicationDisease> mdRegistry = getRegistrar().getMedicationDiseaseRegistry();
            //Active diseases effectiveness
            for (UserDisease userDisease : userDiseases) {
                KeyPair key = new KeyPair(userMedication.medication, userDisease.id);
                if (mdRegistry.containsKey(key)) {
                    MedicationDisease medicationDisease = mdRegistry.get(key);
                    clickValue = (int) Math.floor(clickValue * (medicationDisease.effectiveness / 100d));
                }
            }
            return;
        }

        clickValue =  BASE_CLICK_VALUE;
    }

    public void setCurrentUserMedicationID(int currentUserMedicationID) {
        this.currentUserMedicationID = currentUserMedicationID;
        calcClickValue();
    }
}
