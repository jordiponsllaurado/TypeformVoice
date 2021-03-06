package com.google.cloud.android.speech;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.skyfishjy.library.RippleBackground;

import java.util.HashMap;
import java.util.Locale;

import static com.google.cloud.android.speech.MainActivity.EXTRA_MESSAGE;

/**
 * Created by jordipons on 20/07/2017.
 */

public class ThankYou extends AppCompatActivity {

    public TextToSpeech mTts;
    private TextView mText;
    private TextView mTextResult;


    private SpeechService mSpeechService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thank_you);

        mTextResult = (TextView) findViewById(R.id.result);
        final Resources resources = getResources();
        final Resources.Theme theme = getTheme();

        // Prepare Cloud Speech API
        bindService(new Intent(this, SpeechService.class), mServiceConnection, BIND_AUTO_CREATE);
        mText = (TextView) findViewById(R.id.welcome_text);
        mText.setText(Html.fromHtml("Last block would be <b>Thank you for participating</b> as a thank you screen."));
        TextView isok = (TextView) findViewById(R.id.isok);
        isok.setText(Html.fromHtml("Do you want to <b>accept</b>, <b>rephrase</b>, <b>add a picture</b> or <b>remove</b> the block?"));
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
                                    final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
                                    rippleBackground.startRippleAnimation();
                                    startVoiceRecorder();
                                }
                            });
                        }
                    });
                    mTts.setLanguage(Locale.getDefault());
                    HashMap<String, String> params = new HashMap<String, String>();

                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"stringId");
                    mTts.speak("Last block would be Thank you for participating as a thank you screen.\n Do you want to accept, rephrase, add a picture or remove the block?", TextToSpeech.QUEUE_FLUSH, params);
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
                                    Intent messageIntent = getIntent();
                                    String message = messageIntent.getStringExtra(EXTRA_MESSAGE);
                                    Intent intent = new Intent(getApplicationContext(), Camera.class);
                                    intent.putExtra(EXTRA_MESSAGE, message);
                                    startActivity(intent);
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
