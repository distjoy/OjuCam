package com.tunieapps.ojucam.filter.facedrawing;

import android.content.Context;
import android.graphics.PointF;

import com.tunieapps.ojucam.filter.base.ArFilter;
import com.tunieapps.ojucam.gl.FrameBuffer;
import com.tunieapps.ojucam.object.Mesh;
import com.tunieapps.ojucam.texture.Texture2D;

import java.util.List;

public class MakeUpFilter extends ArFilter {

    Texture2D makeUpTexture;
    MakeUpProgram program;
    Mesh mesh;
    private MakeUpData data;
    private List<Integer> triangulationIndex;

    public MakeUpFilter(Context context, MakeUpData data) {
        super(context);
        this.data = data;
    }

    @Override
    public void init() {
        program = new  MakeUpProgram(getContext());
        makeUpTexture = new Texture2D(0,getContext());
        mesh = new Mesh();
        unpackData();
    }

    private void unpackData() {
        makeUpTexture.setAssetPath("texture/"+data.getData().getMesh().getTexture());
        mesh.setTexCoordinateBuffer(data.getData().getMesh().getResFacePoints());
        //set mainTextureCoordinate
        triangulationIndex = data.getData().getMesh().getVertices();
    }

    @Override
    public void onPreDraw() {
        super.onPreDraw();
        mesh.setVerticesBuffer(getTriangulatedFacePoints());
        mesh.uploadTexCoordinateBuffer(program.getMakeUpTextCoordLoc());
        //upload main texture coordinate
        mesh.uploadVerticesBuffer(program.getMaPositionHandle());
    }


    @Override
    public void onDrawFrame() {
        onPreDraw();
        makeUpTexture.loadToMemory();
        //program.uploadTexture();
        program.uploadMakeUpTexture(makeUpTexture.getId(),0);
        mesh.draw();
        onPostDraw();
    }

    @Override
    public void onPostDraw() {
        super.onPostDraw();
    }

    @Override
    public void destroy() {
        makeUpTexture.delete();
        program.delete();
    }
    private float[] getTriangulatedFacePoints() {
        PointF[] facePoints = getFacePoints()[0];
        float[] points = new float[facePoints.length*2];

        int pointIndex = 0;
        for (int index : triangulationIndex) {
            PointF pointF = getNormalizedCoord(facePoints[index].x, facePoints[index].y);
            points[pointIndex] = pointF.x;
            ++pointIndex;
            points[pointIndex] = pointF.y;
            ++pointIndex;
        }
        return points;
    }


    @Override
    protected FrameBuffer getBuffer() {
        return null;
    }
}
