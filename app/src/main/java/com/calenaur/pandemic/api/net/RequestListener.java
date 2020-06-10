package com.calenaur.pandemic.api.net;

public interface RequestListener {

    void onResponse(HTTPStatusCode code, String result);

}
