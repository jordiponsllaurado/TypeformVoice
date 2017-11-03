package com.google.cloud.android.speech;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Locale;

import static com.google.cloud.android.speech.MainActivity.EXTRA_MESSAGE;

/**
 * Created by jordipons on 20/07/2017.
 */

public class Camera extends AppCompatActivity {

    public TextToSpeech mTts;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        mTts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {

                    mTts.setLanguage(Locale.getDefault());
                    HashMap<String, String> params = new HashMap<String, String>();

                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"stringId");
                    mTts.speak("camera", TextToSpeech.QUEUE_FLUSH, params);
                } else {
                    mTts = null;
                    Log.e("MainActivity", "Failed to initialize the TextToSpeech engine");
                }
            }
        });
    }

    //////// CAMERA ////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageview = (ImageView) findViewById(R.id.ImageView01); //sets imageview as the bitmap
            imageview.setImageBitmap(imageBitmap);

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
                                        Intent intent = new Intent(getApplicationContext(), Submit.class);

                                        Intent messageIntent = getIntent();
                                        String message = messageIntent.getStringExtra(EXTRA_MESSAGE);
                                        intent.putExtra(EXTRA_MESSAGE, message);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                        mTts.setLanguage(Locale.getDefault());
                        HashMap<String, String> params = new HashMap<String, String>();

                        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"stringId");
                        mTts.speak("The picture has added to your block.", TextToSpeech.QUEUE_FLUSH, params);
                    } else {
                        mTts = null;
                        Log.e("MainActivity", "Failed to initialize the TextToSpeech engine");
                    }
                }
            });
        }
    }

}
