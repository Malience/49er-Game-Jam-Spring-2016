package com.base.engine.components.attachments;

public interface Menu extends Activatable
{
	public Menu getParent();
	public void setParent(Menu parent);
	
	public default boolean isParent(Menu menu)
	{
		if(getParent() != null && (getParent() == menu || getParent().isParent(menu))) return true;
		return false;
	}
	
	public void setAction(String actionName, int actionKey);
	
	public String getAction();
	
	public default void openAction(){};
	public default void closeAction(){};
}
