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
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyfishjy.library.RippleBackground;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by jordipons on 20/07/2017.
 */

public class GoodChoice extends AppCompatActivity {

    public TextToSpeech mTts;
    private TextView mText;
    private TextView mTextResult;


    private SpeechService mSpeechService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_choice);

        mTextResult = (TextView) findViewById(R.id.result);

        // Prepare Cloud Speech API
        bindService(new Intent(this, SpeechService.class), mServiceConnection, BIND_AUTO_CREATE);
        mText = (TextView) findViewById(R.id.welcome_text);

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
                                    TextView previous =(TextView) findViewById(R.id.welcome_text);
                                    previous.setTextColor(getResources().getColor(R.color.blank));

                                    String satisfactionDef = "Satisfaction forms can help you find out what people think of your company, get feedback on customer service, and more.";
                                    TextView satisfactinoDefinition =(TextView) findViewById(R.id.satisfaction);
                                    satisfactinoDefinition.setText(satisfactionDef);
                                    satisfactinoDefinition.setTextColor(getResources().getColor(R.color.black));

                                    explainSatisfaction(satisfactionDef);
                                }
                            });
                        }
                    });
                    mTts.setLanguage(Locale.getDefault());
                    HashMap<String, String> params = new HashMap<String, String>();

                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"stringId");
                    mTts.speak("Good choice!", TextToSpeech.QUEUE_FLUSH, params);
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

    private void explainSatisfaction(final String text) {
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
                                    TextView previous =(TextView) findViewById(R.id.satisfaction);
                                    previous.setTextColor(getResources().getColor(R.color.blank));

                                    String question = "The first question would be <b>What's your first name?</b>\n" +
                                            "as a short text field.";
                                    TextView satisfactinoDefinition =(TextView) findViewById(R.id.your_name);
                                    satisfactinoDefinition.setText(Html.fromHtml(question));
                                    satisfactinoDefinition.setTextColor(getResources().getColor(R.color.black));
                                    explainOptions(question.replace("<b>", "").replace("</b>", ""));
                                }
                            });
                        }
                    });
                    mTts.setLanguage(Locale.getDefault());
                    HashMap<String, String> params = new HashMap<String, String>();

                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"stringId");
                    mTts.speak(text, TextToSpeech.QUEUE_FLUSH, params);
                } else {
                    mTts = null;
                    Log.e("MainActivity", "Failed to initialize the TextToSpeech engine");
                }
            }
        });
    }

    private void explainOptions(final String text) {
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
                                    TextView previous =(TextView) findViewById(R.id.your_name);
                                    previous.setTextColor(getResources().getColor(R.color.blank));

                                    String question = "Do you want to <b>accept</b>, <b>rephrase</b> or <b>remove</b> the block?";
                                    TextView questionView =(TextView) findViewById(R.id.question);
                                    questionView.setText(Html.fromHtml(question));
                                    questionView.setTextColor(getResources().getColor(R.color.black));

                                    explainQuestion(question.replace("<b>", "").replace("</b>", ""));
                                }
                            });
                        }
                    });
                    mTts.setLanguage(Locale.getDefault());
                    HashMap<String, String> params = new HashMap<String, String>();

                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"stringId");
                    mTts.speak(text, TextToSpeech.QUEUE_FLUSH, params);
                } else {
                    mTts = null;
                    Log.e("MainActivity", "Failed to initialize the TextToSpeech engine");
                }
            }
        });
    }

    private void explainQuestion(final String text) {
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
                                    ImageView imageView=(ImageView)findViewById(R.id.centerImage);
                                    rippleBackground.startRippleAnimation();
                                    startVoiceRecorder();
                                }
                            });
                        }
                    });
                    mTts.setLanguage(Locale.getDefault());
                    HashMap<String, String> params = new HashMap<String, String>();

                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"stringId");
                    mTts.speak(text, TextToSpeech.QUEUE_FLUSH, params);
                } else {
                    mTts = null;
                    Log.e("MainActivity", "Failed to initialize the TextToSpeech engine");
                }
            }
        });
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
                                    Intent intent = new Intent(getApplicationContext(), CoolNextOne.class);
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