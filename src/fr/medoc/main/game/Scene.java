package fr.medoc.main.game;

import java.util.ArrayList;

import fr.medoc.main.game.collisions.Collider;
import fr.medoc.main.game.collisions.CollisionDetectionAlgorithm;
import fr.medoc.main.game.collisions.HeightmapCollider;
import fr.medoc.main.math.ARGBColor;
import fr.medoc.main.math.Vector3;
import fr.medoc.main.render.Camera;
import fr.medoc.main.render.Renderer;

public class Scene {
	
	private ArrayList<Renderer> rendererList = new ArrayList<Renderer>();
	private ArrayList<GameObject> gameObjectList = new ArrayList<GameObject>();
	private Camera activeCamera;
	
	private CollisionDetectionAlgorithm collisions;
	
	public float fog_density = 0.0015f;
	public Vector3 fog_color = new Vector3(0.36862745098,0.36862745098,0.43137254902);
	public boolean enableFog = true;
	public float ambientLightIntensity = 0.25f;
	public Vector3 sunLightDirection = new Vector3(1,-0.3,1).normalized();
	public float sunLightIntensity = 2f;
	public ARGBColor sunColor = new ARGBColor(1,1,0.7f,0.4f);
	
	public Scene(CollisionDetectionAlgorithm colAlg)
	{
		collisions = colAlg;
	}
	
	
	
	public void update()
	{
		for(GameObject g : gameObjectList)
		{
			g.update();
		}
		
		collisions.collisionCheck();
	}
	
	
	public void render()
	{
		activeCamera.getPerspectiveProjection();
		activeCamera.CameraUpdate();
		
		for(Renderer r : rendererList)
		{
			r.render();
		}
	}
	
	
	
	
	public void setActiveCamera(Camera cam)
	{
		activeCamera = cam;
	}
	
	public Camera getActiveCamera()
	{
		return activeCamera;
	}
	
	public void addGameObject(GameObject obj)
	{
		gameObjectList.add(obj);
	}
	
	public void addRenderer(Renderer rend)
	{
		rendererList.add(rend);
	}
	
	public void addCollider(Collider col)
	{
		collisions.addCollider(col);
	}
	
	public void addHeightmapCollider(HeightmapCollider col)
	{
		collisions.addHeightmapCollider(col);
	}
}
