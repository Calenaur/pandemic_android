package com.calenaur.pandemic.api.store;

import com.android.volley.Request;
import com.calenaur.pandemic.api.model.medication.Medication;
import com.calenaur.pandemic.api.model.medication.MedicationDisease;
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
import com.fasterxml.jackson.jr.ob.ValueIterator;

import java.io.IOException;
import java.util.ArrayList;

public class MedicationStore {

    private HTTPClient httpClient;

    public MedicationStore(HTTPClient httpClient) {
        this.httpClient = httpClient;
    }

    public void getMedications(LocalUser localUser, PromiseHandler<Medication[]> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setLocalUser(localUser)
                .setPath("/medication")
                .setRequestListener((code, result) -> {
                    ArrayList<Medication> medicationList = new ArrayList<>();
                    MedicationResponse response = new MedicationResponse();
                    try {
                        ValueIterator<Medication> values = JSON.std.beanSequenceFrom(Medication.class, result);
                        while (values.hasNext())
                            medicationList.add(values.next());
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    response.medications = medicationList.toArray(new Medication[]{});
                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(response.medications);
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(response));
                }).create();

        httpClient.queue(request);
    }

    public void getMedicationByID(LocalUser localUser, int id, PromiseHandler<Medication> promiseHandler) {
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

    public void getMedicationTraits(LocalUser localUser, PromiseHandler<MedicationTrait[]> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setLocalUser(localUser)
                .setPath("/medication/trait")
                .setRequestListener((code, result) -> {
                    ArrayList<MedicationTrait> medicationTraitList = new ArrayList<>();
                    MedicationTraitResponse response = new MedicationTraitResponse();
                    try {
                        ValueIterator<MedicationTrait> values = JSON.std.beanSequenceFrom(MedicationTrait.class, result);
                        while (values.hasNext())
                            medicationTraitList.add(values.next());
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    response.medicationTraits = medicationTraitList.toArray(new MedicationTrait[]{});
                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(response.medicationTraits);
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(response));
                }).create();

        httpClient.queue(request);
    }

    public void getMedicationTraitByID(LocalUser localUser, int id, PromiseHandler<MedicationTrait> promiseHandler) {
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

    public void getMedicationDiseases(LocalUser localUser, PromiseHandler<MedicationDisease[]> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setLocalUser(localUser)
                .setPath("/disease/medication")
                .setRequestListener((code, result) -> {
                    ArrayList<MedicationDisease> medicationDiseaseList = new ArrayList<>();
                    try {
                        ValueIterator<MedicationDisease> values = JSON.std.beanSequenceFrom(MedicationDisease.class, result);
                        while (values.hasNext())
                            medicationDiseaseList.add(values.next());
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(medicationDiseaseList.toArray(new MedicationDisease[]{}));
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(null));
                }).create();

        httpClient.queue(request);
    }

}
