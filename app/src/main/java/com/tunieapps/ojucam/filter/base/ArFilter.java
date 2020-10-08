package com.tunieapps.ojucam.filter.base;

import android.content.Context;
import android.graphics.PointF;

public abstract class ArFilter extends AbsFilter {
    private PointF[][] facePoints;

    public ArFilter(Context context) {
        super(context);
    }

    protected PointF getNormalizedCoord(float x, float y)
    {
        PointF pointF = new PointF();
        pointF.x = (2.0F * x / this.getWidth() - 1.0F);
        pointF.y = (2.0F * (1.0F - y / this.getHeight()) - 1.0F);
        return pointF;
    }
    protected void onDetectFace(PointF[][] facePoints) {
        this.facePoints = facePoints;
    }

    public PointF[][] getFacePoints() {
        return facePoints;
    }
}
