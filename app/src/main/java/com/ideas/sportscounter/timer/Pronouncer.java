package com.ideas.sportscounter.timer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.util.Pair;

import com.ideas.sportscounter.utils.FileUtils;
import com.ideas.sportscounter.utils.SettingsPreferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

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
        final List<Pair<String, String>> paths = new LinkedList<>();
        for (int i = 1; i <= PRONOUNCE_TIME; i++) {
            String text = Integer.toString(i);
            paths.add(new Pair<>(text, FileUtils.getWavFilePath(context, text)));
        }
        paths.add(new Pair<>(goString, FileUtils.getWavFilePath(context, goString)));
        checkAndGenerate(paths);
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                // No implementation
            }

            @Override
            public void onDone(String utteranceId) {
                if (paths.isEmpty()) {
                    //TODO: return listener to hide loading dialog
                    Timber.d("Done");
                    if (initFinishedListener != null) {
                        initFinishedListener.onCreateFinished();
                    }
                    return;
                }
                Timber.d("Rest %d", paths.size());
                checkAndGenerate(paths);
            }

            @Override
            public void onError(String utteranceId) {
                //TODO: add timber, or write custom logs
                Timber.e(new FileNotFoundException(), "Error on creating");
            }
        });
    }

    private void checkAndGenerate(List<Pair<String, String>> paths) {
        Pair<String, String> info = paths.get(0);
        File file = new File(info.second);
        if (file.exists() && !file.delete()) {
            Timber.e("Cannot delete file %s", info.second);
        }
        generateFile(info.first, file);
        paths.remove(0);
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
            Timber.d("result is %d", result);
        }
    }

    public interface InitFinishedListener {
        void onInitFinished();

        void onCreateFinished();
    }
}
