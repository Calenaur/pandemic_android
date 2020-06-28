package com.calenaur.pandemic.api.model;

import android.graphics.Color;

import java.io.Serializable;

public enum Tier implements Serializable {

    COMMON(1, "Common", Color.parseColor("#616161")),
    Uncommon(2, "Uncommon", Color.parseColor("#81d4fa")),
    Rare(3, "Rare", Color.parseColor("#2196f3")),
    Epic(4, "Epic", Color.parseColor("#673ab7")),
    Legendary(5, "Legendary", Color.parseColor("#ff5722"));

    private int id;
    private String name;
    private int color;

    Tier(int id, String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public static Tier fromID(int id) {
        for (Tier tier : values())
            if (tier.getID() == id)
                return tier;

        return null;
    }

    public static abstract class Tiered {

        private Tier innerTier;

        protected Tier getTier(int tier) {
            if (innerTier != null)
                return innerTier;

            innerTier = Tier.fromID(tier);
            return innerTier;
        }

        public abstract Tier getTier();
    }
}
