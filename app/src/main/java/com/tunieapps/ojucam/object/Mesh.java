package com.tunieapps.ojucam.object;

import android.opengl.GLES20;

import com.tunieapps.ojucam.util.GLUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Mesh {
    private FloatBuffer mVerticesBuffer;
    private FloatBuffer mTexCoordinateBuffer;
    private static final int BYTES_PER_FLOAT = 4;

    public Mesh (){ }
    public void uploadVerticesBuffer(int positionHandle){
        if (mVerticesBuffer == null) return;
        mVerticesBuffer.position(0);

        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, mVerticesBuffer);
        GLUtil.checkError("glVertexAttribPointer maPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLUtil.checkError("glEnableVertexAttribArray maPositionHandle");
    }

    public void uploadTexCoordinateBuffer(int textureCoordinateHandle){
        if (mTexCoordinateBuffer == null) return;
        mTexCoordinateBuffer.position(0);

        GLES20.glVertexAttribPointer(textureCoordinateHandle, 2, GLES20.GL_FLOAT, false, 0, mTexCoordinateBuffer);
        GLUtil.checkError("glVertexAttribPointer maTextureHandle");
        GLES20.glEnableVertexAttribArray(textureCoordinateHandle);
        GLUtil.checkError("glEnableVertexAttribArray maTextureHandle");
    }



    public void setTexCoordinateBuffer(float[] textCoordinate) {
        mTexCoordinateBuffer =  ByteBuffer
                .allocateDirect(textCoordinate.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textCoordinate);
        mTexCoordinateBuffer.position(0);
    }

    public void setVerticesBuffer(float[] vertices) {
        mVerticesBuffer = ByteBuffer
                .allocateDirect(vertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertices);
        mVerticesBuffer .position(0);
    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 4);
    }
}
