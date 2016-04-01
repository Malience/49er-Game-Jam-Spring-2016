package com.base.engine.animation.SkeletonLoading;

import java.util.ArrayList;

public class Animation 
{
	String name;
	float framerate;
	ArrayList<KeyFrame> frames;
	
	public static final float DEFAULT_FRAMERATE = 60;
	
	public Animation(float framerate){this("", framerate);}
	public Animation(String name){this(name, DEFAULT_FRAMERATE);}
	public Animation(String name, float framerate)
	{
		this.name = name;
		this.framerate = framerate;
		frames = new ArrayList<KeyFrame>();
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public KeyFrame getFrame(int index)
	{
		return frames.get(index);
	}
	
	public int getFrames()
	{
		return frames.size();
	}
	
	public void addPose(Pose pose)
	{
		frames.get(frames.size() - 1).addPose(pose);
	}
	
	public void addFrame(KeyFrame frame)
	{
		frames.add(frame);
	}
	
	public float getFramerate()
	{
		return framerate;
	}
	
	public void setFramerate(Float framerate) 
	{
		this.framerate = framerate;
	}
}
