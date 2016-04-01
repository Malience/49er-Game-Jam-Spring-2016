package com.base.engine.animation.SkeletonLoading;

import java.util.ArrayList;

import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Transform;

public class Skeleton 
{
	ArrayList<Bone> bones;
	ArrayList<Animation> animations;
	
	public Skeleton()
	{
		bones = new ArrayList<Bone>();
		animations = new ArrayList<Animation>();
	}
	public Skeleton(ArrayList<Bone> bones, ArrayList<Animation> animations)
	{
		this.bones = bones;
		this.animations = animations;
	}
	
	public void addBone(Bone bone)
	{
		bones.add(bone);
	}
	
	public void addAnimation(Animation animation)
	{
		animations.add(animation);
	}
	
	public Animation getAnimation(int index)
	{
		return animations.get(index);
	}
	
	public int getBoneSize()
	{
		return bones.size();
	}
	
	public Transform getTransform(int index)
	{
		return bones.get(index).transform;
	}
	
	public void setPose(int index, Transform transform)
	{
		bones.get(index).setTransform(transform);
	}
	
	public Matrix4f[] getTransform()
	{
		Matrix4f[] transforms = new Matrix4f[bones.size()];
		for(int i = 0; i < transforms.length; i++)
		{
			transforms[i] = bones.get(i).getTransform();
		}
		return transforms;
	}
}
