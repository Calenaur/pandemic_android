package com.calenaur.pandemic.api.net;

public enum HTTPStatusCode {
    NONE(0),
    OK(200),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    SERVER_ERROR(500),
    SERVICE_UNAVAILABLE(503);

    private int code;

    HTTPStatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static HTTPStatusCode fromCode(int code) {
        for (HTTPStatusCode statusCode : HTTPStatusCode.values())
            if (statusCode.getCode() == code)
                return statusCode;

        return NONE;
    }
}
