package com.calenaur.pandemic.api.store;

import com.android.volley.Request;
import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.medication.MedicationTrait;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.net.HTTPClient;
import com.calenaur.pandemic.api.net.HTTPStatusCode;
import com.calenaur.pandemic.api.net.PandemicRequest;
import com.calenaur.pandemic.api.net.response.ErrorCode;
import com.calenaur.pandemic.api.net.response.medication.MedicationByIdResponse;
import com.calenaur.pandemic.api.net.response.medication.MedicationResponse;
import com.calenaur.pandemic.api.net.response.medication.MedicationTraitByIdResponse;
import com.calenaur.pandemic.api.net.response.medication.MedicationTraitResponse;
import com.fasterxml.jackson.jr.ob.JSON;

import java.io.IOException;

public class MedicineStore {

    private HTTPClient httpClient;

    public MedicineStore(HTTPClient httpClient) {
        this.httpClient = httpClient;
    }

    public void medications(LocalUser localUser, PromiseHandler<Medication[]> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setLocalUser(localUser)
                .setPath("/medication")
                .setRequestListener((code, result) -> {
                    MedicationResponse response;
                    try {
                        response = JSON.std.beanFrom(MedicationResponse.class, result);
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(response.medications);
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(response));
                }).create();

        httpClient.queue(request);
    }

    public void medicationById(LocalUser localUser, int id, PromiseHandler<Medication> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setLocalUser(localUser)
                .setPath("/medication/" + id)
                .setRequestListener((code, result) -> {
                    MedicationByIdResponse response;
                    try {
                        response = JSON.std.beanFrom(MedicationByIdResponse.class, result);
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(response.medication);
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(response));
                }).create();

        httpClient.queue(request);
    }

    public void medicationTraits(LocalUser localUser, PromiseHandler<MedicationTrait[]> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setLocalUser(localUser)
                .setPath("/medication/trait")
                .setRequestListener((code, result) -> {
                    MedicationTraitResponse response;
                    try {
                        response = JSON.std.beanFrom(MedicationTraitResponse.class, result);
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(response.medicationTraits);
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(response));
                }).create();

        httpClient.queue(request);
    }

    public void medicationTraitById(LocalUser localUser, int id, PromiseHandler<MedicationTrait> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setLocalUser(localUser)
                .setPath("/medication/trait/" + id)
                .setRequestListener((code, result) -> {
                    MedicationTraitByIdResponse response;
                    try {
                        response = JSON.std.beanFrom(MedicationTraitByIdResponse.class, result);
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(response.medicationTrait);
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(response));
                }).create();

        httpClient.queue(request);
    }

}
