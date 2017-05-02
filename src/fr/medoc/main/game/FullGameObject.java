package fr.medoc.main.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.lwjgl.util.vector.Vector2f;

import fr.medoc.main.math.Vector3;
import fr.medoc.main.render.MeshRenderer;
import fr.medoc.main.render.Renderer;
import fr.medoc.main.render.renderingfunctions.HeightMapRF;
import fr.medoc.main.render.renderingfunctions.OBJMeshRF;
import fr.medoc.main.render.texture.Texture;

public class FullGameObject extends ColliderRendererGameObject{

	public FullGameObject(Renderer renderer, Vector3 colliderScale, Vector3 colliderCenter) {
		super(renderer, colliderScale, colliderCenter);
	}
	
	
	public static GameObject loadObject(String file)
	{
		String name = "untitled";
		int type = 0;//0: gameobject, 1: renderergameobject, 2: colliderrenderergameobject
		int rendererType = 0;//0: heightmap, 1: mesh
		Texture tex = Texture.textures.get("default");
		Texture nor = Texture.textures.get("default_nor");
		Vector2f texuv = new Vector2f(1,1);
		Vector2f noruv = new Vector2f(1,1);
		float hmscalefactor = 1;
		Vector3 collScale = new Vector3(1,1,1);
		Vector3 collCenter = new Vector3();
		OBJMeshRF mesh = null;
		String heightmaptex = null;
		
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(file))) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		        String[] parse = line.split(" ");
		        if(parse[0].equals("name:"))
		        	name = parse[1];
		        if(parse[0].equals("type:")){
		        	if(parse[1].equals("colliderRenderer"))
		        		type = 2;
		        	if(parse[1].equals("renderer"))
		        		type = 1;
		        	if(parse[1].equals("gameobject"))
		        		type = 0;
		        }
		        if(parse[0].equals("texture:"))
		        	tex = Texture.textures.get(parse[1]);
		        if(parse[0].equals("normal:"))
		        	nor = Texture.textures.get(parse[1]);
		        if(parse[0].equals("renderer.type:")){
		        	if(parse[1].equals("heightmap"))
		        		rendererType = 0;
		        	if(parse[1].equals("mesh"))
		        		rendererType = 1;
		        }
		        if(parse[0].equals("renderer.mesh:"))
		        	mesh = OBJMeshRF.meshes.get(parse[1]);
		        if(parse[0].equals("renderer.heightmaptex:"))
		        	heightmaptex = parse[1];
		        if(parse[0].equals("renderer.texUVRepeat:"))
		        	texuv = new Vector2f(Float.parseFloat(parse[1]) , Float.parseFloat(parse[2]));
		        if(parse[0].equals("renderer.norUVRepeat:"))
		        	texuv = new Vector2f(Float.parseFloat(parse[1]) , Float.parseFloat(parse[2]));
		        if(parse[0].equals("heightmap.scaleFactor:"))
		        	hmscalefactor = Float.parseFloat(parse[1]);
		        if(parse[0].equals("collider.scale:"))
		        	collScale = new Vector3(Float.parseFloat(parse[1]) , Float.parseFloat(parse[2]), Float.parseFloat(parse[3]));
		        if(parse[0].equals("collider.center:"))
		        	collCenter = new Vector3(Float.parseFloat(parse[1]) , Float.parseFloat(parse[2]), Float.parseFloat(parse[3]));

		    }
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
		
		if(type == 0)
		{
			GameObject res = new GameObject();
			res.name = name;
			return res;
		}else if(type == 1 || type == 2){
			Renderer rend = null;
			if(rendererType == 0)
			{
				rend = new Renderer(tex, nor, new HeightMapRF(hmscalefactor,heightmaptex));
			}else if(rendererType == 1)
			{
				rend = new MeshRenderer(mesh,tex);
				rend.normal = nor;
			}
			
			rend.norUVRepeat = noruv;
			rend.texUVRepeat = texuv;
			
			if(type == 1)
			{
				RendererGameObject res = new RendererGameObject(rend);
				return res;
			}else if(type == 2)
			{
				ColliderRendererGameObject res = new ColliderRendererGameObject(rend,collScale,collCenter);
				return res;
			}
		}
		return null;
	}
}
