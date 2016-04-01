package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.Draggable;
import com.base.engine.components.attachments.DropLocation;
import com.base.engine.core.math.Vector3f;

public abstract class UIDropLocation extends UIComponent implements DropLocation
{

	UIDropLocation() {}
	
	@Override
	public boolean drop(Draggable drop)
	{
		Vector3f pos = drop.getTransform().getPos();
		if(isWithin(pos.x, pos.y))
		{
			add(drop);
			return true;
		}
		return false;
	}
	
	public abstract boolean isWithin(float x, float y);
	public abstract void add(Draggable add);
}
