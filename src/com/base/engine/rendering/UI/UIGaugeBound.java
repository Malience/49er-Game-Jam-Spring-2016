package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.UIRenderable;

public class UIGaugeBound extends UIGauge implements UIRenderable {
	float clockwiseBound;
	float ccwiseBound;

	public UIGaugeBound(String texturePath, float radius, float start, float length) {
		this(texturePath, radius, start, length, 0, -90, 90);
	}

	public UIGaugeBound(String texturePath, float radius, float start, float length, float pivot, float cwBound,
			float ccwBound) {
		super(texturePath, radius, start, length, pivot);
		this.clockwiseBound = cwBound;
		this.ccwiseBound = ccwBound;
	}

	public void rotateClockwise(float amount) {
		this.current -= amount;
		if (this.current < clockwiseBound)
			this.current = clockwiseBound;
	}

	public void rotateCounterClockwise(float amount) {
		this.current += amount;
		if (this.current > ccwiseBound)
			this.current = ccwiseBound;
	}
}
