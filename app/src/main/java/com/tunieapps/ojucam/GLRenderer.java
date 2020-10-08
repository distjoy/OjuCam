package com.tunieapps.ojucam;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.tunieapps.ojucam.camera.CameraEngine;
import com.tunieapps.ojucam.camera.FrameListener;
import com.tunieapps.ojucam.filter.base.ArFilterGroup;
import com.tunieapps.ojucam.filter.base.FilterGroup;
import com.tunieapps.ojucam.filter.base.OESFilter;
import com.tunieapps.ojucam.util.GLUtil;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer , OnSuccessListener<List<Face>> {

    private CameraEngine cameraEngine;
    private int surfaceWidth;
    private int surfaceHeight;
    private SurfaceTexture mSurfaceTexture;
    private RenderingEventsListener renderingEventsListener;
    private FilterGroup renderFilter;
    private ArFilterGroup arFilterGroup;
    private OESFilter oesFilter;
    private Context context;

    final Object mFaceDetectorLock = new Object();
    FaceDetector mFaceDetector;
    List<Face> latestFaces =  new ArrayList<>();
    int mFaceCount = 0;
    PointF[][] mFaceDetectResultLst;

    public GLRenderer(CameraEngine cameraEngine, Context context){
        this.cameraEngine = cameraEngine;
        this.context = context;
        renderFilter = new FilterGroup(context);
        arFilterGroup = new ArFilterGroup(context);
        cameraEngine.setFrameUpdateListener(cameraFrameListener);

    }

    public void initFaceDetector() {
        synchronized (mFaceDetectorLock) {
            if (null != mFaceDetector) {
                return;
            }
            FaceDetectorOptions realTimeOpts =
                    new FaceDetectorOptions.Builder()
                            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                            .build();
            mFaceDetector = FaceDetection.getClient(realTimeOpts);
        }
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
        runFaceDetect();
        synchronized (mFaceDetectorLock) {
            getFaceDetectResult();
        }
        arFilterGroup.onDetectFace(mFaceDetectResultLst);
        arFilterGroup.onDrawFrame();
    }

    private long getSTMatrix(float[] container){
        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(container);
        return mSurfaceTexture.getTimestamp();
    }

    public void setRenderingEventsListener(RenderingEventsListener renderingEventsListener) {
        this.renderingEventsListener = renderingEventsListener;
    }
    public int getFaceDetectResult() {
        int faceCount = 0;
        return faceCount;
    }
    @Override
    public void onSuccess(List<Face> faces) {
        // Task completed successfully
        latestFaces = faces;
        if (null != renderingEventsListener) {
            renderingEventsListener.updateUi();
        }
    }
    void runFaceDetect() {
        //save rendered frame
        //run detection on rendered frame
        ByteBuffer buffer = GLUtil.getFrameBuffer(surfaceWidth,surfaceHeight,context);
        synchronized (mFaceDetectorLock) {
            if (null != mFaceDetector) {
                InputImage image = InputImage.fromByteBuffer(
                        buffer,
                        surfaceWidth,
                        surfaceHeight,
                        0,
                        InputImage.IMAGE_FORMAT_YV12 // or IMAGE_FORMAT_YV12
                );
                mFaceDetector.process(image).addOnSuccessListener(this);
            }
        }
    }
    public  interface RenderingEventsListener { void updateUi();}
    private FrameListener cameraFrameListener = () -> {

    };


}
