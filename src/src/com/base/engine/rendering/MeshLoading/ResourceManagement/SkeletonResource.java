package com.base.engine.rendering.MeshLoading.ResourceManagement;

import java.util.ArrayList;

import com.base.engine.animation.SkeletonLoading.Animation;
import com.base.engine.animation.SkeletonLoading.Bone;
import com.base.engine.animation.SkeletonLoading.Skeleton;
import com.base.engine.core.ReferenceCounter;
import com.base.engine.core.math.Vector2f;

public class SkeletonResource extends ReferenceCounter
{
	private ArrayList<Vector2f> weights;
	private ArrayList<Animation> animations;
	private ArrayList<Bone> bones;
	
	public SkeletonResource(ArrayList<Vector2f> weights, ArrayList<Animation> animations, ArrayList<Bone> bones)
	{
		this.weights = weights;
		this.animations = animations;
		this.bones = bones;
	}
	
	public ArrayList<Vector2f> getWeights()
	{
		return weights;
	}
	
	public Skeleton createSkeleton()
	{
		return new Skeleton(new ArrayList<Bone>(bones), new ArrayList<Animation>(animations));
	}
}
