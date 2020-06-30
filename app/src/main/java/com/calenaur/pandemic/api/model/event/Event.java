package com.calenaur.pandemic.api.model.event;

import com.calenaur.pandemic.api.model.Tier;

public class Event extends Tier.Tiered {

    public int id;
    public String name;
    public String description;
    public int rarity;
    public int tier;

    @Override
    public Tier getTier() {
        return getTier(tier);
    }
}
