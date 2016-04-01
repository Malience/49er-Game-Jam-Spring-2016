package com.base.engine.audio;

import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourcef;

public class Music extends Sound {
	public Music(String fileName) {
		super(fileName);
	}

	@Override
	public void play() {
		// System.out.println(getSource());
		if (alGetSourcei(getSource(), AL_SOURCE_STATE) != AL_PLAYING) {
			// System.out.println("Playing song");
			alSourcef(getSource(), AL_GAIN, AudioEngine.getTotalVolume() * AudioEngine.getMusicVolume());
			alSourcePlay(getSource());
		}
	}
}
