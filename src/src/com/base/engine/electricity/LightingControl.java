package com.base.engine.electricity;

import com.base.engine.components.GameComponent;
import com.base.engine.components.attachments.Tickable;
import com.base.engine.components.lighting.DirectionalLight;
import com.base.engine.core.GameObject;
import com.base.engine.core.math.Vector3f;

public class LightingControl extends GameObject
{	
	private static final Vector3f lightColor = new Vector3f(1f,1f,1f);
	private static final Vector3f lowColor = new Vector3f(.75f,.75f,.75f);
	private static final Vector3f darkColor = new Vector3f(.5f,0f,0f);
	private static final Vector3f noColor = new Vector3f(0f,0f,0f);
	
	
	private static final float lightIntensity = 1;
	private static final float lowIntensity = .75f;
	private static final float darkIntensity = .3f;
	private static final float noIntensity = 0f;
	
	private static final DirectionalLight lighting = new DirectionalLight(noColor, noIntensity);
	
	private byte level;
	
	private RoomPowerUnit rpu;
	
	public LightingControl(RoomPowerUnit rpu)
	{
		this.addComponent(lighting);
		this.addComponent(new ControlUnit());
		this.rpu = rpu;
		this.getTransform().rotate(new Vector3f(1,0,0), (float)Math.toRadians(-90));
		this.addChild(rpu);
	}
	
	private class ControlUnit extends GameComponent implements Tickable
	{
		@Override
		public void tick() {
			//System.out.println(rpu.getPower());
			
			if(rpu.checkLevel(RoomPowerUnit.LIGHTING))
			{
				if(level != 3)
				{
					lighting.setColor(lightColor);
					lighting.setIntensity(lightIntensity);
					level = 3;
				}
			}
			else if(rpu.checkLevel(RoomPowerUnit.LOWLIGHTS))
			{
				if(level != 2)
				{
					lighting.setColor(lowColor);
					lighting.setIntensity(lowIntensity);
					level = 2;
				}
			}
			else if(rpu.checkLevel(RoomPowerUnit.DARKLIGHTS))
			{
				if(level != 1)
				{
					lighting.setColor(darkColor);
					lighting.setIntensity(darkIntensity);
					level = 1;
				}
			}
			else if(level != 0)
			{
				lighting.setColor(noColor);
				lighting.setIntensity(noIntensity);
				level = 0;
			}
		}	
	}
	
	public DirectionalLight getDirectionalLight()
	{
		return lighting;
	}
}
