package com.base.engine.components.attachments;

import java.util.ArrayList;

import com.base.engine.core.math.Vector2f;

public interface Draggable extends ComponentAttachment
{
	public default boolean drag(float delta, Vector2f mousePos){return false;}
	public default int dragging(float delta, Vector2f mousePos){return 1;}
	public default int drop(float delta, Vector2f mousePos, ArrayList<DropLocation> dropLocations){return 1;}
}
