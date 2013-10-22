package com.notpongwars.core;

import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;
import android.view.MotionEvent;

public final class InputManager {
	
	private static MotionEvent currentMotionEvent;
	private static SensorEvent currentAccelerometerEvent;
	
	private static boolean isAccelerometerSupported = false;
	public static boolean isAccelerometerSupported() { return isAccelerometerSupported; }
	protected static void setIsAccelerometerSupported(boolean supported) { isAccelerometerSupported = supported; Log.d("INPUTMANAGER", "Accelerometer Supported: " + isAccelerometerSupported); }
	
	
	public static final MotionEvent getMotionEvent() {
		return currentMotionEvent;
	}
	
	protected static final void updateTouchPanelState(final MotionEvent event) {
		currentMotionEvent = event;
	}
	
	protected static final void updateAccelerometerState(final SensorEvent event) {
		if(isAccelerometerSupported)
			currentAccelerometerEvent = event.sensor.getType() == Sensor.TYPE_ACCELEROMETER ? event : null;
	}
	
	public static final boolean isTouchDown() {
		return currentMotionEvent == null ? false : (currentMotionEvent.getAction() == MotionEvent.ACTION_DOWN || currentMotionEvent.getAction() == MotionEvent.ACTION_MOVE);
	}
	
	public static final boolean isTouchDown(Rect region) {
		if(currentMotionEvent == null) return false;
		
		if(currentMotionEvent.getAction() == MotionEvent.ACTION_DOWN || currentMotionEvent.getAction() == MotionEvent.ACTION_MOVE) {
			return region.contains((int)currentMotionEvent.getX(), (int)currentMotionEvent.getY());
		} else return false;
	}
	
	public static final boolean isTouchUp() {
		return currentMotionEvent == null ? false : currentMotionEvent.getAction() == MotionEvent.ACTION_UP;
	}
	
	public static final float getAccelerometerX() {
		return currentAccelerometerEvent != null ? currentAccelerometerEvent.values[0] : 0f;
	}
	
	public static final float getAccelerometerY() {
		return currentAccelerometerEvent != null ? currentAccelerometerEvent.values[1] : 0f;
	}
	
	public static final float getAccelerometerZ() {
		return currentAccelerometerEvent != null ? currentAccelerometerEvent.values[2] : 0f;
	}
	
}
