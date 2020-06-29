package com.calenaur.pandemic.api.store;

import com.android.volley.Request;
import com.calenaur.pandemic.api.model.Tier;
import com.calenaur.pandemic.api.model.user.Friend;

import com.calenaur.pandemic.api.model.user.JWT.JSONWebToken;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.model.user.UserDisease;
import com.calenaur.pandemic.api.model.user.UserEvent;
import com.calenaur.pandemic.api.model.user.UserMedication;
import com.calenaur.pandemic.api.net.HTTPClient;
import com.calenaur.pandemic.api.net.HTTPStatusCode;
import com.calenaur.pandemic.api.net.PandemicRequest;
import com.calenaur.pandemic.api.net.response.DefaultResponse;
import com.calenaur.pandemic.api.net.response.ErrorCode;
import com.calenaur.pandemic.api.net.response.user.FriendResponse;

import com.calenaur.pandemic.api.net.response.user.LoginResponse;
import com.calenaur.pandemic.api.net.response.user.NewUserMedicationResponse;
import com.calenaur.pandemic.api.net.response.user.UserMedicationByIdResponse;
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
                        if (jwt == null) {
                            promiseHandler.onError(null);
                            return;
                        }

                        LocalUser localUser = LocalUser.fromToken(jwt);
                        localUser.setBalance(response.balance);
                        localUser.setTier(Tier.fromID(response.tier));
                        promiseHandler.onDone(localUser);
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
                .setPath("/user/event")
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
        if (localUser == null || userEvent == null) {
            promiseHandler.onError(null);
            return;
        }

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

    public void getUserDiseases(LocalUser localUser, PromiseHandler<UserDisease[]> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setLocalUser(localUser)
                .setPath("/user/disease")
                .setRequestListener((code, result) -> {
                    ArrayList<UserDisease> userDiseaseList = new ArrayList<>();
                    try {
                        ValueIterator<UserDisease> values = JSON.std.beanSequenceFrom(UserDisease.class, result);
                        while (values.hasNext())
                            userDiseaseList.add(values.next());
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(userDiseaseList.toArray(new UserDisease[]{}));
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(null));
                }).create();

        httpClient.queue(request);
    }

    public void putUserDisease(LocalUser localUser, UserDisease userDisease, PromiseHandler<Void> promiseHandler) {
        if (localUser == null || userDisease == null) {
            promiseHandler.onError(null);
            return;
        }

        Map<String, Object> formData = new HashMap<>();
        formData.put("diseaseid", userDisease.id);
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.POST)
                .setLocalUser(localUser)
                .setPath("/user/disease")
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

    public void putUserMedication(LocalUser localUser, UserMedication userMedication, PromiseHandler<Integer> promiseHandler) {
        if (localUser == null || userMedication == null) {
            promiseHandler.onError(null);
            return;
        }

        HashMap<String, Object[]> formData = new HashMap<>();
        formData.put("medication", new Object[]{userMedication.medication});
        formData.put("trait", userMedication.traits);
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.POST)
                .setLocalUser(localUser)
                .setPath("/user/medication")
                .setFormDataArray(formData)
                .setRequestListener((code, result) -> {
                    NewUserMedicationResponse response;
                    try {
                        response = JSON.std.beanFrom(NewUserMedicationResponse.class, result);
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(response.id);
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(response));
                }).create();

        httpClient.queue(request);
    }

    public void putBalance(LocalUser localUser, long newBalance, PromiseHandler<Void> promiseHandler) {
        if (localUser == null) {
            promiseHandler.onError(null);
            return;
        }

        Map<String, Object> formData = new HashMap<>();
        formData.put("newbalance", newBalance);
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.PUT)
                .setLocalUser(localUser)
                .setPath("/user/balance")
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

    public void putTier(LocalUser localUser, Tier tier, PromiseHandler<Void> promiseHandler) {
        if (localUser == null) {
            promiseHandler.onError(null);
            return;
        }

        Map<String, Object> formData = new HashMap<>();
        formData.put("tier", tier.getID());
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.PUT)
                .setLocalUser(localUser)
                .setPath("/user/tier")
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

    public void friends(LocalUser localUser, PromiseHandler<Friend[]> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setPath("/user/friends")
                .setLocalUser(localUser)
                .setRequestListener((code, result) -> {
                    ArrayList<Friend> friendList = new ArrayList<>();
                    FriendResponse response = new FriendResponse();
                    try {
                        ValueIterator<Friend> values = JSON.std.beanSequenceFrom(Friend.class, result);
                        while (values.hasNext())
                            friendList.add(values.next());
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    response.friends = friendList.toArray(new Friend[]{});
                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(response.friends);
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(response));
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

    public void removeFriend(LocalUser localUser, String friend, PromiseHandler<Void> promiseHandler) {
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
