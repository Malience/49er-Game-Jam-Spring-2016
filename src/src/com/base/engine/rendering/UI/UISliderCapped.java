package com.base.engine.rendering.UI;

import java.util.ArrayList;

import com.base.engine.components.attachments.Draggable;
import com.base.engine.components.attachments.DropLocation;
import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.core.Input;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Texture;

public class UISliderCapped extends UISlider
{
	float max, min;
	
	public UISliderCapped(String texturePath, Vector2f size, Vector2f sliderSize, float start, boolean horizontal, float max, float min) 
	{
		super (texturePath, size, sliderSize, start, horizontal);
		
		this.max = max;
		this.min = min;
	}
	
	public void change(Vector2f newloc)
	{
		if(horizontal) current = (newloc.x - this.getTransform().getTransformedPos().x) / (trackHalfSize * 2) + .5f;
		else current = (newloc.y - this.getTransform().getTransformedPos().y) / (trackHalfSize * 2) + .5f;
		if(current > max) current = max;
		else if(current < min) current = min;
		if(horizontal) slider.getTransform().getPos().x = (current * 2 - 1f) * trackHalfSize;
		else slider.getTransform().getPos().y = (current * 2 - 1f) * trackHalfSize;
	}
	
	
	public void setMax(float max)
	{
		this.max = max;
	}
	
	public void setMin(float min)
	{
		this.min = min;
	}
}
