package com.base.engine.components.movenlook;

import com.base.engine.components.attachments.Controlable;
import com.base.engine.core.GameObject;
import com.base.engine.core.Input;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.physics.RigidBody.ForceGenerators.ConnectedPlanet;

public class PlanetLook extends BoundedLook implements Controlable {
	private static Vector3f yAxis = new Vector3f(0, 1, 0);
	ConnectedPlanet planet;

	public PlanetLook(float sensitivity, ConnectedPlanet planet) {
		super(sensitivity);
		this.planet = planet;
	}

	//public static float currentXRot = 0;
	//public final static float maxAngle = 1.7f;

	@Override
	public int input(float delta) {
		if(planet != null)
		{
			Matrix4f mat = new Matrix4f();
			mat.initRotation(this.getTransform().getRot().getForward(), this.getTransform().getPos().sub(planet.object.getPosition()).normal());
			this.getTransform().setRot(new Quaternion(mat));
			yAxis = this.getTransform().getRot().getUp().normal();
		}
		else
		{
			Matrix4f mat = new Matrix4f();
			mat.initRotation(this.getTransform().getRot().getForward(), new Vector3f(0,1,0));
			this.getTransform().setRot(new Quaternion(mat));
			yAxis = new Vector3f(0,1,0);
		}
		
		if (Input.isMouseLocked()) {
			Vector2f deltaPos = Input.getCurrentMousePosition().sub(Input.getCenter());

			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;
			if (rotY || rotX)
				Input.setMousePosition(Input.getCenter());

			if (rotY)
				getTransform().rotate(yAxis, (float) Math.toRadians(deltaPos.getX() * sensitivity));
			if (rotX) {
				//currentXRot += (float) -Math.toRadians(deltaPos.getY() * sensitivity);
//				if (currentXRot > maxAngle) {
//					currentXRot = maxAngle;
//					return 0;
//				}
//				if (currentXRot < -maxAngle) {
//					currentXRot = -maxAngle;
//					return 0;
//				}
				getTransform().rotate(getTransform().getRot().getRight(),
						(float) -Math.toRadians(deltaPos.getY() * sensitivity));
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
	
	public static void setyAxis(Vector3f yaxis)
	{
		yAxis = yaxis;
	}
}
