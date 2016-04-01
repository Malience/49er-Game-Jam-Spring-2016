package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.Draggable;
import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.core.math.Vector2f;
import com.base.engine.rendering.Texture;

public class UIInventoryItemRectSpecific extends UIInventoryItemRect implements UIRenderable, Draggable {
	private String type;

	public UIInventoryItemRectSpecific(String texturePath, Vector2f halfSize, String type) {
		super(texturePath, halfSize);
		this.type = type;
	}

	public UIInventoryItemRectSpecific(Texture texture, Vector2f halfSize, String type) {
		super(texture, halfSize);
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
