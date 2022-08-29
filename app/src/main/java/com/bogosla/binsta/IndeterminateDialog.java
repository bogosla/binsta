package com.bogosla.binsta;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class IndeterminateDialog extends DialogFragment {
    public IndeterminateDialog() {}

    public static IndeterminateDialog newInstance(String title, String msg) {
        IndeterminateDialog fr = new IndeterminateDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("msg", msg);
        fr.setArguments(args);
        return fr;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String msg = getArguments().getString("msg");
        ProgressDialog p = new ProgressDialog(getContext());
        p.setTitle(title);
        p.setMessage(msg);
        return p;
    }
}
