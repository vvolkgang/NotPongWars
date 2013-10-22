package com.notpongwars.game;

import android.graphics.Point;

public class DummyPlayer extends Player
{

	public DummyPlayer(PaddleSide side, Point screenSize) {
		super(side, screenSize, null);
	
	}
	
	@Override
	public void update() {
		
	}
	
	@Override
	protected void handleInput() {
		
	}
	
}
