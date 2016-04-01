package com.base.engine.rendering.UI;

import java.util.ArrayList;

import com.base.engine.components.attachments.Draggable;
import com.base.engine.components.attachments.DropLocation;
import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Texture;

public abstract class UIDraggable extends UIElement implements UIRenderable, Draggable {

	UIDraggable(Texture texture) {
		super(texture);
	}

	UIDraggable(String texturePath) {
		super(texturePath);
	}

	@Override
	public boolean drag(float delta, Vector2f mousePos) {
		if (isWithin(mousePos.x, mousePos.y)) {
			save();
			return true;
		}
		return false;
	}

	@Override
	public int dragging(float delta, Vector2f mousePos) {
		this.getTransform().setPos(new Vector3f(mousePos.x, mousePos.y, 0));
		return 1;
	}

	@Override
	public int drop(float delta, Vector2f mousePos, ArrayList<DropLocation> dropLocations) {
		for (DropLocation loc : dropLocations) {
			if (loc.drop(this))
				return 1;
		}
		restore();
		return 1;
	}

	public abstract boolean isWithin(float x, float y);

	public abstract void save();

	public abstract void restore();
}
