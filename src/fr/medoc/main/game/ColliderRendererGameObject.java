package fr.medoc.main.game;

import fr.medoc.main.game.collisions.Collider;
import fr.medoc.main.math.Vector3;
import fr.medoc.main.render.Renderer;

public class ColliderRendererGameObject extends RendererGameObject{

	Collider collider;
	
	public ColliderRendererGameObject(Renderer renderer, Vector3 colliderScale) {
		super(renderer);
		this.collider = new Collider(transform, colliderScale, new Vector3());
	}
	
	
	public ColliderRendererGameObject(Renderer renderer, Vector3 colliderScale, Vector3 colliderCenter) {
		super(renderer);
		this.collider = new Collider(transform, colliderScale, colliderCenter);
	}
	
	public void addToScene(Scene scene)
	{
		super.addToScene(scene);
		scene.addCollider(collider);
	}

}
