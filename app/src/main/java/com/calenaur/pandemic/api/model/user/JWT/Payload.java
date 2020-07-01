package com.calenaur.pandemic.api.model.user.JWT;

import java.io.Serializable;

public class Payload implements Serializable {
    public String iss;
    public String sub;
    public String aud;
    public String exp;
    public String nbf;
    public String jti;
    public String name;
    public int access;
}
