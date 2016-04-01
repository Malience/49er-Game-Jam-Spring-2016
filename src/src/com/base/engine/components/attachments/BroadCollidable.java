package com.base.engine.components.attachments;

import com.base.engine.physics.collision.broadphase.BoundingCollider;

public interface BroadCollidable extends ComponentAttachment
{
	public default int checkCollisions(){return 1;}
	//public default Rigid getCollider(){return null;}
	//public RigidBody getBody();
	public BoundingCollider getCollider();
}
