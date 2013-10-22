package com.notpongwars.core;

import android.content.res.Resources;

public abstract class Component {

	protected boolean active = true;
	public boolean isActive() { return this.active; }
	public void setActive(boolean active) { this.active = active; }
	
	private String name = "Component";
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	
	public void load(Resources res) { }
	
	public void update() { }
	
}
