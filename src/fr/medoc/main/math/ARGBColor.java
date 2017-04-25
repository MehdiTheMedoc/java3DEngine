package fr.medoc.main.math;

public class ARGBColor {
	public float r;
	public float g;
	public float b;
	public float a;
	
	public ARGBColor(float a, float r,float g,float b)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public ARGBColor(float r, float g, float b)
	{
		this(1.0f,r,g,b);
	}
	
	public ARGBColor()
	{
		this(1.0f,1.0f,1.0f,1.0f);
	}
	
	public float getLuminance()
	{
		return ((float)(r+g+b))/3.0f;
	}
}
