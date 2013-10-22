package com.notpongwars.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import com.notslip.notpongwars.R;
import com.notpongwars.core.AudioPlayer;
import com.notpongwars.core.bluetooth.BluetoothManager;
import com.notpongwars.core.bluetooth.BluetoothManager.SelectedOptionItem;
import com.notpongwars.core.bluetooth.BluetoothService;
import com.notpongwars.game.GameSettings;
import com.notpongwars.game.GameSettings.MultiplayerType;
import com.notpongwars.game.NotPongWarsMultiplayerGame;


public class MultiplayerActivity extends Activity implements OnClickListener {

	private String mConnectedDeviceName;

	// Debugging
	private static final String TAG = "MultiplayerActivity";
	private static final boolean D = false;

	//Bluetooth
	BluetoothManager btManager;

	//Game
	NotPongWarsMultiplayerGame game;
	GameSettings settings;
	MultiplayerType mpType = MultiplayerType.NULL;


	// Layout Views
	private TextView mTitle;

	//Command names
	public static final byte SETTINGS = 1;
	public static final byte START_GAME = 2;
	public static final byte BALL_POSITION = 3;
	public static final byte PLAYER_LEFT_POSITION = 4;
	public static final byte PLAYER_RIGHT_POSITION = 5;
	public static final byte END_GAME = 6;
	public static final byte BULLET = 7;
	public static final byte PLAYER_LEFT_SCORE = 8;
	public static final byte PLAYER_RIGHT_SCORE = 9;
	public static final String SPLITTER = ";";

	public static final String START_GAME_STRING = START_GAME+SPLITTER;
	public static final String END_GAME_STRING = END_GAME+SPLITTER;

	//public static final String SETTINGS_STRING = 1;
	//public static final String BALL_POSITION_STRING = 3;
	//public static final String PLAYER_LEFT_POSITION_STRING = 4;
	//public static final String PLAYER_RIGHT_POSITION_STRING = 5;
	//public static final String SPLITTER_STRING = ";";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.multiplayer);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


		//CONNECT BLUETOOTH
		btManager = new BluetoothManager(this, btHandler);

		// Set up the custom title
		mTitle = (TextView) findViewById(R.id.title_left_text);
		mTitle.setText(R.string.app_name);
		mTitle = (TextView) findViewById(R.id.title_right_text);

		//AudioPlayer.playMusic(R.raw.music);


	}


	@Override
	protected void onStart() {
		btManager.onStart();

		super.onStart();
	}

	@Override
	protected void onResume() {
		btManager.onResume();

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		AudioPlayer.stopMusic();
		btManager.onDestroy();

		super.onDestroy();
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		return btManager.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = btManager.onOptionsItemSelected(item);

		if(btManager.getSelectedOptionItem() == SelectedOptionItem.SCAN)
			mpType = MultiplayerType.CLIENT;

		if(btManager.getSelectedOptionItem() == SelectedOptionItem.DISCOVER)
			mpType = MultiplayerType.SERVER;

		if(D) Log.i(TAG, "Multiplayer Type> "+ (mpType == MultiplayerType.SERVER? "Server": "Client"));

		return result;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		btManager.onActivityResult(requestCode, resultCode, data);

	}

	private void decodeMessage(String message){
		String [] decoded = message.split(SPLITTER);

		try{
			switch(Byte.parseByte(decoded[0])){
			case SETTINGS:if(decoded.length >2) setSettings(decoded[1]);break; //the only setting sent by the server is the EndGameScore
			case START_GAME: startGame(); break;
			case END_GAME: endGame(); break;
			case BALL_POSITION: if(decoded.length >3) setBallPosition(decoded[1],decoded[2]); break;
			case PLAYER_LEFT_POSITION: if(decoded.length >3) setPlayerLeftPosition(decoded[1],decoded[2]); break;
			case PLAYER_RIGHT_POSITION:if(decoded.length >3) setPlayerRightPosition(decoded[1],decoded[2]); break;
			case BULLET: break;
			case PLAYER_LEFT_SCORE: if(decoded.length >2) setPlayerLeftScore(Integer.parseInt(decoded[1])); break;
			case PLAYER_RIGHT_SCORE: if(decoded.length >2) setPlayerRightScore(Integer.parseInt(decoded[1])); break;

			}
		}
		catch(NumberFormatException e){
			Log.e(TAG, e.getLocalizedMessage());
			Log.e(TAG, e.getMessage());
		}
	}

	private void setSettings(String endScore){
		//settings.setEndScore(Integer.parseInt(endScore));
	}

	private void startGame(){
		game = new NotPongWarsMultiplayerGame(this, settings, btManager);
		setContentView(game);
	}

	private void setBallPosition(String x, String y){
		if(game != null){
			Log.e(TAG, "Ball Position> "+x +" , "+y);
			PointF point = new PointF(Float.parseFloat(x),Float.parseFloat(y));
			game.setBallPosition(point);
		}
	}

	private void setPlayerLeftPosition(String x, String y){
		if(game != null){
			PointF point = new PointF(Float.parseFloat(x),Float.parseFloat(y));
			game.setPlayerLeftPosition(point);
		}
	}

	private void setPlayerRightPosition(String x, String y){
		if(game != null){
			PointF point = new PointF(Float.parseFloat(x),Float.parseFloat(y));
			game.setPlayerRightPosition(point);
		}
	}

	private void endGame(){
		if(game != null){
			game.endGame();

		}
	}
	
	private void setPlayerLeftScore(int score){
		if(game != null)
			game.setPlayerLeftScore(score);
	}

	private void setPlayerRightScore(int score){
		if(game != null)
			game.setPlayerRightScore(score);
	}
	
	// The Handler that gets information back from the BluetoothService
	private final Handler btHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BluetoothManager.MESSAGE_STATE_CHANGE:
				if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:					
					mTitle.setText(R.string.title_connected_to);
					mTitle.append(mConnectedDeviceName);
					if(D) Log.d(TAG, "Connected.");

					settings = new GameSettings(MultiplayerActivity.this, mpType);

					if(mpType == MultiplayerType.SERVER){
						btManager.sendMessage(SETTINGS+";"+ settings.getEndScore());
						btManager.sendMessage(START_GAME_STRING);

						if(D) Log.d(TAG, "Server sent settings and start game.");
					}

					startGame();

					break;
				case BluetoothService.STATE_CONNECTING:
					mTitle.setText(R.string.title_connecting);
					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					mTitle.setText(R.string.title_not_connected);
					break;
				}
				break;

			case BluetoothManager.MESSAGE_WRITE: //this is the message that was sent, use only if confirmation is needed
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);

				if(D) Log.d(TAG, "MESSAGE_WRITE> "+writeMessage);

				/*
				if(mpType == MultiplayerType.SERVER && writeMessage.equals(START_GAME_STRING)){
					if(D) Log.d(TAG, "Server will start the game.");

					startGame();
				}
				 */
				break;

			case BluetoothManager.MESSAGE_READ: //this is the message received from the other mobile
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);

				if(D) Log.d(TAG, "MESSAGE_READ> "+readMessage);

				decodeMessage(readMessage);

				break;

			case BluetoothManager.MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(BluetoothManager.DEVICE_NAME);
				Toast.makeText(getApplicationContext(), "Connected to "
						+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();
				break;

			case BluetoothManager.MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(), msg.getData().getString(BluetoothManager.TOAST),
						Toast.LENGTH_SHORT).show();

				mpType = MultiplayerType.NULL;

				//check if any game was going on, if so, close it!
				if(game != null)
					game.close();
				break;

			}
		}
	};

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}


}
