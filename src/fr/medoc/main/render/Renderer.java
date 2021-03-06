package fr.medoc.main.render;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;

import fr.medoc.main.game.Game;
import fr.medoc.main.game.GameObjectComponent;
import fr.medoc.main.game.Scene;
import fr.medoc.main.math.ARGBColor;
import fr.medoc.main.math.Transform;
import fr.medoc.main.math.Vector3;
import fr.medoc.main.render.renderingfunctions.RenderingFunction;
import fr.medoc.main.render.texture.Texture;

public class Renderer implements GameObjectComponent{
	
	public Transform transform = new Transform();
	public int shaderEffects = 0; // 0:all; 1:none;
	public Texture texture = Texture.DEFAULT;
	public Vector2f texUVRepeat = new Vector2f(1,1);
	public Texture normal = Texture.DEFAULT_NORMAL;
	public Vector2f norUVRepeat = new Vector2f(1,1);
	public float normalFactor = 0;
	public float shaderHardness = 100;
	public float shaderSpecular = 1f;
	public ARGBColor color = new ARGBColor(1,1,1,1);
	private int renderingList;
	Shader shader;
	RenderingFunction renderingFunction;
	
	public Renderer(ARGBColor color)
	{
		this();
		this.color = color;
	}
	
	public Renderer()
	{
		texture = Texture.DEFAULT;
	}
	
	public Renderer(Texture tex)
	{
		texture = tex;
	}
	
	public Renderer(Texture tex, ARGBColor color)
	{
		this(tex);
		this.color = color;
	}
	
	public Renderer(Texture tex, Texture nor, ARGBColor color)
	{
		this(tex);
		this.color = color;
		this.normal = nor;
	}
	
	public Renderer(Texture tex, RenderingFunction func)
	{
		texture = tex;
		setCompileRenderingFunction(func);
	}
	
	public Renderer(Texture tex, Texture nor, RenderingFunction func)
	{
		texture = tex;
		setCompileRenderingFunction(func);
		normal = nor;
	}
	
	public Renderer(Texture tex, RenderingFunction func, ARGBColor color)
	{
		this(tex,func);
		this.color = color;
	}
	
	public Renderer(Texture tex, Texture nor, RenderingFunction func, ARGBColor color)
	{
		this(tex,func);
		this.color = color;
		this.normal = nor;
	}
	
	
	public void render()
	{
		/*
		// Create FloatBuffer that can hold 16 values.
		FloatBuffer buf = BufferUtils.createFloatBuffer(16);
		FloatBuffer bufnorm = BufferUtils.createFloatBuffer(16);

		// Get current modelview matrix:
		glGetFloat(GL_MODELVIEW_MATRIX, buf);

		// Rewind buffer. Not sure if this is needed, but it can't hurt.
		buf.rewind();

		// Create a Matrix4f.
		Matrix4f mat = new Matrix4f();

		// Load matrix from buf into the Matrix4f.
		mat.load(buf);
		 // calcul de la matrice de transformation
		
		mat.invert();
		mat.transpose();
		
		mat.store(bufnorm);*/
		
		/*if(shader != null) 
		{
			shader.setUniform("mainColor", color);
			shader.bind();
		}
		else 
		{
			Shader.MAIN.setUniform("mainColor", color);
			Shader.MAIN.setUniform("fogColor", Game.getActiveScene().fog_color);
			Shader.MAIN.setUniform("fogDensity", Game.getActiveScene().fog_density);
			Shader.MAIN.setUniform("sunLightDir", transform.transformDirection(Game.getActiveScene().sunLightDirection));
			Shader.MAIN.setUniform("ambientLightIntensity", Game.getActiveScene().ambientLightIntensity);
			Shader.MAIN.setUniform("texRepeat", texture.uvRepeat);
			Shader.MAIN.bind();
		}*/
		Shader.MAIN.setUniform("effects", shaderEffects);
		Shader.MAIN.setUniform("mainColor", color);
		Shader.MAIN.setUniform("hardness", shaderHardness);
		Shader.MAIN.setUniform("specular", shaderSpecular);
		Shader.MAIN.setUniform("fogColor", Game.getActiveScene().fog_color);
		Shader.MAIN.setUniform("fogDensity", Game.getActiveScene().fog_density);
		Shader.MAIN.setUniform("cameraPosition", transform.transformPosition2(Game.getActiveScene().getActiveCamera().transform.position));
		Shader.MAIN.setUniform("sunLightDir", transform.transformDirection2(Game.getActiveScene().sunLightDirection));
		Shader.MAIN.setUniform("sunColor", Game.getActiveScene().sunColor);
		Shader.MAIN.setUniform("sunLightIntensity", Game.getActiveScene().sunLightIntensity);
		Shader.MAIN.setUniform("ambientLightIntensity", Game.getActiveScene().ambientLightIntensity);
		Shader.MAIN.setUniform("texRepeat", texUVRepeat);
		Shader.MAIN.setUniform("normalRepeat", norUVRepeat);
		Shader.MAIN.setUniform("normalFactor", normalFactor);

		
		Shader.MAIN.bind();
		texture.bindHighRes(0,"tex");
		texture.bindLowRes(1,"texLR");
		normal.bindHighRes(2,"normal");
		
		glPushMatrix();
		transform.glTransform();
		glCallList(renderingList);
		glPopMatrix();
	}
	
	
	public void setShader(Shader shad)
	{
		shader = shad;
	}
	
	
	public void compileRendering()
	{
		renderingList = glGenLists(1);
		
		glNewList(renderingList, GL_COMPILE);
			renderingFunction.OnCall();
		glEndList();
	}

	
	public void setCompileRenderingFunction(RenderingFunction func)
	{
		renderingFunction = func;
		compileRendering();
	}
	
	
	
	
	
	
	public static void GenerateDebugPyramid(Vector3 position)
	{
		glPushMatrix();
		
			glTranslatef(position.x, position.y, position.z);
			
			glBegin(GL_TRIANGLES);
	
			glColor3f(1, 1, 1);
	
			glVertex3f(0, 0, 0);
			glVertex3f(1, 0, -1);
			glVertex3f(1, 0, 0);
	
			glVertex3f(0, 0, -1);
			glVertex3f(1, 0, -1);
			glVertex3f(0, 0, 0);
	
			glVertex3f(0, 0, -1);
			glColor3f(1, 0, 0);glVertex3f(0.5f, 1, -0.5f);glColor3f(1, 1, 1);
			glVertex3f(1, 0, -1);
	
			glVertex3f(0, 0, -1);
			glVertex3f(0, 0, 0);
			glColor3f(0, 1, 0);glVertex3f(0.5f, 1, -0.5f);glColor3f(1, 1, 1);
	
			glColor3f(0, 0, 1);glVertex3f(0.5f, 1, -0.5f);glColor3f(1, 1, 1);
			glVertex3f(1, 0, 0);
			glVertex3f(1, 0, -1);
	
			glVertex3f(0, 0, 0);
			glVertex3f(1, 0, 0);
			glColor3f(1, 1, 0);glVertex3f(0.5f, 1, -0.5f);
	
			glEnd();
		
		glPopMatrix();
	}

	@Override
	public void update() {
		//nothing to do here
	}

	@Override
	public void addToScene(Scene s) {
		s.addRenderer(this);
	}
}
