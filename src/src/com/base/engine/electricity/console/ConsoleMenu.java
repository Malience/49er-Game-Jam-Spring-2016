package com.base.engine.electricity.console;

import com.base.engine.components.attachments.Menu;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.UI.UIMenu;
import com.base.engine.rendering.UI.UIRect;

public class ConsoleMenu extends UIMenu implements Menu
{
	private static final Vector2f screenSize = new Vector2f(175,175);
	
	public ConsoleMenu()
	{
		UIRect rect = new UIRect("consoleScreen.png", screenSize);
		rect.priority = 1;
		this.addComponent(rect);
		
		this.getTransform().setPos(new Vector3f(400,300,0));
	}
}
