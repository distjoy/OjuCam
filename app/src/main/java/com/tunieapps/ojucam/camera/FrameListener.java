package com.tunieapps.ojucam.camera;

public interface FrameListener {
    void previewFrame(byte[] data, int width, int height);
}
