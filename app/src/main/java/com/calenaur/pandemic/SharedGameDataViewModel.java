package com.calenaur.pandemic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.calenaur.pandemic.api.model.medication.Medication;

public class SharedGameDataViewModel extends ViewModel {
    private MutableLiveData<Long> balance = new MutableLiveData<>();
    private MutableLiveData<Medication> medications = new MutableLiveData<>();

    public void incrementBalance(Integer incrementValue){
        Long value = balance.getValue();
        if(value == null){
            balance.setValue(0L);
        }else{
            balance.setValue(balance.getValue() + incrementValue);
        }
    }

    public void setBalance(Long input){
        balance.setValue(input);
    }
    public LiveData<Long> getBalance(){
        return balance;
    }

    public void setMedications(MutableLiveData<Medication> medications) {
        this.medications = medications;
    }
    public MutableLiveData<Medication> getMedications() {
        return medications;
    }
}
