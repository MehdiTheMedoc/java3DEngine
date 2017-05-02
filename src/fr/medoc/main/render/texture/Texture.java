package fr.medoc.main.render.texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;

import fr.medoc.main.render.Shader;

public class Texture {
	private int id;
	private int width, height;
	public Vector2f uvRepeat = new Vector2f(1,1);
	
	public static final Texture DEFAULT = new Texture("/textures/default.png");
	public static final Texture DEFAULT_NORMAL = new Texture("/textures/defaultNor.png");
	
	public Texture(String path)
	{
		this(path, GL_LINEAR);
	}
	
	public Texture(String path, Vector2f uvRepeat)
	{
		this(path, GL_LINEAR, uvRepeat);
	}
	
	public Texture(String path, int filter)
	{
		this(path, GL_LINEAR, new Vector2f(1,1));
	}
	
	public Texture(String path, int filter, Vector2f uvRepeat)
	{
		this.uvRepeat = uvRepeat;
		
		int[] pixels = null;
		
		BufferedImage image = ImageUtil.loadImage(path);
		width = image.getWidth();
		height = image.getHeight();
		
		pixels = new int[width*height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		
		int[] data = new int[width*height];
		
		for( int i=0; i<data.length; i++)
		{
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
		
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		this.id = id;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void bind(int activeTextureId, String name)
	{
		int texLoc = glGetUniformLocation(Shader.MAIN.program, name);
		glUniform1i(texLoc, activeTextureId);
		glActiveTexture(GL_TEXTURE0 + activeTextureId);
		glBindTexture(GL_TEXTURE_2D, id);
	}
}
