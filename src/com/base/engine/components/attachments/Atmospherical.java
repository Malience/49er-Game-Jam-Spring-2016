package com.base.engine.components.attachments;

public interface Atmospherical extends ComponentAttachment {
	public int effect(float delta);

	public boolean updateEnviroment();

	public boolean updateEnviroment(EnviromentalComponent enviro);

	public void setEnviroment(EnviromentalComponent enviro);
}
