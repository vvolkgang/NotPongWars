package com.notpongwars.game;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;

public class Paddle extends PongSprite {

	public enum PaddleSide {
		LEFT, RIGHT
	}

	public enum MovementDirection {
		NONE, UP, DOWN
	}

	private float originalSpeed;

	private PointF _up =  new PointF(0f, -1f);
	private PointF _down =  new PointF(0f, 1f);
	private PointF _none =  new PointF(0f, 0f);

	private long lastDamagedTime;
	private long damageDuration = 3000;

	private float damagedSpeed;

	private Bullet bullet;
	public Bullet getBullet() { return this.bullet; }

	private PaddleSide side;
	public PaddleSide getPaddleSide() { return this.side; }

	private int score = 0;
	public int getScore() { return this.score; }
	public void setScore(int score) { this.score = score; }

	private boolean damaged = false;
	public boolean isDamaged() { return this.damaged; }
	public void setDamaged(boolean damaged) {
		this.damaged = damaged;
		if(damaged) {
			this.lastDamagedTime = System.currentTimeMillis();
			this.speed = damagedSpeed;
		} else this.speed = originalSpeed;
	}

	private float damagePenalty = 0.1f;
	public float getDamagePenalty() { return this.damagePenalty; }
	public void setDamagePenalty(float damage) { this.damagePenalty = damage / 100; }

	@Override
	public void setSpeed(float speed) {
		// update original speed
		this.originalSpeed = speed;
		this.damagedSpeed = speed * damagePenalty;
		super.setSpeed(speed);
	}


	public Paddle(PaddleSide side, Point screenSize) {
		super(null, new Point(10, 100), screenSize);

		setSpeed(15f);

		this.side = side;
		if(side == PaddleSide.LEFT) {
			this.position = new PointF(50, (screenSize.y / 2) - (size.y / 2));
		} else {
			this.position = new PointF(screenSize.x - 50 - size.x, (screenSize.y / 2) - (size.y));
		}

		bullet = new Bullet(this, screenSize);
	}

	@Override
	public void update() {
		if(damaged)
			if(System.currentTimeMillis() - lastDamagedTime > damageDuration)
				setDamaged(false);

		super.update();

		// clamp paddle position
		if(getPosition().y < 0) {
			position.y = 0;
		} else if(getPosition().y > (getScreenSize().y - size.y)) {
			position.y = getScreenSize().y - size.y;
		}

		bullet.update();
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		bullet.draw(canvas);
	}

	public void move(MovementDirection dir) {
		if(dir == MovementDirection.UP) {
			super.velocity = _up;
		} else if(dir == MovementDirection.DOWN) {
			super.velocity = _down;
		} else super.velocity = _none;
	}

	public void move(float yVelocity) {
		super.velocity.y = yVelocity / 4; // the value goes from 0 to ~10. the max speed is available at 40% of total inclination
	}

	public void reset() {
		this.position.y = getScreenSize().y / 2 - this.size.y / 2;
	}

	public void fireBullet() {
		bullet.fire();
	}

}
