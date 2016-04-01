package com.base.engine.electricity.console;

import com.base.engine.components.attachments.Tickable;
import com.base.engine.core.World;
import com.base.engine.electricity.PowerControl;
import com.base.engine.electricity.RoomPowerUnit;

public abstract class PoweredConsole extends Console {
	private RoomPowerUnit rpu;
	private float powerLevel;
	boolean on = false;

	public PoweredConsole(RoomPowerUnit rpu) {
		this(rpu, RoomPowerUnit.SUBSYSTEM);
	}

	public PoweredConsole(RoomPowerUnit rpu, float powerLevel) {
		super();
		this.rpu = rpu;

		this.powerLevel = powerLevel;

		this.addComponent(new PowerUnit());
	}

	private class PowerUnit extends PowerControl implements Tickable {
		@Override
		public void tick() {
			if (rpu != null) {
				if (rpu.checkLevel(powerLevel))
					on = true;
				else
					on = false;
			}
		}
	}

	@Override
	public void open() {
		if (on)
			super.open();
		else
			World.world.switchMenu(BLANK_SCREEN);
	}
}
