package com.tunieapps.ojucam.gl.filter;

import android.content.Context;

import com.tunieapps.ojucam.gl.FrameBuffer;
import com.tunieapps.ojucam.gl.Task;
import java.util.List;

public class FilterGroup extends AbsFilter {
    FrameBuffer[] frameBufferList;
    protected List<AbsFilter> filters;
    private FrameBuffer parentFrameBuffer;
    public FilterGroup(Context context) {
        super(context);
    }

    @Override
    public void init() {
        for (AbsFilter filter : filters) {
            filter.init();
        }
        initFrameBuffers();
    }
    private void initFrameBuffers() {
        parentFrameBuffer =null;
        int size = filters.size();
        if(frameBufferList != null){
            destroyFrameBuffers();
            frameBufferList=null;
        }

        frameBufferList = new FrameBuffer[size-1];
        for (int i = 0; i < size-1; i++) {
            AbsFilter filter=filters.get(i);
            frameBufferList[i]=FrameBuffer.newInstance(filter.getWidth(),filter.getHeight());
        }
    }

    @Override
    public void onDrawFrame(int textureId) {

    }

    @Override
    public void onSizeChanged(int surfaceWidth, int surfaceHeight) {
        super.onSizeChanged(surfaceWidth, surfaceHeight);
        int size = filters.size();
        for (int i = 0; i < size; i++) {
            filters.get(i).onSizeChanged(surfaceWidth, surfaceHeight);
        }
    }

    public void addFilter(AbsFilter filter){
        if (filter==null) return;
        //If one filter is added multiple times,
        //it will execute the same times
        //BTW: Pay attention to the order of execution
        addPreDrawTask(() -> {
            filter.init();
            filters.add(filter);
            initFrameBuffers();
        });
    }


    @Override
    public void destroy() {
        destroyFrameBuffers();
        for (AbsFilter filter : filters) {
            filter.destroy();
        }
    }

    private void destroyFrameBuffers() {
        parentFrameBuffer =null;
        for(FrameBuffer fbo:frameBufferList)
            fbo.destroy();
    }
}
