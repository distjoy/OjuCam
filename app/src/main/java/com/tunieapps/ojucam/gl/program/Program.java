package com.tunieapps.ojucam.gl.program;

import android.content.Context;
import android.opengl.GLES20;

import com.tunieapps.ojucam.util.StringUtil;
import com.tunieapps.ojucam.util.GLUtil;

abstract class Program {
    private int mProgramId;
    private String mVertexShader;
    private String mFragmentShader;


    public Program(Context context, int vertexShaderRes, int fragmentShaderRes){
        mVertexShader = StringUtil.stringFromResource(context,vertexShaderRes);
        mFragmentShader= StringUtil.stringFromResource(context,fragmentShaderRes);
        create();
    }

    private void create(){
        mProgramId = GLUtil.createProgram(mVertexShader, mFragmentShader);
    }

    public void use(){
        GLES20.glUseProgram(mProgramId);
    }
}
