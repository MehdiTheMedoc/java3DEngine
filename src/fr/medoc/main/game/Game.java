package fr.medoc.main.game;


import fr.medoc.main.game.collisions.matrixcollisiondetection.MatrixCollisionDetection;
import fr.medoc.main.math.Vector3;
import fr.medoc.main.render.Camera;
import fr.medoc.main.render.CustomCamera;
import fr.medoc.main.render.Renderer;
import fr.medoc.main.render.SkyBoxRenderer;
import fr.medoc.main.render.renderingfunctions.OBJMeshRF;
import fr.medoc.main.render.texture.Texture;

public class Game {
	
	private static Scene activeScene;
	
	public Game()
	{
		activeScene = new Scene(new MatrixCollisionDetection(20,20));
		
		Texture.loadTextures();
		OBJMeshRF.loadMeshes();
		
		Camera cam;
		
		cam = new CustomCamera();
		cam.transform.position.y += 50;
		cam.setPerspectiveProjection(75.0f, 0.1f, 1000.0f);
		
		GameObject testObj = FullGameObject.loadObject("res/gameobjects/rocks.txt");
		testObj.addToScene(activeScene);
		
		GameObject testObj2 = FullGameObject.loadObject("res/gameobjects/bubeHM.txt", new Vector3(128,0,0));
		testObj2.addToScene(activeScene);
		
		GameObject testObj3 = FullGameObject.loadObject("res/gameobjects/suzanne.txt");
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
