package com.tunieapps.ojucam.program;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.tunieapps.ojucam.model.Constants;
import com.tunieapps.ojucam.util.GLUtil;

public class OESProgram extends Program {

    private int sTextureHandle;
    private int stMatHandle;

    public OESProgram(Context context) {
        super(context, "filter/vertex_shader/oes.glsl", "filter/fragment_shader/oes.glsl");
    }

    @Override
    protected void create() {
        super.create();
        stMatHandle = GLES20.glGetUniformLocation(getId(), "uSTMatrix");
        GLUtil.checkError("glGetUniformLocation uSTMatrix");
        if (stMatHandle == -1) {
            throw new RuntimeException("Could not get uniform location for uSTMatrix");
        }
        sTextureHandle = GLES20.glGetUniformLocation(getId(),"sTexture");
        GLUtil.checkError("glGetUniformLocation uniform samplerExternalOES sTexture");
    }

    @Override
    public void uploadTexture(int textureId,int textureUnitIndex){
        if (textureId != Constants.NO_TEXTURE) {
            GLES20.glActiveTexture(GLUtil.ActiveTextureUnits[textureUnitIndex]);
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, getId());
            GLES20.glUniform1i(getTextureHandle(), textureUnitIndex);
        }
    }
    @Override
    protected int getTextureHandle() {
        return sTextureHandle;
    }

    public void setStMatValue(float[] sTMatrix){
        GLES20.glUniformMatrix4fv(stMatHandle, 1, false, sTMatrix, 0);
    }

    public int getsTextureHandle() {
        return sTextureHandle;
    }

}
