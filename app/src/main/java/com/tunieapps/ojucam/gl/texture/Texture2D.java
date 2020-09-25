package com.tunieapps.ojucam.gl.texture;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.tunieapps.Constants;

public class Texture2D extends Texture {

    public Texture2D(int index) {
        super(index);
    }

    @Override
    protected void setParams() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,getId());
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


    //might need to bind texture again before loading into memory
    //load to memory in onPredraw
    public void loadToMemory(Bitmap bitmap){
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);
        bitmap.recycle();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
    }

}
