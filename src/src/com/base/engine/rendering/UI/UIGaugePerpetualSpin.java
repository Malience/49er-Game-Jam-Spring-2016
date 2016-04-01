package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.components.attachments.Updatable;

public class UIGaugePerpetualSpin extends UIGauge implements UIRenderable, Updatable
{

	public UIGaugePerpetualSpin(String texturePath, float radius, float start, float length){this(texturePath, radius, start, length, 0);}
	public UIGaugePerpetualSpin(String texturePath, float radius, float start, float length, float pivot)  
	{
		super(texturePath, radius, start, length,pivot);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int update(float delta)
	{
		this.rotatePointer(delta*200);
		return 1;
	}
}
