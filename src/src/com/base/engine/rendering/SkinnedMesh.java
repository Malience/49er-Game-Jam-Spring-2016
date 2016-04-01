package com.base.engine.rendering;

import java.util.ArrayList;
import java.util.HashMap;

import com.base.engine.animation.SkeletonLoading.Skeleton;
import com.base.engine.core.Util;
import com.base.engine.core.math.Vector2f;
import com.base.engine.rendering.MeshLoading.IQEModel;
import com.base.engine.rendering.MeshLoading.IndexedModel;
import com.base.engine.rendering.MeshLoading.ResourceManagement.SkeletonResource;

public class SkinnedMesh extends Mesh
{
	private static HashMap<String, SkeletonResource> loadedSkeletons = new HashMap<String, SkeletonResource>();
	private SkeletonResource skeleton;
	
	/**
	 * Loads in a mesh file in .obj format
	 * @param fileName Name of the file containing a mesh. Only accepts .obj files
	 */
	public SkinnedMesh(String fileName)
	{
		super(fileName);
		SkeletonResource oldSkeleton = loadedSkeletons.get(fileName);
		
		if(oldSkeleton != null)
		{
			skeleton = oldSkeleton;
			skeleton.addReference();
		}
		else
		{
			loadMesh(fileName);
			loadedSkeletons.put(fileName, skeleton);
		}
	}
	
	public ArrayList<Vector2f> getWeights()
	{
		return skeleton.getWeights();
	}
	
	public Skeleton createSkeleton()
	{
		return skeleton.createSkeleton();
	}
	
	@Override
	protected void finalize()
	{
		super.finalize();
		if(skeleton.removeReference() && !fileName.isEmpty())
			loadedSkeletons.remove(fileName);
	}
	
	private SkinnedMesh loadMesh(String fileName)
	{
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];
		
		
		if(!ext.equals("iqe"))
		{
			System.err.println("Error: File format not supported for mesh data: " + ext);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		IQEModel test = new IQEModel("./res/models/" + fileName);
		IndexedModel model = test.toIndexedModel();
		model.calcNormals();
		
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		
		for(int i = 0; i < model.getPositions().size(); i++)
		{
			vertices.add(new Vertex(model.getPositions().get(i), 
					model.getTexCoords().get(i),
					model.getNormals().get(i)));
		}
		
		Vertex[] vertexData = new Vertex[vertices.size()];
		vertices.toArray(vertexData);
		
		Integer[] indexData = new Integer[model.getIndices().size()];
		model.getIndices().toArray(indexData);
		
		
		super.addVertices(vertexData, Util.toIntArray(indexData), false);
		
		skeleton = new SkeletonResource(test.toWeights(), test.toAnimations(), test.toBones());
		
		return null;
	}
}