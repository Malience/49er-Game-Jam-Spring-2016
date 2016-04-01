package com.base.engine.electricity;

import com.base.engine.components.attachments.Tickable;

public class PowerGenerator extends PowerSource implements Tickable
{
	private float powerGeneration;
	public PowerGenerator(float startingPower, float powerGeneration) {
		super(startingPower);
		this.powerGeneration = powerGeneration;
	}
	
	@Override
	public void tick()
	{
		this.currentPower = powerGeneration;
	}
	
}
