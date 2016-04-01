package com.base.engine.electricity;

import com.base.engine.components.attachments.Tickable;
import com.base.engine.core.GameObject;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.MaterialAttachment;
import com.base.engine.rendering.RenderGenerator;

public class RoomPowerUnit extends GameObject {
	// POWER LEVELS
	public final static float LARGEMACHINE = .95f;
	public final static float THRUSTER = .95f;
	public final static float FLIGHTSYSTEM = .90f;
	public final static float LIGHTING = .60f;
	public final static float PODS = .50f;
	public final static float AIRLOCK = .40f;
	public final static float LOWLIGHTS = .35f;
	public final static float SUBSYSTEM = .35f;
	public final static float ATMOS = .20f;
	public final static float PRIORITYSYSTEM = .15f;
	public final static float DARKLIGHTS = .05f;

	private final static RenderGenerator render = new RenderGenerator("RPU001.obj", "RPU001.png",
			new MaterialAttachment[] { new MaterialAttachment("specularIntensity", 1.0f),
					new MaterialAttachment("specularPower", 30.0f) });

	PowerSource source;
	PowerBattery battery;
	PowerUnit unit;

	public RoomPowerUnit(float startingPower, float capacity) {
		battery = new PowerBattery(startingPower, capacity);
		unit = new PowerUnit();
		this.addComponent(unit);
		this.addComponent(render.generate());

	}

	private final static float drain = 10;
	private final static float power = 100;
	private float currentLevel = 1f;

	private class PowerUnit extends PowerControl implements Tickable {
		@Override
		public void tick() {
			if (source != null) {
				float back = battery.charge(source.routePower(power));
				source.charge(back);
			}
			battery.decharge(drain);
			currentLevel = battery.getPowerPercent();
		}
	}

	public boolean checkLevel(float level) {
		return level <= currentLevel;
	}

	public void drain(float amount) {
		battery.decharge(amount);
	}

	public float getPower() {
		return battery.getPower();
	}
}
