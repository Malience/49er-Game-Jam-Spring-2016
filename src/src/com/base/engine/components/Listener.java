package com.base.engine.components;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.ALContext;
import org.lwjgl.openal.ALDevice;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.math.Vector3f;

public class Listener extends GameComponent
{
	private float radius;
	private float totalVolume;
	private float soundEffectVolume;
	private float musicVolume;
//	private float voiceVolume;
	
	private ALContext context;
	private ALDevice device;
	
	public Listener(float radius, float totalVolume, float soundEffectVolume, float musicVolume)
	{
		this.radius = radius;
		this.totalVolume = totalVolume;
		this.soundEffectVolume = soundEffectVolume;
		this.musicVolume = musicVolume;
		
		context = ALContext.create();
		device = context.getDevice();
	}

	public FloatBuffer getBufferPosition()
	{
		Vector3f listenerPos = getTransform().getTransformedPos().mul(-1);
		return BufferUtils.createFloatBuffer(3).put(new float[] { listenerPos.getX(), listenerPos.getY(), listenerPos.getZ()});
	}
	
	public Vector3f getPosition()
	{
		return getTransform().getTransformedPos().mul(-1);
	}
	
	public FloatBuffer getVelocity()
	{
		//TODO: Apply physics for OpenAL
		//Vector3f listenerVel = getTransform().getTransformedPos().mul(-1);
		return BufferUtils.createFloatBuffer(3).put(new float[] { 0,0,0});
	}
	
	public FloatBuffer getOrientation()
	{
		//TODO: Apply rotations for OpenAL
		//Vector3f listenerVel = getTransform().getTransformedPos().mul(-1);
		return BufferUtils.createFloatBuffer(3).put(new float[] { 0,0,0});
	}
	
	public float getRadius()
	{
		return radius;
	}
	
	public float getTotalVolume()
	{
		return totalVolume;
	}
	
	public float getSoundEffectVolume()
	{
		return soundEffectVolume;
	}
	
	public float getMusicVolume()
	{
		return musicVolume;
	}

	public ALContext getContext() 
	{
		return context;
	}

	public ALDevice getDevice() 
	{
		return device;
	}

	@Override
	public void finalize()
	{
		device.destroy();
		context.destroy();
	}
	
	@Override
	public void addToEngine(CoreEngine engine)
	{
		//engine.getAudioEngine().addListener(this);
	}
}
