package com.google.cloud.android.speech;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by jordipons on 20/07/2017.
 */

public class WhatTitle extends AppCompatActivity {

    public TextToSpeech mTts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.what_title);

        mTts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    mTts.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {

                        @Override
                        public void onUtteranceCompleted(String utteranceId) {

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    //TODO
                                }
                            });
                        }
                    });
                    mTts.setLanguage(Locale.getDefault());
                    HashMap<String, String> params = new HashMap<String, String>();

                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"stringId");
                    mTts.speak("What title do you want?", TextToSpeech.QUEUE_FLUSH, params);
                } else {
                    mTts = null;
                    Log.e("MainActivity", "Failed to initialize the TextToSpeech engine");
                }
            }
        });
    }
}
