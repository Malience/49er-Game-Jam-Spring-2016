package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.Controlable;
import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.core.Input;
import com.base.engine.core.math.Vector2f;

public class UIButtonRect extends UIButton implements UIRenderable, Controlable
{
	Vector2f halfSize;
	
	public UIButtonRect(String texturePath, float width, float height){this(texturePath,new Vector2f(width,height));}
	public UIButtonRect(String texturePath, Vector2f halfSize)
	{
		super(texturePath);
		this.halfSize = halfSize;
		material.addFloat("pressedColorEffect", pressedColorEffect);
		material.addBoolean("isPressed",isPressed);
		//generate();
	}
	
	@Override
	public void generate()
	{
		Vector2f position = this.getTransform().getTransformedPos().getXY();
		vertices = new Vector2f[]{
				new Vector2f(position.x + halfSize.x, position.y + halfSize.y),
				new Vector2f(position.x - halfSize.x, position.y + halfSize.y),
				new Vector2f(position.x + halfSize.x, position.y - halfSize.y),
				new Vector2f(position.x - halfSize.x, position.y - halfSize.y)
		};
		
		indices = new int[]{
				0,2,1,
				1,2,3
		};
		
		texCoords = new Vector2f[]{
			new Vector2f(1,1),
			new Vector2f(0,1),
			new Vector2f(1,0),
			new Vector2f(0,0)
		};
	}
	
	@Override
	public int input(float delta)
	{
		Vector2f m = Input.getCurrentMousePosition();
		Vector2f position = this.getTransform().getTransformedPos().getXY();
		float one = m.x - (position.x - halfSize.x);
		if(
				m.x < (position.x + halfSize.x) && 
				m.y < (position.y + halfSize.y) && 
				m.x > (position.x - halfSize.x) && 
				m.y > (position.y - halfSize.y))
		{
			hover = true;
			hover();
			if(Input.getMousePressed(0))
			{
				press();
				isPressed = true;
			}
			else
			{
				isPressed = false;
			}
		}
		else
		{
			hover = false;
		}
		material.addBoolean("isPressed",isPressed);
		return 1;
	}
	
	@Override
	public void press() 
	{
		System.out.println("Yay!");
	}
}
