package com.base.engine.atmospherics;

import com.base.engine.atmospherics.Enviroments.Enviroment;
import com.base.engine.components.GameComponent;
import com.base.engine.components.attachments.Atmospherical;
import com.base.engine.components.attachments.EnviromentalComponent;

public class AtmosphericsComponent extends GameComponent implements Atmospherical {
	protected Enviroment enviro = null;

	public AtmosphericsComponent() {

	}

	@Override
	public int effect(float delta) {
		if (enviro == null)
			return 1;
		return 1;
	}

	@Override
	public boolean updateEnviroment() {
		return this.getTransform().hasChanged();
	}

	@Override
	public boolean updateEnviroment(EnviromentalComponent enviro) {
		if (enviro instanceof Enviroment) {
			Enviroment envi = (Enviroment) enviro;
			if (envi.isInside(this.getTransform().getTransformedPos())) {
				this.enviro = envi;
				return true;
			}
		}
		return false;
	}

	public void setEnviroment(EnviromentalComponent enviro) {
		if (enviro instanceof Enviroment)
			this.enviro = (Enviroment) enviro;
		else
			this.enviro = null;
	}

}
