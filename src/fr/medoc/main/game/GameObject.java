package fr.medoc.main.game;

import fr.medoc.main.math.Transform;

/**
 * 
 * the main parent class to every objects in the game
 * it contains the transform, that contains all the spatial informations
 * of the object
 * 
 * @author Medoc
 *
 */
public class GameObject {
	public Transform transform = new Transform();
	
	public GameObject()
	{
	}
	
	public void update()
	{
		
	}
}
