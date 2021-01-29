package com.tunieapps.ojucam.filter.simple2d;
import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.tunieapps.ojucam.filter.common.Texture;
import com.tunieapps.ojucam.util.BitmapUtil;
import com.tunieapps.ojucam.util.GLUtil;

public class Texture2D extends Texture {

    Bitmap bitmap;
    private String assetPath;
    private final Context context;

    public Texture2D(int index, Context context) {
        super(index);
        this.context = context;
    }

    @Override
    protected void setParams() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,getId());
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
 /*       GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);*/
    }

    @Override
    protected void bind() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getId());
    }


    //might need to bind texture again before loading into memory
    //load to memory in onPredraw
    public void loadToMemory(){
        if(bitmap!=null) {
            bind();
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            GLUtil.checkError("GLUtils.texImage2D");
            bitmap.recycle();
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }
    }

    private void loadBitmap() {
        //assume path is an asset path
        bitmap = BitmapUtil.bitmapFromAssetPath(context,assetPath);
    }
    public void setAssetPath(String assetPath) {
        this.assetPath = assetPath;
        loadBitmap();
    }
}
