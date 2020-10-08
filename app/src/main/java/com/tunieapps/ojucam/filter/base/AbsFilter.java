package com.tunieapps.ojucam.filter.base;

import android.content.Context;
import android.opengl.GLES20;

import com.tunieapps.ojucam.model.Constants;
import com.tunieapps.ojucam.gl.FrameBuffer;
import com.tunieapps.ojucam.Task;
import com.tunieapps.ojucam.object.Plane;

import java.util.LinkedList;

public abstract class AbsFilter {


    protected int width, height;
    protected Plane plane;
    private Context context;
    private final LinkedList<Task> mPreDrawTaskList;
    private final LinkedList<Task> mPostDrawTaskList;

    public AbsFilter(Context context){
        this.context = context;
        plane = new Plane();
        mPreDrawTaskList = new LinkedList<>();
        mPostDrawTaskList = new LinkedList<>();
    }

    public abstract void init();

    void setViewport(){
        GLES20.glViewport(0, 0, width, height);
    }
    public void onSizeChanged(int surfaceWidth, int surfaceHeight){
        this.width =surfaceWidth;
        this.height =surfaceHeight;
    }


    public void onPreDraw(){
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1f);
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        runPreDrawTasks();
    }
    abstract public void onDrawFrame();

    public void onPostDraw(){
        runPostDrawTasks();
    }
    private void runPreDrawTasks() {
        while (!mPreDrawTaskList.isEmpty()) {
            mPreDrawTaskList.removeFirst().run();
        }
    }

    public void addPreDrawTask(final Task task) {
        synchronized (mPreDrawTaskList) {
            mPreDrawTaskList.addLast(task);
        }
    }

    private void runPostDrawTasks() {
        while (!mPostDrawTaskList.isEmpty()) {
            mPostDrawTaskList.removeFirst().run();
        }
    }

    public void addPostDrawTask(final Task task) {
        synchronized (mPreDrawTaskList) {
            mPostDrawTaskList.addLast(task);
        }
    }
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

    public  int getTexture(){return Constants.NO_TEXTURE;}

    protected abstract FrameBuffer getBuffer();

}