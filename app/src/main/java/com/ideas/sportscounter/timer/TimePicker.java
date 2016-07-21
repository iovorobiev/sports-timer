package com.ideas.sportscounter.timer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.ideas.sportscounter.R;
import com.ideas.sportscounter.core.Injector;

public class TimePicker {

    public static void pickTime(final Context context, @StringRes int res,
                                final OnTimeChosedListener timeChosedListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View pickView = LayoutInflater.from(context).inflate(R.layout.time_chooser, null);
        builder.setView(pickView);
        builder.setMessage(res);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing, dirty hack for default AD behaviour
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editor = (EditText) pickView.findViewById(R.id.time);
                String result = editor.getText().toString();
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                if (!TextUtils.isDigitsOnly(result)) {
                    Injector.forApp(context).notifier().notifyWith(R.string.dig_only);
                    return;
                }
                int resultTime = Integer.parseInt(result);
                timeChosedListener.onTimeChosed(resultTime);
                dialog.dismiss();
            }
        });
    }

    public interface OnTimeChosedListener {
        void onTimeChosed(int time);
    }
}
