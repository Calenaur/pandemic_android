package com.calenaur.pandemic.api.store;

import com.android.volley.Request;
import com.calenaur.pandemic.api.net.HTTPClient;
import com.calenaur.pandemic.api.net.HTTPStatusCode;
import com.calenaur.pandemic.api.net.PandemicRequest;
import com.calenaur.pandemic.api.net.response.DefaultResponse;
import com.calenaur.pandemic.api.net.response.ErrorCode;
import com.fasterxml.jackson.jr.ob.JSON;

import java.io.IOException;

public class MedicineStore {

    private HTTPClient httpClient;

    public MedicineStore(HTTPClient httpClient) {
        this.httpClient = httpClient;
    }

    public void medications(PromiseHandler<Object> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setPath("/medication")
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

    public void medicationById(int id, PromiseHandler<Object> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setPath("/medication/" + id)
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

    public void medicationsTrait(PromiseHandler<Object> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setPath("/medication/trait")
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

    public void medicationsTraitById(int id, PromiseHandler<Object> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setPath("/medication/trait/" + id)
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

}
