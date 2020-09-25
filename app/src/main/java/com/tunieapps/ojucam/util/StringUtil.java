package com.tunieapps.ojucam.util;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import timber.log.Timber;

public class StringUtil {

    /**
     * Reads in text from path to an asset file and returns a String containing the
     * text.
     */
    public static String stringFromAssetPath(Context context,
                                            String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getStringFromStream(inputStream);
    }

    /**
     * Reads in text from a resource file and returns a String containing the
     * text.
     */
    public static String stringFromResource(Context context,
                                                  int resourceId) {

        InputStream inputStream = null;
        try {
            inputStream = context.getResources()
                    .openRawResource(resourceId);
        }
        catch (Resources.NotFoundException nfe) {
            Timber.e(nfe);
        }

       return getStringFromStream(inputStream);
    }

    private static String getStringFromStream(InputStream inputStream){
        if(inputStream==null) return null;
        StringBuilder body = new StringBuilder();
        try{
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream);
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);

            String nextLine;

            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append('\n');
            }
        } catch (IOException e) {
            Timber.e(e);
        }
        return body.toString();
    }

}
