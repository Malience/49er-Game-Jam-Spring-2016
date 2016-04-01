package com.base.engine.rendering.UI;

import java.util.ArrayList;

import com.base.engine.components.attachments.Draggable;
import com.base.engine.components.attachments.DropLocation;
import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.core.CoreEngine;
import com.base.engine.core.Input;
import com.base.engine.core.math.Vector2f;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Texture;

public class UIInventoryItemRect extends UIInventoryItem implements UIRenderable, Draggable {
	Vector2f halfSize;

	UIInventoryItemRect(Texture texture, Vector2f halfSize) {
		super(texture);
		this.halfSize = halfSize;
	}

	public UIInventoryItemRect(String texturePath, Vector2f halfSize) {
		super(texturePath);
		this.halfSize = halfSize;
		this.priority = 5;
	}

	@Override
	public int dragging(float delta, Vector2f mousePos) {
		CoreEngine.debugBreak();
		this.generate(mousePos);
		RenderingEngine.addRender(this);
		return 1;
	}

	@Override
	public int drop(float delta, Vector2f mousePos, ArrayList<DropLocation> dropLocations) {
		for (DropLocation loc : dropLocations) {
			if (loc instanceof UIInventorySlot && loc.drop(this)) {
				current = (UIInventorySlot) loc;
				generate();
				return 1;
			}
		}
		restore();
		return 1;
	}

	@Override
	public void generate() {
		Vector2f position = this.getTransform().getTransformedPos().getXY();
		autogenerate = true;
		vertices = new Vector2f[] { new Vector2f(position.x + halfSize.x, position.y + halfSize.y),
				new Vector2f(position.x - halfSize.x, position.y + halfSize.y),
				new Vector2f(position.x + halfSize.x, position.y - halfSize.y),
				new Vector2f(position.x - halfSize.x, position.y - halfSize.y) };

		indices = new int[] { 0, 2, 1, 1, 2, 3 };

		texCoords = new Vector2f[] { new Vector2f(1, 1), new Vector2f(0, 1), new Vector2f(1, 0), new Vector2f(0, 0) };
	}

	public void generate(Vector2f position) {
		autogenerate = false;
		vertices = new Vector2f[] { new Vector2f(position.x + halfSize.x, position.y + halfSize.y),
				new Vector2f(position.x - halfSize.x, position.y + halfSize.y),
				new Vector2f(position.x + halfSize.x, position.y - halfSize.y),
				new Vector2f(position.x - halfSize.x, position.y - halfSize.y) };

		indices = new int[] { 0, 2, 1, 1, 2, 3 };

		texCoords = new Vector2f[] { new Vector2f(1, 1), new Vector2f(0, 1), new Vector2f(1, 0), new Vector2f(0, 0) };
	}

	@Override
	public void save() {
		super.save();
		this.generate(Input.getCurrentMousePosition());
	}

	@Override
	public void restore() {
		super.restore();
		this.generate();
	}

	@Override
	public boolean isWithin(float x, float y) {
		CoreEngine.debugBreak();
		Vector2f max = this.getTransform().getTransformedPos().getXY().add(halfSize);
		Vector2f min = this.getTransform().getTransformedPos().getXY().sub(halfSize);

		return x < max.x && y < max.y && x > min.x && y > min.y;
	}
}
