package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.Draggable;
import com.base.engine.components.attachments.DropLocation;
import com.base.engine.core.Input;
import com.base.engine.core.math.Vector2f;

public class UIInventorySlotSpecific extends UIInventorySlot implements DropLocation {
	String type;

	public UIInventorySlotSpecific(Vector2f halfSize, String type) {
		super(halfSize);
		this.type = type;
	}

	public boolean drop(Draggable drop) {
		if (item != null)
			return false;
		Vector2f mousePos = Input.getCurrentMousePosition();
		if (isWithin(mousePos.x, mousePos.y)) {
			if (drop instanceof UIInventoryItemRectSpecific) {
				UIInventoryItemRectSpecific itemSpecific = (UIInventoryItemRectSpecific) drop;
				if (itemSpecific.getType().equals(type)) {
					item = itemSpecific;
					return true;
				}
			}
		}
		return false;
	}

}
