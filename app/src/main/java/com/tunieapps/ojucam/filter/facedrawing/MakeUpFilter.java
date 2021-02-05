package com.tunieapps.ojucam.filter.facedrawing;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.Matrix;
import android.util.Log;

import com.tunieapps.ojucam.filter.base.ArFilter;
import com.tunieapps.ojucam.filter.common.FrameBuffer;
import com.tunieapps.ojucam.object.Mesh;
import com.tunieapps.ojucam.filter.common.Texture;
import com.tunieapps.ojucam.filter.simple2d.Texture2D;
import com.tunieapps.ojucam.filter.oes.TextureOES;

import java.util.List;

import static com.tunieapps.ojucam.util.StringUtil.getString;

public class MakeUpFilter extends ArFilter {

    private static final String TAG = "MakeUpFilter";
    Texture2D makeUpTexture;
    Texture2D mainTexture;
    MakeUpProgram program;

    Mesh mesh;
    private MakeUpData data;
    private List<Integer> triangulationIndex;
    private float[] STMatrix = new float[16]; //only needed because main texture is OES
    public MakeUpFilter(Context context, MakeUpData data) {
        super(context);
        this.data = data;
        program = new  MakeUpProgram(getContext());
        makeUpTexture = new Texture2D(0,getContext());
        Matrix.setIdentityM(STMatrix,0);
    }

    @Override
    public void init() {
        unpackData();
        makeUpTexture.loadToMemory();
    }

    private void unpackData() {
        makeUpTexture.setAssetPath("texture/"+data.getData().getMesh().getTexture());
        triangulationIndex = data.getData().getMesh().getVertices();
        int vertices = triangulationIndex.size();
        mesh = new Mesh(vertices);
        mesh.initBuffer(vertices*2);
    }

    boolean drawn = false;
    @Override
    public void onPreDraw() {
        program.use();
        super.onPreDraw();
        Log.d(TAG, "onPreDraw() called use program "+ program.getId());
      //  plane.uploadTexCoordinateBuffer(program.getMaTextureCoordinateHandle());
       // plane.uploadVerticesBuffer(program.getMaPositionHandle());
        program.setStMatValue(STMatrix);
        mesh.setMakeUpTexCoordinateBuffer(getMakeUpTextureCoordinate());

        mesh.setVerticesBuffer(getTriangulations());
        mesh.setMainTexCoordinateBuffer(getFaceTextureCoordinates());

    }

    @Override
    public void onDrawFrame() {
        onPreDraw();
        mesh.uploadMakeUpTexCoordinateBuffer(program.getMakeUpTextCoordLoc());


        //program.setStage(0);
        program.uploadTexture(mainTexture.getId(),0);
       // plane.draw();
        //onPostDraw();
        mesh.uploadVerticesBuffer(program.getMaPositionHandle());
       mesh.uploadMainTexCoordinateBuffer(program.getMaTextureCoordinateHandle());
      program.uploadMakeUpTexture(makeUpTexture.getId(),0);
       program.setStage(1);
      //  plane.draw();
        mesh.draw();

        onPostDraw();
    }

    @Override
    public void onPostDraw() {
        super.onPostDraw();
        program.disableAttributes();
        program.unbindTexture();
    }

    @Override
    public void destroy() {
        makeUpTexture.delete();
        program.delete();
    }


    private float[] getFaceTextureCoordinates() {
        PointF[] facePoints = getFacePoints()[0];
        float[] points = new float[triangulationIndex.size()*2];

        int pointIndex = 0;
        for (int index : triangulationIndex) {

            points[pointIndex] =  facePoints[index].y/getHeight();
            ++pointIndex;
            points[pointIndex]  =facePoints[index].x/getWidth();
            ++pointIndex;
        }
        return points;
    }


    private float[] getMakeUpTextureCoordinate() {
        float[] resFacePoints = data.getData().getMesh().getResFacePoints();
    /*    for(int it = 0,t=0; it < indexedTriangleVertices.length; it++,t+=2){
            PointF pf =new PointF(triangles[t],triangles[t+1]);
            //   triangleVertices[it] = pf;
            indexedTriangleVertices[it] = rList.indexOf(pf);
        }
*/
        float[] points = new float[triangulationIndex.size()*2];

        int pointIndex = 0;
        for (int index : triangulationIndex) {
            int i = index*2;
            points[pointIndex] = resFacePoints[i];
            ++pointIndex;
            points[pointIndex] = 1-resFacePoints[i+1];
            ++pointIndex;
        }
        return points;
    }
    private float[] getTriangulations() {

        PointF[] facePoints = getFacePoints()[0];
        float[] points = new float[triangulationIndex.size()*2];

        int pointIndex = 0;
        for (int index : triangulationIndex) {
            PointF pointF = getNormalizedCoord(facePoints[index].x, facePoints[index].y);
            points[pointIndex] = pointF.x;
            ++pointIndex;
            points[pointIndex] = pointF.y;
            ++pointIndex;
        }
      //  Timber.i(" getTriangulations facepoints :%s", getString(facePoints)) ;
        return points;
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
        if(texture instanceof Texture2D)
            mainTexture = (Texture2D) texture;
    }

    public void setSTMatrix(float[] STMatrix) {
        this.STMatrix = STMatrix;
    }
}
