package com.notpongwars.game;

import com.notpongwars.core.AudioPlayer;
import com.notpongwars.game.Paddle.PaddleSide;
import com.notslip.notpongwars.R;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

public class Bullet extends PongSprite {

	private long lastFireTime;

	private Paddle owner;
	public Paddle getOwner() { return this.owner; }

	private long fireDelay = 3000;
	public long getFireDelay() { return this.fireDelay; }
	public void setFireDelay(long fireDelay) { this.fireDelay = fireDelay; }

	private int fireSound;

	public Bullet(Paddle owner, Point screenSize) {
		super(new PointF(), new Point(10, 5), screenSize);

		active = false;

		speed = 30f;

		this.owner = owner;

		if(owner != null) {
			if(owner.getPaddleSide() == PaddleSide.LEFT)
				setVelocity(new PointF(1f, 0));
			else
				setVelocity(new PointF(-1f, 0));
		}

		fireSound =  AudioPlayer.createSound(R.raw.laser1);
		
		lastFireTime = System.currentTimeMillis() - fireDelay;
	}

	@Override
	public void update() {
		if(active) super.update();
	}

	@Override
	public void draw(Canvas canvas) {
		if(active) super.draw(canvas);
	}

	public void fire() {
		if(System.currentTimeMillis() - lastFireTime > fireDelay) {
			if(owner != null){
				lastFireTime = System.currentTimeMillis();
				setPosition(new PointF(owner.getPosition().x, owner.getPosition().y + owner.getSize().y / 2));
				active = true;
				AudioPlayer.playSound(fireSound);
			}
		}
	}

	public void onCollideWith(Paddle other) {
		active = false;
		other.setDamaged(true);
	}

}
