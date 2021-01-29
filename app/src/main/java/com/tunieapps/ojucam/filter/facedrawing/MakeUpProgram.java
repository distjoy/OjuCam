package com.tunieapps.ojucam.filter.facedrawing;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.util.Log;

import com.tunieapps.ojucam.model.Constants;
import com.tunieapps.ojucam.filter.common.Program;
import com.tunieapps.ojucam.util.GLUtil;

import timber.log.Timber;

import static com.tunieapps.ojucam.util.GLUtil.checkError;

public class MakeUpProgram extends Program {

    private static final String TAG = "MakeUpProgram";

    private int mainTextureLoc;
    private int makeUpTextureLoc;
    private int makeUpTextCoordLoc;
    private int stMatHandle;
    private int stage;
    public MakeUpProgram(Context context) {
        super(context, "filter/vertex_shader/make_up.glsl", "filter/fragment_shader/make_up.glsl");
    }

    @Override
    protected void create() {
        super.create();
        makeUpTextCoordLoc = GLES20.glGetAttribLocation(getId(), "aTextCoord2");
        checkError("glGetAttribLocation aTextCoord2");
        if (makeUpTextCoordLoc == -1) {
            //throw new RuntimeException("Could not get uniform location for aTextCoord2 program id "+getId());
        }
        mainTextureLoc = GLES20.glGetUniformLocation(getId(),"mainImageText");
        checkError("glGetUniformLocation uniform sampler2D mainImageText");
        if (mainTextureLoc == -1) {
           // throw new RuntimeException("Could not get uniform location for mainImageText");
        }
        makeUpTextureLoc = GLES20.glGetUniformLocation(getId(),"imageText2");
        checkError("glGetUniformLocation uniform sampler2D imageText2");
        if (makeUpTextureLoc == -1) {
           // throw new RuntimeException("Could not get uniform location for imageText2");
        }
        stMatHandle = GLES20.glGetUniformLocation(getId(), "uSTMatrix");
        checkError("glGetUniformLocation uSTMatrix");
        if (stMatHandle == -1) {
            //throw new RuntimeException("Could not get uniform location for uSTMatrix");
        }

        stage = GLES20.glGetUniformLocation(getId(), "stage");
        checkError("glGetUniformLocation stage");
        if (stage == -1) {
           // throw new RuntimeException("Could not get uniform location for stage");
        }

        Timber.d("Program  create() called makeUpTextureLoc =[ "+makeUpTextureLoc+"]");
    }

    @Override
    public void uploadTexture(int textureId, int textureUnitIndex) {
        if (textureId != Constants.NO_TEXTURE ||mainTextureLoc!=-1) {
            GLES20.glActiveTexture(GLUtil.ActiveTextureUnits[textureUnitIndex]);
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
            GLES20.glUniform1i(mainTextureLoc, textureUnitIndex);
            checkError("uploadMakeUpTexture");
        }
    }

    public void uploadMakeUpTexture(int textureId, int textureUnitIndex) {
        Timber.d("uploadMakeUpTexture() called with: textureId = [" + textureId + "], textureUnitIndex = [" + textureUnitIndex + "], makeUpTextureLoc = ["+ makeUpTextureLoc +"]");
        if (textureId != Constants.NO_TEXTURE||makeUpTextureLoc!=-1) {

            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLES20.glUniform1i(makeUpTextureLoc, 1);
            checkError("uploadMakeUpTexture");
        }
    }
    public void disableAttributes(){
        checkError("disableAttributes1");
        super.disableAttributes();
        checkError("disableAttributes2");
        if(makeUpTextCoordLoc==-1) return;
        GLES20.glDisableVertexAttribArray(makeUpTextCoordLoc);
        checkError("disableAttributes3");
    }

    @Override
    public void unbindTexture() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
    }

    public void setStMatValue(float[] sTMatrix){
        if(stMatHandle==-1)return;
        Log.d(TAG, "setStMatValue() called with: sTMatrix = [" + sTMatrix + "]");
        GLES20.glUniformMatrix4fv(stMatHandle, 1, false, sTMatrix, 0);
    }
    @Override
    protected int getTextureHandle() {
        return 0;
    }

    public int getMakeUpTextCoordLoc() {
        return makeUpTextCoordLoc;
    }

    public void setStage(int i) {
        setUniform1i("stage",i);
    }
}
