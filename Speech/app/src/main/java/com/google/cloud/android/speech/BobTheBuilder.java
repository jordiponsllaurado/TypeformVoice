package com.google.cloud.android.speech;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;


/**
 * Created by pauboix on 19/07/2017.
 */

public class BobTheBuilder extends AsyncTask<Void, Void, Void> {

    private JSONObject json;
    public String body;
    public BobTheBuilder(JSONObject json) {
        this.json = json;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        HttpClient httpclient = new DefaultHttpClient();
        String bobTheBuilderPath = "https://api.typeform.com/forms";
        try {
            StringEntity entityPayload = new StringEntity(json.toString());
            HttpPost httpost = new HttpPost(bobTheBuilderPath);

            httpost.setEntity(entityPayload);
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");
            httpost.setHeader("Authorization", "Bearer 8g5iwwC8mLPNJd21NwRY8OdPVDYlGaUmgNZBNpxL9mA=");

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            HttpResponse response = httpclient.execute(httpost);
            body = responseHandler.handleResponse(response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getResponseBody() {
        return body;
    }
}

