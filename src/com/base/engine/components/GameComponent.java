package com.base.engine.components;

import com.base.engine.components.attachments.ComponentAttachment;
import com.base.engine.core.CoreEngine;
import com.base.engine.core.GameObject;
import com.base.engine.core.math.Transform;

public abstract class GameComponent implements ComponentAttachment {
	protected GameObject parent;
	boolean active = true;

	public int attach(GameObject parent) {
		this.parent = parent;
		return 1;
	}

	public Transform getTransform() {
		return parent.getTransform();
	}

	public void positionChanged() {
	}

	public GameObject getParent() {
		return parent;
	}

	public void addToEngine(CoreEngine engine) {

	}

	@Override
	public boolean isActive() {
		return active;
	};

	@Override
	public void activate() {
		active = true;
	};

	@Override
	public void deactivate() {
		active = false;
	};

	@Override
	public void toggleActive() {
		active = !active;
	};

	@Override
	public void setActive(boolean active) {
		this.active = active;
	};

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}