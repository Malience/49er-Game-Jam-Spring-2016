package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.components.attachments.Updatable;

public class UIBarRectBoundedIncrement extends UIBarRectBounded implements UIRenderable, Updatable {
	boolean decrement;

	public UIBarRectBoundedIncrement(String texturePath, float width, float height) {
		this(texturePath, width, height, false);
	}

	public UIBarRectBoundedIncrement(String texturePath, float width, float height, boolean decrement) {
		super(texturePath, width, height);
		this.decrement = decrement;
	}

	private static final float AMOUNT = .5f;

	@Override
	public int update(float delta) {
		if (decrement)
			this.sub(delta * AMOUNT);
		else
			this.add(delta * AMOUNT);
		return 1;
	}

}
