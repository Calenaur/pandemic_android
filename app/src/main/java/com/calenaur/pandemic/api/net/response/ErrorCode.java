package com.calenaur.pandemic.api.net.response;

import com.calenaur.pandemic.R;

public enum ErrorCode {
    UNKNOWN(2999, R.string.error_unknown),
    USERNAME_LENGTH(2000, R.string.error_username_length),
    USERNAME_SPECIAL(2001, R.string.error_username_special),
    PASSWORD_LENGTH(2010, R.string.error_password_length),
    PASSWORD_UPPERCASE(2011, R.string.error_password_uppercase),
    PASSWORD_NUMERIC(2012, R.string.error_password_numeric),
    INVALID_PASSWORD(2100, R.string.error_invalid_password),
    USER_NOT_FOUND(2200, R.string.error_user_not_found);

    private int code;
    private int resource;

    ErrorCode(int code, int resource) {
        this.code = code;
        this.resource = resource;
    }

    public int getCode() {
        return code;
    }

    public int getResource() {
        return resource;
    }

    public static ErrorCode fromCode(int code) {
        for (ErrorCode errorCode : ErrorCode.values())
            if (errorCode.getCode() == code)
                return errorCode;

        return UNKNOWN;
    }

    public static ErrorCode fromResponse(DefaultResponse response) {
        if (response == null)
            return UNKNOWN;

        return fromCode(response.error.code);
    }

    public int getField() {
        return 0;
    }
}
