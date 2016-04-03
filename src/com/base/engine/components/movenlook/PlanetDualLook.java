package com.base.engine.components.movenlook;

import com.base.engine.components.attachments.Controlable;
import com.base.engine.core.GameObject;
import com.base.engine.core.Input;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.physics.RigidBody.ForceGenerators.ConnectedPlanet;

public class PlanetDualLook extends BoundedLook implements Controlable {
	private static Vector3f yAxis = new Vector3f(0, 1, 0);
	ConnectedPlanet planet;
	GameObject object;
	GameObject yobject;
	GameObject xobject;

	public PlanetDualLook(float sensitivity, GameObject object, GameObject yobject, GameObject xobject, ConnectedPlanet planet) {
		super(sensitivity);
		this.planet = planet;
		this.object = object;
		this.yobject = yobject;
		this.xobject = xobject;
	}

	public static float currentXRot = 0;
	public final static float maxAngle = 1.7f;

	@Override
	public int input(float delta) {
		if(planet != null)
		{
			Vector3f a = object.getTransform().getTransformedPos().sub(planet.object.getPosition()).normal();
			Vector3f b = object.getTransform().getRot().getForward();
			Vector3f c = a.cross(b);
			Vector3f d = a.cross(c).mul(-1);
			Matrix4f mat = new Matrix4f();
			mat.initRotation(d, a);
			object.getTransform().setRot(new Quaternion(mat));
		}
		else
		{
			Matrix4f mat = new Matrix4f();
			mat.initRotation(yobject.getTransform().getRot().getForward(), new Vector3f(0,1,0));
			object.getTransform().setRot(new Quaternion(mat).normalized());
		}
		
		if (Input.isMouseLocked()) {
			Vector2f deltaPos = Input.getCurrentMousePosition().sub(Input.getCenter());

			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;
			if (rotY || rotX)
				Input.setMousePosition(Input.getCenter());

			if(rotY)
				yobject.getTransform().rotate(yAxis, (float) Math.toRadians(deltaPos.getX() * sensitivity));
			if(rotX)
			{
//				currentXRot +=(float) -Math.toRadians(deltaPos.getY() * sensitivity);
//				if(currentXRot > maxAngle)
//				{
//					currentXRot = maxAngle;
//					return 0;
//				}
//				if(currentXRot < -maxAngle)
//				{
//					currentXRot = -maxAngle;
//					return 0;
//				}
				xobject.getTransform().rotate(xobject.getTransform().getRot().getRight(), (float) -Math.toRadians(deltaPos.getY() * sensitivity));
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
