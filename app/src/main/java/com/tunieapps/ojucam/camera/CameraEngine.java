package com.tunieapps.ojucam.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import java.io.IOException;

public class CameraEngine implements Camera.PreviewCallback {

    private Camera camera;

    private boolean cameraOpened;
    private FrameListener frameUpdateListener;

    public CameraEngine(){

    }

    public void open()  {
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
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

    }




}
