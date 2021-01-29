uniform mat4 uMVPMatrix;
attribute vec4 aPosition;
varying vec2 vMainTextCoord;
attribute vec4 aMainTextCoord;
void main() {
  gl_Position = aPosition;
  vMainTextCoord = (aMainTextCoord).xy;
}