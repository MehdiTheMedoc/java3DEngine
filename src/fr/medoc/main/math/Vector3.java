package fr.medoc.main.math;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Vector3 {
	public float x;
	public float y;
	public float z;
	
	public static final Vector3 RIGHT = new Vector3(1,0,0);
	public static final Vector3 UP = new Vector3(0,1,0);
	public static final Vector3 FORWARD = new Vector3(0,0,1);
	public static final Vector3 LEFT = new Vector3(-1,0,0);
	public static final Vector3 BOTTOM = new Vector3(0,-1,0);
	public static final Vector3 BACK = new Vector3(0,0,-1);
	
	public Vector3(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3(double x, double y, double z)
	{
		this.x = (float)x;
		this.y = (float)y;
		this.z = (float)z;
	}
	
	public Vector3()
	{
		this(0, 0, 0);
	}
	
	
	
	public static Vector3 add(Vector3 a, Vector3 b)
	{
		return a.copy().add(b);
	}
	
	public static Vector3 sub(Vector3 a, Vector3 b)
	{
		return a.copy().sub(b);
	}
	
	
	public Vector3f toVector3f()
	{
		return new Vector3f(x,y,z);
	}
	
	public Vector3 add(Vector3 other)
	{
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		
		return this;
	}
	
	public static Vector3 mul(Vector3 v, float val)
	{
		return v.copy().mul(val);
	}
	
	public Vector3 mul(float val)
	{
		this.x *= val;
		this.y *= val;
		this.z *= val;
		
		return this;
	}
	
	public Vector3 sub(Vector3 other)
	{
		this.x -= other.x;
		this.y -= other.y;
		this.z -= other.z;
		
		return this;
	}
	
	public float magnitude()
	{
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
	
	public Vector3 normalize()
	{
		float magn = magnitude();
		x = x/magn;
		y = y/magn;
		z = z/magn;
		
		return this;
	}
	
	public Vector3 normalized()
	{
		return copy().normalize();
	}
	
	public Vector3 copy()
	{
		return new Vector3(x,y,z);
	}
	
	public static Vector3 rotateAround(Vector3 npos, Vector3 nrot, float rotation) {
		   Matrix4f matrix = new Matrix4f();

		   Vector3 pos = npos.copy();

		   matrix.m03 = pos.x;
		   matrix.m13 = pos.y;
		   matrix.m23 = pos.z;

		   Vector3 rot = nrot.copy();

		   Matrix4f.rotate((float) Math.toRadians(rotation), rot.toVector3f(), matrix, matrix);

		   return new Vector3(matrix.m03, matrix.m13, matrix.m23);
		}
}
