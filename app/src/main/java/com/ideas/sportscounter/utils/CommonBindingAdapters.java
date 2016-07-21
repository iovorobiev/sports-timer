package com.ideas.sportscounter.utils;

import android.databinding.BindingAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.ideas.sportscounter.R;

public class CommonBindingAdapters {

    @BindingAdapter("onTextChanged")
    public static void onTextChanged(EditText editText, final OnTextChangedListener listener) {
        TextWatcher currentWatcher = editText.getTag(R.id.watcher_key) == null ? null
                : (TextWatcher) editText.getTag(R.id.watcher_key);
        if (currentWatcher != null) {
            editText.removeTextChangedListener(currentWatcher);
        }
        currentWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No implementation
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No implementation
            }

            @Override
            public void afterTextChanged(Editable s) {
                listener.textChanged(s.toString());
            }
        };
        editText.addTextChangedListener(currentWatcher);
        editText.setTag(R.id.watcher_key, currentWatcher);
    }

    @BindingAdapter("onClick")
    public static void onClick(View view, final OnEmptyArgumentListener listener) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.callAction();
            }
        });
    }

    public interface OnEmptyArgumentListener {
        void callAction();
    }

    public interface OnTextChangedListener {
        void textChanged(String text);
    }
}
