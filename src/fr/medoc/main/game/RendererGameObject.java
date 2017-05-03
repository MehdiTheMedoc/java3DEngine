package fr.medoc.main.game;

import fr.medoc.main.render.Renderer;

public class RendererGameObject extends GameObject{
	
	Renderer renderer;

	public RendererGameObject(Renderer renderer)
	{
		super();
		this.renderer = renderer;
		this.renderer.transform = this.transform;
	}
	
	public void update()
	{
		super.update();
	}
	
	public void addToScene(Scene scene)
	{
		super.addToScene(scene);
		scene.addRenderer(renderer);
	}
}
