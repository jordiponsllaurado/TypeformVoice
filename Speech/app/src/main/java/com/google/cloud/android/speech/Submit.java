package com.google.cloud.android.speech;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import static com.google.cloud.android.speech.MainActivity.EXTRA_MESSAGE;

/**
 * Created by jordipons on 20/07/2017.
 */

public class Submit extends AppCompatActivity {

    public TextToSpeech mTts;
    private TextView mText;
    private TextView mTextResult;
    public BobTheBuilder bob;

    private SpeechService mSpeechService;

    // Resource caches
    private int mColorHearing;
    private int mColorNotHearing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit);

        Intent messageIntent = getIntent();
        String message = messageIntent.getStringExtra(EXTRA_MESSAGE);

        String json = "{" +
                "  \"title\":\"Challenge satisfaction survey\"," +
                "\"settings\": {" +
                "    \"language\": \"en\"" +
                "    }," +
                "            \"thankyou_screens\": [" +
                "    {" +
                "      \"title\": \"Thank you for participating!\"," +
                "      \"properties\": {" +
                "        \"show_button\": true," +
                "        \"button_text\": \"start\"," +
                "        \"button_mode\": \"redirect\"," +
                "        \"redirect_url\": \"http://www.typeform.com\"," +
                "        \"share_icons\": false" +
                "    }" +
                "}" +
                "  ]," +
                "          \"fields\": [" +
                "          {" +
                "          \"title\": \"What's your name?\"," +
                "          \"type\": \"short_text\"," +
                "          \"validations\": {" +
                "          \"required\": false," +
                "          \"max_length\": 20" +
                "          }" +
                "          }," +
                "          {" +
                "          \"title\": \"" + message + "\"," +
                "          \"type\": \"opinion_scale\"," +
                "          \"properties\": {" +
                "          \"description\": \"\"," +
                "          \"steps\": 10," +
                "          \"start_at_one\": true," +
                "          \"labels\": {" +
                "          \"left\": \" left label\"," +
                "          \"center\": \"center label\"," +
                "          \"right\": \"right label\"" +
                "          }" +
                "          }," +
                "          \"validations\": {" +
                "          \"required\": false" +
                "          }" +
                "          }" +
                "          ]" +
                "          }";
        try {
            JSONObject jsonObject = new JSONObject(json);

            bob = new BobTheBuilder(jsonObject);
            bob.execute();
        } catch (JSONException e) {
        }


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
                                    startVoiceRecorder();
                                }
                            });
                        }
                    });
                    mTts.setLanguage(Locale.getDefault());
                    HashMap<String, String> params = new HashMap<String, String>();

                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"stringId");
                    mTts.speak("Your Typeform is Done!\n Do you want to check the Typeform first or to send it out now?", TextToSpeech.QUEUE_FLUSH, params);
                } else {
                    mTts = null;
                    Log.e("MainActivity", "Failed to initialize the TextToSpeech engine");
                }
            }
        });
    }

    @Override
    protected void onStop() {
        // Stop listening to voice
        stopVoiceRecorder();

        // Stop Cloud Speech API
        mSpeechService.removeListener(mSpeechServiceListener);
        unbindService(mServiceConnection);
        mSpeechService = null;

        super.onStop();
    }

    //////////////// SPEECH API ////////////////
    private VoiceRecorder mVoiceRecorder;
    private final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {

        @Override
        public void onVoiceStart() {
            if (mSpeechService != null) {
                mSpeechService.startRecognizing(mVoiceRecorder.getSampleRate());
            }
        }

        @Override
        public void onVoice(byte[] data, int size) {
            if (mSpeechService != null) {
                mSpeechService.recognize(data, size);
            }
        }

        @Override
        public void onVoiceEnd() {
            if (mSpeechService != null) {
                mSpeechService.finishRecognizing();
            }
        }

    };



    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            mSpeechService = SpeechService.from(binder);
            mSpeechService.addListener(mSpeechServiceListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mSpeechService = null;
        }

    };

    private final SpeechService.Listener mSpeechServiceListener =
            new SpeechService.Listener() {
                @Override
                public void onSpeechRecognized(final String text, final boolean isFinal) {
                    if (isFinal) {
                        mVoiceRecorder.dismiss();
                    }
                    if (mText != null && !TextUtils.isEmpty(text)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTextResult.setText(text);

                                if (isFinal) {
                                    mTextResult.setText(text);
                                    mVoiceRecorder.stop();
                                    String responseBody = bob.getResponseBody();
                                    //TODO: OPEN GMAIL
                                } else {
                                    mTextResult.setText(text);
                                }
                            }
                        });
                    }
                }
            };

    private void startVoiceRecorder() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            if (mVoiceRecorder != null) {
                mVoiceRecorder.stop();
            }
            mVoiceRecorder = new VoiceRecorder(mVoiceCallback);
            mVoiceRecorder.start();
        }
    }

    private void stopVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
            mVoiceRecorder = null;
        }
    }
}
