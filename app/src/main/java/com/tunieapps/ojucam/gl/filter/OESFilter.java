package com.tunieapps.ojucam.gl.filter;

import android.content.Context;

import com.tunieapps.ojucam.gl.program.OESProgram;
import com.tunieapps.ojucam.gl.texture.TextureOES;

public class OESFilter extends AbsFilter{

    private TextureOES texture;
    private OESProgram program;
    private float[] STMatrix;
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
    public void onDrawFrame(int textureId) {
        onPreDraw();
        texture.upload(program.getsTextureHandle());
        plane.draw();
        onPostDraw();
    }

    @Override
    public void destroy() {
        texture.delete();
        program.delete();
    }

    public void setSTMatrix(float[] STMatrix) {
        this.STMatrix = STMatrix;
    }
}
