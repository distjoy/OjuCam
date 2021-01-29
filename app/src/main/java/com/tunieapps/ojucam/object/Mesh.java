package com.tunieapps.ojucam.object;

import android.opengl.GLES20;
import android.util.Log;

import com.tunieapps.ojucam.util.GLUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import timber.log.Timber;

public class Mesh {
    private static final String TAG = "Mesh";
    private FloatBuffer mVerticesBuffer;
    private FloatBuffer makeUpTexCoordinateBuffer;
    private FloatBuffer mainTexCoordinateBuffer;
    private static final int BYTES_PER_FLOAT = 4;
    private int vertices;

    public Mesh (int vertices){
        this.vertices = vertices;
    }
    public void uploadVerticesBuffer(int positionHandle){
        Log.d(TAG, "uploadVerticesBuffer() called with: positionHandle = [" + positionHandle + "]");
        if (mVerticesBuffer == null||positionHandle==-1) return;
        mVerticesBuffer.position(0);

        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, mVerticesBuffer);
        GLUtil.checkError("glVertexAttribPointer maPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLUtil.checkError("glEnableVertexAttribArray maPositionHandle");
    }

    public void uploadMakeUpTexCoordinateBuffer(int textureCoordinateHandle){
        Timber.d("uploadMakeUpTexCoordinateBuffer()");
        if (makeUpTexCoordinateBuffer == null||textureCoordinateHandle==-1) return;
        makeUpTexCoordinateBuffer.position(0);
        Timber.d("uploadMakeUpTexCoordinateBuffer() called with: makeUpTexCoordinateBuffer = [" + makeUpTexCoordinateBuffer.hasArray() + "]");
        GLES20.glVertexAttribPointer(textureCoordinateHandle, 2, GLES20.GL_FLOAT, false, 0, makeUpTexCoordinateBuffer);
        GLUtil.checkError("glVertexAttribPointer uploading texture coordinate");
        GLES20.glEnableVertexAttribArray(textureCoordinateHandle);
        GLUtil.checkError("glEnableVertexAttribArray enabling texture coordinate");
    }

    public void uploadMainTexCoordinateBuffer(int textureCoordinateHandle){
        Log.d(TAG, "uploadMainTexCoordinateBuffer() called with: textureCoordinateHandle = [" + textureCoordinateHandle + "]");
        if (mainTexCoordinateBuffer == null||textureCoordinateHandle==-1) return;
        mainTexCoordinateBuffer.position(0);

        GLES20.glVertexAttribPointer(textureCoordinateHandle, 2, GLES20.GL_FLOAT, false, 0, mainTexCoordinateBuffer);
        GLUtil.checkError("glVertexAttribPointer uploading main face texture coordinate");
        GLES20.glEnableVertexAttribArray(textureCoordinateHandle);
        GLUtil.checkError("glEnableVertexAttribArray enabling main face texture coordinate");
    }





    public void setMakeUpTexCoordinateBuffer(float[] textCoordinate) {
        makeUpTexCoordinateBuffer.position(0);
        for( float f : textCoordinate)
            makeUpTexCoordinateBuffer  .put(f);
        makeUpTexCoordinateBuffer.position(0);
    }

    public void setMainTexCoordinateBuffer(float[] textCoordinate) {
        mainTexCoordinateBuffer  .put(textCoordinate);
        mainTexCoordinateBuffer.position(0);
    }

    public void setVerticesBuffer(float[] vertices) {
        mVerticesBuffer.position(0);
        for( float f : vertices)
            mVerticesBuffer  .put(f);
        mVerticesBuffer.position(0);
    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices);
        GLUtil.checkError("Drawing mesh");
    }

    public void initBuffer(int size) {
        mVerticesBuffer = ByteBuffer
                .allocateDirect(size * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        makeUpTexCoordinateBuffer =  ByteBuffer
                .allocateDirect(size* BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mainTexCoordinateBuffer =  ByteBuffer
                .allocateDirect(size* BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
    }
}
