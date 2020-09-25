package com.tunieapps.ojucam.gl.texture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import com.tunieapps.Constants;

public class TextureOES extends Texture {

    public TextureOES(int index) {
        super(index);
    }

    @Override
    protected void setParams() {
            GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_LINEAR);
    }

    @Override
    protected void bind() {
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, getId());
    }



}
