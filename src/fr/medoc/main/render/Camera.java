package fr.medoc.main.render;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TRANSFORM_BIT;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;

import fr.medoc.main.game.GameObject;

public class Camera extends GameObject{
	private float fov;
	private float zNear;
	private float zFar;
	
	
	public Camera() {
	}

	/**
	 * sets the rendering parameters of the camera
	 * @param fov
	 * @param znear
	 * @param zfar
	 * @return
	 */
	public Camera setPerspectiveProjection(float fov, float znear, float zfar)
	{
		this.fov = fov;
		this.zNear = znear;
		this.zFar = zfar;
		return this;
	}
	
	/**
	 * ?
	 */
	public void getPerspectiveProjection()
	{
		glEnable(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(fov, (float)Display.getWidth()/(float)Display.getHeight(), zNear, zFar);
		glEnable(GL_MODELVIEW);
	}
	
	/**
	 * ?
	 */
	public void CameraUpdate()
	{
		glPushAttrib(GL_TRANSFORM_BIT);
			glRotatef(transform.eulerAngles.x, 1,0,0);
			glRotatef(transform.eulerAngles.y, 0,1,0);
			glRotatef(transform.eulerAngles.z, 0,0,1);
			glTranslatef(-transform.position.x, -transform.position.y, -transform.position.z);
		glPopMatrix();
	}
}
