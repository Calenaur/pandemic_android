package com.calenaur.pandemic.api.net.response;

import com.calenaur.pandemic.R;

public enum ErrorCode {
    UNKNOWN(-1, R.string.error_unknown);

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
}
