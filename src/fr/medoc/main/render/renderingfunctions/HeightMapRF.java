package fr.medoc.main.render.renderingfunctions;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.awt.image.BufferedImage;

import fr.medoc.main.math.Vector3;
import fr.medoc.main.render.texture.ImageUtil;

public class HeightMapRF implements RenderingFunction{

	Vector3 position;
	float yscaleFactor;
	String file;
	boolean computeColors = false;
	Vector3 hmScale;
	int width;
	int height;
	
	public HeightMapRF(Vector3 hmScale, String file, boolean computeColors)
	{
		this(hmScale, file);
		this.computeColors = computeColors;
	}
	
	public HeightMapRF(Vector3 hmScale, String file)
	{
		this.hmScale = hmScale;
		this.file = file;
	}
	
	public HeightMapRF(String file)
	{
		this(new Vector3(1,1,1), file);
	}
	
	@Override
	public void OnCall() {
		GenerateHeightMap(position, hmScale, file, computeColors);
	}
	
	
	public static void GenerateHeightMap(Vector3 position, Vector3 hmscale , String file, boolean computeColors)
	{
		BufferedImage map = ImageUtil.loadImage(file);
		
		int width = map.getWidth();
		int height = map.getHeight();
		
		
		Vector3 scale = new Vector3(hmscale.x / (float)width, hmscale.y, hmscale.z / (float)height);
		Vector3[] normals = new Vector3[width*height];
		
		glPushMatrix();
		
		//glTranslatef(position.x, position.y, position.z);
		//glScalef(scale.x, scale.y, scale.z);
		
		glBegin(GL_QUADS);
		for(int x=0; x<width-1; x++)
		{
			for(int z =0; z<height-1; z++)
			{
				int pixel0 = map.getRGB(x, z+1);
				int pixel1 = map.getRGB(x+1, z+1);
				int pixel2 = map.getRGB(x+1, z);
				int pixel3 = map.getRGB(x, z);
				
				float xx = x*scale.x;
				float zz = z*scale.z;
				
		        float y_val0 = ImageUtil.pixelToARGB(pixel0).getLuminance()*scale.y;
		        float y_val1 = ImageUtil.pixelToARGB(pixel1).getLuminance()*scale.y;
		        float y_val2 = ImageUtil.pixelToARGB(pixel2).getLuminance()*scale.y;
		        float y_val3 = ImageUtil.pixelToARGB(pixel3).getLuminance()*scale.y;
		     
		        int index = x + (z+1)*width;
		        if(normals[index] == null)
		        	normals[index] = new Vector3((y_val0-y_val1)/scale.x,1,-(y_val0-y_val2)/scale.z); 
		        Vector3 n = normals[index];
		        n.normalize();
		        glNormal3f(n.x,n.y,n.z);
		        
		        glTexCoord2f((float)x/(float)width, (float)(z+1)/(float)width);
		        if(computeColors)
		        glColor3f(y_val0/scale.y, y_val0/scale.y, y_val0/scale.y);
		        glVertex3f(xx+0, y_val0, zz+scale.z);
		        
		        
		        index = (x+1) + (z+1)*width;
		        if(normals[index] == null)
		        {
		        	n = new Vector3((y_val0-y_val1)/scale.x,1,-(y_val1-y_val2)/scale.z);
		        	normals[index] = n;
		        }
		        else
		        	n = normals[index];
		        n.normalize();
		        glNormal3f(n.x,n.y,n.z);
		        
		        
		        glTexCoord2f((float)(x+1)/(float)width, (float)(z+1)/(float)width);
		        if(computeColors)
		        glColor3f(y_val1/scale.y, y_val1/scale.y, y_val1/scale.y);
				glVertex3f(xx+scale.x, y_val1, zz+scale.z);
				
				index = (x+1) + (z)*width;
				if(normals[index] == null)
		        {
		        	n = new Vector3(-(y_val2-y_val3)/scale.x,1,(y_val2-y_val1)/scale.z);
		        	normals[index] = n;
		        }
		        else
		        	n = normals[index];
			    n.normalize();
			    glNormal3f(n.x,n.y,n.z);
				
				glTexCoord2f((float)(x+1)/(float)width, (float)(z+0)/(float)width);
				if(computeColors)
				glColor3f(y_val2/scale.y, y_val2/scale.y, y_val2/scale.y);
				glVertex3f(xx+scale.x, y_val2, zz+0);
				

				index = (x) + (z)*width;
				if(normals[index] == null)
		        {
		        	n = new Vector3((y_val3-y_val2)/scale.x,1,(y_val3-y_val0)/scale.z);
		        	normals[index] = n;
		        }
		        else
		        	n = normals[index];
			    n.normalize();
			    glNormal3f(n.x,n.y,n.z);
				
				glTexCoord2f((float)(x+0)/(float)width, (float)(z+0)/(float)width);
				if(computeColors)
				glColor3f(y_val3/scale.y, y_val3/scale.y, y_val3/scale.y);
				glVertex3f(xx+0, y_val3, zz+0);
			}
		}
		glEnd();
		glPopMatrix();
	}

}
