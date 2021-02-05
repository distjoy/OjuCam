package com.tunieapps.ojucam.filter.simple2d;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.tunieapps.ojucam.filter.common.Texture;
import com.tunieapps.ojucam.util.BitmapUtil;
import com.tunieapps.ojucam.util.GLUtil;

import java.nio.ByteBuffer;

public class Texture2D extends Texture {

    private static final String TAG = "Texture2D";
    private final Context context;
    Bitmap bitmap;
    private String assetPath;
    private boolean isLoaded = false;

    public Texture2D(int index, Context context) {
        super(index);
        this.context = context;
    }

    @Override
    protected void setParams() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getId());
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
    }

    @Override
    protected void bind() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getId());
    }


    public void loadToMemory() {
        if (bitmap != null) {
            bind();
            if (!isLoaded)
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            else
                GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, bitmap);
            GLUtil.checkError("GLUtils.texImage2D");
            bitmap.recycle();
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            isLoaded = true;
        }
    }


    public void loadToMemory(ByteBuffer byteBuffer, int width, int height) {
        bind();
        Log.d(TAG, "loadToMemory() called with: byteBuffer = [" + byteBuffer + "], width = [" + width + "], height = [" + height + "]");

        if (isLoaded)
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0,
                    width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, byteBuffer);
        else
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height,
                    0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, byteBuffer);

        GLUtil.checkError("GLUtils.texImage2D");
        isLoaded = true;
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);


    }

    private void loadBitmap() {
        //assume path is an asset path
        bitmap = BitmapUtil.bitmapFromAssetPath(context, assetPath);
    }

    public void setAssetPath(String assetPath) {
        this.assetPath = assetPath;
        loadBitmap();
    }

    public void setBitmap(Bitmap saveBitmapFromBuffer) {
        bitmap = saveBitmapFromBuffer;
    }
}
