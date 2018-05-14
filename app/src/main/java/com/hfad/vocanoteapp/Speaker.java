package com.hfad.vocanoteapp;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

public class Speaker implements TextToSpeech.OnInitListener {

    public static final String TTS = "TTS";
    private TextToSpeech tts;
    private Locale language;
    private boolean ready = false;


    public Speaker(Context context, Locale language){
        tts = new TextToSpeech(context, this);
        this.language = language;
    }

    @Override
    public void onInit(final int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(language);
            ready = true;
            if ((result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED)) {
                Log.e(TTS, "This Language is not supported");
            }
        }
    }

    public void speak(String text){
        // Speak only if the TTS is ready
        // and the user has allowed speech
        if(ready) {
            HashMap<String, String> hash = new HashMap<>();
            hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                    String.valueOf(AudioManager.STREAM_MUSIC));
            tts.speak(text, TextToSpeech.QUEUE_ADD, hash);
        }
    }

    public void destroy(){
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
