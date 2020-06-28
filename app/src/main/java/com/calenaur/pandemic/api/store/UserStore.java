package com.calenaur.pandemic.api.store;

import com.android.volley.Request;
import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.user.JWT.JSONWebToken;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.model.user.UserMedication;
import com.calenaur.pandemic.api.net.HTTPClient;
import com.calenaur.pandemic.api.net.HTTPStatusCode;
import com.calenaur.pandemic.api.net.PandemicRequest;
import com.calenaur.pandemic.api.net.response.DefaultResponse;
import com.calenaur.pandemic.api.net.response.ErrorCode;
import com.calenaur.pandemic.api.net.response.medication.MedicationResponse;
import com.calenaur.pandemic.api.net.response.user.LoginResponse;
import com.calenaur.pandemic.api.net.response.user.UserMedicationByIdResponse;
import com.calenaur.pandemic.api.net.response.user.UserMedicationResponse;
import com.fasterxml.jackson.jr.ob.JSON;
import com.fasterxml.jackson.jr.ob.ValueIterator;

import java.io.IOException;
import java.util.ArrayList;
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

        PandemicRequest request = new PandemicRequest.Builder(httpClient)
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
                        JSONWebToken jwt = JSONWebToken.fromString(response.token);
                        if (jwt == null)
                            promiseHandler.onError(null);

                        promiseHandler.onDone(LocalUser.fromToken(jwt));
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(response));
                }).create();

        httpClient.queue(request);
    }

    public void register(String username, String password, PromiseHandler<Object> promiseHandler) {
        Map<String, Object> formData = new HashMap<>();
        formData.put("username", username);
        formData.put("password", password);

        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.POST)
                .setPath("/signup")
                .setFormData(formData)
                .setRequestListener((code, result) -> {
                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(null);
                        return;
                    }

                    try {
                        DefaultResponse response = JSON.std.beanFrom(DefaultResponse.class, result);
                        promiseHandler.onError(ErrorCode.fromResponse(response));
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                    }
                }).create();

        httpClient.queue(request);
    }

    public void userMedications(LocalUser localUser, PromiseHandler<UserMedicationResponse[]> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setLocalUser(localUser)
                .setPath("/user/medication")
                .setRequestListener((code, result) -> {
                    ArrayList<UserMedicationResponse> medicationList = new ArrayList<>();
                    try {
                        ValueIterator<UserMedicationResponse> values = JSON.std.beanSequenceFrom(UserMedicationResponse.class, result);
                        while (values.hasNext())
                            medicationList.add(values.next());
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(medicationList.toArray(new UserMedicationResponse[]{}));
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(null));
                }).create();

        httpClient.queue(request);
    }

    public void medicationsById(LocalUser localUser, int id, PromiseHandler<UserMedication> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setPath("/medication/"+id)
                .setRequestListener((code, result) -> {
                    UserMedicationByIdResponse response;
                    try {
                        response = JSON.std.beanFrom(UserMedicationByIdResponse.class, result);
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(response.userMedication);
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(response));
                }).create();

        httpClient.queue(request);
    }
}
