package fr.medoc.main.game;

import org.lwjgl.util.vector.Vector2f;

import fr.medoc.main.math.Vector3;
import fr.medoc.main.render.Camera;
import fr.medoc.main.render.CustomCamera;
import fr.medoc.main.render.MeshRenderer;
import fr.medoc.main.render.Renderer;
import fr.medoc.main.render.SkyBoxRenderer;
import fr.medoc.main.render.renderingfunctions.HeightMapRF;
import fr.medoc.main.render.texture.Texture;

public class Game {
	
	private static Scene activeScene;
	

	public Game()
	{
		activeScene = new Scene();
		
		Camera cam;
		Renderer rend;
		Renderer rend1;
		Renderer rend2;
		
		cam = new CustomCamera();
		cam.transform.position.y += 50;
		cam.setPerspectiveProjection(75.0f, 0.1f, 1000.0f);
		
		rend = new Renderer(new Texture("/textures/grass.png"));
		rend.transform.eulerAngles.x = -45;
		rend.setCompileRenderingFunction(new HeightMapRF(15,"/textures/heightmap.png"));
		
		rend1 = new Renderer( new Texture("/textures/bube.png",new Vector2f(5,5)));
		rend1.setCompileRenderingFunction(new HeightMapRF(new Vector3(128,0,128),15,"/textures/heightmap.png"));
		
		/*rend2 = new Renderer(new ARGBColor(0,0,0,1));
		rend2.setCompileRenderingFunction(new HeightMapRF(new Vector3(-128,0,0),30,"/textures/bube.png"));*/
		
		rend2 = new MeshRenderer("/meshes/suzanne.obj","/textures/suzanne.png",50);
		
		Renderer sky = new SkyBoxRenderer("/textures/skyBoxLayout.png");
		
		activeScene.addRenderer(rend);
		activeScene.addRenderer(rend1);
		activeScene.addRenderer(rend2);
		activeScene.addRenderer(sky);
		activeScene.addGameObject(cam);
		activeScene.setActiveCamera(cam);
	}
	
	public void update()
	{
		activeScene.update();
	}
	
	public void render()
	{
		activeScene.render();
	}
	
	
	public static Scene getActiveScene()
	{
		return activeScene;
	}
}
