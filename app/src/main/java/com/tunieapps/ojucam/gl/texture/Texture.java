package com.tunieapps.ojucam.gl.texture;

import android.opengl.GLES20;

import com.tunieapps.Constants;
import com.tunieapps.ojucam.util.GLUtil;

public abstract class Texture {
    protected final int textureUnit;
    protected final int textureUnitIndex;
    private int textureId = Constants.NO_TEXTURE;

    public Texture(int textureUnitIndex){
        this.textureUnitIndex = textureUnitIndex;
        this.textureUnit = GLUtil.ActiveTextureUnits[textureUnitIndex];
    }

    protected abstract void setParams();

    public void generate(){
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        textureId = textures[0];
        if (textureId == Constants.NO_TEXTURE) {
            GLUtil.checkError("glBindTexture mTextureID");
            textureId = 0;
        }else{
            //set parameters
            bind();
            setParams();
        }
    }

    public void upload(int textureHandle){
        if (textureId != Constants.NO_TEXTURE) {
            GLES20.glActiveTexture(textureUnit);
            bind();
            GLES20.glUniform1i(textureHandle, textureUnitIndex);
        }
    }
    protected abstract void bind();

    public int getId(){
     return textureId;
    }

    public void delete(){
        int[] textures = new int[1];
        textures[0]=textureId;
        GLES20.glDeleteTextures(1,textures,0);
    }

}
