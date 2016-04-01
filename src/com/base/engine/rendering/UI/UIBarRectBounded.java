package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.UIRenderable;

public class UIBarRectBounded extends UIBarRect implements UIRenderable {
	float min;
	float max;

	public UIBarRectBounded(String texturePath, float width, float height) {
		this(texturePath, width, height, 0, 1);
	}

	public UIBarRectBounded(String texturePath, float width, float height, float min, float max) {
		super(texturePath, width, height);
		this.min = min;
		this.max = max;
	}

	@Override
	public void set(float amount) {
		current = amount;
		if (current < min)
			current = min;
		else if (current > max)
			current = max;
		generate();
	}

	@Override
	public void add(float amount) {
		current += amount;
		if (current > max)
			current = max;
		generate();
	}

	@Override
	public void sub(float amount) {
		current -= amount;
		if (current < min)
			current = min;
		generate();
	}
}
