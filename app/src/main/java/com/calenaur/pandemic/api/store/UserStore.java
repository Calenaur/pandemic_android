package com.calenaur.pandemic.api.store;

import android.util.Log;

import com.android.volley.Request;
import com.calenaur.pandemic.api.model.user.Friend;

import com.calenaur.pandemic.api.model.user.JWT.JSONWebToken;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.model.user.UserEvent;
import com.calenaur.pandemic.api.model.user.UserMedication;
import com.calenaur.pandemic.api.net.HTTPClient;
import com.calenaur.pandemic.api.net.HTTPStatusCode;
import com.calenaur.pandemic.api.net.PandemicRequest;
import com.calenaur.pandemic.api.net.response.DefaultResponse;
import com.calenaur.pandemic.api.net.response.ErrorCode;
import com.calenaur.pandemic.api.net.response.user.FriendResponse;

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

    public void getUserMedications(LocalUser localUser, PromiseHandler<UserMedication[]> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setLocalUser(localUser)
                .setPath("/user/medication")
                .setRequestListener((code, result) -> {
                    ArrayList<UserMedication> medicationList = new ArrayList<>();
                    try {
                        ValueIterator<UserMedication> values = JSON.std.beanSequenceFrom(UserMedication.class, result);
                        while (values.hasNext())
                            medicationList.add(values.next());
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(medicationList.toArray(new UserMedication[]{}));
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(null));
                }).create();

        httpClient.queue(request);
    }

    public void getUserEvents(LocalUser localUser, PromiseHandler<UserEvent[]> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setLocalUser(localUser)
                .setPath("/user/event/mine")
                .setRequestListener((code, result) -> {
                    ArrayList<UserEvent> userEventList = new ArrayList<>();
                    try {
                        ValueIterator<UserEvent> values = JSON.std.beanSequenceFrom(UserEvent.class, result);
                        while (values.hasNext())
                            userEventList.add(values.next());
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(userEventList.toArray(new UserEvent[]{}));
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(null));
                }).create();

        httpClient.queue(request);
    }

    public void putUserEvent(LocalUser localUser, UserEvent userEvent, PromiseHandler<Void> promiseHandler) {
        if (localUser == null || userEvent == null)
            return;

        Map<String, Object> formData = new HashMap<>();
        formData.put("event", userEvent.id);
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.PUT)
                .setLocalUser(localUser)
                .setPath("/user/event")
                .setFormData(formData)
                .setRequestListener((code, result) -> {
                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(null);
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(null));
                }).create();

        httpClient.queue(request);
    }

    public void getUserMedicationByID(LocalUser localUser, int id, PromiseHandler<UserMedication> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setPath("/medication/"+id)
                .setLocalUser(localUser)
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

    public void friends(LocalUser localUser, PromiseHandler<FriendResponse[]> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setPath("/user/friends")
                .setLocalUser(localUser)
                .setRequestListener((code, result) -> {
                    ArrayList<FriendResponse> friendList = new ArrayList<>();
                    try {
                        ValueIterator<FriendResponse> values = JSON.std.beanSequenceFrom(FriendResponse.class, result);
                        while (values.hasNext())
                            friendList.add(values.next());
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(friendList.toArray(new FriendResponse[]{}));
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(null));
                }).create();

        httpClient.queue(request);
    }

    public void sendFriendRequest(LocalUser localUser, String friend, PromiseHandler<Object> promiseHandler) {
        Map<String, Object> formData = new HashMap<>();
        formData.put("friend", friend);

        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.POST)
                .setPath("/user/friend_request")
                .setLocalUser(localUser)
                .setFormData(formData)
                .setRequestListener((code, result) -> {
                    DefaultResponse response;
                    try {
                        response = JSON.std.beanFrom(FriendResponse.class, result);
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(null);
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(response));
                }).create();

        httpClient.queue(request);
    }

    public void sendFriendResponse(LocalUser localUser, String friend, int userResponse, PromiseHandler<Object> promiseHandler) {
        Map<String, Object> formData = new HashMap<>();
        formData.put("friend", friend);
        formData.put("response", userResponse);

        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.PUT)
                .setPath("/user/friend_response")
                .setLocalUser(localUser)
                .setFormData(formData)
                .setRequestListener((code, result) -> {
                    DefaultResponse response;
                    try {
                        response = JSON.std.beanFrom(FriendResponse.class, result);
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(null);
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(response));
                }).create();

        httpClient.queue(request);
    }

    public void removeFriend(LocalUser localUser, String friend, PromiseHandler<Object> promiseHandler) {
        Map<String, Object> formData = new HashMap<>();
        formData.put("friend", friend);

        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.DELETE)
                .setPath("/user/friend")
                .setLocalUser(localUser)
                .setFormData(formData)
                .setRequestListener((code, result) -> {
                    DefaultResponse response;
                    try {
                        response = JSON.std.beanFrom(FriendResponse.class, result);
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(null);
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(response));
                }).create();

        httpClient.queue(request);
    }
}
