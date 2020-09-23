package com.tunieapps.ojucam.gl.program;

import android.content.Context;
import android.opengl.GLES20;

import com.tunieapps.ojucam.util.StringUtil;
import com.tunieapps.ojucam.util.GLUtil;

import java.nio.FloatBuffer;

abstract class Program {
    private int mProgramId;
    private String mVertexShader;
    private String mFragmentShader;


    public Program(Context context, int vertexShaderRes, int fragmentShaderRes){
        mVertexShader = StringUtil.stringFromResource(context,vertexShaderRes);
        mFragmentShader= StringUtil.stringFromResource(context,fragmentShaderRes);
        create();
    }
    public Program(Context context, String vertexShaderPath, String fragmentShaderPath){
        mVertexShader = StringUtil.stringFromAssetPath(context,vertexShaderPath);
        mFragmentShader= StringUtil.stringFromAssetPath(context,fragmentShaderPath);
        create();
    }
    private void create(){
        mProgramId = GLUtil.createProgram(mVertexShader, mFragmentShader);
    }

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
}
