package fr.medoc.main.render;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import fr.medoc.main.math.ARGBColor;
import fr.medoc.main.math.Vector3;

public class Shader {
	
	public static final Shader MAIN = new Shader();
	public static final Shader SKYBOX = new Shader("/shaders/main.vert","/shaders/skybox.frag");
	
	public int program;
	
	public Shader()
	{
		this("/shaders/main.vert","/shaders/main.frag");
	}

	public Shader(String vertex, String fragment) {
		program = glCreateProgram();

		if (program == GL_FALSE) {
			System.err.println("Shader program error !");
			System.exit(1);
		}
		
		createShader(loadShader(vertex), GL_VERTEX_SHADER);
		createShader(loadShader(fragment), GL_FRAGMENT_SHADER);

		glLinkProgram(program);
		glValidateProgram(program);
	}
	
	private void createShader(String source, int type)
	{
		int shader = glCreateShader(type);
		
		if(shader == GL_FALSE)
		{
			System.err.println("Shader error : " + shader);
			System.exit(1);
		}
		
		glShaderSource(shader, source);
		glCompileShader(shader);
		
		if(glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println(glGetShaderInfoLog(shader,2048));
			System.exit(1);
		}
		
		glAttachShader(program, shader);
	}

	private String loadShader(String input) {
		String r = "";
		try {
			InputStream is = Shader.class.getResourceAsStream(input);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			String buffer = "";

			while ((buffer = reader.readLine()) != null) {
				r += buffer + "\n";
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return r;
	}
	
	public void setUniform(String name, int v)
	{
		glUniform1i(glGetUniformLocation(program, name), v);
	}
	
	public void setUniform(String name, float v)
	{
		glUniform1f(glGetUniformLocation(program, name), v);
	}
	
	public void setUniform(String name, Vector3 v)
	{
		glUniform3f(glGetUniformLocation(program, name), v.x, v.y, v.z);
	}
	
	public void setUniform(String name, Vector2f v)
	{
		glUniform2f(glGetUniformLocation(program, name), v.x, v.y);
	}
	
	public void setUniform(String name, Vector4f v)
	{
		glUniform4f(glGetUniformLocation(program, name), v.x, v.y, v.z, v.w);
	}
	
	public void setUniform(String name, ARGBColor c)
	{
		glUniform4f(glGetUniformLocation(program, name), c.r, c.g, c.b, c.a);
	}
	

	public void setUniform(String name, FloatBuffer mat) {
		GL20.glUniformMatrix4(glGetUniformLocation(program, name), false, mat);
	}
	
	
	public void bind()
	{
		glUseProgram(program);
	}
}
