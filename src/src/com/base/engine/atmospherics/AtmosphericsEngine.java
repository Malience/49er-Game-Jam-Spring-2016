package com.base.engine.atmospherics;

import java.util.ArrayList;

import com.base.engine.atmospherics.Enviroments.Vacuum;
import com.base.engine.components.attachments.Atmospherical;
import com.base.engine.components.attachments.EnviromentalComponent;
import com.base.engine.core.World;

public class AtmosphericsEngine 
{
	ArrayList<Atmospherical> atmosphericals;
	ArrayList<EnviromentalComponent> enviromentals;
	EnviromentalComponent defaultEnviro = new Vacuum(null, null);
	
	public void gather()
	{
		atmosphericals = World.world.getAtmospherics();
		enviromentals = World.world.getEnviromentals();
	}
	
	public void setupEnviroments()
	{
		for(Atmospherical atmo : atmosphericals)
		{
			if(atmo.updateEnviroment()) updateEnviroment(atmo);
		}
	}
	
	public void updateEnviroment(Atmospherical atmo)
	{
		for(EnviromentalComponent enviro : enviromentals)
		{
			if(atmo.updateEnviroment(enviro)) return;
		}
		atmo.setEnviroment(defaultEnviro);
	}
	
	public void simulate(float delta)
	{
		for(Atmospherical atmo : atmosphericals)
		{
			if(atmo.isActive()) atmo.effect(delta);
		}
	}
}
