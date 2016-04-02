package com.base.engine.physics.PremadeObjects;

import com.base.engine.core.Shapes;
import com.base.engine.physics.RigidBody.RigidSphere;
import com.base.engine.physics.collision.Sphere;

public class StaticSphere extends StaticPremade {
	Sphere sphere;

	public StaticSphere() {
		this(1);
	}

	public StaticSphere(float radius) {
		super(Shapes.sphere());
		setPrimitive(new Sphere(radius));
		this.getTransform().setScale(radius);
	}
}
