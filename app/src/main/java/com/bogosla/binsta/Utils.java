package com.bogosla.binsta;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

public class Utils {
    public static  void takeImage(Activity ctx, int request_code) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ctx.startActivityForResult(i, request_code);
    }
}
