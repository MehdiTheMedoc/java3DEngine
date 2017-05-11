package fr.medoc.main.game.collisions;

import java.awt.image.BufferedImage;


import fr.medoc.main.game.GameObjectComponent;
import fr.medoc.main.game.Scene;
import fr.medoc.main.math.Transform;
import fr.medoc.main.math.Vector3;
import fr.medoc.main.render.texture.ImageUtil;

public class HeightmapCollider implements GameObjectComponent{

	public Transform transform;
	private float[][] heights;
	private Vector3[][] normals;
	public Vector3 HMscale = new Vector3();
	public String file;
	
	public HeightmapCollider(Transform t) {
		this.transform = t;
	}
	
	public HeightmapCollider(Transform t, Vector3 hmscale, String file) {
		this.HMscale = hmscale;
		this.transform = t;
		this.file = file;
		updateValues();
	}
	
	public void updateValues()
	{
		fillHeights(file);
		fillNormals();
	}

	
	private void fillHeights(String file)
	{
		BufferedImage map = ImageUtil.loadImage(file);
		
		int width = map.getWidth();
		int height = map.getHeight();
		
		heights = new float[width][height];
		
		for(int x=0; x<width; x++)
		{
			for(int z=0; z<height; z++)
			{
				int pixel3 = map.getRGB(x, z);
				heights[x][z] = ImageUtil.pixelToARGB(pixel3).getLuminance()*HMscale.y;
			}
		}
	}
	
	private void fillNormals()
	{
		normals = new Vector3[heights.length][heights[0].length];
		
		for(int x=0; x<heights.length-1; x++)
		{
			for(int z=0; z<heights[0].length-1; z++)
			{
				normals[x][z] = calculateNormal(x,z);
			}
		}
	}
	
	
	public Vector3 checkCollisionNormal(Vector3 point)
	{
		Vector3 pos = Vector3.sub(point, transform.position);
		int x = (int) (pos.x / HMscale.x);
		int z = (int) (pos.z / HMscale.z);
		float xMixVal = (pos.x / HMscale.x) - x;
		float zMixVal = (pos.z / HMscale.z) - z;
		
		if(x < 0 || x > heights.length-2 || z < 0 || z > heights[0].length-2)
			return null;
		
		float height = (heights[x][z] * xMixVal + heights[x+1][z] * (1-xMixVal)) * 0.5f;
		height = (height + (heights[x][z] * zMixVal + heights[x][z+1] * (1-zMixVal)) * 0.5f)*0.5f;
		
		if(height < pos.y)
		{
			return null;
		}
		
		return normals[x][z].copy();
	}
	
	
	private Vector3 calculateNormal(int x, int z)
	{
		Vector3 res = new Vector3();
		
		res = Vector3.cross(new Vector3(0,heights[x][z+1] - heights[x][z],HMscale.z), new Vector3(HMscale.x,heights[x+1][z] - heights[x][z],0));
		
		return res.normalized();
	}


	@Override
	public void update() {
		//nothing to do here
	}


	@Override
	public void addToScene(Scene s) {
		s.addHeightmapCollider(this);
	}
}
