package com.calenaur.pandemic.api.model.user;

import com.calenaur.pandemic.api.model.Manufacture;
import java.io.Serializable;

public class User implements Serializable {

    private String uid;
    private String username;
    private int accessLevel;
    private int balance;
    private Manufacture manufacture;

    public User(String uid, String username, int accessLevel, int balance) {
        this.uid = uid;
        this.username = username;
        this.accessLevel = accessLevel;
        this.balance = balance;
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
}
