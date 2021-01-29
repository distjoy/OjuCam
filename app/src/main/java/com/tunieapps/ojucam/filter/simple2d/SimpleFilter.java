package com.tunieapps.ojucam.filter.simple2d;

import android.content.Context;

import com.tunieapps.ojucam.filter.base.AbsFilter;
import com.tunieapps.ojucam.filter.common.FrameBuffer;
import com.tunieapps.ojucam.filter.common.Texture;

public class SimpleFilter extends AbsFilter {

    public SimpleFilter(Context context) {
        super(context);
    }

    @Override
    public void init() {

    }

    @Override
    public void onDrawFrame() {

    }

    @Override
    public void destroy() {

    }

    @Override
    protected FrameBuffer getBuffer() {
        return null;
    }

    @Override
    public Texture getTexture() {
        return null;
    }

    @Override
    public void setTexture(Texture texture) {

    }
}
