package com.base.engine.animation.SkeletonLoading;

import java.util.ArrayList;

import com.base.engine.core.math.Transform;

public class KeyFrame {
	ArrayList<Pose> poses;

	public KeyFrame() {
		poses = new ArrayList<Pose>();
	}

	public Transform getPose(int index) {
		return poses.get(index).getTransform();
	}

	public void addPose(Pose pose) {
		poses.add(pose);
	}
}
