package com.base.engine.components;

import com.base.engine.audio.AudioEngine;
import com.base.engine.components.attachments.Audible;

import static org.lwjgl.openal.AL10.*;

public class AudioComponent extends GameComponent implements Audible
{	
	int soundID;
	char type;
	
	public AudioComponent()
	{
		type = ' ';
		soundID = -1;
	}
	
	public AudioComponent(String soundName){this(soundName, AudioEngine.findType(soundName));}
	public AudioComponent(String soundName, char type){this(AudioEngine.findSoundLocation(soundName, type), type);}
	public AudioComponent(int soundID, char type)
	{
		this.soundID = soundID;
		this.type = type;
	}

	@Override
	public int getSoundID() {
		return soundID;
	}

	@Override
	public char getSoundType() {
		return type;
	}
}
