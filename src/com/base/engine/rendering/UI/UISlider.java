package com.base.engine.rendering.UI;

import java.util.ArrayList;

import com.base.engine.components.attachments.Draggable;
import com.base.engine.components.attachments.DropLocation;
import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.core.Input;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Texture;

public class UISlider extends UIObject {
	boolean horizontal;
	UIObject slider;
	float current;
	float trackHalfSize;

	// public UISlider(String texturePath, Vector2f size, boolean
	// horizontal){this(texturePath, size, new Vector2f(5,10), 0, horizontal);}
	public UISlider(String texturePath, Vector2f size, Vector2f sliderSize, float start, boolean horizontal) {
		this.horizontal = horizontal;
		slider = new UIObject();
		slider.addComponent(new Slider(texturePath, sliderSize, this));

		UIRect track = new UIRect("cyan.png", size);
		track.priority = 12;
		this.addComponent(track);

		if (horizontal)
			trackHalfSize = size.x;
		else
			trackHalfSize = size.y;

		current = start;
		slider.getTransform().setPos(convert(current));

		this.addChild(slider);

	}

	public Vector3f convert(float pos) {
		pos = (pos * 2 - 1f) * trackHalfSize;
		if (horizontal)
			return new Vector3f(pos, 0, 0);
		else
			return new Vector3f(0, pos, 0);
	}

	public float convert(Vector3f loc) {
		if (horizontal)
			return loc.x / trackHalfSize + .5f;
		else
			return loc.y / trackHalfSize + .5f;
	}

	public void change(Vector2f newloc) {
		if (horizontal)
			current = (newloc.x - this.getTransform().getTransformedPos().x) / (trackHalfSize * 2) + .5f;
		else
			current = (newloc.y - this.getTransform().getTransformedPos().y) / (trackHalfSize * 2) + .5f;
		if (current > 1.0f)
			current = 1.0f;
		else if (current < 0.0f)
			current = 0.0f;
		if (horizontal)
			slider.getTransform().getPos().x = (current * 2 - 1f) * trackHalfSize;
		else
			slider.getTransform().getPos().y = (current * 2 - 1f) * trackHalfSize;
	}

	public float get() {
		return current;
	}

	public void set(float slider) {
		current = slider;
	}

	class Slider extends UIRect implements Draggable, UIRenderable {
		UISlider parent;

		public Slider(String texturePath, Vector2f size, UISlider parent) {
			super(texturePath, size);
			this.priority = 13;
			this.parent = parent;
		}

		@Override
		public boolean drag(float delta, Vector2f mousePos) {
			if (isWithin(mousePos.x, mousePos.y)) {
				return true;
			}
			return false;
		}

		@Override
		public int dragging(float delta, Vector2f mousePos) {
			Vector3f center = parent.getTransform().getTransformedPos();
			if (horizontal) {
				mousePos.y -= mousePos.y - Input.getPrevMousePosition().y;
				if (mousePos.x > center.x + trackHalfSize)
					mousePos.x = center.x + trackHalfSize;
				else if (mousePos.x < center.x - trackHalfSize)
					mousePos.x = center.x - trackHalfSize;
			} else {
				mousePos.x -= mousePos.x - Input.getPrevMousePosition().x;
				if (mousePos.y > center.y + trackHalfSize)
					mousePos.y = center.y + trackHalfSize;
				else if (mousePos.y < center.y - trackHalfSize)
					mousePos.y = center.y - trackHalfSize;
			}

			change(mousePos);
			Input.setMousePosition(mousePos);
			return 1;
		}

		@Override
		public int drop(float delta, Vector2f mousePos, ArrayList<DropLocation> dropLocations) {
			return dragging(delta, mousePos);
		}

		public boolean isWithin(float x, float y) {
			Vector2f max = this.getTransform().getTransformedPos().getXY().add(halfSize);
			Vector2f min = this.getTransform().getTransformedPos().getXY().sub(halfSize);

			return x < max.x && y < max.y && x > min.x && y > min.y;
		}
	}
}
