package com.tunieapps.ojucam.filter.oes;

import android.content.Context;
import android.util.Log;

import com.tunieapps.ojucam.filter.base.AbsFilter;
import com.tunieapps.ojucam.model.Constants;
import com.tunieapps.ojucam.filter.common.FrameBuffer;
import com.tunieapps.ojucam.filter.common.Texture;


public class OESFilter extends AbsFilter {


    private static final String TAG = "OESFilter";
    private TextureOES texture;
    private FrameBuffer frameBuffer;
    private OESProgram program;
    private float[] STMatrix = new float[16];

    public OESFilter(Context context) {
        super(context);
        program = new OESProgram(context);
        texture = new TextureOES(0);
    }

    @Override
    public void init() { }

    @Override
    public void onPreDraw() {
        super.onPreDraw();
        //upload necessary values
        program.use();
        Log.d(TAG, "onPreDraw() called use program "+ program.getId());
        plane.uploadTexCoordinateBuffer(program.getMaTextureCoordinateHandle());
        plane.uploadVerticesBuffer(program.getMaPositionHandle());
        program.setStMatValue(STMatrix);
    }

    @Override
    public void onDrawFrame() {
        onPreDraw();
        program.uploadTexture(getTextureId(),0);
        plane.draw();
        onPostDraw();
    }
    @Override
    public void onPostDraw() {
        super.onPostDraw();
        //upload necessary values
        program.disableAttributes();
        program.unbindTexture();
    }

    @Override
    public void destroy() {
       if(texture!=null) texture.delete();
       if(frameBuffer!=null) frameBuffer.destroy();
        program.delete();
    }

    @Override
    public int getTextureId() {
        if(texture!=null)
        return texture.getId();
        else if(frameBuffer!=null)
            return frameBuffer.getFrameBufferTextureId();
        else return Constants.NO_TEXTURE;
    }

    @Override
    protected FrameBuffer getBuffer() {
        return frameBuffer;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void setTexture(Texture texture) { }

    public float[] getSTMatrix() {
        return  STMatrix;
    }
}
