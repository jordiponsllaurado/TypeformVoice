package com.google.cloud.android.speech;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by jordipons on 20/07/2017.
 */

public class GoodChoice extends AppCompatActivity {

    public TextToSpeech mTts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_choice);

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
                                    ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.content_main);
                                    TextView satisfactinoDefinition = new TextView(getApplicationContext());
                                    String satisfactionDef = "Satisfaction forms can help you find out what people think of your company, get feedback on customer service, and more.";
                                    satisfactinoDefinition.setText(satisfactionDef);
                                    ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(
                                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                            ConstraintLayout.LayoutParams.WRAP_CONTENT);
                                    layout.addView(satisfactinoDefinition, -1, newParams);
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
                                    ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.content_main);
                                    TextView satisfactinoDefinition = new TextView(getApplicationContext());
                                    String question = "The first question would be <b>What's your first name?</b>\n" +
                                            "as a short text field.";
                                    satisfactinoDefinition.setText(question);
                                    ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(
                                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                            ConstraintLayout.LayoutParams.WRAP_CONTENT);
                                    layout.addView(satisfactinoDefinition, -1, newParams);
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
                                    ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.content_main);
                                    TextView satisfactinoDefinition = new TextView(getApplicationContext());
                                    String question = "Do you want to <b>accept</b>, <b>rephrase</b> or <b>remove</b> the block?";
                                    satisfactinoDefinition.setText(question);
                                    ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(
                                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                            ConstraintLayout.LayoutParams.WRAP_CONTENT);
                                    layout.addView(satisfactinoDefinition, -1, newParams);
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
                                    //TODO
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
}