package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.UIRenderable;

public class UIBarRectBoundedScaled extends UIBarRectBounded implements UIRenderable {
	float scale;

	public UIBarRectBoundedScaled(String texturePath, float width, float height) {
		this(texturePath, width, height, 100);
	}

	public UIBarRectBoundedScaled(String texturePath, float width, float height, float scale) {
		super(texturePath, width, height, 0, 1);
		this.scale = scale;
	}

	@Override
	public void set(float amount) {
		current = amount / scale;
		if (current < min)
			current = min;
		else if (current > max)
			current = max;
		generate();
	}

	@Override
	public void add(float amount) {
		current += amount / scale;
		if (current > max)
			current = max;
		generate();
	}

	@Override
	public void sub(float amount) {
		current -= amount / scale;
		if (current < min)
			current = min;
		generate();
	}

	@Override
	public float getAmount() {
		return super.getAmount() * scale;
	}
}
