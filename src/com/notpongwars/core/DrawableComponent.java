package com.notpongwars.core;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

public abstract class DrawableComponent extends Component {
	
	protected boolean visible = true;
	public boolean getVisible() { return this.visible; }
	public void setVisible(boolean visible) { this.visible = visible; }
	
	protected PointF position;
	public PointF getPosition() { return this.position; }
	public void setPosition(PointF position) { this.position = position; updateBoudingBox(); }
	
	protected Point size;
	public Point getSize() { return this.size; }
	public void setSize(Point size) { this.size = size; updateBoudingBox(); }
	
	protected RectF boundingBox;
	public RectF getBoudingBox() { return this.boundingBox; }
	
	
	public DrawableComponent(PointF position, Point size) {
		this.position = position == null ? new PointF() : position;
		this.size     = size     == null ? new Point()  : size;
		
		this.updateBoudingBox();
	}
	
	@Override
	public void update() {
		updateBoudingBox();
	}
	
	public abstract void draw(Canvas canvas);
	
	
	private void updateBoudingBox() {
		this.boundingBox = new RectF(this.position.x, this.position.y, this.position.x + this.size.x, this.position.y + size.y);
	}
}
