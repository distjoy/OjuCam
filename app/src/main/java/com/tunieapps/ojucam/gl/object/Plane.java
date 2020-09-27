package com.tunieapps.ojucam.gl.object;

import android.opengl.GLES20;

import com.tunieapps.ojucam.util.GLUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Plane {
    private FloatBuffer mVerticesBuffer;
    private FloatBuffer mTexCoordinateBuffer;
    private static final int BYTES_PER_FLOAT = 4;
    private final float[] trianglesData = {
            -1.0f, 1.0f, 0f,
            1.0f, 1.0f, 0f,
            -1.0f, -1.0f, 0f,
            1.0f, -1.0f, 0f
    };

    public static final float[] textureDataFlippedAntiClock90 = {
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 0.0f,
        1.0f, 1.0f
    };

    public Plane (){
        mVerticesBuffer = ByteBuffer
                .allocateDirect(trianglesData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(trianglesData);
        mVerticesBuffer .position(0);

        mTexCoordinateBuffer =  ByteBuffer
                .allocateDirect(textureDataFlippedAntiClock90.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureDataFlippedAntiClock90);
        mTexCoordinateBuffer.position(0);
    }
    public void uploadVerticesBuffer(int positionHandle){
        FloatBuffer vertexBuffer = getVerticesBuffer();
        if (vertexBuffer == null) return;
        vertexBuffer.position(0);

        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLUtil.checkError("glVertexAttribPointer maPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLUtil.checkError("glEnableVertexAttribArray maPositionHandle");
    }

    public void uploadTexCoordinateBuffer(int textureCoordinateHandle){
        FloatBuffer textureBuffer = getTexCoordinateBuffer();
        if (textureBuffer == null) return;
        textureBuffer.position(0);

        GLES20.glVertexAttribPointer(textureCoordinateHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
        GLUtil.checkError("glVertexAttribPointer maTextureHandle");
        GLES20.glEnableVertexAttribArray(textureCoordinateHandle);
        GLUtil.checkError("glEnableVertexAttribArray maTextureHandle");
    }

    public FloatBuffer getVerticesBuffer() {
        return mVerticesBuffer;
    }

    public FloatBuffer getTexCoordinateBuffer() {
        return mTexCoordinateBuffer;
    }

    public void setTexCoordinateBuffer(FloatBuffer mTexCoordinateBuffer) {
        this.mTexCoordinateBuffer = mTexCoordinateBuffer;
    }

    public void setVerticesBuffer(FloatBuffer mVerticesBuffer) {
        this.mVerticesBuffer = mVerticesBuffer;
    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
