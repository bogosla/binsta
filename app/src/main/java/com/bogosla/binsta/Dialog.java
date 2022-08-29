package com.bogosla.binsta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Dialog extends DialogFragment {
    private static final String KEY_TITLE = "title";
    private static final String KEY_MESSAGE = "msg";

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onNegativeClick(DialogInterface dialog);
        void onPositiveClick(DialogInterface dialog);
    }

    public Dialog() {}

    public static Dialog newInstance(String title, String msg) {
        Dialog dialog = new Dialog();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putString(KEY_MESSAGE, msg);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        AlertDialog.Builder al = new AlertDialog.Builder(getActivity());
        al.setTitle(bundle.getString(KEY_TITLE));
        al.setMessage(bundle.getString(KEY_MESSAGE));
        al.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onNegativeClick(dialogInterface);
            }
        });

        al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onPositiveClick(dialogInterface);
            }
        });

        return al.create();
    }
}
