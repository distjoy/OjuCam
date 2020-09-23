package com.tunieapps.ojucam.gl.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.tunieapps.ojucam.gl.object.Plane;

public abstract class AbsFilter {


    protected int width, height;
    protected Plane plane;
    private Context context;


    public AbsFilter(Context context){
        this.context = context;
    }

    public abstract void init();

    void setViewport(){
        GLES20.glViewport(0, 0, width, height);
    }
    public void onSizeChanged(int surfaceWidth, int surfaceHeight){
        this.width =surfaceWidth;
        this.height =surfaceHeight;
    }
    abstract public void onDrawFrame(final int textureId);
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Plane getPlane() {
        return plane;
    }

    public Context getContext(){
        return context;
    }
    abstract public void destroy();
}
