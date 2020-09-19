package com.tunieapps.ojucam.gl.texture;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.tunieapps.Constants;
import com.tunieapps.ojucam.util.GLUtil;

public class Texture2D extends Texture {

    @Override
    void setParams() {
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
    public boolean activate(int activeTextureID) {
        if (getId() != Constants.NO_TEXTURE) {
            GLES20.glActiveTexture(activeTextureID);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getId());
            return true;
        }
        return false;
    }


    //might need to bind texture again before loading into memory
    public void loadToMemory(Bitmap bitmap){
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);
        bitmap.recycle();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
    }

}
