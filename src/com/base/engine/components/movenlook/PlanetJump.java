package com.base.engine.components.movenlook;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import com.base.engine.components.GameComponent;
import com.base.engine.components.attachments.Controlable;
import com.base.engine.core.Input;
import com.base.engine.core.math.Vector3f;
import com.base.engine.physics.RigidBody.RigidBody;
import com.base.engine.physics.RigidBody.ForceGenerators.ConnectedPlanet;

public class PlanetJump extends GameComponent implements Controlable {
	private float force;
	private RigidBody body;
	private int jumpKey;
	private static final float jumpEpsilon = .1f;
	ConnectedPlanet planet;
	

	public PlanetJump(float speed, RigidBody body) {
		this(speed, body, GLFW_KEY_SPACE);
	}

	public PlanetJump(float speed, RigidBody body, int jumpKey) {
		this.jumpKey = jumpKey;
		force = speed;
		this.body = body;
	}

	@Override
	public int input(float delta) {
		if (Input.getKeyPressed(jumpKey)) {
			Vector3f velocity;
			if(planet != null) velocity = this.getTransform().getPos().sub(planet.object.getPosition()).normal().mul(body.getVelocity());
			else velocity = new Vector3f(0, 1, 0).mul(body.getVelocity());
			float currentMotion = velocity.dot(velocity);

			float bias = (float) Math.pow(0.5f, delta);

			float motion = Math.abs((1 - bias) * currentMotion);
			if (motion < jumpEpsilon)
			{
				if(planet != null) body.addVelocity(this.getTransform().getPos().sub(planet.object.getPosition()).normal().mul(force));
				else body.addVelocity(new Vector3f(0, 1, 0).mul(force));
			}
		}
		return 1;
	}
	
	public ConnectedPlanet getPlanet()
	{
		return this.planet;
	}
	
	public void setPlanet(ConnectedPlanet planet)
	{
		this.planet = planet;
	}
}
