package fr.medoc.main.game;

import fr.medoc.main.math.Vector3;
import fr.medoc.main.render.Renderer;

public class FullGameObject extends ColliderRendererGameObject{

	public FullGameObject(Renderer renderer, Vector3 colliderScale, Vector3 colliderCenter) {
		super(renderer, colliderScale, colliderCenter);
	}
	
	
	public static FullGameObject loadObject(String file)
	{
		Renderer r = null;
		Vector3 cs = null;
		Vector3 cc = null;
		
		//TODO: make the loading function
		
		return new FullGameObject(r, cs, cc);
	}
}
