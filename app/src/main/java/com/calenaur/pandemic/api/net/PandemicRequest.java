package com.calenaur.pandemic.api.net;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class PandemicRequest extends JsonRequest<String> {

    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";

    private PandemicRequest(String url, String body, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, body, listener, errorListener);
    }

    static PandemicRequest withFormData(String url, Map<String, Object> formData, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        StringBuilder formDataBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            if (formDataBuilder.length() > 0)
                formDataBuilder.append("&");

            formDataBuilder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return new PandemicRequest(url, formDataBuilder.toString(), listener, errorListener);
    }

    @Override
    public String getBodyContentType() {
        return CONTENT_TYPE;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
