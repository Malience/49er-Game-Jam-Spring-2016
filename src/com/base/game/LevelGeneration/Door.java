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

public class Door extends GameObject {
	public final static Vector3f WALL_HALF_SIZE = new Vector3f(5f, 5f, 0.5f);
	private final static RenderGenerator render = new RenderGenerator("", "wall.png", new MaterialAttachment[] {
			new MaterialAttachment("specularIntensity", 1.0f), new MaterialAttachment("specularPower", 30.0f) });

	public Door() {
		this(new Vector3f(0, 0, 0));
	}

	public Door(Vector3f position) {
		this(position, 0);
	}

	public Door(Vector3f position, float rot) {
		this.getTransform().setPos(position);
		if (rot != 0)
			this.getTransform().rotate(World.Y_AXIS, (float) Math.toRadians(rot));

		this.addComponent(render.generate(Shapes.box(new Vector3f(.5f, .5f, .5f))));
		// collider = new Box(new Vector3f(.5f,.5f,.5f));
		// this.addComponent(collider);
	}

	public Door(Wall wall) {
		this.getTransform().setPos(wall.getPosition());
		this.getTransform().setRot(wall.getTransform().getRot());

		this.addComponent(render.generate(Shapes.box(new Vector3f(.5f, .5f, .5f))));
	}
}
