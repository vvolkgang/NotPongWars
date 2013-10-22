package com.notpongwars.activities;

import com.notpongwars.core.AudioPlayer;
import com.notpongwars.core.InputManager;
import com.notpongwars.game.GameSettings;
import com.notpongwars.game.NotPongWarsSinglePlayer;
import com.notslip.notpongwars.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SinglePlayerActivity extends Activity {

	public static final String KEY_DIFFICULTY = "SinglePlayer.Difficulty";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        int difficulty = getIntent().getIntExtra(KEY_DIFFICULTY, 1);
        GameSettings gameSettings = new GameSettings(this, difficulty);
                
        if(gameSettings.getMusic()) {
        	AudioPlayer.playMusic(R.raw.music);
        }
        AudioPlayer.setEnableSoundsEffect(gameSettings.getSounds());

        
        
        setContentView(new NotPongWarsSinglePlayer(this, gameSettings));
	}
	
	@Override
	protected void onDestroy() {
		AudioPlayer.stopMusic();
		
		super.onDestroy();
	}
}
