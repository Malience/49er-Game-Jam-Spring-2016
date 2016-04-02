package com.base.engine.components.movenlook;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import com.base.engine.components.GameComponent;
import com.base.engine.components.attachments.Controlable;
import com.base.engine.core.Input;
import com.base.engine.core.math.Vector3f;
import com.base.engine.physics.RigidBody.RigidBody;

public class JumpMove extends GameComponent implements Controlable {
	private float force;
	private RigidBody body;
	private int jumpKey;
	private static final float jumpEpsilon = .01f;

	public JumpMove(float speed, RigidBody body) {
		this(speed, body, GLFW_KEY_SPACE);
	}

	public JumpMove(float speed, RigidBody body, int jumpKey) {
		this.jumpKey = jumpKey;
		force = speed;
		this.body = body;
	}

	@Override
	public int input(float delta) {
		if (Input.getKeyPressed(jumpKey)) {
			Vector3f velocity = this.getTransform().getRot().getUp().mul(body.getVelocity());
			float currentMotion = velocity.dot(velocity);

			float bias = (float) Math.pow(0.5f, delta);

			float motion = Math.abs((1 - bias) * currentMotion);
			if (motion < jumpEpsilon)
				body.addVelocity(this.getTransform().getRot().getUp().mul(force));
		}
		return 1;
	}
}
