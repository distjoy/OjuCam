package com.tunieapps.ojucam.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import java.io.IOException;

public class CameraEngine  {

    private Camera camera;

    private boolean cameraOpened;

    public CameraEngine(){

    }

    public void open()  {
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
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
        camera.startPreview();
    }

    public void close(){
        camera.release();
         cameraOpened = false;
    }


}
