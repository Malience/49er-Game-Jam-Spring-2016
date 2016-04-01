package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.Draggable;
import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.core.math.Transform;
import com.base.engine.rendering.Texture;

public abstract class UIInventoryItem extends UIDraggable implements UIRenderable, Draggable {
	UIInventorySlot save;
	UIInventorySlot current;

	UIInventoryItem(Texture texture) {
		super(texture);
		// TODO: RULE THE UNIVERSE WITH ITEM SHENANIGANS
	}

	public UIInventoryItem(String texturePath) {
		super(texturePath);
	}

	@Override
	public Transform getTransform() {
		if (current != null)
			return current.getTransform();
		return save.getTransform();
	}

	@Override
	public void save() {
		save = current;
		save.item = null;
		current = null;
	}

	@Override
	public void restore() {
		current = save;
		current.item = this;
		save = null;
	}
}
