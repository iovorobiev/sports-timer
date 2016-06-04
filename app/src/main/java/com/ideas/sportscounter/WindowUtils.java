package com.ideas.sportscounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class WindowUtils {
    public static Dialog showLoaderDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.v_c_progress, null);
        builder.setView(view);
        builder.setCancelable(false);
        return builder.show();
    }
}
