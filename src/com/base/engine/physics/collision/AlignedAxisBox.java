package com.base.engine.physics.collision;

import com.base.engine.components.attachments.Collidable;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Vector3f;

public class AlignedAxisBox extends Box implements Collidable {
	public AlignedAxisBox() {
		super();
	}

	public AlignedAxisBox(Vector3f halfSize) {
		super(halfSize);
	}

	@Override
	public Vector3f getAxis(int index) {
		Vector3f pos = parent.getTransform().getTransformedPos();
		Vector3f scale = parent.getTransform().getScale();
		Matrix4f translationMatrix = new Matrix4f().initTranslation(pos.getX(), pos.getY(), pos.getZ());
		Matrix4f scaleMatrix = new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ());

		return translationMatrix.mul(scaleMatrix).mul(offset).getAxisVector(index);
	}
}
