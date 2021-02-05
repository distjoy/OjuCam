package com.tunieapps.ojucam.camera;

public interface FrameListener {
    void previewFrame(byte[] data, int bufferSize, int width, int height);
}
