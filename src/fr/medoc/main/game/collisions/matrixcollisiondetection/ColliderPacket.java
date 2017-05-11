package fr.medoc.main.game.collisions.matrixcollisiondetection;

import java.util.LinkedList;

import fr.medoc.main.game.collisions.Collider;

public class ColliderPacket {
	public LinkedList<Collider> colliders = new LinkedList<Collider>();
	
	private int id = 0;
	
	public ColliderPacket(int id)
	{
		this.id = id;
	}
	
	
	public String toString()
	{
		return Integer.toString(id);
	}
	
	public void addCollider(Collider c)
	{
		if(colliders.contains(c) == false)
			colliders.add(c);
	}
	
	public void verifyCollisions()
	{
		Collider a;
		Collider b;
		for(int i=0; i<colliders.size()-1; i++)
		{
			a = colliders.get(i);
			
			for(int j=i+1; j<colliders.size(); j++)
			{	
				b = colliders.get(j);
				a.checkCollision(b);
			}
		}
		colliders.clear();
	}
}
