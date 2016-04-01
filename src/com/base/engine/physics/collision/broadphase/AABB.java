package com.base.engine.physics.collision.broadphase;

import com.base.engine.core.math.Vector3f;

public class AABB extends BoundingCollider {
	public Vector3f min;
	public Vector3f max;

	public AABB(Vector3f min, Vector3f max) {
		super();
		this.min = min;
		this.max = max;
	}
}
