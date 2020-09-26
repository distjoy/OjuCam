package com.tunieapps.ojucam.gl.program;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.tunieapps.Constants;
import com.tunieapps.ojucam.util.StringUtil;
import com.tunieapps.ojucam.util.GLUtil;

import java.nio.FloatBuffer;

abstract class Program {
    private int mProgramId;
    private String mVertexShader;
    private String mFragmentShader;
    private int maPositionHandle;
    private int maTextureCoordinateHandle;

    public Program(Context context, int vertexShaderRes, int fragmentShaderRes){
        mVertexShader = StringUtil.stringFromResource(context,vertexShaderRes);
        mFragmentShader= StringUtil.stringFromResource(context,fragmentShaderRes);

    }
    public Program(Context context, String vertexShaderPath, String fragmentShaderPath){
        mVertexShader = StringUtil.stringFromAssetPath(context,vertexShaderPath);
        mFragmentShader= StringUtil.stringFromAssetPath(context,fragmentShaderPath);

    }
    public void create(){
        mProgramId = GLUtil.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgramId, "aPosition");
        GLUtil.checkError("glGetAttribLocation aPosition");
        if (maPositionHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }
        maTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramId, "aTextureCoord");
        GLUtil.checkError("glGetAttribLocation aTextureCoord");
        if (maTextureCoordinateHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aTextureCoord");
        }
    }

    abstract void uploadTexture(int textureId, int textureUnitIndex);

    public void setUniform1i(final int programId, final String name, final int intValue) {
        int location=GLES20.glGetUniformLocation(programId,name);
        GLES20.glUniform1i(location,intValue);
    }

    public void setUniform1f(final int programId, final String name, final float floatValue) {
        int location=GLES20.glGetUniformLocation(programId,name);
        GLES20.glUniform1f(location,floatValue);
    }

    public void setUniform2fv(final int programId, final String name,final float[] arrayValue) {
        int location=GLES20.glGetUniformLocation(programId,name);
        GLES20.glUniform2fv(location, 1, FloatBuffer.wrap(arrayValue));
    }

    public void use(){
        GLES20.glUseProgram(mProgramId);
    }


    public int getId() {
        return mProgramId;
    }

    public int getMaPositionHandle() {
        return maPositionHandle;
    }

    protected  abstract int getTextureHandle();

    public int getMaTextureCoordinateHandle() {
        return maTextureCoordinateHandle;
    }

    public void delete(){
        GLES20.glDeleteProgram(mProgramId);
    }
}
