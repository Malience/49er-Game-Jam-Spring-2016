package com.base.engine.physics.collision;

import com.base.engine.components.attachments.Collidable;
import com.base.engine.core.GameObject;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.physics.RigidBody.RigidBody;

public class Primitive extends Collider implements Collidable
{
	RigidBody body;
	Matrix4f offset;
	boolean active = true;
	
	public void calculateInternals()
	{
		if(offset == null)
		{
			offset = new Matrix4f();
			offset = offset.initIdentity();
		}
	}
	
	
	@Override
	public Primitive getPrimitive()
	{
		return this;
	}
	
	public void attach(RigidBody body)
	{
		this.body = body;
	}
	
	public void setOffset(Matrix4f m)
	{
		this.offset = m;
	}
	
	@Override
	public int attach(GameObject parent)
	{
		super.attach(parent);
		
		this.calculateInternals();
		
		return 1;
	}
	
	public Vector3f getAxis(int index)
	{
		return parent.getTransform().getTransformation().mul(offset).getAxisVector(index);
	}

	@Override
	public void setActive(boolean active)
	{
		this.active = active;
	}
	
	public boolean isStatic()
	{
		return body == null;
	}
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	
}
