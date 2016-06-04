package com.ideas.sportscounter.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.StringRes;

import java.io.File;

public class FileUtils {
    private static final String WAV = ".wav";

    public static String getWavFilePath(Context context, String filename) {
        return context.getCacheDir() + File.separator + filename + WAV;
    }
}
