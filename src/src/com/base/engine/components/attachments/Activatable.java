package com.base.engine.components.attachments;

public interface Activatable 
{
	public default boolean isActive(){return true;};
	public default void activate(){};
	public default void deactivate(){};
	public default void toggleActive(){};
	public default void setActive(boolean active){};
}
