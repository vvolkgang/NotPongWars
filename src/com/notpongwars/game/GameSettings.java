package com.notpongwars.game;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.notpongwars.game.GameSettings.InputType;
import com.notpongwars.game.GameSettings.MultiplayerType;

abstract class Settings {
	public float playerPaddleSpeed = 1;
	public float enemyAwarenessMultiplier = 0.5f; //between 0 and 1, 0 meaning 100% awareness
	public float enemyPaddleSpeed= 5;
	public float ballInitialSpeed = 5;
	public float ballMaxSpeed = 20;
	public float ballSpeedMultiplier = 1.02f; // ball increment, bigger than 1
	
	public int endScore = 5;
	public boolean musicOn = true;
	public boolean soundsOn = true;

	public float AccelerometerDeadZone = 0.25f;
	public float AccelerometerSensorOffset = 0f;

	public MultiplayerType multiplayerType = MultiplayerType.NULL;
	public InputType inputType = InputType.TOUCH_ONLY;
}


public class GameSettings{
	Settings settings;

	public enum MultiplayerType{
		NULL,
		SERVER,
		CLIENT
	}

	public enum InputType { 
		ACCELEROMETER,
		TOUCH_ONLY
	}

	//--------------------- Constructors
	public GameSettings(Context context){
		settings = new DefaultSettings();

		getPreferences(context);

	}

	//use this for single player
	public GameSettings(Context context, int difficulty){
		switch(difficulty){
		case 0: settings = new EasySettings(); break;
		case 1: settings = new NormalSettings(); break;
		case 2: settings = new HardSettings(); break;
		case 3: settings = new ImpossibruSettings(); break;
		case 4: settings = new EnduranceSettings(); break;
		}
		getPreferences(context);
		
		if(difficulty == 4)
			settings.endScore = 1;

	}

	//use this for multiplayer
	public GameSettings(Context context, MultiplayerType multiplayerType){
		this(context);
		settings = new MultiplayerSettings(multiplayerType);

		getPreferences(context);
	}

	//End 

	//--------------------- Class Methods
	public float getPlayerPaddleSpeed(){ return settings.playerPaddleSpeed;}
	public float getEnemyAwarenessMultiplier(){ return settings.enemyAwarenessMultiplier;}
	public float getEnemyPaddleSpeed(){return settings.enemyPaddleSpeed;}
	public float getBallInitialSpeed(){return settings.ballInitialSpeed;}
	public float getBallMaxSpeed(){return settings.ballMaxSpeed;}
	public float getBallSpeedMultiplier() {return settings.ballSpeedMultiplier;}
	public int getEndScore(){return settings.endScore;}
	public boolean getMusic(){return settings.musicOn;}
	public boolean getSounds(){return settings.soundsOn;}
	public boolean getMultiplayerTypeIsServer(){return settings.multiplayerType == MultiplayerType.SERVER;}
	public boolean getInputTypeIsAccelerometer(){return settings.inputType == InputType.ACCELEROMETER;}
	public MultiplayerType getMultiplayerType(){return settings.multiplayerType;}
	public InputType getInputType(){return settings.inputType;}
	public float getAccelerometerDeadzone() { return settings.AccelerometerDeadZone; }
	public float getAccelerometerSensorOffset() { return settings.AccelerometerSensorOffset; }

	public void setEndScore(int score){settings.endScore = score;}

	private void getPreferences(Context context){
		settings.endScore = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getString("endScore", "5"));
		settings.musicOn = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getBoolean("music", true);
		settings.soundsOn = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getBoolean("sounds", true);
		settings.inputType = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getBoolean("accelerometer", true) ? InputType.ACCELEROMETER : InputType.TOUCH_ONLY;
		settings.AccelerometerDeadZone = Float.parseFloat(PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getString("acceldeadzone", "0.25f"));
		settings.AccelerometerSensorOffset = Float.parseFloat(PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getString("acceloffset", "0f"));
	}
	//---------------------End 

	//--------------------- Default Settings 
	private class DefaultSettings extends Settings{

	}

	private class MultiplayerSettings extends Settings{
		public MultiplayerSettings(MultiplayerType type){
			multiplayerType = type;

		}
	}
	
	private class EnduranceSettings extends Settings{
		public EnduranceSettings(){
			playerPaddleSpeed = 1;
			enemyPaddleSpeed = 10;
			enemyAwarenessMultiplier = 0.5f;
			ballInitialSpeed = 7.5f;
			ballMaxSpeed = 17.5f;
			ballSpeedMultiplier = 1.12f;
			
			Log.d("GAME SETTINGS", "Endurance Settings Loaded.");
		}
	}

	private class EasySettings extends Settings{
		public EasySettings(){
			playerPaddleSpeed = 1;
			enemyPaddleSpeed = 0.2f;
			enemyAwarenessMultiplier = 0.80f;
			ballInitialSpeed = 5f;
			ballMaxSpeed = 12.5f;
			ballSpeedMultiplier = 1.10f;
			
			Log.d("GAME SETTINGS", "Easy Settings Loaded.");
		}
	}

	private class NormalSettings extends Settings{
		public NormalSettings(){
			playerPaddleSpeed = 1;
			enemyPaddleSpeed = 10;
			enemyAwarenessMultiplier = 0.5f;
			ballInitialSpeed = 7.5f;
			ballMaxSpeed = 17.5f;
			ballSpeedMultiplier = 1.12f;

			Log.d("GAME SETTINGS", "Normal Settings Loaded.");
		}
	}

	private class HardSettings extends Settings{
		public HardSettings(){
			playerPaddleSpeed = 1;
			enemyPaddleSpeed = 20;
			enemyAwarenessMultiplier = 0.3f;
			ballInitialSpeed = 10f;
			ballMaxSpeed = 15f;
			ballSpeedMultiplier = 1.10f;

			Log.d("GAME SETTINGS", "Hard Settings Loaded.");
		}
	}

	private class ImpossibruSettings extends Settings{
		public ImpossibruSettings(){
			playerPaddleSpeed = 1;
			enemyPaddleSpeed = 50;
			enemyAwarenessMultiplier = 0f;
			ballInitialSpeed = 7f;
			ballMaxSpeed = 20f;
			ballSpeedMultiplier = 1.10f;

			Log.d("GAME SETTINGS", "Impossibru Settings Loaded.");
		}
	}
	//End


}

