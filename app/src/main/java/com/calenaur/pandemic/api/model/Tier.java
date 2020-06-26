package com.calenaur.pandemic.api.model;

import java.io.Serializable;

public class Tier implements Serializable {

    public int id;
    public String name;
    public String color;

    public Tier(int id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

}
