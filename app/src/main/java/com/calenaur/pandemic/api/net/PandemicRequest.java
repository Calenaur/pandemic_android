package com.calenaur.pandemic.api.net;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.store.PromiseHandler;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PandemicRequest extends JsonRequest<String> {

    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";

    private LocalUser localUser;

    private PandemicRequest(int method, String path, String formData, LocalUser localUser, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, path, formData, listener, errorListener);
        this.localUser = localUser;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (localUser == null)
            return Collections.emptyMap();

        HashMap<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("Authorization", String.format("Bearer %s", localUser.getJWT().getOrigin()));
        return extraHeaders;
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

    public static class Builder {
        private int method;
        private String path;
        private String formData;
        private LocalUser localUser;
        private RequestListener requestListener;

        public Builder setMethod(int method) {
            this.method = method;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setFormData(Map<String, Object> formData) {
            StringBuilder formDataBuilder = new StringBuilder();
            for (Map.Entry<String, Object> entry : formData.entrySet()) {
                if (formDataBuilder.length() > 0)
                    formDataBuilder.append("&");

                formDataBuilder.append(entry.getKey()).append("=").append(entry.getValue());
            }

            this.formData = formDataBuilder.toString();
            return this;
        }

        public Builder setLocalUser(LocalUser localUser) {
            this.localUser = localUser;
            return this;
        }

        public Builder setRequestListener(RequestListener requestListener) {
            this.requestListener = requestListener;
            return this;
        }

        public PandemicRequest create() {
            return new PandemicRequest(method, path, formData, localUser,
                    response -> {
                        if (requestListener == null)
                            return;

                        requestListener.onResponse(HTTPStatusCode.OK, response);
                    },
                    error -> {
                        if (requestListener == null)
                            return;

                        requestListener.onResponse(HTTPStatusCode.fromCode(error.networkResponse.statusCode), new String(error.networkResponse.data));
                    }
            );
        }
    }
}
