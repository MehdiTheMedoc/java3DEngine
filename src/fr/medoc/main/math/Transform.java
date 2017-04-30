package fr.medoc.main.math;

import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * 
 * the transform class contains all the information
 * necessary to localize something in the game
 * 
 * @author Medoc
 *
 */
public class Transform {
	public Vector3 position;
	public Vector3 eulerAngles;
	private Transform parent;
	
	public Transform(Vector3 position, Vector3 eulerAngles)
	{
		this.position = position;
		this.eulerAngles = eulerAngles;
	}
	
	public Transform()
	{
		this(new Vector3(), new Vector3());
	}
	
	public Transform getParent()
	{
		return parent;
	}
	
	public void setParent(Transform parent)
	{
		this.parent = parent;
	}
	
	/**
	 * @return the forward vector of this transform
	 */
	public Vector3 getForward()
	{
		return new Vector3(Math.cos(Math.toRadians(eulerAngles.y+90)) * Math.cos(Math.toRadians(eulerAngles.x)),
				Math.sin(Math.toRadians(eulerAngles.x)),
				Math.sin(Math.toRadians(eulerAngles.y+90)) * Math.cos(Math.toRadians(eulerAngles.x)));
	}
	
	/**
	 * @return the right vector of this transform
	 */
	public Vector3 getRight()
	{
		return new Vector3(Math.cos(Math.toRadians(eulerAngles.y)),
				0,
				Math.sin(Math.toRadians(eulerAngles.y))).mul(-1);
	}
	
	/**
	 * a simple function that allows to rotate this transform with mouse
	 */
	public void simpleMouseLook()
	{
		this.eulerAngles.y += Mouse.getDX();
		this.eulerAngles.x -= Mouse.getDY();
		
		if(this.eulerAngles.x < -90) this.eulerAngles.x = -90;
		if(this.eulerAngles.x > 90) this.eulerAngles.x = 90;
	}
	
	/**
	 * a simple function that allows to move this transform with ZQSZ keys
	 */
	public void simpleZQSDMove()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_Z)) this.position.add(this.getForward().mul(-0.5f));
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) this.position.add(this.getForward().mul(0.5f));
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) this.position.add(this.getRight().mul(-0.5f));
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)) this.position.add(this.getRight().mul(0.5f));
	}
	
	public void glTransform()
	{
		glRotatef(eulerAngles.x, 1,0,0);
		glRotatef(eulerAngles.y, 0,1,0);
		glRotatef(eulerAngles.z, 0,0,1);
		glTranslatef(position.x, position.y, position.z);
	}
	
	public Vector3 transformDirection(Vector3 direction)
	{
		Vector3 res = Vector3.rotateAround(direction, Vector3.RIGHT, -eulerAngles.x);
		res = Vector3.rotateAround(res, Vector3.UP, -eulerAngles.y);
		res = Vector3.rotateAround(res, Vector3.FORWARD, -eulerAngles.z);
		return res;
	}
	
	public Vector3 transformDirection2(Vector3 direction)
	{
		Vector3 res = Vector3.rotateAround(direction, Vector3.RIGHT, eulerAngles.x);
		res = Vector3.rotateAround(res, Vector3.UP, eulerAngles.y);
		res = Vector3.rotateAround(res, Vector3.FORWARD, eulerAngles.z);
		return res;
	}
	
	public Vector3 transformPosition(Vector3 position)
	{
		Vector3 res = Vector3.sub(position, this.position);
		res = Vector3.rotateAround(res, Vector3.RIGHT, -eulerAngles.x);
		res = Vector3.rotateAround(res, Vector3.UP, -eulerAngles.y);
		res = Vector3.rotateAround(res, Vector3.FORWARD, -eulerAngles.z);
		return res;
	}
	
	public Vector3 transformPosition2(Vector3 position)
	{
		Vector3 res = Vector3.sub(position, this.position);
		res = Vector3.rotateAround(res, Vector3.RIGHT, eulerAngles.x);
		res = Vector3.rotateAround(res, Vector3.UP, eulerAngles.y);
		res = Vector3.rotateAround(res, Vector3.FORWARD, eulerAngles.z);
		return res;
	}
}
