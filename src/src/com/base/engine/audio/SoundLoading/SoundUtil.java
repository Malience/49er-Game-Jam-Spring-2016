package com.base.engine.audio.SoundLoading;

import java.io.File;

public class SoundUtil 
{
	public static int compileSource(String fileName)
	{
		return compileSource(new File("./res/audio/" + fileName));
	}
	

	public static int compileSource(File soundFile)
	{
		switch(findType(soundFile.getName()))
		{
		case WAVE:
			return new WaveFile().compileSource(soundFile);
		default:
			return 0;
		}
	}
	
	
	private static AudioType findType(String fileName)
	{
		String[] str = fileName.split("\\.");
		String extension = str[str.length-1];
		
		switch(extension)
		{
		case "wav":
		case "wave":
			return AudioType.WAVE;
		}
		System.err.println("Unsupported audio extension ." + extension + " from file " + fileName);
		new Exception().printStackTrace();
		return null;
	}
	
	private enum AudioType
	{
		WAVE;
	}
}
