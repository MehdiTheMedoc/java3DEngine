/*******************************
SKYBOX rendering shader

handles :
- parameter texture

- parameter color


@Author Medoc
********************************/
#version 120


uniform sampler2D tex;
uniform vec4 mainColor;

varying vec2 texCoord;


void main() {
    gl_FragColor = texture2D(tex,texCoord);
}