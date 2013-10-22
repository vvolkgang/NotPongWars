package com.notpongwars.activities;

import com.notslip.notpongwars.R;
import com.notpongwars.core.AudioPlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class NotPongWarsActivity extends Activity implements OnClickListener {

	public static final String TAG = "[CORE]" + NotPongWarsActivity.class.getSimpleName();

	Button spButton;
	Button mpButton;
	//Button settingsButton;
	Button helpButton;
	Button quitButton;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		setContentView(R.layout.main);
		
		spButton = (Button) findViewById(R.id.sp_button);
		spButton.setOnClickListener(this);

		mpButton = (Button)findViewById(R.id.mp_button);
		mpButton.setOnClickListener((OnClickListener) this);

		helpButton = (Button)findViewById(R.id.help_button);
		helpButton.setOnClickListener((OnClickListener) this);

		quitButton = (Button)findViewById(R.id.quit_button);
		quitButton.setOnClickListener((OnClickListener) this);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		AudioPlayer.setContext(this);

		
		Log.d(TAG, "View added");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	
    	return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.settings:
    		startActivity(new Intent(this, GamePreferencesActivity.class));

    		return true;
    	
    	// More items go there
    	}
    	
    	return false;
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "Destroying...");
		
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "Stopping...");

		super.onStop();
	}

	public void onClick(View v) {
		lockButtons();
		switch (v.getId()) {
		case R.id.sp_button: //Singleplayer button
			//openNewGameDialog();
			openChooseGameMode();
			break;
		case R.id.mp_button: //Multiplayer button
			Intent i = new Intent(this, MultiplayerActivity.class);
			startActivity(i);
			break;
		//case R.id.settings_button:
		//	unlockButtons();
		//	break;
		case R.id.help_button: //About button
			Intent in = new Intent(this, HelpView.class);
			startActivity(in);
			break;
		case R.id.quit_button: //Quit button
			openRealyQuitDialog();
			break;
		}


	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus)
			unlockButtons();
		super.onWindowFocusChanged(hasFocus);
	}

	private void unlockButtons(){
		spButton.setEnabled(true);
		mpButton.setEnabled(true);
		//settingsButton.setEnabled(true);
		helpButton.setEnabled(true);
		quitButton.setEnabled(true);

		spButton.setClickable(true);
		mpButton.setClickable(true);
		//settingsButton.setClickable(true);
		helpButton.setClickable(true);
		quitButton.setClickable(true);
	}

	private void lockButtons(){
		spButton.setEnabled(false);
		mpButton.setEnabled(false);
		//settingsButton.setEnabled(false);
		helpButton.setEnabled(false);
		quitButton.setEnabled(false);

		spButton.setClickable(false);
		mpButton.setClickable(false);
		//settingsButton.setClickable(false);
		helpButton.setClickable(false);
		quitButton.setClickable(false);
	}



	private void openRealyQuitDialog() {
		new AlertDialog.Builder(this)
		.setTitle(R.string.quit_title)
		.setItems(R.array.quit, 
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if(which == 0)
					finish();
			}
		})
		.show();

	}

	private void openNewGameDialog() {
		new AlertDialog.Builder(this)
		.setTitle(R.string.single_player_title)
		.setItems(R.array.difficulty,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialoginterface, int i) {
				startGame(i);
			}
		})
		.show();
	}
	
	private void openChooseGameMode() {
		new AlertDialog.Builder(this)
		.setTitle(R.string.gamemode_title)
		.setItems(R.array.gamemode, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialoginterface, int i) {
				if(i == 0)
					openNewGameDialog();
				else
					startGame(4);
			}
		})
		.show();
	}

	private void startGame(int i) {
		Intent in = new Intent(this, SinglePlayerActivity.class);
		in.putExtra(SinglePlayerActivity.KEY_DIFFICULTY, i);
		startActivity(in);
	}

}
