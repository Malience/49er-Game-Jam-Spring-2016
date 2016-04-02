package com.base.engine.physics.RigidBody.ForceGenerators;

import com.base.engine.core.GameObject;
import com.base.engine.core.math.Vector3f;
import com.base.engine.physics.RigidBody.RigidBody;

public class ConnectedPlanet implements ForceGenerator {
	public GameObject object;
	float power;

	public ConnectedPlanet(GameObject object, float p) {
		this.object = object;
		power = p;
	}

	@Override
	public void updateForce(RigidBody body, float delta) {
		Vector3f direction = object.getPosition().sub(body.getParent().getPosition());
		direction.normalize();
		body.addForce(direction.mul(power));
	}

}
