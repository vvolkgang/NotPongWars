package com.notpongwars.game;

import android.graphics.Point;

public class Enemy extends Player {

	private float awareness = 0f;
	
	private Ball ball;
	public void setBall(Ball ball) { this.ball = ball; }
	
	
	/**
	 * 
	 * @param side
	 * @param screenSize
	 * @param ball
	 * @param speed
	 * @param awareness
	 */
	public Enemy(PaddleSide side, Point screenSize, Ball ball, float speed, float awareness) {
		super(side, screenSize, null);
		
		this.ball = ball;
		this.speed = speed;
		this.awareness = awareness;
	}
	
	
	@Override
	protected void handleInput() {
		if(ball.getPosition().x > awareness) {

			if(ball.getPosition().y < position.y) {
				velocity.y = -1f;
			} else if(ball.getPosition().y > position.y) {
				velocity.y = 1f;
			} else velocity.y = 0f;
			
			float newY = position.y + velocity.y * speed;
	
			float tempNewY = ball.getPosition().y;
			
			float d1 = Math.abs(newY - position.y);
			float d2 = Math.abs(tempNewY - position.y);
	
			if(d1 < d2) {
				position.y = newY;
			} else { position.y = tempNewY; }
			
		} 
		
		velocity.y = 0f;
		
	}
}
