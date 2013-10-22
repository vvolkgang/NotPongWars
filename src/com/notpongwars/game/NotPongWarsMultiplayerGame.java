package com.notpongwars.game;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.util.Log;

import com.notslip.notpongwars.R;
import com.notpongwars.activities.MultiplayerActivity;
import com.notpongwars.core.GamePanel;
import com.notpongwars.core.InputManager;
import com.notpongwars.core.MathHelper;
import com.notpongwars.core.Sprite;
import com.notpongwars.core.bluetooth.BluetoothManager;
import com.notpongwars.game.Paddle.PaddleSide;


public class NotPongWarsMultiplayerGame extends NotPongWarsSinglePlayer{


	private boolean isServer;
	private BluetoothManager btManager;
	NumberFormat formatter;

	public NotPongWarsMultiplayerGame(Context context, GameSettings settings, BluetoothManager manager){
		super(context, settings);

		btManager = manager;
	}

	@Override
	public void onInitialize() {
		Resources res = getResources();
		formatter = new DecimalFormat(".00");

		screenSize = new Point(getWidth(), getHeight());
		this.isServer = settings.getMultiplayerTypeIsServer();
		this.endScore = settings.getEndScore();

		if(isServer){
			initializeBall(res);
			initializeLeftPlayer(res);
			initializeRightDummyPlayer(res);
			
			ball.addPaddle(leftPaddle);
			ball.addPaddle(rightPaddle);
			ball.setPosition(MathHelper.PointOne);
		}
		else{
			initializeDummyBall(res);
			initializeLeftDummyPlayer(res);
			initializeRightPlayer(res);
		}



		collisionSystem = new CollisionSystem(leftPaddle, rightPaddle);
		
	}

	@Override
	public void onUpdate() {
		
		if(!isGameOver) {
			if(isServer){
				checkGameOver();

				leftPaddle.update();
				ball.update();
				
				
				//send info
				btManager.sendMessage(positionToString(MultiplayerActivity.PLAYER_LEFT_POSITION, leftPaddle.getPosition()));
				btManager.sendMessage(positionToString(MultiplayerActivity.BALL_POSITION, ball.getPosition()));

				btManager.sendMessage(MultiplayerActivity.PLAYER_LEFT_SCORE + ";"+leftPaddle.getScore());
				btManager.sendMessage(MultiplayerActivity.PLAYER_RIGHT_SCORE + ";"+rightPaddle.getScore());
			}
			else{
				rightPaddle.update();
				btManager.sendMessage(positionToString(MultiplayerActivity.PLAYER_RIGHT_POSITION, rightPaddle.getPosition()));
			}
			
			
			collisionSystem.update();
		}
		else {
			btManager.sendMessage(MultiplayerActivity.END_GAME_STRING);
			if(InputManager.isTouchDown()) close();
		}
	}

	private String positionToString(byte object, PointF point){
		String message = object + MultiplayerActivity.SPLITTER +
				(int)point.x + MultiplayerActivity.SPLITTER +
				(int)point.y;

		return message; 
	}

	public void endGame(){
		isGameOver = true;
	}

	//@Override
	protected void checkGameOver() {
		super.checkGameOver();
		if(isGameOver)
			btManager.sendMessage(MultiplayerActivity.END_GAME_STRING);
	}

	//players setters
	public void setPlayerLeftScore(int score){ leftPaddle.setScore(score);}
	public void setPlayerRightScore(int score){ rightPaddle.setScore(score);}

	public void setPlayerLeftPosition(PointF position){leftPaddle.setPosition(position);}
	public void setPlayerRightPosition(PointF position){rightPaddle.setPosition(position);}

	public void resetPlayerLeft(){leftPaddle.reset();}
	public void resetPlayerRight(){rightPaddle.reset();}

	//ball setters
	public void setBallPosition(PointF position){ ball.setPosition(position); }


}
