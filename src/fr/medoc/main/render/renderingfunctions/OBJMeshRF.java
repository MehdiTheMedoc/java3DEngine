package fr.medoc.main.render.renderingfunctions;

import static org.lwjgl.opengl.GL11.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import fr.medoc.main.math.Vector3;
import fr.medoc.main.render.texture.Texture;

public class OBJMeshRF implements RenderingFunction{
	
	private String path;
	private float scaleFactor;
	
	public static final OBJMeshRF skyboxMesh = new OBJMeshRF("/meshes/skybox.obj",500);
	public static HashMap<String,OBJMeshRF> meshes = new HashMap<String,OBJMeshRF>();
	
	public static void loadMeshes()
	{
		String file = Texture.class.getResource("/meshes.txt").getPath();
		
		file = "res/meshes.txt";
		
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(file))) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		        String[] parse = line.split(" ");
		        OBJMeshRF.meshes.put(parse[0], new OBJMeshRF("/meshes/"+parse[1] , Float.parseFloat(parse[2])));
		        System.out.println("loaded mesh : " +parse[1]);
		    }
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	public OBJMeshRF(String path)
	{
		this.path = path;
		this.scaleFactor = 1;
	}
	
	public OBJMeshRF(String path, float scaling)
	{
		this.path = path;
		this.scaleFactor = scaling;
	}

	@Override
	public void OnCall() {
		glPushMatrix();
		glScalef(scaleFactor,scaleFactor,scaleFactor);
		GenerateLoadedOBJMesh(path);
		glPopMatrix();
	}
	
	public static void GenerateLoadedOBJMesh(String path)
	{
		File file = new File(OBJMeshRF.class.getResource(path).getPath());
		
		ArrayList<Vector3> vertices = new ArrayList<Vector3>();
		ArrayList<Vector3> uvs = new ArrayList<Vector3>();
		ArrayList<Vector3> normals = new ArrayList<Vector3>();
		ArrayList<int[][]> facesQUAD = new ArrayList<int[][]>();
		ArrayList<int[][]> facesTRI = new ArrayList<int[][]>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       if(line.split(" ")[0].equals("v"))
		       {
		    	   vertices.add(parseVerticeOrNormal(line));
		       }
		       if(line.split(" ")[0].equals("vt"))
		       {
		    	   uvs.add(parseUV(line));
		       }
		       if(line.split(" ")[0].equals("vn"))
		       {
		    	   normals.add(parseVerticeOrNormal(line));
		       }
		       if(line.split(" ")[0].equals("f"))
		       {
		    	   int[][] face = parseFace(line);
		    	   if(face.length == 3)
		    		   facesTRI.add(face);
		    	   else
		    		   facesQUAD.add(face);
		       }
		    }
		    
		    glBegin(GL_QUADS);
		    for(int[][] face : facesQUAD)
		    {
		    	for(int i=0; i<face.length; i++)
		    	{
			    	glNormal3f(normals.get(face[i][2]).x , normals.get(face[i][2]).y, normals.get(face[i][2]).z);
			    	glTexCoord2f(uvs.get(face[i][1]).x , uvs.get(face[i][1]).y);
			    	glVertex3f(vertices.get(face[i][0]).x , vertices.get(face[i][0]).y, vertices.get(face[i][0]).z);
		    	}
		    }
		    glEnd();
		    glBegin(GL_TRIANGLES);
		    for(int[][] face : facesTRI)
		    {
		    	for(int i=0; i<face.length; i++)
		    	{
			    	glNormal3f(normals.get(face[i][2]).x , normals.get(face[i][2]).y, normals.get(face[i][2]).z);
			    	glTexCoord2f(uvs.get(face[i][1]).x , uvs.get(face[i][1]).y);
			    	glVertex3f(vertices.get(face[i][0]).x , vertices.get(face[i][0]).y, vertices.get(face[i][0]).z);
		    	}
		    }
		    glEnd();
		}catch(Exception e){
			System.out.println("error while parsing file");
			e.printStackTrace();
		}
	}
	
	public static Vector3 parseVerticeOrNormal(String line)
	{
		float x = Float.parseFloat(line.split(" ")[1]);
		float y = Float.parseFloat(line.split(" ")[2]);
		float z = Float.parseFloat(line.split(" ")[3]);
		return new Vector3(x,y,z);
	}
	
	public static Vector3 parseUV(String line)
	{
		float x = Float.parseFloat(line.split(" ")[1]);
		float y = 1-Float.parseFloat(line.split(" ")[2]);
		
		return new Vector3(x,y,0);
	}
	
	public static int[][] parseFace(String line)
	{
		String[] parsed = line.split(" ");
		int[][] res = new int[parsed.length-1][3];
		
		for(int i=0; i<parsed.length-1; i++)
		{
			res[i][0] = Integer.parseInt(parsed[i+1].split("/")[0])-1;
			res[i][1] = Integer.parseInt(parsed[i+1].split("/")[1])-1;
			res[i][2] = Integer.parseInt(parsed[i+1].split("/")[2])-1;
		}
		
		return res;
	}
	
}
