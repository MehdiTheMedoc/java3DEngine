package fr.medoc.main.render.texture;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;

import fr.medoc.main.math.Vector3;
import fr.medoc.main.render.Shader;
import fr.medoc.main.render.renderingfunctions.OBJMeshRF;

public class Texture {
	private int id;
	private int width, height;
	
	public static final Texture DEFAULT = new Texture("/textures/default.png");
	public static final Texture DEFAULT_NORMAL = new Texture("/textures/defaultNor.png");
	public static HashMap<String,Texture> textures = new HashMap<String,Texture>();
	
	public static void loadTextures()
	{
		String file = Texture.class.getResource("/textures.txt").getPath();
		
		file = "res/textures.txt";
		
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(file))) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		        String[] parse = line.split(" ");
		        Texture.textures.put(parse[0], new Texture("/textures/"+parse[1]));
		        System.out.println(parse[1]);
		    }
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	public Texture(String path)
	{
		this(path, GL_LINEAR);
	}
	
	public Texture(String path, int filter)
	{	
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
