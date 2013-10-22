package com.notpongwars.activities;

import com.notslip.notpongwars.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnCreateContextMenuListener;

abstract class SplashActivity extends Activity {


	protected boolean active = true;
	protected int splashTime = 5000;
	
	protected int layout;
	protected Class nextClass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(layout);

		final Context con = this;
		
		Thread splashTread = new Thread() {
	        @Override
	        public void run() {
	            try {
	                int waited = 0;
	                while(active && (waited < splashTime)) {
	                    sleep(100);
	                    if(active) {
	                        waited += 100;
	                    }
	                }
	            } catch(InterruptedException e) {
	                // do nothing
	            } finally {
	                finish();
	                startActivity(new Intent(con, nextClass));
	                //close();
	            }
	        }
	    };
	    splashTread.start();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			active = false;
		}
		return true;
	}
}
