/*******************************
Main rendering shader

handles :
- parameter texture
- uv repeat
- parameter color
- vertex-relative color
- parameter fog
- depthBlur/Fade that blures the texture when distant, to prevent some dirty noisy aspect
- directionnal sun diffuse Light
- directionnal sun specular Light
- ambient diffuse light

@Author Medoc
********************************/
#version 120


uniform sampler2D tex;
uniform int effects;
uniform vec4 mainColor;
uniform float hardness;
uniform float specular;
uniform float fogDensity;
uniform vec3 fogColor;
uniform vec3 sunLightDir;
uniform float sunLightIntensity;
uniform vec3 cameraPosition;
uniform float ambientLightIntensity;
uniform vec2 texRepeat;

varying vec2 texCoord;
varying vec4 view;
varying vec4 fragPos;
varying vec4 vColor;
varying vec3 vNormal;

vec4 depthBlur(sampler2D mainTex, vec2 mainTexCoord, float dist, float start, float depth, float iterations)
{
	float power = clamp(dist-start, 0.0, depth+start)/((depth+start)*20);
	
	vec2 mainTexCoordRepeat = vec2(mainTexCoord.x * texRepeat.x , mainTexCoord.y * texRepeat.y);
	
	vec4 res = texture2D(mainTex, mainTexCoordRepeat);
	for(float i=iterations; i>0; i--)
	{
		float i_power = i/iterations * power;
		vec4 val1 = texture2D(mainTex, mainTexCoordRepeat+vec2(i_power,0));
		vec4 val2 = texture2D(mainTex, mainTexCoordRepeat+vec2(-i_power,0));
		vec4 val3 = texture2D(mainTex, mainTexCoordRepeat+vec2(0,i_power));
		vec4 val4 = texture2D(mainTex, mainTexCoordRepeat+vec2(0,i_power));
		
		val1 = mix(val1,val2,0.5);
		val3 = mix(val3,val4,0.5);
		
		res = mix(res, mix(val1,val3,0.5), i/iterations);
	}
	return res;
}

vec4 depthFade(sampler2D mainTex, vec2 mainTexCoord, float dist, float start, float depth)
{
	float power = clamp(dist-start, 0.0, depth+start)/((depth+start));
	vec4 res = texture2D(mainTex, mainTexCoord);
	return mix(res,texture2D(mainTex, vec2(0.5,0.5)), power );
}


float lightCalculation()
{
	return clamp(dot(vNormal, -sunLightDir)*sunLightIntensity, ambientLightIntensity, 2.0);
	/*vec3 L = normalize(vec3(0,0,1000) - v);   
	float Idiff = max(dot(N,L), 0.0);  
	return clamp(Idiff, 0.0, 1.0);*/
}

float specularCalculation()
{  
	//return pow(max(0.0, dot(reflect(-ViewSunLightDir, vViewNormal), normalize(view.xyz))), 100);
	//return 0;
	return pow(max(0.0, dot(reflect(-sunLightDir, vNormal), normalize(fragPos.xyz - cameraPosition))), hardness)*specular*sunLightIntensity;
}

void main() {
	if(effects == 0)
	{
		float dist = length(view);
		float fog = exp(-dist * fogDensity);
		fog = clamp(fog, 0.0, 1.0);
		
		//diffuse color + filtered texture + diffuse light
		gl_FragColor = mainColor * vColor * depthBlur(tex, texCoord, dist , 100, 100,5) * lightCalculation();
		//fog
		gl_FragColor = mix(vec4(fogColor, 1.0), gl_FragColor, vec4(fog));
		//specular
		gl_FragColor = mix(gl_FragColor, vec4(1,1,1,1), specularCalculation());
		
		gl_FragColor = clamp(gl_FragColor,0,1);
	}
	else if(effects == 1)
	{
		gl_FragColor = mainColor * vColor * texture2D(tex, texCoord);
	}
}