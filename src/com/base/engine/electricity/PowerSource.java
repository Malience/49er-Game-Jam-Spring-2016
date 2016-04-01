package com.base.engine.electricity;

import com.base.engine.components.GameComponent;
import com.base.engine.components.attachments.Tickable;

public abstract class PowerSource extends GameComponent implements Tickable {
	float currentPower;

	public PowerSource(float startingPower) {
		this.currentPower = startingPower;
	}

	public float charge(float amount) {
		this.currentPower += amount;
		return 0f;
	}

	public float decharge(float amount) {
		this.currentPower -= amount;
		float temp = 0;
		if (currentPower < 0) {
			temp -= currentPower;
			currentPower = 0;
		}
		return temp;
	}

	public float routePower(float amount) {
		if (amount < currentPower) {
			currentPower -= amount;
			return amount;
		} else
			return 0;
	}

	public void tick() {

	}

}
