package com.ideas.sportscounter.timer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.ideas.sportscounter.R;
import com.ideas.sportscounter.utils.FileUtils;
import com.ideas.sportscounter.utils.SettingsPreferences;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class Pronouncer implements TextToSpeech.OnInitListener {
    private static final int PRONOUNCE_TIME = 5;
    private TextToSpeech textToSpeech;
    private final String goString;
    private InitFinishedListener initFinishedListener;
    private boolean enabled;
    private Context context;
    private final MediaPlayer[] mediaPlayer = new MediaPlayer[PRONOUNCE_TIME + 1];
    private final SettingsPreferences preferences;

    public Pronouncer(Context context, String goString) {
        this.goString = goString;
        this.context = context;
        preferences = new SettingsPreferences(context);
    }

    void initEnabled() {
        enabled = preferences.isVoiceEnabled();
        if (enabled) {
            initMediaPlayers();
        }
    }

    private void initMediaPlayers() {
        try {
            for (int i = 0; i < mediaPlayer.length; i++) {
                if (mediaPlayer[i] == null) {
                    mediaPlayer[i] = initMediaPlayer(context, FileUtils.getWavFilePath(context, getStringFromSeconds(i)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private MediaPlayer initMediaPlayer(Context context, String uri) throws IOException {
        MediaPlayer player = new MediaPlayer();
        Log.d("Sports", uri);
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // no implementation
            }
        });
        player.setDataSource(uri);
        player.prepareAsync();
        player.seekTo(0);
        return player;
    }

    public void initTextToSpeech(Context context, InitFinishedListener listener) {
        textToSpeech = new TextToSpeech(context, this);
        this.initFinishedListener = listener;
    }

    public void destroyTextToSpeech() {
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
        for (int i = 0; i < mediaPlayer.length; i++) {
            if (mediaPlayer[i] != null) {
                mediaPlayer[i].release();
                mediaPlayer[i] = null;
            }
        }
    }

    public void speakSeconds(int seconds) {
        if (seconds <= PRONOUNCE_TIME && enabled) {
            if (mediaPlayer[seconds] != null) {
                mediaPlayer[seconds].start();
            }
        }
    }

    private String getStringFromSeconds(int seconds) {
        if (seconds == 0) {
            return goString;
        }
        return Integer.toString(seconds);
    }

    public void createFiles() {
        String path;
        File file;
        for (int i = 1; i <= PRONOUNCE_TIME; i++) {
            String text = Integer.toString(i);
            path = FileUtils.getWavFilePath(context, text);
            file = new File(path);
            if (!file.exists()) {
                generateFile(text, file);
            }

        }
        path = FileUtils.getWavFilePath(context, goString);
        file = new File(path);
        if (!file.exists()) {
            generateFile(goString, file);
        }
    }

    //TODO: move work with files to util class
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.US);
            if (initFinishedListener != null) {
                initFinishedListener.onInitFinished();
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void generateFile(String text, File file) {
        int result;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            result = textToSpeech.synthesizeToFile(text, null, file, text);
        } else {
            result = textToSpeech.synthesizeToFile(text, null, file.getAbsolutePath());
        }
        if (result != TextToSpeech.SUCCESS) {
            Log.d("Sports", "result is " + result);
        }
    }

    public interface InitFinishedListener {
        void onInitFinished();
    }
}
