package com.base.engine.physics.collision.broadphase;

import com.base.engine.core.math.Vector3f;

public class BroadCollisionDetector 
{
	public boolean checkCollision(BoundingCollider one, BoundingCollider two)
	{
		if(!one.isActive() || !two.isActive()) return false;
		
		String s1 = one.getClass().getSimpleName();
		String s2 = two.getClass().getSimpleName();
		
		if(s1.equals("BoundingSphere") && s2.equals("BoundingSphere"))
		{
			return sphereAndSphere((BoundingSphere)one, (BoundingSphere)two);
		}
		return false;
	}
	
	boolean sphereAndSphere(BoundingSphere one, BoundingSphere two)
	{
		
		Vector3f pos1 = one.getTransform().getTransformedPos();
		Vector3f pos2 = two.getTransform().getTransformedPos();
		
		Vector3f midline = pos1.sub(pos2);
		float size = midline.magnitude();
		
		if(size <= 0.0f | size >= one.radius + two.radius)
		{
			return false;
		}
		
		return true;
	}
}
