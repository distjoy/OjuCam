package com.tunieapps.ojucam.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

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

    public static Bitmap saveBitmapFromBuffer(ByteBuffer buffer, int width, int height) {
        long start = System.nanoTime();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        buffer.rewind();
        bmp.copyPixelsFromBuffer(buffer);
        File FILES_DIR = Environment.getExternalStorageDirectory();
        File outputFile = new File(FILES_DIR,
                String.format("Frame %s", 564));
        try {

            BufferedOutputStream bos  = null;
            bos = new BufferedOutputStream(new FileOutputStream(outputFile));
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bmp.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        Timber.i( "createBitmap time: " + (end - start)/1000000+" ms filename: "+ outputFile.getAbsolutePath());
        return bmp;
    }
    public static Bitmap bitmapFromBuffer(ByteBuffer buffer, int width, int height) {
        long start = System.nanoTime();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        buffer.rewind();
        bmp.copyPixelsFromBuffer(buffer);
        long end = System.nanoTime();
        Timber.i( "createBitmap time: " + (end - start)/1000000+" ms");
        return bmp;
    }

}
