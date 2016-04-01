package com.base.engine.audio;

import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourcef;

public class SoundEffect extends Sound {
	public SoundEffect(String fileName) {
		super(fileName);
	}

	@Override
	public void play() {
		if (alGetSourcei(this.getSource(), AL_SOURCE_STATE) != AL_PLAYING) {
			System.out.println("Playing song");
			alSourcef(this.getSource(), AL_GAIN, AudioEngine.getTotalVolume() * AudioEngine.getSoundEffectVolume());
			alSourcePlay(this.getSource());
		}
	}
}
