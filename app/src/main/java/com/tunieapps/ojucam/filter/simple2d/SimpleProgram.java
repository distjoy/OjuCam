package com.tunieapps.ojucam.filter.simple2d;

import android.content.Context;
import android.opengl.GLES20;

import com.tunieapps.ojucam.filter.common.Program;
import com.tunieapps.ojucam.model.Constants;
import com.tunieapps.ojucam.util.GLUtil;

import timber.log.Timber;

import static com.tunieapps.ojucam.util.GLUtil.checkError;

public class SimpleProgram extends Program {

    private int sTextureHandle;
    private int matrixHandle;

    public SimpleProgram(Context context) {
        super(context, "filter/vertex_shader/pass_through.glsl", "filter/fragment_shader/pass_through.glsl");
    }

    @Override
    protected void create() {
        super.create();
        matrixHandle = GLES20.glGetUniformLocation(getId(), "uMVPMatrix");
        GLUtil.checkError("glGetUniformLocation uMVPMatrix");
        if (matrixHandle == -1) {
            throw new RuntimeException("Could not get uniform location for uMVPMatrix");
        }
        sTextureHandle = GLES20.glGetUniformLocation(getId(),"mainImageText");
        GLUtil.checkError("glGetUniformLocation uniform samplerExternalOES mainImageText");
        if (sTextureHandle == -1) {
            throw new RuntimeException("Could not get uniform location for mainImageText");
        }
    }

    @Override
    public void uploadTexture(int textureId, int textureUnitIndex) {
        Timber.d("uploadMakeUpTexture() called with: textureId = [" + textureId + "], textureUnitIndex = [" + textureUnitIndex + "], makeUpTextureLoc = ["+ sTextureHandle +"]");
        if (textureId != Constants.NO_TEXTURE||sTextureHandle!=-1) {

            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLES20.glUniform1i(sTextureHandle, 1);
            checkError("uploadMakeUpTexture");
        }
    }

    @Override
    public void unbindTexture() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    protected int getTextureHandle() {
        return sTextureHandle;
    }
}
