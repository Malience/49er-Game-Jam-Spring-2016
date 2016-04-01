package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.Menu;
import com.base.engine.core.Input;

public abstract class UIMenu extends UIObject implements Menu
{
	private Menu parent = null;
	private String action = null;
	
	public UIMenu()
	{
		super();
		this.deactivate();
	}
	
	@Override
	public Menu getParent(){return parent;}
	@Override
	public void setParent(Menu parent){this.parent = parent;}
	
	@Override
	public void setAction(String actionName, int actionKey)
	{
		Input.setKeyBinding(actionName, actionKey);
		action = actionName;
	}
	
	@Override
	public String getAction(){return action;}
}
