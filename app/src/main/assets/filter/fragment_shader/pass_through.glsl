precision mediump float;
varying highp vec2 vMainTextCoord;
uniform sampler2D mainImageText;
void main() {
   gl_FragColor = texture2D(mainImageText, vMainTextCoord);
}

