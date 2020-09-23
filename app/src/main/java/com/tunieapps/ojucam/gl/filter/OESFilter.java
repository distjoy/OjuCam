package com.tunieapps.ojucam.gl.filter;

import android.content.Context;

import com.tunieapps.ojucam.gl.program.OESProgram;
import com.tunieapps.ojucam.gl.texture.TextureOES;

public class OESFilter extends AbsFilter{

    private TextureOES texture;
    private OESProgram program;
    public OESFilter(Context context) {
        super(context);
        program = new OESProgram(context);
    }

    @Override
    public void init() {

    }

    @Override
    public void onDrawFrame(int textureId) {

    }

    @Override
    public void destroy() {

    }
}
