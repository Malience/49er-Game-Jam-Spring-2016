package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.Controlable;
import com.base.engine.components.attachments.UIRenderable;

public abstract class UITextButtonRect extends UIObject {
	private Button button;
	private UIText text;

	public UITextButtonRect(String texturePath, float width, float height, String fontPath, String text, float size) {

		button = new Button(this, texturePath, width, height);
		button.priority = 11;
		this.text = new UIText(fontPath, text, size);
		this.text.priority = 12;
		this.text.center = true;

		this.addComponent(button);
		this.addComponent(this.text);
	}

	public void generate() {
		button.generate();
		text.generate();
	}

	private class Button extends UIButtonRect implements UIRenderable, Controlable {
		UITextButtonRect parent;

		public Button(UITextButtonRect parent, String texturePath, float width, float height) {
			super(texturePath, width, height);
			this.parent = parent;
		}

		@Override
		public void press() {
			parent.press();
		}

	}

	protected abstract void press();
}
