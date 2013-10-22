package com.notpongwars.core;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

public class Sprite extends DrawableComponent {

	private int textureId;
	private Bitmap texture;
	
	
	public Sprite(int textureId, PointF position, Point size) {
		super(position, size);
		this.textureId = textureId;
	}
	
	@Override
	public void load(Resources resources) {
		this.texture = BitmapFactory.decodeResource(resources, textureId);
	}
	
	public void draw(Canvas canvas) {
		Rect dst = new Rect((int)position.x, (int)position.y, (int)position.x + size.x, (int)position.y + size.y);
		
		canvas.drawBitmap(texture, null, dst, null);
	}

}
