package com.base.engine.animation.SkeletonLoading;

import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Transform;

public class Bone {
	String name;
	int parent;
	Transform transform;

	public Bone(String name, int parent) {
		this.name = name;
		this.parent = parent;
	}

	public void setPose(Pose pose) {
		this.transform = pose.getTransform();
	}

	public void setTransform(Transform mat) {
		transform = mat;
	}

	public Matrix4f getTransform() {
		return transform.getTransformation();
	}
}
