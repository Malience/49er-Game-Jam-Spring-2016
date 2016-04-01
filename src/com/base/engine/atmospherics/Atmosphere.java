package com.base.engine.atmospherics;

public class Atmosphere implements Atmospherics {
	float o2;

	public Atmosphere() {
		this(0);
	}

	public Atmosphere(float oxygen) {
		this.o2 = oxygen;
	}

	@Override
	public float getO2Level() {
		return o2;
	}
}
