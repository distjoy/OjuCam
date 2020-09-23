package com.tunieapps.ojucam.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.tunieapps.ojucam.gl.GLRenderer;
import com.tunieapps.ojucam.camera.CameraEngine;
import com.tunieapps.ojucam.databinding.ActivityCameraBinding;

public class CameraActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private ActivityCameraBinding binding;
    private GLRenderer.RenderingEventsListener renderingEventsListener = () -> {
        binding.surfaceView.requestRender();
    };
    private GLRenderer renderer;
    private CameraEngine cameraEngine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cameraEngine = new CameraEngine();
        renderer = new GLRenderer(cameraEngine,this);
        renderer.setRenderingEventsListener(renderingEventsListener);
        setupCameraView();
    }

    private void setupCameraView() {
        binding.surfaceView.setEGLContextClientVersion(2);
        binding.surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        binding.surfaceView.setRenderer(renderer);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
