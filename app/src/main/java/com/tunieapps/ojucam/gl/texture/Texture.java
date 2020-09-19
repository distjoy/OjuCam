package com.tunieapps.ojucam.gl.texture;

import android.opengl.GLES20;

import com.tunieapps.Constants;
import com.tunieapps.ojucam.util.GLUtil;

public abstract class Texture {
    private int textureId = Constants.NO_TEXTURE;

    abstract void setParams();

    public void generate(){
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        textureId = textures[0];
        if (textureId == 0) {
            GLUtil.checkError("glBindTexture mTextureID");
            textureId = 0;
        }
    }

    abstract boolean activate(int activeTextureID);

    public int getId(){
     return textureId;
    }

    public void deleteTexture(){
        int[] textures = new int[1];
        textures[0]=textureId;
        GLES20.glDeleteTextures(1,textures,0);
    }

}
