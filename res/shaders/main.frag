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
uniform sampler2D normal;
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
uniform vec2 normalRepeat;

varying vec2 texCoord;
varying vec4 view;
varying vec4 fragPos;
varying vec4 vColor;
varying vec3 vNormal;

vec3 finalNormal;


mat4 rotationMatrix(vec3 axis, float angle)
{
    axis = normalize(axis);
    float s = sin(angle);
    float c = cos(angle);
    float oc = 1.0 - c;
    
    return mat4(oc * axis.x * axis.x + c,           oc * axis.x * axis.y - axis.z * s,  oc * axis.z * axis.x + axis.y * s,  0.0,
                oc * axis.x * axis.y + axis.z * s,  oc * axis.y * axis.y + c,           oc * axis.y * axis.z - axis.x * s,  0.0,
                oc * axis.z * axis.x - axis.y * s,  oc * axis.y * axis.z + axis.x * s,  oc * axis.z * axis.z + c,           0.0,
                0.0,                                0.0,                                0.0,                                1.0);
}


vec3 finalNormalCalculation(vec3 normalvec, vec3 normaltexRGB)
{
	vec3 orthonormalvec1 = normalize(vec3(1.0/normalvec.x , 1.0/normalvec.y, -2.0/normalvec.z));
	vec3 orthonormalvec2 = cross(normalvec,orthonormalvec1);
	
	/*vec4 res = rotationMatrix(orthonormalvec1, (normaltexRGB.r*2-1)*0.1) * vec4(normalvec,0);
	res = rotationMatrix(orthonormalvec1, (normaltexRGB.g*2-1)*0.1) * res;*/
	
	vec4 res = rotationMatrix(vec3(1,0,0), (normaltexRGB.r*2-1)) * vec4(normalvec,0);
	res = rotationMatrix(vec3(0,1,0), (normaltexRGB.g*2-1)) * res;
	
	return res.xyz;
}


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
	return clamp(dot(finalNormal, -sunLightDir)*sunLightIntensity, ambientLightIntensity, 2.0);
	/*vec3 L = normalize(vec3(0,0,1000) - v);   
	float Idiff = max(dot(N,L), 0.0);  
	return clamp(Idiff, 0.0, 1.0);*/
}

float specularCalculation()
{  
	//return pow(max(0.0, dot(reflect(-ViewSunLightDir, vViewNormal), normalize(view.xyz))), 100);
	//return 0;
	return pow(max(0.0, dot(reflect(-sunLightDir, finalNormal), normalize(fragPos.xyz - cameraPosition))), hardness)*specular*sunLightIntensity;
}

void main() {
	if(effects == 0)
	{
		vec2 normalCoordRepeat = vec2(texCoord.x * normalRepeat.x , texCoord.y * normalRepeat.y);
		//finalNormal = normalize( vNormal + (normalize(texture2D(normal, normalCoordRepeat).rgb) * 2 - 1));
		finalNormal = finalNormalCalculation(vNormal, normalize(texture2D(normal, normalCoordRepeat).rgb));
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