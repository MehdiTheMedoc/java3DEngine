package fr.medoc.main.render;

import fr.medoc.main.game.Game;
import fr.medoc.main.render.renderingfunctions.OBJMeshRF;

public class SkyBoxRenderer extends MeshRenderer{
	
	public SkyBoxRenderer(String skyboxTexturePath)
	{
		super(OBJMeshRF.skyboxMesh, skyboxTexturePath);
		shader = Shader.SKYBOX;
	}
	
	
	public void render()
	{
		transform.position = Game.getActiveScene().getActiveCamera().transform.position;
		super.render();
	}
}
