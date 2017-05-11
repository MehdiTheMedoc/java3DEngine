package fr.medoc.main.game.collisions;

import java.util.Iterator;
import java.util.LinkedList;

import fr.medoc.main.game.GameObjectComponent;
import fr.medoc.main.game.Scene;
import fr.medoc.main.math.Transform;
import fr.medoc.main.math.Vector3;

public class Collider implements Iterable<Collision>, GameObjectComponent{
	
	public Transform transform;
	public BoundingBox boundingBox;
	private boolean isColliding = false;
	private LinkedList<Collision> collisions = new LinkedList<Collision>();
	@SuppressWarnings("unused")
	private boolean collisionsComputed = false;
	private LinkedList<Vector3> ColliderShape = new LinkedList<Vector3>();
	
	public Collider(Transform t, Vector3 scale)
	{
		this(t,scale,new Vector3());
	}
	
	public Collider(Transform t, Vector3 scale, Vector3 center)
	{
		transform = t;
		boundingBox = new BoundingBox(this,scale,center);
	}
	
	
	public void checkCollision(Collider other)
	{
		if( this.boundingBox.boundingBoxCollision(other.boundingBox))
		{
			this.addCollision(new Collision(other, null, null));
			other.addCollision(new Collision(this, null, null));
		}
	}
	
	
	public boolean colliderIsColliding()
	{
		return isColliding;
	}
	
	public void addCollision(Collision c)
	{
		collisions.add(c);
	}
	
	public void validateCollisions()
	{
		collisionsComputed = true;
		
		//DEBUG
		/*if(isColliding)
			System.out.println("colliding !");*/
	}
	
	public void reinitiateCollisions()
	{
		collisions.clear();
		collisionsComputed = false;
		isColliding = false;
	}

	@Override
	public Iterator<Collision> iterator() {
		return collisions.iterator();
	}

	@Override
	public void update() {
		//nothing to do here
	}

	@Override
	public void addToScene(Scene s) {
		s.addCollider(this);
	}


}
