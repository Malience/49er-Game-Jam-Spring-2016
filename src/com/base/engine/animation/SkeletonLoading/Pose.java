package com.base.engine.animation.SkeletonLoading;

import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Transform;
import com.base.engine.core.math.Vector3f;

public class Pose {
	Transform transform;

	public Pose(Vector3f pos, Quaternion rot, Vector3f scale) {
		this(new Transform(pos, rot, scale));
	}

	public Pose(Transform transform) {
		this.transform = transform;
	}

	public Transform getTransform() {
		return transform;
	}
}
