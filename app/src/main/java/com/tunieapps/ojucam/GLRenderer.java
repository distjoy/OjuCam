package com.tunieapps.ojucam;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.lemon.faceu.sdk.utils.JniEntry;
import com.tunieapps.ojucam.camera.CameraEngine;
import com.tunieapps.ojucam.camera.FrameListener;
import com.tunieapps.ojucam.filter.base.ArFilterGroup;
import com.tunieapps.ojucam.filter.base.FilterGroup;
import com.tunieapps.ojucam.filter.facedrawing.MakeUpData;
import com.tunieapps.ojucam.filter.facedrawing.MakeUpFilter;
import com.tunieapps.ojucam.filter.oes.OESFilter;
import com.tunieapps.ojucam.filter.simple2d.SimpleFilter;
import com.tunieapps.ojucam.model.PreviewBuffer;
import com.tunieapps.ojucam.util.BitmapUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import timber.log.Timber;

import static com.tunieapps.ojucam.util.StringUtil.getString;
import static com.tunieapps.ojucam.util.StringUtil.stringFromAssetPath;

public class GLRenderer implements GLSurfaceView.Renderer , OnSuccessListener<List<Face>> {

    private static final String TAG = "GLRenderer";
    private CameraEngine cameraEngine;
    private int surfaceWidth;
    private int surfaceHeight;
    private int cameraWidth;
    private int cameraHeight;
    private SurfaceTexture mSurfaceTexture;
    private RenderingEventsListener renderingEventsListener;
    private FilterGroup renderFilter;
   private ArFilterGroup arFilterGroup;
    private OESFilter oesFilter;
    private SimpleFilter simpleFilter;
    private Context context;

    boolean isDetecting;
    final Object mFaceDetectorLock = new Object();
    FaceDetector mFaceDetector;
    List<Face> latestFaces =  new ArrayList<>();
    final Queue<PreviewBuffer> bufferQueue = new LinkedList<>();;
    int mFaceCount = 0;
    PointF[][] mFaceDetectResultLst;
     //ByteBuffer pixelBuffer;
    int width = 480;
    int height = 640;
    MakeUpFilter makeUpFilter;
    Gson gson = new Gson();
    MakeUpData makeUpData;

    ByteBuffer frameBuffer;
    public GLRenderer(CameraEngine cameraEngine, Context context){
        this.cameraEngine = cameraEngine;
        this.context = context;

        renderFilter = new FilterGroup(context);
        arFilterGroup = new ArFilterGroup(context);
        cameraEngine.setFrameUpdateListener(cameraFrameListener);
        initFaceDetector();

        makeUpData  = gson.fromJson(stringFromAssetPath(context,"makeup_filter.json"), MakeUpData.class);
   }

    public void initFaceDetector() {
        mFaceDetectResultLst = new PointF[1][133];
        for (int i = 0; i < mFaceDetectResultLst.length; ++i) {
            PointF[] pointFs = mFaceDetectResultLst[i];
            for (int j = 0; j < pointFs.length; ++j) {
                pointFs[j] = new PointF(0, 0);
            }
        }
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

    private void initSurfaceTexture() {
        mSurfaceTexture = new SurfaceTexture(oesFilter.getTextureId());
        mSurfaceTexture.setOnFrameAvailableListener(surfaceTexture -> {/*
            if(renderingEventsListener!=null)
                renderingEventsListener.updateUi();*/
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
        simpleFilter = new SimpleFilter(context);
        renderFilter.addFilter(simpleFilter);
        renderFilter.init();
        makeUpFilter = new MakeUpFilter(context,makeUpData);
        makeUpFilter.setTexture(simpleFilter.getTexture());
        arFilterGroup.addFilter(makeUpFilter);
        arFilterGroup.init();
        initSurfaceTexture();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
     //   eglGetCurrentDisplay()
        this.surfaceWidth=width;
        this.surfaceHeight=height;
     //   pixelBuffer  = ByteBuffer.allocate(surfaceWidth * surfaceHeight*4).order(ByteOrder.LITTLE_ENDIAN);
      //  GLES20.glViewport(0,0,width,height);
        renderFilter.onSizeChanged(width,height);
        arFilterGroup.onSizeChanged(width,height);
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
        Log.d(TAG, "onDrawFrame() called with: start");
        getSTMatrix(oesFilter.getSTMatrix());
        System.arraycopy(oesFilter.getSTMatrix(),0,simpleFilter.getMatrix(),0,oesFilter.getSTMatrix().length);
        makeUpFilter.setSTMatrix(oesFilter.getSTMatrix());

        synchronized (bufferQueue) {
            while (!bufferQueue.isEmpty()) {
                PreviewBuffer buffer = bufferQueue.poll();
                if (buffer != null) {
                    if (frameBuffer == null || frameBuffer.capacity() != cameraWidth * cameraHeight * 4) {
                        frameBuffer = ByteBuffer.allocate(cameraWidth * cameraHeight * 4);
                    }
                    JniEntry.YUVtoRBGA(buffer.getFrame(), cameraWidth, cameraHeight, frameBuffer.array());
                    simpleFilter.updateTextureContent(frameBuffer,cameraWidth,cameraHeight);
                    frameBuffer.clear();
                }
            }
        }
        renderFilter.onDrawFrame();
        synchronized (mFaceDetectorLock) {
           getFaceDetectResult();
        }
        arFilterGroup.onDetectFace(mFaceDetectResultLst);
        arFilterGroup.onDrawFrame();
        Log.d(TAG, "onDrawFrame() called with: end");
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
        if (null != latestFaces) {
            mFaceCount = latestFaces.size();
            for (int f = 0; f < mFaceCount; ++f) {
                int index = 0;
                for(FaceContour contour : latestFaces.get(f).getAllContours())
                    for(PointF pointF : contour.getPoints()) {
                        PointF pf = new PointF();
                        pf.x = ( (cameraHeight-pointF.x)/cameraHeight ) *surfaceWidth;
                      //  pf.x = pointF.x;
                        pf.y = (( pointF.y)/cameraWidth)*surfaceHeight;
                    //    pf.y = pointF.y;
                        mFaceDetectResultLst[f][index] = pf;
                        ++index;
                    }
            }
        }
      Timber.i(" mFaceDetectResultLst :%s with width: %s and height: %s", getString(mFaceDetectResultLst[0]), surfaceWidth,surfaceHeight);
        return faceCount;
    }
    @Override
    public void onSuccess(List<Face> faces) {
        Log.d(TAG, "onSuccess() called with: faces = [" + faces + "]");
        // Task completed successfully
        latestFaces = faces;
        if (null != renderingEventsListener) {
            renderingEventsListener.updateUi();
        }isDetecting = false;
    }
    void runFaceDetect(byte[] data) {
        synchronized (mFaceDetectorLock) {
            if (null != mFaceDetector) {
                Log.d(TAG, "runFaceDetect() called with: width = [" + cameraWidth + "] height +[" + cameraHeight +"]");
                InputImage image = InputImage.fromByteArray(
                        data,
                        cameraWidth,
                        cameraHeight,
                        270,
                        InputImage.IMAGE_FORMAT_YV12

                );
                mFaceDetector.process(image).addOnSuccessListener(this);
            }
        }
    }

/*    void runFaceDetect() {
        //save rendered frame
        //run detection on rendered frame
        ByteBuffer buffer = GLUtil.getFrameBuffer(surfaceWidth,surfaceHeight,pixelBuffer);
        synchronized (mFaceDetectorLock) {
            if (null != mFaceDetector) {
                InputImage image = InputImage.fromBitmap(
                        BitmapUtil.bitmapFromBuffer(buffer,surfaceWidth,surfaceHeight),
                        180
                );
                mFaceDetector.process(image).addOnSuccessListener(this);
                //BitmapUtil.bitmapFromBuffer(buffer,surfaceWidth,surfaceHeight,context);
            }
        }
    }*/

    public  interface RenderingEventsListener { void updateUi();}
    private FrameListener cameraFrameListener = (byte[] data, int bufferSize, int width, int height) -> {
        PreviewBuffer buffer = new PreviewBuffer(bufferSize,width,height);
        buffer.putData(data);

        synchronized (bufferQueue){
            if(bufferQueue.isEmpty()) {
                processFrame(buffer);
                bufferQueue.add(buffer);
            }
        }

    };

    private void processFrame(PreviewBuffer previewBuffer){
        cameraWidth = previewBuffer.getWidth();
        cameraHeight = previewBuffer.getHeight();
        if(!isDetecting) runFaceDetect(previewBuffer.getFrame());
    }



}
