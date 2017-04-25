#version 120

uniform vec3 sunLightDir;

varying vec4 fragPos;
varying vec2 texCoord;
varying vec4 view;
varying vec4 vColor;
varying vec3 vNormal;
varying vec3 ViewSunLightDir;
varying vec3 vViewNormal;
varying vec4 WorldView;

void main()
{
	fragPos = gl_Vertex;
	view = gl_ModelViewMatrix * fragPos;
	WorldView = ftransform() * view;
	texCoord = gl_MultiTexCoord0.xy;
	vColor = gl_Color;
	vNormal = gl_Normal;
	vViewNormal = vec3(gl_ModelViewMatrix * vec4(vNormal,0));
	ViewSunLightDir = vec3(gl_ModelViewMatrix * vec4(sunLightDir,0));
    gl_Position = ftransform();
}