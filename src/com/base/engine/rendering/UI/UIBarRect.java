package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.core.math.Vector2f;

public class UIBarRect extends UIElement implements UIRenderable {
	Vector2f size;
	float current; // Standard 0-1
	boolean horizontal;

	public UIBarRect(String texturePath, float width, float height) {
		this(texturePath, new Vector2f(width, height));
	}

	public UIBarRect(String texturePath, Vector2f size) {
		this(texturePath, size, true);
	}

	public UIBarRect(String texturePath, Vector2f size, boolean horizontal) {
		super(texturePath);
		this.size = size;
		current = 1.0f;
		this.horizontal = horizontal;
	}

	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}

	@Override
	public void generate() {
		Vector2f position = this.getTransform().getTransformedPos().getXY();

		indices = new int[] { 0, 2, 1, 1, 2, 3 };

		if (!horizontal) {
			vertices = new Vector2f[] { new Vector2f(position.x + size.x, position.y + size.y * current),
					new Vector2f(position.x, position.y + size.y * current),
					new Vector2f(position.x + size.x, position.y - size.y),
					new Vector2f(position.x, position.y - size.y) };

			texCoords = new Vector2f[] { new Vector2f(1, current), new Vector2f(0, current), new Vector2f(1, 0),
					new Vector2f(0, 0) };
		} else {
			vertices = new Vector2f[] { new Vector2f(position.x + size.x * current, position.y + size.y),
					new Vector2f(position.x, position.y + size.y),
					new Vector2f(position.x + size.x * current, position.y - size.y),
					new Vector2f(position.x, position.y - size.y) };

			texCoords = new Vector2f[] { new Vector2f(1, 1), new Vector2f(0, 1), new Vector2f(1, 0),
					new Vector2f(0, 0) };
		}
	}

	public void set(float amount) {
		current = amount;
		generate();
	}

	public void add(float amount) {
		current += amount;
		generate();
	}

	public void sub(float amount) {
		current -= amount;
		generate();
	}

	public float getAmount() {
		return current;
	}
}
