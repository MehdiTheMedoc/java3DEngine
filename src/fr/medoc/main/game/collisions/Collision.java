package fr.medoc.main.game.collisions;

import fr.medoc.main.math.Vector3;

public class Collision {
	public Collider collider;
	public Vector3 point;
	public Vector3 normal;
	
	public Collision(Collider c, Vector3 p, Vector3 n)
	{
		collider = c;
		point = p;
		normal = n;
	}
}
