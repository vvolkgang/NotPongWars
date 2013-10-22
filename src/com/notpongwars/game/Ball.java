package com.notpongwars.game;

import java.util.Random;

//import com.notpongwars.R;
import com.notslip.notpongwars.R;
import com.notpongwars.core.AudioPlayer;
import com.notpongwars.core.MathHelper;
import com.notpongwars.game.Paddle.PaddleSide;

import android.graphics.Point;
import android.graphics.PointF;

public class Ball extends PongSprite {	
	
	private Paddle leftPaddle, rightPaddle;
	private int soundWithPaddle, soundWithWall;
	
	private float initialSpeed;
	public float getInitialSpeed() { return this.initialSpeed; }
	public void setInitialSpeed(float initialSpeed) { this.initialSpeed = initialSpeed; }
	
	private float maxSpeed;
	public float getMaxSpeed() { return this.maxSpeed; }
	public void setMaxSpeed(float maxSpeed) { this.maxSpeed = maxSpeed; }
	
	private float speedMultiplier;
	
	
	public Ball(Point screenSize, GameSettings settings) {
		super(new PointF(), new Point(10, 10), screenSize);
		
		soundWithPaddle = AudioPlayer.createSound(R.raw.pongblip1);
		soundWithWall = AudioPlayer.createSound(R.raw.pongblip2);
		
		initialSpeed = speed = settings.getBallInitialSpeed();
		maxSpeed = settings.getBallMaxSpeed();
		speedMultiplier = settings.getBallSpeedMultiplier();
		
		reset(new PointF(1f,1f));
	}
	
	public void addPaddle(Paddle paddle) {
		if(paddle.getPaddleSide() == PaddleSide.LEFT) this.leftPaddle = paddle;
		else rightPaddle = paddle;
	}
	
	@Override
	public void update() {
		super.update();
		
		handleCollisions();
	}
	
	public void handleCollisions() {
		handleCollisionWithWalls();
		handleCollisionWithPaddle(leftPaddle);
		handleCollisionWithPaddle(rightPaddle);
	}
	
	private void handleCollisionWithWalls() {
		boolean collided = false;
		
		if(position.x < 0 || position.x + size.x > getScreenSize().x) {
			collided = true;
			velocity.x *= -1;
		}
		if(position.y < 0 || position.y + size.y > getScreenSize().y) {
			collided = true;
			velocity.y *= -1;
		}
		
		if(position.x < 0) {
			rightPaddle.setScore(rightPaddle.getScore() + 1);
			reset(new PointF(1f,generateRandomAngle()));
		}
		if(position.x + size.x > getScreenSize().x) {
			leftPaddle.setScore(leftPaddle.getScore() + 1);
			reset(new PointF(-1,generateRandomAngle()));
		}
		
		//If collided with horizontal walls, add VectorOne if direction.x is positive
		//or add -vectorOne.x ifdirection is negative.
		
		if(collided) {
			AudioPlayer.playSound(soundWithWall);
		}
	}
	
	private void handleCollisionWithPaddle(Paddle paddle) {
		if(paddle == null) return;

		if(super.boundingBox.intersect(paddle.getBoudingBox())) {
			if(paddle.getPaddleSide() == PaddleSide.LEFT) {
				position.x = paddle.getPosition().x + paddle.getSize().x;
			} else
				position.x = paddle.getPosition().x - size.x;

			super.velocity.x *= -1;
			
			MathHelper.AddToPoint1ByRef(velocity, paddle.getVelocity());
			MathHelper.NormalizeByRef(velocity);
			
			AudioPlayer.playSound(soundWithPaddle);
		
			incrementSpeed(speedMultiplier);
		}
	}
	
	public void reset(PointF newVelovity) {
		MathHelper.NormalizeByRef(newVelovity);
		this.setVelocity(newVelovity);
		this.setPosition(new PointF(getScreenSize().x / 2 - getSize().x / 2, getScreenSize().y / 2 - getSize().y / 2));
		this.speed = initialSpeed;
		
		
		if(leftPaddle != null) { leftPaddle.reset(); leftPaddle.setDamaged(false); }
		if(rightPaddle != null) { rightPaddle.reset(); rightPaddle.setDamaged(false); }
	}
	
	private float generateRandomAngle() {
		Random rnd = new Random();
		float n = clamp(rnd.nextFloat(), 0.3f, 1.0f);
		boolean inv = rnd.nextBoolean();
		
		return inv ? -n : n;
	}

	private float clamp (float value, float low, float high) {
		return java.lang.Math.max (java.lang.Math.min (value, high), low);
	}
	
	private void incrementSpeed(float multiplier) {
		this.speed = clamp(this.speed * multiplier, initialSpeed, maxSpeed);
	}
	
	
}
