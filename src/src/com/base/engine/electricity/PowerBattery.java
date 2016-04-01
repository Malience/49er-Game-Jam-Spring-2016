package com.base.engine.electricity;

public class PowerBattery extends PowerSource
{
	private float capacity;
	public PowerBattery(float startingPower, float capacity) {
		super(startingPower < capacity ? startingPower : capacity); //Check that comp sci shenangians!
		this.capacity = capacity;
	}
	
	@Override
	public float charge(float power)
	{
		float temp = capacity - currentPower;
		if(power <= temp)
		{
			currentPower += temp;
			return 0;
		}
		currentPower += temp;
		return power - temp;
	}
	
	
	public float getPower()
	{
		return this.currentPower;
	}
	
	public float getPowerPercent()
	{
		return this.currentPower/this.capacity;
	}
}
