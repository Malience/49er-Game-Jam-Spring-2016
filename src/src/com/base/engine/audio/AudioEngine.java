package com.base.engine.audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.openal.ALContext;
import org.lwjgl.openal.ALDevice;

import com.base.engine.components.Listener;
import com.base.engine.core.CoreEngine;
import com.base.engine.core.World;
import com.base.engine.core.math.Point;
import com.base.engine.core.math.SpatialHash;

public class AudioEngine 
{
	private static Listener mainListener;
	ALContext context;
	ALDevice device;
	int currentTrack = -1;
	boolean playMusic = true;
	
	private static HashMap<String, Integer> musicMap = new HashMap<String, Integer>();
	private static HashMap<String, Integer> soundsMap = new HashMap<String, Integer>();
	private static HashMap<String, Integer> effectsMap = new HashMap<String, Integer>();
	
	private static ArrayList<Music> music;
	private static ArrayList<Sound> sounds = new ArrayList<Sound>();
	private static ArrayList<SoundEffect> soundEffects = new ArrayList<SoundEffect>();
	
	public AudioEngine()
	{
		context = ALContext.create();
		device = context.getDevice();
		
		mainListener = new Listener(10, .2f, 1, 1);
		mainListener.getContext().makeCurrent();
		
		//context.makeCurrent();
		
		initMusicList();
		initSoundList();
		initEffectsList();
		
	}
	
	public void gather()
	{
		//TODO: Sound stuff here
	}
	
	public void play()
	{
		if(mainListener == null)
		{
			//mainListener = (Listener) World.world.focus.getAllComponentsOf("Listener").get(0);
			//mainListener = new Listener(10, .4f, 1, 1);
			//mainListener.getContext().makeCurrent();
		}
		
		if(currentTrack == -1 && playMusic)
		{
			//Find an appropriate song to play
			currentTrack = 0;
		}
		
		if(currentTrack != -1)
		{
			music.get(currentTrack).play();
		}
		
		//TODO: Take in AudioComponents and register what they want to play
		
	}
	
	public static void addMusic(Music music)
	{
		AudioEngine.music.add(music);
		musicMap.put(music.getFileName(), AudioEngine.music.indexOf(music));
	}
	
	public static char findType(String soundName)
	{
		if(musicMap.containsKey(soundName)) return 'M';
		if(soundsMap.containsKey(soundName)) return 'S';
		if(effectsMap.containsKey(soundName)) return 'E';
		return ' ';
	}
	
	public static int findSoundLocation(String soundName, char type)
	{
		switch(type)
		{
		case 'M':
			return musicMap.get(soundName);
		case 'S':
			return soundsMap.get(soundName);
		case 'E':
			return effectsMap.get(soundName);
		}
		return -1;
	}
	
	public static float getTotalVolume()
	{
		if(mainListener != null)
		{
			return mainListener.getTotalVolume();
		}
		return .01f;
	}
	
	public static float getSoundEffectVolume()
	{
		if(mainListener != null)
		{
			return mainListener.getSoundEffectVolume();
		}
		return .1f;
	}
	
	public static float getMusicVolume()
	{
		if(mainListener != null)
		{
			return mainListener.getMusicVolume();
		}
		return .01f;
	}
	
	public static void addListener(Listener listener)
	{
		mainListener = listener;
	}
	
	public void cleanUp()
	{
		device.destroy();
		context.destroy();
		mainListener.getDevice().destroy();
	}
	
	@Override
	public void finalize()
	{
		cleanUp();
	}
	
	public static void initMusicList()
	{
		music = new ArrayList<Music>();
		addMusic(new Music("Starflash.wav"));
		
	}
	
	public static void initSoundList()
	{
		
	}
	
	public static void initEffectsList()
	{
		
	}
}
