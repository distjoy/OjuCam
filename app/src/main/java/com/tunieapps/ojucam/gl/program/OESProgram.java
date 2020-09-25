package com.tunieapps.ojucam.gl.program;

import android.content.Context;
import android.opengl.GLES20;

import com.tunieapps.ojucam.util.GLUtil;

public class OESProgram extends Program {

    private int sTextureHandle;
    private int stMatHandle;

    public OESProgram(Context context) {
        super(context, "filter/vertex_shader/oes.glsl", "filter/fragment_shader/oes.glsl");
    }

    @Override
    public void create() {
        super.create();
        stMatHandle = GLES20.glGetUniformLocation(getId(), "uSTMatrix");
        GLUtil.checkError("glGetUniformLocation uSTMatrix");
        if (stMatHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uSTMatrix");
        }
        sTextureHandle = GLES20.glGetUniformLocation(getId(),"sTexture");
        GLUtil.checkError("glGetUniformLocation uniform samplerExternalOES sTexture");
    }

    public void setStMatValue(float[] sTMatrix){
        GLES20.glUniformMatrix4fv(stMatHandle, 1, false, sTMatrix, 0);
    }

    public int getsTextureHandle() {
        return sTextureHandle;
    }

}
