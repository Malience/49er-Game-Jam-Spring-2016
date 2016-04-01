package com.base.engine.rendering.UI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import com.base.engine.core.Util;
import com.base.engine.core.math.Vector2f;

public class UIMesh 
{
	//TODO: loading shenanigans
	//private static HashMap<String, MeshResource> loadedModels = new HashMap<String, MeshResource>();
	protected String fileName;
	private Vector2f[] vertices;
	private Vector2f[] texCoords;
	private Integer[] indices;
	
	/**
	 * Loads in a mesh file in .obj format
	 * @param fileName Name of the file containing a mesh. Only accepts .obj files
	 */
	public UIMesh(String fileName)
	{
		this.fileName = fileName;
//		MeshResource oldResource = loadedModels.get(fileName);
//		
//		if(oldResource != null)
//		{
//			resource = oldResource;
//			resource.addReference();
//		}
//		else
//		{
//			loadMesh(fileName);
//			loadedModels.put(fileName, resource);
//		}
		loadUIMesh(fileName);
	}
	
	/**
	 * Creates Mesh using vertices and indices. The indices decide the direction in which an object is drawn
	 * @param vertices The vertices of the mesh
	 * @param indices An array of indices in reference to the vertices array
	 */
	public UIMesh(Vector2f[] vertices, Vector2f[] texCoords, Integer[] indices)
	{
		fileName = " ";
		this.vertices = vertices;
		this.texCoords = texCoords;
		this.indices = indices;
		//addVertices(vertices, indices);
	}
	
//	@Override
//	protected void finalize()
//	{
//		if(resource.removeReference() && !fileName.isEmpty())
//			loadedModels.remove(fileName);
//	}
//	
//	protected void addVertices(Vertex[] vertices, int[] indices)
//	{
//		resource = new MeshResource(indices.length);
//		
//		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
//		GL15.glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);
//		
//		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
//		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);
//	}
	
	//TODO: Make uimesh more like mesh and thus making ui more efficient
//	public void draw()
//	{
//		glEnableVertexAttribArray(0);
//		glEnableVertexAttribArray(1);
//		glEnableVertexAttribArray(2);
//		
//		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
//		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
//		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12);
//		glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20);
//		
//		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
//		glDrawElements(GL_TRIANGLES, resource.getSize(), GL_UNSIGNED_INT, 0);
//		
//		glDisableVertexAttribArray(0);
//		glDisableVertexAttribArray(1);
//		glDisableVertexAttribArray(2);
//	}
	
	private void loadUIMesh(String fileName)
	{
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];
		
		
		if(!ext.equals("obj"))
		{
			System.err.println("Error: File format not supported for mesh data: " + ext);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		ArrayList<Vector2f> vertices = new ArrayList<Vector2f>();
		ArrayList<Vector2f> texCoords = new ArrayList<Vector2f>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		boolean hasTexCoords = false;
		
		BufferedReader meshReader = null;
		
		try
		{
			meshReader = new BufferedReader(new FileReader("./res/models/" + fileName));
			String line;
			
			while((line = meshReader.readLine()) != null)
			{
				String[] tokens = line.split(" ");
				tokens = Util.removeEmptyStrings(tokens);
				
				if(tokens.length == 0 || tokens[0].equals("#"))
					continue;
				else if(tokens[0].equals("v"))
				{
					vertices.add(new Vector2f(Float.valueOf(tokens[1]),
												Float.valueOf(tokens[2])));
				}
				else if(tokens[0].equals("vt"))
				{
					texCoords.add(new Vector2f(Float.valueOf(tokens[1]),
							Float.valueOf(tokens[2])));
				}
				else if(tokens[0].equals("f"))
				{
					for(int i = 0; i < tokens.length - 3; i++)
					{
					indices.add(parseIndex(tokens[1]));
					indices.add(parseIndex(tokens[2 + i]));
					indices.add(parseIndex(tokens[3 + i]));
					}
				}
			}
			
			this.vertices = new Vector2f[vertices.size()];
			vertices.toArray(this.vertices);
			
			this.texCoords = new Vector2f[texCoords.size()];
			texCoords.toArray(this.texCoords);
			
			this.indices = new Integer[indices.size()];
			indices.toArray(this.indices);
			
			meshReader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		//addVertices(vertexData, Util.toIntArray(indexData), false);
	}
	
	private Integer parseIndex(String token)
	{
		String[] values = token.split("/");
//		if(values.length > 1)
//		{
//			hasTexCoords = true;
//			result.texCoordIndex = Integer.parseInt(values[1]) - 1;
//		}
		
		return Integer.parseInt(values[0]) - 1;
	}
	
	public Vector2f[] getVertices()
	{
		return vertices;
	}
	
	public Vector2f[] getTexCoords()
	{
		return texCoords;
	}
	
	public Integer[] getIndices()
	{
		return indices;
	}
	
	@Override
	public String toString()
	{
		if(fileName.equals(" "))
		{
			return "UIMesh";
		}
		else
		{
			return "UIMesh." + fileName;
		}
	}
	
}
