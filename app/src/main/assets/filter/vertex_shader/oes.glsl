uniform mat4 uSTMatrix;
attribute vec4 aPosition;
attribute vec4 aMainTextCoord;
varying vec2 vMainTextCoord;
void main() {
  gl_Position = aPosition;
  vMainTextCoord = (uSTMatrix * aMainTextCoord).xy;
}