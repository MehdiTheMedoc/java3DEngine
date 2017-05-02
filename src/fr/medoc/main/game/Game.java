package fr.medoc.main.game;

import org.lwjgl.util.vector.Vector2f;

import fr.medoc.main.game.collisions.matrixcollisiondetection.MatrixCollisionDetection;
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
		activeScene = new Scene(new MatrixCollisionDetection(20,20));
		
		Texture.loadTextures();
		
		Camera cam;
		
		cam = new CustomCamera();
		cam.transform.position.y += 50;
		cam.setPerspectiveProjection(75.0f, 0.1f, 1000.0f);
		
		ColliderRendererGameObject testObj = new ColliderRendererGameObject(new Renderer(Texture.textures.get("rocks"),Texture.textures.get("rocksnor"),new HeightMapRF(0.5f,"/textures/heightmap.png")), 
				new Vector3(128,1,128), 
				new Vector3(64,0,64));
		testObj.renderer.shaderSpecular = 0.5F;
		testObj.renderer.shaderHardness = 100;
		testObj.renderer.normalFactor = 0.25f;
		testObj.addToScene(activeScene);
		
		ColliderRendererGameObject testObj2 = new ColliderRendererGameObject(new Renderer(new Texture("/textures/bube.png"),new HeightMapRF(5f,"/textures/heightmap.png")), 
				new Vector3(128,1,128), 
				new Vector3(64,0,64));
		testObj2.transform.position = new Vector3(128,0,128);
		testObj2.addToScene(activeScene);
		
		
		ColliderRendererGameObject testObj3 = new ColliderRendererGameObject( new MeshRenderer("/meshes/suzanne.obj","/textures/suzanne.png",50), 
				new Vector3(50,50,50));
		testObj3.addToScene(activeScene);
		
		
		Renderer sky = new SkyBoxRenderer("/textures/skyBoxLayout.png");
		
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
