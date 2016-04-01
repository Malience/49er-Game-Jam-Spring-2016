package com.base.engine.physics.collision.broadphase;

public class BoundingSphere extends BoundingCollider
{
	public float radius;
	
	public BoundingSphere(float radius)
	{
		super();
		this.radius = radius;
	}
}
