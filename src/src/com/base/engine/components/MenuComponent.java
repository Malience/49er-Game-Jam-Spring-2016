package com.base.engine.components;

import java.util.ArrayList;

import com.base.engine.components.attachments.Controlable;
import com.base.engine.components.attachments.Menu;
import com.base.engine.core.Input;

public class MenuComponent extends GameComponent implements Controlable
{
	private Menu currentMenu = null;
	private ArrayList<Menu> menuList;
	public static Menu optionsMenu;
	
	public MenuComponent()
	{
		menuList = new ArrayList<Menu>();
	}
	
	@Override
	public int input(float delta)
	{
		if(Input.getBindingPressed("Options"))
		{
			if(currentMenu != null)
			{
				closeMenu(currentMenu);
				if(currentMenu.getParent() != null)
				{
					currentMenu = currentMenu.getParent();
					openMenu(currentMenu);
				}
				else
				{
					currentMenu = null;
				}
			}
			else
			{
				currentMenu = optionsMenu;
				openMenu(currentMenu);
			}
		}
		if(currentMenu != optionsMenu)
		{
			for(Menu menu : menuList)
			{
				if(Input.getBindingPressed(menu.getAction()))
				{
					if(currentMenu == null || menu.isParent(currentMenu))
					{
						currentMenu = menu;
						openMenu(currentMenu);
					}
					else if(currentMenu == menu)
					{
						closeMenu(menu);
						currentMenu = null;
					}
					else if(currentMenu.isParent(menu))
					{
						closeMenu(currentMenu);
						currentMenu = menu;
						openMenu(currentMenu);
					}
				}
			}
		}
		
		return 1;
	}
	
	public void switchMenu(Menu menu)
	{
		if(currentMenu != null)closeMenu(currentMenu);
		currentMenu = menu;
		openMenu(currentMenu);
	}
	
	private void openMenu(Menu menu)
	{
		menu.activate();
		menu.openAction();
		if(menu.getParent() != null)openMenu(menu.getParent());
		Input.unlockMouse();
	}
	
	private void closeMenu(Menu menu)
	{
		menu.deactivate();
		menu.closeAction();
		if(menu.getParent() != null)closeMenu(menu.getParent());
		Input.lockMouse();
	}

	public void addMenu(Menu menu) {menuList.add(menu);}
}