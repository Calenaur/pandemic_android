package com.calenaur.pandemic.api.model.user;

import com.calenaur.pandemic.api.model.Manufacture;
import com.calenaur.pandemic.api.model.Tier;

import java.io.Serializable;

public class User implements Serializable {

    private String uid;
    private String username;
    private int accessLevel;
    private int balance;
    private Manufacture manufacture;
    private Tier tier;

    public User(String uid, String username, int accessLevel, int balance, Tier tier) {
        this.uid = uid;
        this.username = username;
        this.accessLevel = accessLevel;
        this.balance = balance;
        this.tier = tier;
        this.balance = 500;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public int getBalance() {
        return balance;
    }

    public boolean pay(int amount) {
        if (getBalance() >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}
