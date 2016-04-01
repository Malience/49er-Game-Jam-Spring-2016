package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.Draggable;
import com.base.engine.components.attachments.DropLocation;
import com.base.engine.core.Input;
import com.base.engine.core.math.Vector2f;

public class UIInventorySlot extends UISlot implements DropLocation
{
	UIInventory inventory;
	UIInventoryItem item;
	
	public UIInventorySlot(Vector2f halfSize) {
		super(halfSize);
		// TODO Auto-generated constructor stub
	}
	
//	@Override
//	public int render(RenderingEngine renderingEngine)
//	{
//		if(item != null)item.render(renderingEngine);
//		return 1;
//	}
	
	public boolean drop(Draggable drop)
	{
		if(item != null) return false;
		Vector2f mousePos = Input.getCurrentMousePosition();
		if(isWithin(mousePos.x, mousePos.y))
		{
			if(drop instanceof UIInventoryItem)
			{
				item = (UIInventoryItem)drop;
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void add(Draggable add) 
	{
		// TODO Auto-generated method stub
		
	}
	
	public int getPriority()
	{
		return 6;
	}
}
