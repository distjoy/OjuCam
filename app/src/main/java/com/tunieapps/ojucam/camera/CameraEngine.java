package com.tunieapps.ojucam.camera;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraEngine implements Camera.PreviewCallback {

    private Camera camera;
    private Camera.Parameters mParams;
    private boolean cameraOpened;
    private FrameListener frameUpdateListener;

    private byte[] mBuffer;

    //frameWidth=size.height
    Camera.Size previewSize;
    int bufferSize;
    private static final double preferredRatio=16.0/9;
    private int frameWidth;
    private int frameHeight;
    public CameraEngine(){

    }

    public void open()  {
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            mParams = camera.getParameters();
            List<Camera.Size> supportedPictureSizesList=mParams.getSupportedPictureSizes();
            List<Camera.Size> supportedVideoSizesList=mParams.getSupportedVideoSizes();
            List<Camera.Size> supportedPreviewSizesList=mParams.getSupportedPreviewSizes();
          //  Logger.logCameraSizes(supportedPictureSizesList);
           // Logger.logCameraSizes(supportedVideoSizesList);
            //Logger.logCameraSizes(supportedPreviewSizesList);

            previewSize=choosePreferredSize(supportedPreviewSizesList,preferredRatio);
            Camera.Size photoSize=choosePreferredSize(supportedPictureSizesList,preferredRatio);

            frameHeight=previewSize.width;
            frameWidth=previewSize.height;
         //   Log.d(TAG, "openCamera: choose preview size"+previewSize.height+"x"+previewSize.width);
            mParams.setPreviewSize(frameHeight,frameWidth);

            mParams.setPictureSize(photoSize.width,photoSize.height);
         //   Log.d(TAG, "openCamera: choose photo size"+photoSize.height+"x"+photoSize.width);

            //mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            bufferSize = frameWidth*frameHeight;
            bufferSize = bufferSize * ImageFormat.getBitsPerPixel(mParams.getPreviewFormat()) / 8;
            if (mBuffer==null || mBuffer.length!=bufferSize)
                mBuffer = new byte[bufferSize];
           // mFrameChain[0].init(size);
           // mFrameChain[1].init(size);
            camera.addCallbackBuffer(mBuffer);
            camera.setParameters(mParams);
            cameraOpened=true;
            camera.setPreviewCallback(this);
            cameraOpened = true;
        }catch (Exception exc){
            exc.printStackTrace();
            cameraOpened = false;
        }

    }
    public boolean isCameraOpened() {
        return cameraOpened;
    }
    public void startPreview(SurfaceTexture surfaceTexture){
       try {
            camera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    private static Camera.Size choosePreferredSize(List<Camera.Size> sizes, double aspectRatio) {
        List<Camera.Size> options = new ArrayList<>();
        for (Camera.Size option : sizes) {
            if(option.width==1280 && option.height==720)
                return option;
            if (Math.abs((int)(option.height * aspectRatio)-option.width)<10) {
                options.add(option);
            }
        }
        if (options.size() > 0) {
            return Collections.max(options, new CompareSizesByArea());
        } else {
            return sizes.get(sizes.size()-1);
        }
    }
    static class CompareSizesByArea implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.width * lhs.height -
                    (long) rhs.width * rhs.height);
        }
    }

    public void stopPreview(){
        camera.stopPreview();
    }

    public void close(){
        camera.release();
        camera.setPreviewCallback(null);
         cameraOpened = false;
    }
    public void setFrameUpdateListener(FrameListener frameUpdateListener) {
        this.frameUpdateListener = frameUpdateListener;
    }


    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        frameUpdateListener.previewFrame(data,bufferSize,camera.getParameters().getPreviewSize().width,
                camera.getParameters().getPreviewSize().height);
    }





}
