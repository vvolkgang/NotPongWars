package com.notpongwars.activities;

import com.notslip.notpongwars.R;

public class IpcaSplashActivity extends SplashActivity {
	
	public IpcaSplashActivity(){
		splashTime = 3000;
		
		layout = R.layout.ipca_splash;
		nextClass = LogoSplashActivity.class;
	}
}
