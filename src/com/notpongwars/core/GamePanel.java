package com.notpongwars.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class GamePanel extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {

	private SensorManager sensorManager;
	private Sensor accelerometer;

	public static final String TAG = "[CORE]" + GamePanel.class.getSimpleName();

	private MainThread thread;

	private String avgFPS;
	public void setAvgFPS(String avgFPS) {
		this.avgFPS = avgFPS;
	}


	public GamePanel(final Context context) {
		super(context);

		getHolder().addCallback(this);

		thread = new MainThread(this.getHolder(), this);

		setFocusable(true);
	}

	public final void surfaceCreated(SurfaceHolder arg0) {
		onInitialize();
		Log.d(TAG, "Main Game Panel initialized");
		Log.d(TAG, "ScreenSize=(X=" + getWidth() + ",Y=" + getHeight() + ")");

		sensorManager = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		InputManager.setIsAccelerometerSupported(accelerometer != null);
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
		
		thread.setRunning(true);
		thread.start();
	}

	public final void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		Log.d(TAG, "Surface Changed");

		//sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	public final void surfaceDestroyed(SurfaceHolder arg0) {
		thread.setRunning(false);
		
		sensorManager.unregisterListener(this);

		boolean retry = true;
		while(retry) {
			try {
				thread.join();
				retry = false;
			} catch(InterruptedException ex) {
				// try again shutting down the thread...
			}
		}
	}

	/**
	 * This is the game update method. It iterates through all the objects
	 * and calls their update method if they have one or calls specific
	 * engine's update method.
	 */
	public final void update() {
		onUpdate();
	}

	public final void render(final Canvas canvas) {
		// Clear screen
		canvas.drawColor(Color.BLACK);

		onDraw(canvas);

		// Display FPS
		displayFPS(canvas, avgFPS);
	}


	private void displayFPS(final Canvas canvas, String fps) {
		if(canvas != null && fps != null) {
			Paint paint = new Paint();
			paint.setARGB(255, 255, 255, 255);
			canvas.drawText(fps, this.getWidth() - 50, 20, paint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputManager.updateTouchPanelState(event);

		return super.onTouchEvent(event);
	}


	public void onSensorChanged(SensorEvent arg0) {
		InputManager.updateAccelerometerState(arg0);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) { }


	public void close() {
		thread.setRunning(false);

		Activity act = (Activity)getContext();
		act.finish();
	}


	public abstract void onInitialize();

	public abstract void onUpdate();

	public abstract void onDraw(Canvas canvas);

}
