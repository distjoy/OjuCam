#extension GL_OES_EGL_image_external : require
precision mediump float;
varying vec2 vMainTextCoord;
uniform samplerExternalOES mainImageText;
void main() {
    gl_FragColor=texture2D(mainImageText, vMainTextCoord);
}