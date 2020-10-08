package com.tunieapps.ojucam.filter.facedrawing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MakeUpData {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

   static class Data {

        @SerializedName("mesh")
        @Expose
        private Mesh mesh;

        public Mesh getMesh() {
            return mesh;
        }

        public void setMesh(Mesh mesh) {
            this.mesh = mesh;
        }

    }
    static class Mesh {
        @SerializedName("texture")
        @Expose
        private String texture;
        @SerializedName("vertices")
        @Expose
        private List<Integer> vertices = null;
        @SerializedName("resFacePoints")
        @Expose
        private float[] resFacePoints = null;
        @SerializedName("inheritoffset")
        @Expose
        private Integer inheritoffset;

        public String getTexture() {
            return texture;
        }

        public void setTexture(String texture) {
            this.texture = texture;
        }

        public List<Integer> getVertices() {
            return vertices;
        }

        public void setVertices(List<Integer> vertices) {
            this.vertices = vertices;
        }

        public float[] getResFacePoints() {
            return resFacePoints;
        }

        public void setResFacePoints(float[] resFacePoints) {
            this.resFacePoints = resFacePoints;
        }

        public Integer getInheritoffset() {
            return inheritoffset;
        }

        public void setInheritoffset(Integer inheritoffset) {
            this.inheritoffset = inheritoffset;
        }

    }
}
