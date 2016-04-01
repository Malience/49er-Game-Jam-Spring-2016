package com.base.engine.audio.SoundLoading;

import static org.lwjgl.openal.AL10.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

public class WaveFile extends SoundFile {
	@Override
	public int compileSource(AudioInputStream soundStream) {
		try {
			int buffer = alGenBuffers();

			AudioFormat fmt = soundStream.getFormat();

			int sampleRate = (int) fmt.getSampleRate();
			int format = fmt.getChannels();
			int fFormat = 0;

			if (format == 1) {
				if (fmt.getSampleSizeInBits() == 8)
					fFormat = AL_FORMAT_MONO8;
				else if (fmt.getSampleSizeInBits() == 16)
					fFormat = AL_FORMAT_MONO16;
			} else if (format == 2) {
				if (fmt.getSampleSizeInBits() == 8)
					fFormat = AL_FORMAT_STEREO8;
				else if (fmt.getSampleSizeInBits() == 16)
					fFormat = AL_FORMAT_STEREO16;
			}

			// Length of the WAV file
			int length = 0;
			try {
				length = soundStream.available();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			if (length <= 0)
				length = soundStream.getFormat().getChannels() * (int) soundStream.getFrameLength()
						* soundStream.getFormat().getSampleSizeInBits() / 8;

			byte[] samples = new byte[length];
			DataInputStream dis = new DataInputStream(soundStream);
			try {
				dis.readFully(samples);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			ByteBuffer src = ByteBuffer.wrap(samples);
			src.order(ByteOrder.LITTLE_ENDIAN);

			ByteBuffer data = convertAudioBytes(src, fmt.getSampleSizeInBits() == 16);

			// Close the input streams
			try {
				soundStream.close();
				dis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			alBufferData(buffer, fFormat, data, sampleRate);

			int source = alGenSources();
			alSourcei(source, AL_BUFFER, buffer);

			alDeleteBuffers(buffer);

			return source;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return 0;
	}
}
