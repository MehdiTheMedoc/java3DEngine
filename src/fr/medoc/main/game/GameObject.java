package fr.medoc.main.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

import fr.medoc.main.game.collisions.Collider;
import fr.medoc.main.game.collisions.HeightmapCollider;
import fr.medoc.main.math.Transform;
import fr.medoc.main.math.Vector3;
import fr.medoc.main.render.Renderer;
import fr.medoc.main.render.renderingfunctions.HeightMapRF;
import fr.medoc.main.render.renderingfunctions.OBJMeshRF;
import fr.medoc.main.render.texture.Texture;

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
	public String name = "gameobject";
	public LinkedList<GameObjectComponent> components = new LinkedList<GameObjectComponent>();
	
	public GameObject()
	{
	}
	
	public void update()
	{
		for(GameObjectComponent i : components)
		{
			i.update();
		}
	}
	
	/**
	 * adds a component to the gameObject
	 * BE CAREFUL
	 * this function doesn't add the component to the scene
	 * (needed when it is a collider or a renderer for example)
	 * 
	 * @param c
	 */
	public void addComponent(GameObjectComponent c)
	{
		components.add(c);
	}
	
	
	public void addComponentAndAddToScene(GameObjectComponent c, Scene s)
	{
		addComponent(c);
		c.addToScene(s);
	}
	
	public void addToScene(Scene scene)
	{
		scene.addGameObject(this);
		
		for(GameObjectComponent i : components)
		{
			i.addToScene(scene);
		}
	}
	
	public static GameObject loadObject(String file, Vector3 position)
	{
		GameObject res = loadObject(file);
		res.transform.position = position.copy();
		return res;
	}
	
	public static GameObject loadObject(String file)
	{
		GameObject object = new GameObject();
		ArrayList<GameObjectComponent> components = new ArrayList<GameObjectComponent>();
		
		Vector3 tempVector0 = null;
		
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(file))) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		        String[] parse = line.split(" ");
		        
		        switch(parse[0])
		        {
			        case "name:":
			        	object.name = parse[1];
			        	break;
			        case "component_number:":
			        	//I dunno what to do here for the moment
			        	break;
			        case "component_data:":
			        	String[] temp = parse[1].split("_");
			        	int compNb = Integer.parseInt(temp[0]);
			        	String[] parsedata = temp[1].split("=");
			        	switch(parsedata[0])
				        {
					        case "type":
					        	switch(parsedata[1])
						        {
							        case "RENDERER":
							        	components.add(new Renderer());
							        	break;
							        case "COLLIDER":
							        	components.add(new Collider(object.transform, new Vector3()));
							        	break;
							        case "HEIGHTMAPCOLLIDER":
							        	components.add(new HeightmapCollider(object.transform));
							        	break;
						        }
					        	break;
					        case "renderer.texture":
					        	((Renderer)components.get(compNb)).texture = Texture.textures.get(parsedata[1]);
					        	break;
					        case "renderer.normalmap":
					        	((Renderer)components.get(compNb)).normal = Texture.textures.get(parsedata[1]);
					        	break;
					        case "renderer.heightmap.scaleFactor":
					        	String[] t = parsedata[1].split(",");
					        	tempVector0 = new Vector3(Float.parseFloat(t[0]),Float.parseFloat(t[1]),Float.parseFloat(t[2]));
					        	break;
					        case "renderer.heightmaptex":
					        	((Renderer)components.get(compNb)).setCompileRenderingFunction(new HeightMapRF(tempVector0 , parsedata[1]));
					        	break;
					        case "renderer.mesh":
					        	((Renderer)components.get(compNb)).setCompileRenderingFunction(OBJMeshRF.meshes.get(parsedata[1]));
					        	break;
					        case "renderer.texUVRepeat":
					        	String[] t1 = parsedata[1].split(",");
					        	((Renderer)components.get(compNb)).texUVRepeat.x = Float.parseFloat(t1[0]);
					        	((Renderer)components.get(compNb)).texUVRepeat.y = Float.parseFloat(t1[1]);
					        	break;
					        case "renderer.norUVRepeat":
					        	String[] t2 = parsedata[1].split(",");
					        	((Renderer)components.get(compNb)).norUVRepeat.x = Float.parseFloat(t2[0]);
					        	((Renderer)components.get(compNb)).norUVRepeat.y = Float.parseFloat(t2[1]);
					        	break;
					        case "renderer.shaderSpecular":
					        	((Renderer)components.get(compNb)).shaderSpecular = Float.parseFloat(parsedata[1]);
					        	break;
					        case "renderer.shaderHardness":
					        	((Renderer)components.get(compNb)).shaderHardness = Float.parseFloat(parsedata[1]);
					        	break;
					        case "renderer.normalFactor":
					        	((Renderer)components.get(compNb)).normalFactor = Float.parseFloat(parsedata[1]);
					        	break;
					        case "collider.scale":
					        	String[] t3 = parsedata[1].split(",");
					        	((Collider)components.get(compNb)).boundingBox.scale = new Vector3(Float.parseFloat(t3[0]),Float.parseFloat(t3[1]),Float.parseFloat(t3[2]));
					        	break;
					        case "collider.center":
					        	String[] t4 = parsedata[1].split(",");
					        	((Collider)components.get(compNb)).boundingBox.center = new Vector3(Float.parseFloat(t4[0]),Float.parseFloat(t4[1]),Float.parseFloat(t4[2]));
					        	break;
					        case "heightmapcollider.heightmaptex":
					        	((HeightmapCollider)components.get(compNb)).file = parsedata[1];
					        	((HeightmapCollider)components.get(compNb)).updateValues();
					        	break;
					        case "heightmapcollider.scaleFactor":
					        	String[] t5 = parsedata[1].split(",");
					        	((HeightmapCollider)components.get(compNb)).HMscale = new Vector3(Float.parseFloat(t5[0]),Float.parseFloat(t5[1]),Float.parseFloat(t5[2]));
					        	((HeightmapCollider)components.get(compNb)).updateValues();
					        	break;
					        default:
					        	System.out.println("unknown identifier " + parsedata[0]);
					        	break;
				        }
			        	break;
			        default:
			        	System.out.println("unknown identifier " + parse[0]);
			        	break;
		        }
		    }
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
		
		for(GameObjectComponent i : components)
			object.addComponent(i);
		
		return object;
	}

}
