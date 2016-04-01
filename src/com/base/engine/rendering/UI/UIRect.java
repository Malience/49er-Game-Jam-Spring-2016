package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.core.math.Vector2f;

public class UIRect extends UIElement implements UIRenderable {
	Vector2f halfSize;

	public UIRect(String texturePath, float width, float height) {
		this(texturePath, new Vector2f(width, height));
	}

	public UIRect(String texturePath, Vector2f halfSize) {
		super(texturePath);
		this.halfSize = halfSize;
	}

	@Override
	public void generate() {
		Vector2f position = this.getTransform().getTransformedPos().getXY();
		vertices = new Vector2f[] { new Vector2f(position.x + halfSize.x, position.y + halfSize.y),
				new Vector2f(position.x - halfSize.x, position.y + halfSize.y),
				new Vector2f(position.x + halfSize.x, position.y - halfSize.y),
				new Vector2f(position.x - halfSize.x, position.y - halfSize.y) };

		indices = new int[] { 0, 2, 1, 1, 2, 3 };

		texCoords = new Vector2f[] { new Vector2f(1, 1), new Vector2f(0, 1), new Vector2f(1, 0), new Vector2f(0, 0) };
	}
}
