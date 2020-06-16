package com.calenaur.pandemic.api.net;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class HTTPClient {

    private String endpointURL;
    private RequestQueue requestQueue;

    public HTTPClient(String endpointURL, Context context) {
        this.endpointURL = endpointURL;
        this.requestQueue = Volley.newRequestQueue(context);
        this.requestQueue.start();
    }

    public void queue(PandemicRequest pandemicRequest) {
        requestQueue.add(pandemicRequest);
    }

    public String getEndpointURL() {
        return endpointURL;
    }
}
