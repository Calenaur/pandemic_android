package com.calenaur.pandemic.api.model.user;

public class Friend {
    private String name;
    private int balance;
    private int tier;

    public Friend(){}

    public Friend(String name, int balance, int tier) {
        this.name = name;
        this.balance = balance;
        this.tier = tier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                ", tier=" + tier +
                '}';
    }
}
