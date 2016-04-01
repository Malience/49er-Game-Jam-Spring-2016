package com.base.engine.physics.collision.broadphase;

import java.util.ArrayList;

import com.base.engine.components.GameComponent;
import com.base.engine.components.attachments.BroadCollidable;
import com.base.engine.physics.collision.Collider;

public abstract class BoundingCollider extends GameComponent implements BroadCollidable
{
	ArrayList<Collider> colliders;
	ArrayList<BoundingCollider> subColliders;
	
	public BoundingCollider()
	{
		colliders = new ArrayList<Collider>();
		subColliders = new ArrayList<BoundingCollider>();
	}
	
	public void addCollider(Collider collider)
	{
		colliders.add(collider);
	}
	
	public void addSubCollider(BoundingCollider collider)
	{
		subColliders.add(collider);
	}
	
	public BoundingCollider getCollider()
	{
		return this;
	}
	
	public ArrayList<Collider> getColliders()
	{
		return colliders;
	}
	
	public ArrayList<BoundingCollider> getSubColliders()
	{
		return subColliders;
	}
}
