package com.notpongwars.game;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.notpongwars.core.InputManager;

public class Player extends Paddle {

	private Rect upperButton;
	private Rect bottomButton;

	private boolean enable = true;
	public boolean isEnable() {return enable;}
	public void setEnable(boolean enable) {	this.enable = enable;}

	private float accelDeadZone = 0f;
	private float accelOffset = 0f;
	
	private GameSettings settings;
	

	public Player(PaddleSide side, Point screenSize, GameSettings settings) {
		super(side, screenSize);

		this.settings = settings;
		
		upperButton = new Rect(0, 0, getScreenSize().x, getScreenSize().y / 2);
		bottomButton = new Rect(0, getScreenSize().y / 2, getScreenSize().x, getScreenSize().y);
	}

	@Override
	public void update() {
		handleInput();

		super.update();
	}


	protected void handleInput(){
		
		if(enable){
			if(settings.getInputTypeIsAccelerometer() && InputManager.isAccelerometerSupported()) {
				float yVelocity = InputManager.getAccelerometerX();
				move((yVelocity > -accelDeadZone && yVelocity < accelDeadZone) ? 0 : yVelocity - accelOffset); // accelOffset = offset para se poder inclinar um pouco o ecra (para n se jogar com o device mxm paralelo ao eixo
			} else {
				if(InputManager.isTouchDown(upperButton)) {
					move(MovementDirection.UP);
				} else if(InputManager.isTouchDown(bottomButton)) {
					move(MovementDirection.DOWN);
				} else move(MovementDirection.NONE);
			}
		}
		
		if(InputManager.isTouchDown()) {
			Log.d("PLAYER", "Fired Bullet");
			fireBullet();
		}
	}

}
