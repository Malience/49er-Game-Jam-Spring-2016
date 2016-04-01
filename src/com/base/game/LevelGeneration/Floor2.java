package com.base.game.LevelGeneration;

import com.base.engine.components.MeshRenderer;
import com.base.engine.core.GameObject;
import com.base.engine.core.Shapes;
import com.base.engine.core.World;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Texture;

public class Floor2 extends GameObject {
	public final static Vector3f WALL_HALF_SIZE = new Vector3f(2.5f, 0.01f, 2.5f);
	public static Material material;
	public final static String texturePath = "floor.png";

	public Floor2() {
		this(new Vector3f(0, 0, 0));
	}

	public Floor2(Vector3f position) {
		this(position, 0);
	}

	public Floor2(Vector3f position, float rot) {
		this.getTransform().setPos(position);
		if (rot != 0)
			this.getTransform().rotate(World.Y_AXIS, rot);

		if (material == null)
			initMaterial();

		this.addComponent(new MeshRenderer(Shapes.box(WALL_HALF_SIZE), material));
	}

	public static void initMaterial() {
		material = new Material();
		material.addTexture("diffuse", new Texture(texturePath));
		material.addFloat("specularIntensity", 1);
		material.addFloat("specularPower", 30);
	}
}
