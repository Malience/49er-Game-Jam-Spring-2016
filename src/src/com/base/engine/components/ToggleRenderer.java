package com.base.engine.components;

import com.base.engine.components.attachments.Renderable;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;

public class ToggleRenderer extends MeshRenderer implements Renderable
{
	private boolean active = true;
	
	public ToggleRenderer(Mesh mesh, Material material){this(mesh, material, true);}
	public ToggleRenderer(Mesh mesh, Material material, boolean active) 
	{
		super(mesh, material);
		this.active = active;
	}
	
	public void setRendering(boolean is)
	{
		active = is;
	}
	
	public void toggle()
	{
		active = !active;
	}
	
	@Override
	public boolean isActive()
	{
		return active;
	}
}
