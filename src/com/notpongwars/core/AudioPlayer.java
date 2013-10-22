package com.notpongwars.core;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class AudioPlayer {
	
	private static MediaPlayer mp = null;
	private static SoundPool soundPool;
	private static Context context;
	
	private static boolean enableSoundEffects = true;
	public static boolean isSoundEffectsEnable() { return enableSoundEffects; }
	public static void setEnableSoundsEffect(boolean enabled) { enableSoundEffects = enabled; }
	
	
	public static void setContext(Context context) {
		AudioPlayer.context = context;
	}
	
	public static void playMusic(int resource) {
		if(context == null) return;
		stopMusic();
		mp = MediaPlayer.create(context, resource);
		mp.setLooping(true);
		mp.start();
	}
	
	public static void stopMusic() {
		if(mp != null) {
			mp.stop();
			mp.release();
			mp = null;
		}
	}
	
	public static int createSound(int sound) {
		if(context == null) return -1;
		if(soundPool == null)
			soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		return soundPool.load(context, sound, 1);
	}
	
	public static void playSound(int soundID) {
		if(!enableSoundEffects) return;
		if(context == null) return;
		
		AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		float actualVolume = (float) audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;
		if(soundPool != null)
			soundPool.play(soundID, volume, volume, 1, 0, 1f);
	}
	
}
