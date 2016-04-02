package com.base.engine.physics.PremadeObjects;

import com.base.engine.core.Shapes;
import com.base.engine.physics.RigidBody.RigidSphere;
import com.base.engine.physics.collision.Sphere;
import com.base.engine.rendering.Mesh;

public class StaticSphere extends StaticPremade {
	Sphere sphere;

	public StaticSphere() {
		this(1);
	}

	public StaticSphere(float radius) {
		super(new Mesh("SphericalSphere001.obj"));
		setPrimitive(new Sphere(radius));
		this.getTransform().setScale(radius);
	}
}
