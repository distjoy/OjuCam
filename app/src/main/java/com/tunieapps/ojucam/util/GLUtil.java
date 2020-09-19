package com.tunieapps.ojucam.util;

import android.opengl.GLES20;
import android.util.Log;

import timber.log.Timber;

public class GLUtil {


    public static int createProgram(String vertexSource, String fragmentSource){
        int vertexShader = compileShader(vertexSource, GLES20.GL_VERTEX_SHADER);
        if(vertexShader==0) return 0;
        int fragmentShader = compileShader(fragmentSource,GLES20.GL_FRAGMENT_SHADER);
        if(fragmentShader==0) return 0;
        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vertexShader);
            checkError("glAttachShader");
            GLES20.glAttachShader(program, fragmentShader);
            checkError("glAttachShader");
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Timber.e( "Could not link program: ");
                Timber.e( GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }


    public static int compileShader(String source, int type){
        int shader = GLES20.glCreateShader(type);
        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            if (compileStatus[0] == 0) {
                Timber.e("Could not compile shader " + type + ":");
                Timber.e (GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    public static void checkError(String label) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) { throw new RuntimeException(label + ": glError " + error );}
    }
}
