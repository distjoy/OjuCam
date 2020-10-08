package com.tunieapps.ojucam.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

import timber.log.Timber;

public class BitmapUtil {

    public static @Nullable Bitmap bitmapFromAssetPath(Context context,
                                                       String filePath) {
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(filePath);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Timber.e(e);
        }
        return bitmap;
    }


}
