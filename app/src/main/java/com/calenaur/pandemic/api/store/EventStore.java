package com.calenaur.pandemic.api.store;

import com.android.volley.Request;
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

public class EventStore {

    private HTTPClient httpClient;

    public EventStore(HTTPClient httpClient) {
        this.httpClient = httpClient;
    }

    public void events(LocalUser localUser, PromiseHandler<Event[]> promiseHandler) {
        PandemicRequest request = new PandemicRequest.Builder(httpClient)
                .setMethod(Request.Method.GET)
                .setPath("/event")
                .setLocalUser(localUser)
                .setRequestListener((code, result) -> {
                    ArrayList<Event> eventList = new ArrayList<>();
                    try {
                        ValueIterator<Event> values = JSON.std.beanSequenceFrom(Event.class, result);
                        while (values.hasNext())
                            eventList.add(values.next());
                    } catch (IOException ignored) {
                        promiseHandler.onError(ErrorCode.fromResponse(null));
                        return;
                    }

                    if (code == HTTPStatusCode.OK) {
                        promiseHandler.onDone(eventList.toArray(new Event[]{}));
                        return;
                    }

                    promiseHandler.onError(ErrorCode.fromResponse(null));
                }).create();

        httpClient.queue(request);
    }

}
