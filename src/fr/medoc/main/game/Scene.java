package fr.medoc.main.game;

import java.util.ArrayList;

import fr.medoc.main.math.Vector3;
import fr.medoc.main.render.Camera;
import fr.medoc.main.render.Renderer;

public class Scene {
	
	private ArrayList<Renderer> rendererList = new ArrayList<Renderer>();
	private ArrayList<GameObject> gameObjectList = new ArrayList<GameObject>();
	private Camera activeCamera;
	
	public float fog_density = 0.0015f;
	public Vector3 fog_color = new Vector3(1,1,1);
	public boolean enableFog = true;
	public float ambientLightIntensity = 0.25f;
	public Vector3 sunLightDirection = new Vector3(0,-1,1).normalized();
	public float sunLightIntensity = 1.5f;
	
	public Scene()
	{
		
	}
	
	
	
	public void update()
	{
		for(GameObject g : gameObjectList)
		{
			g.update();
		}
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
}
