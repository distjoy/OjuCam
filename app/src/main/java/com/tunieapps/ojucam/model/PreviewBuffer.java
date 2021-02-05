package com.tunieapps.ojucam.model;

public class PreviewBuffer {
    private int width;
    private int height;
    private byte[] mFrame=null;

    public PreviewBuffer(int bufferSize, int width, int height) {
        this.width = width;
        this.height = height;
        init(bufferSize);
    }

    private void init(int size) {
        if(mFrame==null || mFrame.length!=size)
            mFrame=new byte[size];
    }

    public void putData(byte[] data){
        if(mFrame!=null)
           System.arraycopy(data, 0, mFrame, 0, data.length);
    }
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public byte[] getFrame() {
        return mFrame;
    }
}
