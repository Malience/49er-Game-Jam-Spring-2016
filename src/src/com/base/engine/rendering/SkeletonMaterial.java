package com.base.engine.rendering;

import java.util.ArrayList;
import java.util.HashMap;

import com.base.engine.animation.SkeletonLoading.Skeleton;
import com.base.engine.core.Util;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Vector2f;

public class SkeletonMaterial extends Material
{
	Skeleton skeleton;
	
	public SkeletonMaterial()
	{
		super();
	}
	
	public SkeletonMaterial(Material material)
	{
		super();
		this.textureHashMap = new HashMap<String, Texture>(material.textureHashMap);
	}
	
	public void getBoneValues(SkinnedMesh mesh)
	{
		ArrayList<Vector2f> vec = mesh.getWeights();
		int size = vec.size();
		int[] indices = new int[size];
		float[] weights = new float[size];
		for(int i = 0; i < size; i++)
		{
			Vector2f v = vec.get(i);
			indices[i] = (int)v.x;
			weights[i] = v.y;
		}
		this.addBuffer("index", Util.arrayToIntBuffer(indices));
		this.addBuffer("weight", Util.arrayToFloatBuffer(weights));
	}
	
	public Matrix4f[] getBones()
	{
		return skeleton.getTransform();
	}
	
	public void setSkeleton(Skeleton skeleton)
	{
		this.skeleton = skeleton;
	}
}
