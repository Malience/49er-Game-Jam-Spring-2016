package com.base.engine.physics.collision;

import com.base.engine.components.attachments.Collidable;
import com.base.engine.core.math.Vector3f;

public class Capsule extends Primitive implements Collidable
{
	private float radius;
	private float height;
	private int axis;
	
	public Capsule(float radius, float height){this(radius, height, 1);}
	public Capsule(float radius, float height, int axis)
	{
		this.radius = radius;
		this.height = height;
		this.axis = axis;
	}
	
	public float getRadius() {
		return radius;
	}
	public float getHeight() {
		return height;
	}
	public Vector3f getAxis() {
		switch(axis)
		{
		case 0:
			return new Vector3f(1,0,0);
		case 1:
			return new Vector3f(0,1,0);
		case 2:
			return new Vector3f(0,0,1);
		}
		return null;
	}
}
