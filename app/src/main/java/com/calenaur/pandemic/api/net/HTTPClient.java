package com.calenaur.pandemic.api.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Map;


public class HTTPClient {

    private String endpointURL;
    private RequestQueue requestQueue;

    public HTTPClient(String endpointURL, Context context) {
        this.endpointURL = endpointURL;
        this.requestQueue = Volley.newRequestQueue(context);
        this.requestQueue.start();
    }

    public void post(String path, Map<String, Object> formData, RequestListener requestListener) {
        System.out.println(formData);
        PandemicRequest request = PandemicRequest.withFormData(
                endpointURL + path,
                formData,
                response -> {
                    requestListener.onResponse(HTTPStatusCode.OK, response);
                },
                error -> {
                    requestListener.onResponse(HTTPStatusCode.fromCode(error.networkResponse.statusCode), new String(error.networkResponse.data));
                }
        );
        requestQueue.add(request);
    }

}
