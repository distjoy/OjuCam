package com.tunieapps.ojucam.filter.simple2d;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.Matrix;
import android.util.Log;

import com.tunieapps.ojucam.filter.base.AbsFilter;
import com.tunieapps.ojucam.filter.common.FrameBuffer;
import com.tunieapps.ojucam.filter.common.Texture;
import com.tunieapps.ojucam.model.Constants;

import java.nio.ByteBuffer;

public class SimpleFilter extends AbsFilter {

    private static final String TAG = "SimpleFilter";
    private Texture2D texture2D;
    private SimpleProgram simpleProgram;
    private MakeUpData data;
    private float[] mvpMat = new float[16];
    public SimpleFilter(Context context) {
        super(context);
        texture2D  = new Texture2D(0,getContext());
        simpleProgram = new SimpleProgram(context);
        Matrix.setIdentityM(mvpMat, 0);
    }

    @Override
    public void init()
    {}

    @Override
    public void onPreDraw() {
        simpleProgram.use();
        super.onPreDraw();
        //upload necessary values
        plane.uploadTexCoordinateBuffer(simpleProgram.getMaTextureCoordinateHandle());
        plane.uploadVerticesBuffer(simpleProgram.getMaPositionHandle());
        simpleProgram.setMvpMat(mvpMat);
    }

    @Override
    public void onDrawFrame() {
        onPreDraw();
        simpleProgram.uploadTexture(getTextureId(),0);
        plane.draw();
        onPostDraw();
    }
    @Override
    public void onPostDraw() {
        super.onPostDraw();
        simpleProgram.disableAttributes();
        simpleProgram.unbindTexture();
    }

    @Override
    public void destroy() {
        texture2D.delete();
        simpleProgram.delete();
    }
    @Override
    public int getTextureId() {
        if(texture2D!=null)
            return texture2D.getId();
        else return Constants.NO_TEXTURE;
    }

    @Override
    protected FrameBuffer getBuffer() {
        return null;
    }

    @Override
    public Texture getTexture() {
        return texture2D;
    }

    @Override
    public void setTexture(Texture texture) { }

    public void updateTextureContent(ByteBuffer frameBuffer, int width, int height) {
        if(texture2D!=null) {
            Log.d(TAG, "updateTextureContent() called with: frameBuffer = [" + frameBuffer + "], width = [" + width + "], height = [" + height + "]");
            texture2D.loadToMemory(frameBuffer, width, height);
        }
    }

    public float[] getMatrix() {
        return mvpMat;
    }

}
