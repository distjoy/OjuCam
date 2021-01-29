#extension GL_OES_EGL_image_external : require
precision mediump float;
varying highp vec2 vMainTextCoord;
varying highp vec2 vTextCoord2;
uniform samplerExternalOES mainImageText;
uniform sampler2D imageText2;
uniform vec4 uColor;
uniform int stage;
vec4 blendNormal(vec4 c1, vec4 c2) {
    vec4 outputColor;
    outputColor.r = c1.r + c2.r * c2.a * (1.0 - c1.a);
    outputColor.g = c1.g + c2.g * c2.a * (1.0 - c1.a);
    outputColor.b = c1.b + c2.b * c2.a * (1.0 - c1.a);
    outputColor.a = c1.a + c2.a * (1.0 - c1.a);
    return outputColor;
}
void main() {
   gl_FragColor = texture2D(mainImageText, vMainTextCoord);
    vec4 color2 = texture2D(imageText2, vTextCoord2);
   gl_FragColor = blendNormal(color2, gl_FragColor);
   // gl_FragColor = vec4(1.0,0.0,1.0,1.0);
 /*   if(stage==1){
        vec4 color2 = texture2D(imageText2, vTextCoord2);
        gl_FragColor = blendNormal(color2, gl_FragColor);
    }
    if (stage == 2){
        gl_FragColor = texture2D(mainImageText, vMainTextCoord);
    }*/
}

