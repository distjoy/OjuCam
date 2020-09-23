package com.tunieapps.ojucam.gl.program;

import android.content.Context;

public class OESProgram extends Program {

    public OESProgram(Context context) {
        super(context, "filter/vsh/base/oes.glsl", "filter/fsh/base/oes.glsl");
    }
}
