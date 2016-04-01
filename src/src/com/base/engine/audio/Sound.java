package com.base.engine.audio;

import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourcef;

import java.util.HashMap;

import com.base.engine.audio.SoundLoading.ResourceManagement.SoundResource;


public class Sound 
{
	protected boolean playing = false;
	private static HashMap<String, SoundResource> loadedSounds = new HashMap<String, SoundResource>();
	private SoundResource resource;
	private String fileName;
	
	public Sound(String fileName)
	{
		this.fileName = fileName;
		SoundResource oldResource = loadedSounds.get(fileName);
		
		if(oldResource != null)
		{
			resource = oldResource;
			resource.addReference();
		}
		else
		{
			resource = new SoundResource(fileName);
			loadedSounds.put(fileName, resource);
		}
	}
	
	@Override
	protected void finalize()
	{
		if(resource.removeReference() && !fileName.isEmpty())
			loadedSounds.remove(fileName);
	}
	
	public void play()
	{
		if (alGetSourcei(this.getSource(), AL_SOURCE_STATE) != AL_PLAYING)
        {
			System.out.println("Playing song");
        	alSourcef(this.getSource(), AL_GAIN, AudioEngine.getTotalVolume());
            alSourcePlay(this.getSource());
        }
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public int getSource()
	{
		return resource.getSource();
	}
}
