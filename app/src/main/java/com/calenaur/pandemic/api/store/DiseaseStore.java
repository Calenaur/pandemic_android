package com.calenaur.pandemic.api.store;

import com.android.volley.Request;
import com.calenaur.pandemic.api.model.disease.Disease;
import com.calenaur.pandemic.api.model.event.Event;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.net.HTTPClient;
import com.calenaur.pandemic.api.net.HTTPStatusCode;
import com.calenaur.pandemic.api.net.PandemicRequest;
import com.calenaur.pandemic.api.net.response.ErrorCode;
import com.fasterxml.jackson.jr.ob.JSON;
import com.fasterxml.jackson.jr.ob.ValueIterator;
import java.io.IOException;
import java.util.ArrayList;

public class DiseaseStore {

    private HTTPClient httpClient;

    public DiseaseStore(HTTPClient httpClient) {
        this.httpClient = httpClient;
    }

    public void getDiseases(LocalUser localUser, PromiseHandler<Disease[]> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setPath("/disease/")
                .setLocalUser(localUser)
                .setRequestListener((code, result) -> {
                    ArrayList<Disease> diseaseList = new ArrayList<>();
                    try {
                        ValueIterator<Disease> values = JSON.std.beanSequenceFrom(Disease.class, result);
                        while (values.hasNext())
                            diseaseList.add(values.next());
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(diseaseList.toArray(new Disease[]{}));
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(null));
                }).create();

        httpClient.queue(request);
    }

}
