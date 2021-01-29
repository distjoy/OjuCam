uniform mat4 uSTMatrix;
attribute vec4 aPosition;
attribute vec4 aMainTextCoord;
attribute vec4 aTextCoord2;
varying vec2 vMainTextCoord;
varying vec2 vTextCoord2;
void main(){
    gl_Position = aPosition;
    gl_PointSize = 10.0;
    vMainTextCoord = (uSTMatrix * aMainTextCoord).xy;
    vTextCoord2 = aTextCoord2.xy;
}