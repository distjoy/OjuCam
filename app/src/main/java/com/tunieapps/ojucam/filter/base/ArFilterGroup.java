package com.tunieapps.ojucam.filter.base;

import android.content.Context;
import android.graphics.PointF;

import com.tunieapps.ojucam.model.FilterGroupData;

public class ArFilterGroup extends FilterGroup {

    public ArFilterGroup(Context context) {
        super(context);

    }


    public void onDetectFace(PointF[][] facePoints){
        for (AbsFilter filter : filters) {
            if(filter instanceof ArFilter)
                ((ArFilter)filter).onDetectFace(facePoints);
        }
    }

}
