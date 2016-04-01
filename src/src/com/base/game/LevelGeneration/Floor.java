package com.base.game.LevelGeneration;

import com.base.engine.components.MeshRenderer;
import com.base.engine.core.GameObject;
import com.base.engine.core.Shapes;
import com.base.engine.core.World;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.MaterialAttachment;
import com.base.engine.rendering.RenderGenerator;
import com.base.engine.rendering.Texture;

public class Floor extends GameObject
{
	public final static Vector3f WALL_HALF_SIZE = new Vector3f(World.tileHalfSize,0.01f,World.tileHalfSize);
	private final static RenderGenerator render = new RenderGenerator("", "floor.png", 
			new MaterialAttachment[]{	new MaterialAttachment("specularIntensity", 1.0f),
										new MaterialAttachment("specularPower", 30.0f)});
	public String attachment;
	private MeshRenderer mesh;
	
	public Floor(){this(new Vector3f(0,0,0));}
	public Floor(Vector3f position){this(position, 0);}
	public Floor(Vector3f position, float rot)
	{
		this.getTransform().setPos(position);
		if(rot != 0) this.getTransform().rotate(World.Y_AXIS, rot);
		
		mesh = render.generate((Shapes.box(WALL_HALF_SIZE)));
		this.addComponent(mesh);
	}
	
	public void setMaterial(Material mat)
	{
		mesh.set(mat);	
	}
}
