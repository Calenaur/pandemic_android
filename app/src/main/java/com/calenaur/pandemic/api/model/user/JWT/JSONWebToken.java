package com.calenaur.pandemic.api.model.user.JWT;

import com.fasterxml.jackson.jr.ob.JSON;

import java.io.IOException;
import java.util.Base64;

public class JSONWebToken {

    private String origin;
    private Header header;
    private Payload payload;
    private String signature;

    private JSONWebToken(String origin, Header header, Payload payload, String signature) {
        this.origin = origin;
        this.header = header;
        this.payload = payload;
        this.signature = signature;
    }

    public static JSONWebToken fromToken(String token) {
        String[] splitToken = token.split("[.]");
        if (splitToken.length < 3)
            return null;

        Base64.Decoder base64 = Base64.getDecoder();
        try {
            Header header = JSON.std.beanFrom(Header.class, new String(base64.decode(splitToken[0])));
            Payload payload = JSON.std.beanFrom(Payload.class, new String(base64.decode(splitToken[1])));
            return new JSONWebToken(token, header, payload, splitToken[2]);
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }

        return null;
    }

    public String getOrigin() {
        return origin;
    }

}
