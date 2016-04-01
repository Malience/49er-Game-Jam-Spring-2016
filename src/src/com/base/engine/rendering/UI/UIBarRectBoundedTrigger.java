package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.UIRenderable;

public abstract class UIBarRectBoundedTrigger extends UIBarRectBounded implements UIRenderable
{
	
	public UIBarRectBoundedTrigger(String texturePath, float width, float height){this(texturePath, width, height, 0, 1);}
	public UIBarRectBoundedTrigger(String texturePath, float width, float height, float min, float max) 
	{
		super(texturePath, width, height, min, max);
	}
	
	@Override
	public void set(float amount)
	{
		current = amount;
		if(current <= min) {current = min; minTrigger();}
		else if(current >= max) {current = max; maxTrigger();}
		generate();
	}
	
	@Override
	public void add(float amount)
	{
		current += amount;
		if(current >= max) {current = max; maxTrigger();}
		generate();
	}
	
	@Override
	public void sub(float amount)
	{
		current -= amount;
		if(current <= min) {current = min; minTrigger();}
		generate();
	}
	
	public abstract void minTrigger();
	public abstract void maxTrigger();
}
