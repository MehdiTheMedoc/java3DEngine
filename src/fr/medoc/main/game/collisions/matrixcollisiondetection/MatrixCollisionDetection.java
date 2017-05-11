package fr.medoc.main.game.collisions.matrixcollisiondetection;

import java.util.ArrayList;

import fr.medoc.main.game.collisions.Collider;
import fr.medoc.main.game.collisions.CollisionDetectionAlgorithm;
import fr.medoc.main.game.collisions.HeightmapCollider;

public class MatrixCollisionDetection implements CollisionDetectionAlgorithm{
	
	private ArrayList<Collider> colliders = new ArrayList<Collider>();
	private ArrayList<HeightmapCollider> heightmapcolliders = new ArrayList<HeightmapCollider>();
	private ColliderPacket[][] colliderMatrix;
	
	private float xmin = -5000;
	private float xmax = 5000;
	private float zmin = -5000;
	private float zmax = 5000;
	
	private int resolutionX;
	private int resolutionZ;
	
	public MatrixCollisionDetection(int resolutionX, int resolutionZ)
	{
		this.resolutionX = resolutionX;
		this.resolutionZ = resolutionZ;
		
		colliderMatrix = new ColliderPacket[resolutionX][resolutionZ];
		
		for(int i=0; i<colliderMatrix.length; i++)
		{
			for(int j=0; j<colliderMatrix[i].length; j++)
			{
				colliderMatrix[i][j] = new ColliderPacket(i+j*resolutionX);
			}
		}
	}
	
	
	@Override
	public void collisionCheck() {
		for(Collider c : colliders)
			c.reinitiateCollisions();
		
		for(Collider c : colliders)
			addToPackets(c);
		
		for(ColliderPacket[] i : colliderMatrix)
		{
			for(ColliderPacket j : i)
			{
				j.verifyCollisions();
			}
		}
		
		for(Collider c : colliders)
			c.validateCollisions();
	}

	@Override
	public void addCollider(Collider collider) {
		// TODO Auto-generated method stub
		colliders.add(collider);
	}

	@Override
	public void removeCollider(Collider collider) {
		// TODO Auto-generated method stub
		colliders.remove(collider);
	}
	
	
	private void addToPackets(Collider c)
	{
		int xbeg = indexX(c.boundingBox.getPosition().x - c.boundingBox.scale.x*0.5f);
		int xend = indexX(c.boundingBox.getPosition().x + c.boundingBox.scale.x*0.5f);
		
		int zbeg = indexZ(c.boundingBox.getPosition().z - c.boundingBox.scale.z*0.5f);
		int zend = indexZ(c.boundingBox.getPosition().z + c.boundingBox.scale.z*0.5f);
		
		for(int x = xbeg; x<xend+1; x++)
			for(int z = zbeg; z<zend+1; z++)
				colliderMatrix[x][z].addCollider(c);
	}
	
	private int indexX(float x)
	{
		return (int) ((x - xmin)/(xmax-xmin)*resolutionX);
	}
	
	private int indexZ(float z)
	{
		return (int) ((z - zmin)/(zmax-zmin)*resolutionZ);
	}


	@Override
	public void addHeightmapCollider(HeightmapCollider collider) {
		heightmapcolliders.add(collider);
	}

}
