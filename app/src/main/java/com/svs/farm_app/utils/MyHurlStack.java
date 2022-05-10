package com.svs.farm_app.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HurlStack;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Wamae on 22-Dec-17.
 */

public class MyHurlStack extends HurlStack {
    @Override
    public HttpResponse performRequest(
            Request<?> request, Map<String, String> additionalHeaders)
            throws IOException, AuthFailureError {

        additionalHeaders.put("X-API-KEY", Config.API_KEY);
        if (!additionalHeaders.containsKey("Accept-Encoding"))
            additionalHeaders.put("Accept-Encoding", "gzip, deflate, br");

        return super.performRequest(request, additionalHeaders);
    }
}
