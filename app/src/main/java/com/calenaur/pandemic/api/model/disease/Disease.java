package com.calenaur.pandemic.api.model.disease;

import com.calenaur.pandemic.api.model.Tier;

public class Disease extends Tier.Tiered {

    public int id;
    public int tier;
    public String name;
    public String description;
    public int rarity;

    @Override
    public Tier getTier() {
        return getTier(tier);
    }
}
