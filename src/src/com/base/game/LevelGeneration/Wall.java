package com.base.game.LevelGeneration;

import com.base.engine.components.MeshRenderer;
import com.base.engine.core.GameObject;
import com.base.engine.core.Shapes;
import com.base.engine.core.World;
import com.base.engine.core.math.Vector3f;
import com.base.engine.physics.collision.Box;
import com.base.engine.physics.collision.Collider;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.MaterialAttachment;
import com.base.engine.rendering.RenderGenerator;
import com.base.engine.rendering.Texture;

public class Wall extends GameObject
{
	public final static Vector3f WALL_HALF_SIZE = new Vector3f(World.tileHalfSize,World.tileHalfSize,0.1f);
	private final static RenderGenerator render = new RenderGenerator("", "wall.png", 
			new MaterialAttachment[]{	new MaterialAttachment("specularIntensity", 1.0f),
										new MaterialAttachment("specularPower", 30.0f)});
	private Collider collider;
	public String attachment;
	private MeshRenderer mesh;
	
	public Wall(){this(new Vector3f(0,0,0));}
	public Wall(Vector3f position){this(position, 0);}
	public Wall(Vector3f position, float rot)
	{
		this.getTransform().setPos(position);
		if(rot != 0) this.getTransform().rotate(World.Y_AXIS, (float)Math.toRadians(rot));
		
		mesh = render.generate(Shapes.box(WALL_HALF_SIZE));
		this.addComponent(mesh);
		collider = new Box(WALL_HALF_SIZE);
		this.addComponent(collider);
		attachment = "";
	}
	
	public void setMaterial(Material mat)
	{
		mesh.set(mat);	
	}
	
	public Collider getCollider()
	{
		return collider;
	}
}
