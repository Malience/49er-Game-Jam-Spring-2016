package com.base.engine.atmospherics.Enviroments;

import com.base.engine.atmospherics.Atmosphere;
import com.base.engine.atmospherics.Atmospherics;
import com.base.engine.components.GameComponent;
import com.base.engine.components.attachments.EnviromentalComponent;
import com.base.engine.core.math.Vector3f;

public abstract class Enviroment extends GameComponent implements EnviromentalComponent {
	Atmosphere atmos;
	Vector3f min, max;

	public Enviroment(Vector3f min, Vector3f max, float oxygen) {
		this.min = min;
		this.max = max;
		atmos = new Atmosphere(oxygen);
	}

	private static final float SAFE_OXYGEN_LEVEL = 20;

	public boolean isBreathable() {
		return atmos.getO2Level() > SAFE_OXYGEN_LEVEL;
	}

	@Override
	public Atmospherics getAtmosphere() {
		return atmos;
	}

	public boolean isInside(Vector3f pos) {
		return pos.x <= max.x && pos.y <= max.y && pos.z <= max.z && pos.x >= min.x && pos.y >= min.y && pos.z >= min.z;
	}
}
