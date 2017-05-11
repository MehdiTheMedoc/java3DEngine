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
uniform sampler2D texLR;
uniform sampler2D normal;
uniform float normalFactor;
uniform int effects;
uniform vec4 mainColor;
uniform float hardness;
uniform float specular;
uniform float fogDensity;
uniform vec3 fogColor;
uniform vec3 sunLightDir;
uniform vec4 sunColor;
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
float distanceFilterPower;

//generate a rotationMatrix according to an axis and an angle. Veeeery useful when it comes to rotating vectors
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

//calculate the distance power, that will be used by some function like texture filter
void setdistanceFilterPower(float dist, float start, float depth)
{
	distanceFilterPower = clamp(dist-start, 0.0, depth+start)/((depth+start));
}

//mixes two textures according to the distance power calculated early
vec4 getTextureColorWithDistanceFilter(vec4 texA, vec4 texB)
{
	return mix(texA,texB, distanceFilterPower);	
}


//compute the new normal according to the normal map
vec3 finalNormalCalculation(vec3 normalvec, vec3 normaltexRGB)
{
	if(normalFactor > 0.1 || normalFactor < -0.1)
	{
		vec3 orthonormalvec1 = normalize(vec3(1.0/normalvec.x , 1.0/normalvec.y, -2.0/normalvec.z));
		vec3 orthonormalvec2 = cross(normalvec,orthonormalvec1);
		
		float fac = 1 - distanceFilterPower; //decrease normal mapping power by distance
		
		vec4 res = rotationMatrix(vec3(1,0,0), (normaltexRGB.g*2-1)*normalFactor*fac) * vec4(normalvec,0);
		res = rotationMatrix(vec3(0,1,0), (normaltexRGB.r*2-1)*normalFactor*fac) * res;
		res = rotationMatrix(vec3(0,0,1), ((normaltexRGB.r + normaltexRGB.r)*2 - 2)*normalFactor*fac) * res;
		
		return res.xyz;
	}
	return normalvec;
}

//OBSELETE : not optimized, not accurate function to make a texture filter
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

//OBSELETE : fade texture with a plain color according to distance
vec4 depthFade(sampler2D mainTex, vec2 mainTexCoord, float dist, float start, float depth)
{
	float power = clamp(dist-start, 0.0, depth+start)/((depth+start));
	vec4 res = texture2D(mainTex, mainTexCoord);
	return mix(res,texture2D(mainTex, vec2(0.5,0.5)), power );
}

//calculate diffuse light on this fragment
float lightCalculation()
{
	return clamp(dot(finalNormal, -sunLightDir)*sunLightIntensity, ambientLightIntensity, 2.0);
}

//calculates specular light on this fragment
float specularCalculation()
{  
	return pow(max(0.0, dot(reflect(-sunLightDir, finalNormal), normalize(fragPos.xyz - cameraPosition))), hardness)*specular*sunLightIntensity;
}

void main() {
	if(effects == 0)
	{
		float dist = length(view);
		float fog = exp(-dist * fogDensity);
		fog = clamp(fog, 0.0, 1.0);
		
		setdistanceFilterPower(dist, 0, 250);
		
		vec2 normalCoordRepeat = vec2(texCoord.x * normalRepeat.x , texCoord.y * normalRepeat.y);
		finalNormal = finalNormalCalculation(vNormal, normalize(texture2D(normal, normalCoordRepeat)).rgb);
		
		vec2 mainTexCoordRepeat = vec2(texCoord.x * texRepeat.x , texCoord.y * texRepeat.y);
		//color, vertex color, texture, diffuse light
		gl_FragColor = mainColor * vColor * getTextureColorWithDistanceFilter(texture2D(tex, mainTexCoordRepeat), texture2D(texLR, mainTexCoordRepeat)) * lightCalculation() * sunColor;
		//fog
		gl_FragColor = mix(vec4(fogColor, 1.0), gl_FragColor, vec4(fog));
		//specular
		gl_FragColor = gl_FragColor + sunColor * specularCalculation();
		
		
		//final clamp to prevent wtf colors
		gl_FragColor = clamp(gl_FragColor,0,1);
	}
	else if(effects == 1)
	{
		gl_FragColor = mainColor * vColor * texture2D(tex, texCoord);
	}
}