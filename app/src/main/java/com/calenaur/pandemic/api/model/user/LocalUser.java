package com.calenaur.pandemic.api.model.user;

import com.calenaur.pandemic.api.model.Tier;
import com.calenaur.pandemic.api.model.user.JWT.JSONWebToken;
import com.calenaur.pandemic.api.model.user.JWT.Payload;

import java.io.Serializable;

public class LocalUser extends User implements Serializable {

    private JSONWebToken jwt;

    public LocalUser(String uid, String username, int accessLevel, int balance, Tier tier, JSONWebToken jwt) {
        super(uid, username, accessLevel, balance, tier);
        this.jwt = jwt;
    }

    public JSONWebToken getJWT() {
        return jwt;
    }

    public static LocalUser fromToken(JSONWebToken jwt) {
        Payload p = jwt.getPayload();
        return new LocalUser(p.sub, p.name, p.access, 0, Tier.COMMON, jwt);
    }
}
