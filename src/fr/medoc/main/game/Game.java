package fr.medoc.main.game;

import org.lwjgl.util.vector.Vector2f;

import fr.medoc.main.game.collisions.Collider;
import fr.medoc.main.game.collisions.matrixcollisiondetection.MatrixCollisionDetection;
import fr.medoc.main.math.ARGBColor;
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
	
	Renderer rend;
	Renderer rend2;
	Collider coll2;
	Collider coll;
	MeshRenderer rend3;
	
	public Game()
	{
		activeScene = new Scene(new MatrixCollisionDetection(20,20));
		
		Camera cam;
		
		Renderer rend1;
		
		
		cam = new CustomCamera();
		cam.transform.position.y += 50;
		cam.setPerspectiveProjection(75.0f, 0.1f, 1000.0f);
		
		rend = new Renderer(new Texture("/textures/grass.png"));
		rend.setCompileRenderingFunction(new HeightMapRF(0.5f,"/textures/heightmap.png"));
		rend.setShader(null);
		coll = new Collider(rend.transform, new Vector3(128,1,128), new Vector3(64,0,64));
		
		rend1 = new Renderer( new Texture("/textures/bube.png",new Vector2f(5,5)));
		rend1.setCompileRenderingFunction(new HeightMapRF(1f,"/textures/heightmap.png"));
		rend1.transform.position = new Vector3(128,0,128);
		
		rend2 = new MeshRenderer("/meshes/suzanne.obj","/textures/suzanne.png",50);
		rend2.setShader(null);
		rend2.transform.position = new Vector3(-100,0,0);
		coll2 = new Collider(rend2.transform, new Vector3(50,50,50));
		
		
		Renderer sky = new SkyBoxRenderer("/textures/skyBoxLayout.png");
		
		activeScene.addRenderer(rend);
		activeScene.addRenderer(rend1);
		activeScene.addRenderer(rend2);
		activeScene.addRenderer(sky);
		activeScene.addGameObject(cam);
		activeScene.addCollider(coll);
		activeScene.addCollider(coll2);
		activeScene.setActiveCamera(cam);
	}
	
	public void update()
	{
		/*rend.transform.eulerAngles.x += 1;
		rend2.transform.eulerAngles.z += 1;*/
		//rend.transform.eulerAngles.y += 1;

		if(coll2.colliderIsColliding())
			rend2.color = new ARGBColor(1,1,0,0);
		else
			rend2.color = new ARGBColor(1,1,1,1);
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
