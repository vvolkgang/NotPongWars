package com.notpongwars.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import com.notpongwars.core.DrawableComponent;
import com.notpongwars.core.MathHelper;

public abstract class PongSprite extends DrawableComponent {

	private Point screenSize;
	protected Point getScreenSize() { return this.screenSize; }

	protected float speed = 100;
	public float getSpeed() { return this.speed; }
	public void setSpeed(float speed) { this.speed = speed; }

	protected PointF velocity;
	public void setVelocity(PointF velocity) { this.velocity = velocity; }
	public PointF getNormalizedVelocity(){ return MathHelper.Normalize(velocity);}
	public PointF getVelocity(){return velocity;}

	public PongSprite(PointF position, Point size, Point screenSize) {
		super(position, size);

		this.screenSize = screenSize;

		this.velocity = new PointF();
	}

	@Override
	public void update() {
		this.position.x += this.velocity.x * speed;
		this.position.y += this.velocity.y * speed;

		super.update();
	}

	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setARGB(255, 255, 255, 255);

		canvas.drawRect(position.x, position.y, position.x + size.x, position.y + size.y, paint);
	}

}
