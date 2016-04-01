package com.base.engine.components;

import com.base.engine.animation.SkeletonLoading.Skeleton;
import com.base.engine.components.attachments.Animate;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Transform;
import com.base.engine.core.math.Vector3f;

public class AnimationComponent extends GameComponent implements Animate {
	private Skeleton skeleton;
	private int current = -1;
	private int next = -1;
	private int defaultAnim;
	private float framerate;
	private float time;
	private float transition;
	private boolean loop;
	private boolean play;
	private boolean captured[][];
	private Transform lastPose[];

	public AnimationComponent() {
		this(null);
	}

	public AnimationComponent(Skeleton skeleton) {
		this(skeleton, -1);
	}

	public AnimationComponent(Skeleton skeleton, int defaultAnim) {
		this.skeleton = skeleton;
		this.loop = true;
		this.defaultAnim = defaultAnim;
		this.framerate = DEFAULT_FRAMERATE;
		this.play = true;
	}

	public void attach(Skeleton skeleton) {
		this.skeleton = skeleton;
		captured = new boolean[3][skeleton.getBoneSize()];
	}

	public void setNextAnimation(int next) {
		this.next = next;
		loop = false;
	}

	public void playAnimation(float delta) {
		if (!play)
			return;

		int lastframe = (int) Math.floor(time);
		int nextframe = (int) Math.ceil(time);
		float lerpFactor = time - (float) Math.floor(time);

		if (transition > 0) {
			if (lastPose == null)
				updateLastPose();

			lerpFactor = 1 / transition;

			for (int i = 0; i < captured.length; i++) {
				skeleton.setPose(i, this.interpolate(i, lastPose[i],
						skeleton.getAnimation(current).getFrame(nextframe).getPose(i), lerpFactor));
			}
			transition -= delta * framerate;
			if (transition < 0)
				time -= transition;
		} else {
			for (int i = 0; i < captured.length; i++) {
				skeleton.setPose(i, this.interpolate(i, skeleton.getAnimation(current).getFrame(lastframe).getPose(i),
						skeleton.getAnimation(current).getFrame(nextframe).getPose(i), lerpFactor));
			}
			time += delta * framerate;
			if (time > skeleton.getAnimation(current).getFrames()) {
				if (loop) {
					playAnimation(current, 0, 0);
				} else {
					transitionIntoNextAnimation();
				}
			}
		}
	}

	@Override
	public void playAnimation(int animNum, float startTime, float transitionTime) {
		if (!play)
			return;
		current = animNum;
		time = startTime;
		transition = transitionTime;
		framerate = skeleton.getAnimation(current).getFramerate();
	}

	// TODO:Reverse animation

	public void playNextAnimation() {
		playAnimation(next, 0, 0);
	}

	private static final float STANDARD_TRANSITION_TIME = 100;

	public void transitionIntoAnimation(int animNum) {
		transitionIntoAnimation(animNum, STANDARD_TRANSITION_TIME);
	}

	public void transitionIntoAnimation(int animNum, float transitionTime) {
		playAnimation(animNum, 0, transitionTime);
	}

	public void transitionIntoAnimationAtTime(int animNum, float startTime) {
		transitionIntoAnimationAtTime(animNum, startTime, STANDARD_TRANSITION_TIME);
	}

	public void transitionIntoAnimationAtTime(int animNum, float startTime, float transitionTime) {
		playAnimation(animNum, startTime, transitionTime);
	}

	public void transitionIntoNextAnimation() {
		transitionIntoNextAnimation(STANDARD_TRANSITION_TIME);
	}

	public void transitionIntoNextAnimation(float transitionTime) {
		if (next == -1) {
			if (defaultAnim == -1)
				return;
			transitionIntoAnimation(defaultAnim, transitionTime);
		} else
			transitionIntoAnimation(next, transitionTime);
	}

	public void captureRotationChannel(int channel) {
		captured[0][channel] = !captured[0][channel];
	}

	public void captureTranslationChannel(int channel) {
		captured[1][channel] = !captured[1][channel];
	}

	public void captureScaleChannel(int channel) {
		captured[2][channel] = !captured[2][channel];
	}

	private void updateLastPose() {
		if (lastPose == null) {
			lastPose = new Transform[captured.length];
		}
		for (int i = 0; i < lastPose.length; i++) {
			lastPose[i] = skeleton.getTransform(i);
		}
	}

	private Transform interpolate(int i, Transform one, Transform two, float lerpFactor) {
		if (captured[0][i] && captured[1][i] && captured[2][i])
			return skeleton.getTransform(i);

		Vector3f pos = null;
		Vector3f scale = null;
		Quaternion rot = null;
		if (!captured[0][i])
			pos = one.getPos().lerp(two.getPos(), lerpFactor);
		else
			pos = skeleton.getTransform(i).getPos();
		if (!captured[1][i])
			scale = one.getScale().lerp(two.getScale(), lerpFactor);
		else
			scale = skeleton.getTransform(i).getScale();
		if (!captured[2][i])
			rot = one.getRot().slerp(two.getRot(), lerpFactor, false);
		else
			rot = skeleton.getTransform(i).getRot();
		return new Transform(pos, rot, scale);
	}

	// GETTERS AND SETTERS

	private static final float DEFAULT_FRAMERATE = 33.333f;

	public void setAnimationFramerate(float framerate) {
		this.framerate = framerate;
	}

	public float getAnimationFramerate() {
		return framerate;
	}

	public int getCurrentAnimation() {
		return current;
	}

	public float getAnimationTime() {
		return time;
	}

	public int getNextAnimation() {
		return next;
	}

	public boolean inTransition() {
		// TODO: inTransition
		return false;
	}

	public Vector3f getGlobalPosition(int channel) {
		// TODO: global position shenanigans
		return null;
	}

	public void stopLooping() {
		loop = false;
	}

	public void startLooping() {
		loop = true;
	}

	public void toggleLooping() {
		loop = !loop;
	}

	public void stopAnimation() {
		play = false;
	}

	public void startAnimation() {
		play = true;
	}

	public void toggleAnimation() {
		play = !play;
	}
}
