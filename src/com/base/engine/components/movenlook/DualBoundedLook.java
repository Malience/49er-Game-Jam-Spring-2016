package com.base.engine.components.movenlook;

import com.base.engine.components.attachments.Controlable;
import com.base.engine.core.GameObject;
import com.base.engine.core.Input;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;

public class DualBoundedLook extends BoundedLook implements Controlable {
	private static Vector3f yAxis = new Vector3f(0, 1, 0);
	GameObject yobject;
	GameObject xobject;

	public DualBoundedLook(float sensitivity, GameObject yobject, GameObject xobject) {
		super(sensitivity);
		this.yobject = yobject;
		this.xobject = xobject;
	}

	public static float currentXRot = 0;
	public final static float maxAngle = 1.7f;

	@Override
	public int input(float delta) {
		if (Input.isMouseLocked()) {
			Vector2f deltaPos = Input.getCurrentMousePosition().sub(Input.getCenter());

			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;
			if (rotY || rotX)
				Input.setMousePosition(Input.getCenter());

			if (rotY)
				yobject.getTransform().rotate(yAxis, (float) Math.toRadians(deltaPos.getX() * sensitivity));
			if (rotX) {
				currentXRot += (float) -Math.toRadians(deltaPos.getY() * sensitivity);
				if (currentXRot > maxAngle) {
					currentXRot = maxAngle;
					return 0;
				}
				if (currentXRot < -maxAngle) {
					currentXRot = -maxAngle;
					return 0;
				}
				xobject.getTransform().rotate(getTransform().getRot().getRight(),
						(float) -Math.toRadians(deltaPos.getY() * sensitivity));
			}
		}

		return 1;
	}
	
	public static void setyAxis(Vector3f yaxis)
	{
		yAxis = yaxis;
	}
}
