package com.calenaur.pandemic.api.model.user;

import com.calenaur.pandemic.api.model.user.JWT.JSONWebToken;

public class LocalUser extends User {

    private JSONWebToken jwt;

    public LocalUser(JSONWebToken jwt) {
        this.jwt = jwt;
    }
}
