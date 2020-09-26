package com.tunieapps.ojucam.gl.filter;

import android.content.Context;

import com.tunieapps.Constants;
import com.tunieapps.ojucam.gl.FrameBuffer;
import com.tunieapps.ojucam.gl.program.OESProgram;
import com.tunieapps.ojucam.gl.texture.TextureOES;

public class OESFilter extends AbsFilter{

    private TextureOES texture;
    private FrameBuffer frameBuffer;
    private OESProgram program;
    private float[] STMatrix = new float[16];
    ;
    public OESFilter(Context context) {
        super(context);
        program = new OESProgram(context);
        texture = new TextureOES(0);
    }

    @Override
    public void init() {
        program.create();
        texture.generate();
    }

    @Override
    public void onPreDraw() {
        super.onPreDraw();
        //upload necessary values
        program.use();
        plane.uploadTexCoordinateBuffer(program.getMaTextureCoordinateHandle());
        plane.uploadVerticesBuffer(program.getMaPositionHandle());
        program.setStMatValue(STMatrix);
    }

    @Override
    public void onDrawFrame() {
        onPreDraw();
        program.uploadTexture(getTexture(),0);
        plane.draw();
        onPostDraw();
    }


    @Override
    public void destroy() {
       if(texture!=null) texture.delete();
       if(frameBuffer!=null) frameBuffer.destroy();
        program.delete();
    }

    @Override
    public int getTexture() {
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

    public float[] getSTMatrix() {
        return  STMatrix;
    }
}
