package com.tunieapps.ojucam.filter.base;

import android.content.Context;

import com.tunieapps.ojucam.filter.common.FrameBuffer;
import com.tunieapps.ojucam.filter.common.Texture;

import java.util.ArrayList;
import java.util.List;

public class FilterGroup extends AbsFilter {
    FrameBuffer[] frameBufferList;
    protected List<AbsFilter> filters = new ArrayList<>();
    private FrameBuffer groupRenderScreen; // groupRender screeen is the system graphics for the main main filter group

    private boolean isInit = false;

    public FilterGroup(Context context) {
        super(context);
    }

    @Override
    public void init() {
        for (AbsFilter filter : filters) {
            filter.init();
        }
        initFrameBuffers();
        isInit = true;
    }
    private void initFrameBuffers() {
        groupRenderScreen =null;
        int size = filters.size();
        if(size==0)
            return;
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
    public void onDrawFrame() {
        onPreDraw();
        int size = filters.size();
        for (int i = 0; i < size; i++) {
            AbsFilter filter = filters.get(i);
            if (i < size - 1) {
                filter.setViewport();
                FrameBuffer renderScreen = filters.get(i+1).getBuffer(); //render to the buffer of the next element
                renderScreen.bind();
               // GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                if(filter instanceof FilterGroup)
                    ((FilterGroup) filter).setGroupRenderScreen(renderScreen);
                filter.onDrawFrame();
                renderScreen.unbind();
            }else{
                if(groupRenderScreen !=null)  //when down a filter group, render the last content on the main parent buffer
                    groupRenderScreen.bind();
                filter.setViewport();
                filter.onDrawFrame();
            }
        }
        onPostDraw();
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
        if(isInit){
            //If one filter is added multiple times,
            //it will execute the same times
            //BTW: Pay attention to the order of execution
            addPreDrawTask(() -> {
                filter.init();
                filters.add(filter);
                initFrameBuffers();
                onSizeChanged(width,height);
            });
        }else
            filters.add(filter);
    }


    @Override
    public void destroy() {
        isInit = false;
        destroyFrameBuffers();
        for (AbsFilter filter : filters) {
            filter.destroy();
        }
    }

    @Override
    protected FrameBuffer getBuffer() {
        if(filters.size()>0)
            return filters.get(0).getBuffer();
        return null;
    }

    @Override
    public Texture getTexture() {
        return null; //get texture to export for group
    }

    @Override
    public void setTexture(Texture texture) {
        //set texture to use as base
    }

    private void destroyFrameBuffers() {
        groupRenderScreen =null;
        for(FrameBuffer fbo:frameBufferList)
            fbo.destroy();
    }

    public void setGroupRenderScreen(FrameBuffer groupRenderScreen) {
        this.groupRenderScreen = groupRenderScreen;
    }

    public FrameBuffer getGroupRenderScreen() {
        return groupRenderScreen;
    }
}
