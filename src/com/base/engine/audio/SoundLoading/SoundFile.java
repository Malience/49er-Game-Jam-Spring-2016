package com.base.engine.audio.SoundLoading;

import java.io.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.BufferUtils;

public abstract class SoundFile {
	public int compileSource(String fileName) {
		try {
			return compileSource(new File("./res/audio/" + fileName));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return 0;
	}

	public int compileSource(File soundFile) {
		try {
			AudioInputStream soundStream = AudioSystem.getAudioInputStream(soundFile);
			int fin = compileSource(soundStream);
			soundStream.close();
			return fin;
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int compileSource(AudioInputStream soundStream) {
		return 0;
	}

	protected ByteBuffer convertAudioBytes(ByteBuffer samples, boolean stereo) {
		ByteBuffer dest = BufferUtils.createByteBuffer(samples.capacity());

		if (stereo) {
			ShortBuffer dest_short = dest.asShortBuffer();
			ShortBuffer src_short = samples.asShortBuffer();

			while (src_short.hasRemaining())
				dest_short.put(src_short.get());
		} else {
			while (samples.hasRemaining())
				dest.put(samples.get());
		}

		dest.rewind();
		return dest;
	}
}
