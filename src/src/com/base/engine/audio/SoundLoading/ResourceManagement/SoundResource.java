package com.base.engine.audio.SoundLoading.ResourceManagement;

import static org.lwjgl.openal.AL10.alDeleteSources;

import com.base.engine.audio.SoundLoading.SoundUtil;
import com.base.engine.core.ReferenceCounter;

public class SoundResource extends ReferenceCounter
{
	private int source;
	
	public SoundResource(String fileName)
	{
		source = SoundUtil.compileSource(fileName);
		if(source == 0)
		{
			new Exception("SoundResource: Source not compiled").printStackTrace();;
		}
	}
	
	public int getSource()
	{
		return source;
	}
	
	@Override
	public void finalize()
	{
		alDeleteSources(source);
	}
}
