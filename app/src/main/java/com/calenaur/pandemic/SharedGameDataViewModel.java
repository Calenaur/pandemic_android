package com.calenaur.pandemic;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.calenaur.pandemic.api.model.medication.Medication;

public class SharedGameDataViewModel extends ViewModel {
    private MutableLiveData<Long> balance = new MutableLiveData<>();
    private MutableLiveData<Medication> medication = new MutableLiveData<>();

    /*
    * Instantiates the value of balance if null.
    * Increment the balance variable using the value of the medication.
    * */
    public void incrementBalance(){
        if(balance.getValue() == null){
            balance.setValue(0L);
        }else{
            //TODO Placeholder noted below for getting medicine worth.
            //balance.setValue(balance.getValue() + medications.getValue().getWorth());
            balance.setValue((long) (balance.getValue() + Math.pow(10, 10)));
        }
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
            for(int i = 0; i < appendixes.length; i++) {
                if (value >= Math.pow(10, i*3) && value < Math.pow(10, (i+1)*3)) {
                    return String.format("%.1f %s", value / Math.pow(10, i*3), appendixes[i]);
                }
            }
            return "-1";
        }
    }
}
