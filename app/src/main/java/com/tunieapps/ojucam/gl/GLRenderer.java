package com.tunieapps.ojucam.gl;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.SurfaceHolder;

import com.tunieapps.ojucam.camera.CameraEngine;
import com.tunieapps.ojucam.gl.filter.FilterGroup;
import com.tunieapps.ojucam.gl.filter.OESFilter;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer {

    private CameraEngine cameraEngine;
    private int surfaceWidth;
    private int surfaceHeight;
    private SurfaceTexture mSurfaceTexture;
    private RenderingEventsListener renderingEventsListener;
    private FilterGroup renderFilter;
    private OESFilter oesFilter;
    private Context context;

    public GLRenderer(CameraEngine cameraEngine, Context context){
        this.cameraEngine = cameraEngine;
        this.context = context;
        renderFilter = new FilterGroup(context);
    }

    private void initTexture() {
        mSurfaceTexture = new SurfaceTexture(oesFilter.getTexture());
        mSurfaceTexture.setOnFrameAvailableListener(surfaceTexture -> {
            if(renderingEventsListener!=null)
                renderingEventsListener.updateUi();
        });
    }

    public void onPause(){
        if(cameraEngine.isCameraOpened()){
            cameraEngine.stopPreview();
            cameraEngine.close();
        }
    }

    public void onResume() {
    }

    public void onDestroy(){
        if(cameraEngine.isCameraOpened()){
            cameraEngine.close();
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        oesFilter = new OESFilter(context);
        renderFilter.addFilter(oesFilter);
        renderFilter.init();
        initTexture();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.surfaceWidth=width;
        this.surfaceHeight=height;
      //  GLES20.glViewport(0,0,width,height);
        renderFilter.onSizeChanged(width,height);
        if(cameraEngine.isCameraOpened()){
            cameraEngine.stopPreview();
            cameraEngine.close();
        }
        cameraEngine.open();
        if(cameraEngine.isCameraOpened())
            cameraEngine.startPreview(mSurfaceTexture);
    }



    @Override
    public void onDrawFrame(GL10 gl) {
        getSTMatrix(oesFilter.getSTMatrix());
        renderFilter.onDrawFrame();

    }

    private long getSTMatrix(float[] container){
        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(container);
        return mSurfaceTexture.getTimestamp();
    }

    public void setRenderingEventsListener(RenderingEventsListener renderingEventsListener) {
        this.renderingEventsListener = renderingEventsListener;
    }
    public  interface RenderingEventsListener { void updateUi();}
    private Camera.PreviewCallback cameraCallBack = (data, camera) -> {

    };


}
