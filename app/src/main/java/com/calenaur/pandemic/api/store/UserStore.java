package com.calenaur.pandemic.api.store;

import com.android.volley.Request;
import com.calenaur.pandemic.api.model.user.JWT.JSONWebToken;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.net.HTTPClient;
import com.calenaur.pandemic.api.net.HTTPStatusCode;
import com.calenaur.pandemic.api.net.PandemicRequest;
import com.calenaur.pandemic.api.net.response.ErrorCode;
import com.calenaur.pandemic.api.net.response.LoginResponse;
import com.fasterxml.jackson.jr.ob.JSON;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserStore {

    private HTTPClient httpClient;

    public UserStore(HTTPClient httpClient) {
        this.httpClient = httpClient;
    }

    public void login(String username, String password, PromiseHandler<LocalUser> promiseHandler) {
        Map<String, Object> formData = new HashMap<>();
        formData.put("username", username);
        formData.put("password", password);

        PandemicRequest request = new PandemicRequest.Builder()
                .setMethod(Request.Method.POST)
                .setPath("/login")
                .setFormData(formData)
                .setRequestListener((code, result) -> {
                    LoginResponse response;
                    try {
                        response = JSON.std.beanFrom(LoginResponse.class, result);
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(new LocalUser(JSONWebToken.fromToken(response.token)));
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(response));
                }).create();

        httpClient.queue(request);
    }

    public void signup() {
    }

}
