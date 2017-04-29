#version 120


varying vec4 fragPos;
varying vec2 texCoord;
varying vec4 view;
varying vec4 vColor;
varying vec3 vNormal;


void main()
{
	fragPos = gl_Vertex;
	view = gl_ModelViewMatrix * fragPos;
	texCoord = gl_MultiTexCoord0.xy;
	vColor = gl_Color;
	vNormal = gl_Normal;
    gl_Position = ftransform();
}