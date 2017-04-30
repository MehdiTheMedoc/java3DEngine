package fr.medoc.main.game.collisions;

import java.util.LinkedList;

import fr.medoc.main.math.Transform;
import fr.medoc.main.math.Vector3;

public class Collider {
	
	public Transform transform;
	public BoundingBox boundingBox;
	protected boolean isColliding = false;
	protected LinkedList<Collider> others = new LinkedList<Collider>();
	@SuppressWarnings("unused")
	private boolean collisionsComputed = false;
	
	public Collider(Transform t, Vector3 scale)
	{
		this(t,scale,new Vector3());
	}
	
	public Collider(Transform t, Vector3 scale, Vector3 center)
	{
		transform = t;
		boundingBox = new BoundingBox(this,scale,center);
	}
	
	public void addCollision(Collider other)
	{
		if(others.contains(other) == false && other.equals(this) == false)
		{
			others.add(other);
			isColliding = true;
		}
	}
	
	public void validateCollisions()
	{
		collisionsComputed = true;
		
		//DEBUG
		if(isColliding)
			System.out.println("colliding !");
	}
	
	public void reinitiateCollisions()
	{
		others.clear();
		collisionsComputed = false;
		isColliding = false;
	}
}