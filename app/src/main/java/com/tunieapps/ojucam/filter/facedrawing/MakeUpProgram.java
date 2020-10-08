package com.tunieapps.ojucam.filter.facedrawing;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.tunieapps.ojucam.model.Constants;
import com.tunieapps.ojucam.program.Program;
import com.tunieapps.ojucam.util.GLUtil;

public class MakeUpProgram extends Program {


    private int mainTextureLoc;
    private int makeUpTextureLoc;
    private int makeUpTextCoordLoc;

    public MakeUpProgram(Context context) {
        super(context, "filter/vertex_shader/make_up.glsl", "filter/fragment_shader/make_up.glsl");
    }

    @Override
    protected void create() {
        super.create();
        makeUpTextCoordLoc = GLES20.glGetAttribLocation(getId(), "aTextCoord2");
        GLUtil.checkError("glGetAttribLocation aTextCoord2");
       // mainTextureLoc = GLES20.glGetUniformLocation(getId(),"mainImageText");
       // GLUtil.checkError("glGetUniformLocation uniform sampler2D mainImageText");
        makeUpTextureLoc = GLES20.glGetUniformLocation(getId(),"imageText2");
        GLUtil.checkError("glGetUniformLocation uniform sampler2D imageText2");
    }

    @Override
    public void uploadTexture(int textureId, int textureUnitIndex) {
        if (textureId != Constants.NO_TEXTURE) {
            GLES20.glActiveTexture(GLUtil.ActiveTextureUnits[textureUnitIndex]);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getId());
            GLES20.glUniform1i(mainTextureLoc, textureUnitIndex);
        }
    }

    public void uploadMakeUpTexture(int textureId, int textureUnitIndex) {
        if (textureId != Constants.NO_TEXTURE) {
            GLES20.glActiveTexture(GLUtil.ActiveTextureUnits[textureUnitIndex]);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getId());
            GLES20.glUniform1i(makeUpTextureLoc, textureUnitIndex);
        }
    }

    @Override
    protected int getTextureHandle() {
        return 0;
    }

    public int getMakeUpTextCoordLoc() {
        return makeUpTextCoordLoc;
    }
}
