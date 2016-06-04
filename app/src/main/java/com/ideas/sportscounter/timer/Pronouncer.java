package com.ideas.sportscounter.timer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.ideas.sportscounter.R;
import com.ideas.sportscounter.utils.FileUtils;

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

    public Pronouncer(Context context, String goString) {
        this.goString = goString;
        this.context = context;

        initEnabled(context);
    }

    public void initEnabled(Context context) {
        enabled = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context
                .getString(R.string.settings_voice), false);
        if (enabled) {
            initMediaPlayers();
        }
    }

    private void initMediaPlayers() {
        try {
            for (int i = 0; i < mediaPlayer.length; i++) {
                if (mediaPlayer[i] == null) {
                    mediaPlayer[i] = initMediaPlayer(context, Uri.parse(FileUtils.getWavFilePath
                            (context, getStringFromSeconds(i))));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private MediaPlayer initMediaPlayer(Context context, Uri uri) throws IOException {
        MediaPlayer player = new MediaPlayer();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // no implementation
            }
        });
        player.setDataSource(context, uri);
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

    public void setEnabled(Context context, boolean enabled) {
        this.enabled = enabled;
    }

    public void createFiles() {
        HashMap<String, String> hashRender = new HashMap<>();
        String path;
        File file;
        for (int i = 1; i <= PRONOUNCE_TIME; i++) {
            String text = Integer.toString(i);
            hashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, text);
            path = FileUtils.getWavFilePath(context, text);
            file = new File(path);
            if (!file.exists()) {
                generateFile(hashRender, text, file);
            }
        }
        hashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, goString);
        path = FileUtils.getWavFilePath(context, goString);
        file = new File(path);
        if (!file.exists()) {
            generateFile(hashRender, goString, file);
        }
    }

    //TODO: move work with files to util class
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.US);

            addSpeech(goString);
            for (int i = 1; i <= PRONOUNCE_TIME; i++) {
                addSpeech(Integer.toString(i));
            }
            if (initFinishedListener != null) {
                initFinishedListener.onInitFinished();
            }
        }
    }

    private void addSpeech(String text) {
        File goFile = new File(FileUtils.getWavFilePath(context, text));
        if (!goFile.exists()) {
            enabled = false;
            Toast.makeText(context, R.string.no_files, Toast.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings("deprecation")
    private void generateFile(HashMap<String, String> renderer, String text, File file) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.synthesizeToFile(text, null, file, text);
        } else {
            textToSpeech.synthesizeToFile(text, renderer, file.getAbsolutePath());
        }
    }

    public interface InitFinishedListener {
        void onInitFinished();
    }
}
