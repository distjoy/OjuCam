package com.tunieapps.ojucam.gl.texture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import com.tunieapps.Constants;

public class TextureOES extends Texture {

    @Override
    void setParams() {
        if(getId()!=0){
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, getId());
            GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_LINEAR);
        }
    }

    @Override
    public boolean activate(int activeTextureID) {
        if (getId() != Constants.NO_TEXTURE) {
            GLES20.glActiveTexture(activeTextureID);
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, getId());
            return true;
        }
        return false;
    }



}
