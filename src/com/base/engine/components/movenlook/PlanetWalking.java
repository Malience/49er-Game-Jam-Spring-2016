package com.base.engine.components.movenlook;

import com.base.engine.components.GameComponent;
import com.base.engine.components.attachments.Controlable;
import com.base.engine.components.attachments.Updatable;
import com.base.engine.core.GameObject;
import com.base.engine.core.Input;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.physics.RigidBody.ForceGenerators.ConnectedPlanet;

public class PlanetWalking extends GameComponent implements Updatable {
	private static final Vector3f yAxis = new Vector3f(0, 1, 0);
	GameObject player;
	ConnectedPlanet planet;

	public PlanetWalking(GameObject player, ConnectedPlanet planet) {
		this.player = player;
		this.planet = planet;
	}

	@Override
	public int update(float delta) {
		Quaternion newRot = getTransform().getLookAtRotation(
				player.getTransform().getTransformedPos(), new Vector3f(0, 0, 1));
		// getTransform().getRot().getUp());

		getTransform().setRot(getTransform().getRot().nlerp(newRot, delta * 5.0f, true));
		return 1;
	}

}
