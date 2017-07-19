package com.google.cloud.android.speech;

import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by pauboix on 19/07/2017.
 */

public class BobTheBuilder {


    public Object BobTheBuilder(JSONObject payload) throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String bobTheBuilderPath = "https://api.typeform.com/forms";
        StringEntity entityPayload = new StringEntity(payload.toString());

        HttpPost httpost = new HttpPost(bobTheBuilderPath);

        httpost.setEntity(entityPayload);
        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json");
        httpost.setHeader("Authorization", "Bearer gozpRNgNbuPm9POrj9M3RGdFFKqaVEivI48F1PE5--0=");

        ResponseHandler responseHandler = new BasicResponseHandler();
        return httpclient.execute(httpost, responseHandler);
    }


}

