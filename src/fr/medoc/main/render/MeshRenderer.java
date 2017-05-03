package fr.medoc.main.render;

import fr.medoc.main.render.renderingfunctions.OBJMeshRF;
import fr.medoc.main.render.texture.Texture;

public class MeshRenderer extends Renderer{
	
	public MeshRenderer(OBJMeshRF mesh, Texture tex)
	{
		super(tex, mesh);
	}
	
	
	
	public MeshRenderer(OBJMeshRF mesh, String texturePath)
	{
		super(new Texture(texturePath), mesh);
	}
	
	public MeshRenderer(String meshPath, String texturePath)
	{
		this(texturePath, meshPath, 1);
	}
	
	public MeshRenderer(String meshPath, String texturePath, float meshScaling)
	{
		super(new Texture(texturePath), new OBJMeshRF(meshPath , meshScaling));
	}
}
