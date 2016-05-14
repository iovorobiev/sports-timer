package com.ideas.sportscounter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ideas.sportscounter.viewmodel.CountersViewModel;


public class TimePicker implements ITimePicker{
//    @Inject
    CountersViewModel model;
    //TODO: make DI here
    public TimePicker(CountersViewModel model) {
        this.model = model;
    }

    public void pickTime(View view) {
        pickTime(view.getContext(), view.getId() == R.id.timerMinutes);
    }

    public void pickTime(final Context context, final boolean isMinutes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View pickView = LayoutInflater.from(context).inflate(R.layout.time_chooser, null);
        builder.setView(pickView);
        builder.setMessage(isMinutes? R.string.chose_minutes : R.string.choose_seconds);
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
                    Toast.makeText(context, R.string.dig_only, Toast.LENGTH_LONG).show();

                    return;
                }
                int resultTime = Integer.parseInt(result);
                if (isMinutes) {
                    model.setMinutes(resultTime);
                } else {
                    model.setSeconds(resultTime);
                }
                model.saveState();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void pickMinutes(Context context) {
        pickTime(context, true);
    }

    @Override
    public void pickSeconds(Context context) {
        pickTime(context, false);
    }
}
