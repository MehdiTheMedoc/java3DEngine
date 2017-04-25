package fr.medoc.main.render;

public class CustomCamera extends Camera{

	public void update()
	{
		transform.simpleZQSDMove();
		transform.simpleMouseLook();
	}
}
