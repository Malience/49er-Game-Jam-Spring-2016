package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.DropLocation;
import com.base.engine.core.math.Vector2f;

public abstract class UISlot extends UIDropLocation implements DropLocation {
	Vector2f halfSize;

	public UISlot(Vector2f halfSize) {
		this.halfSize = halfSize;
	}

	@Override
	public boolean isWithin(float x, float y) {
		Vector2f max = parent.getTransform().getTransformedPos().getXY().add(halfSize);
		Vector2f min = parent.getTransform().getTransformedPos().getXY().sub(halfSize);

		return x < max.x && y < max.y && x > min.x && y > min.y;
	}
}
