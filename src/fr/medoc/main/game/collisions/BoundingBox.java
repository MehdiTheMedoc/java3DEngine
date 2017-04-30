package fr.medoc.main.game.collisions;

import fr.medoc.main.math.Vector3;

public class BoundingBox {
	private Vector3 position;
	private Vector3 center;
	public Collider collider;
	public Vector3 scale; // |<- scale/2 -> + <- scale/2 ->|
	
	public BoundingBox(Collider collider, Vector3 scale, Vector3 center)
	{
		this.collider = collider;
		this.position = this.collider.transform.position;
		this.scale = scale;
		this.center = center;
	}
	
	public Vector3 getPosition()
	{
		return Vector3.add(center, position);
	}
	
	
	public boolean pointCollision(Vector3 point)
	{
		if(point.x > getPosition().x + scale.x*0.5f) return false;
		if(point.x < getPosition().x - scale.x*0.5f) return false;
		if(point.y > getPosition().y + scale.y*0.5f) return false;
		if(point.y < getPosition().y - scale.y*0.5f) return false;
		if(point.z > getPosition().z + scale.z*0.5f) return false;
		if(point.z < getPosition().z - scale.z*0.5f) return false;
		return true;
	}
	
	public boolean boundingBoxCollision(BoundingBox other)
	{
		if(other.getPosition().x - other.scale.x*0.5f > getPosition().x + scale.x*0.5f) return false;
		if(other.getPosition().x + other.scale.x*0.5f < getPosition().x - scale.x*0.5f) return false;
		if(other.getPosition().y - other.scale.y*0.5f > getPosition().y + scale.y*0.5f) return false;
		if(other.getPosition().y + other.scale.y*0.5f < getPosition().y - scale.y*0.5f) return false;
		if(other.getPosition().z - other.scale.z*0.5f > getPosition().z + scale.z*0.5f) return false;
		if(other.getPosition().z + other.scale.z*0.5f < getPosition().z - scale.z*0.5f) return false;
		return true;
	}
}