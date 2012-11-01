package com.raccuddasys.views.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashActivity extends Activity implements View.OnClickListener{
	public static final String PREFS_NAME = "MyPrefsFile";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    boolean nosplash = settings.getBoolean("splash", false);
	    setContentView(R.layout.splash_layout);
	    

		final Button button = (Button) findViewById(R.id.close_splash);
        button.setOnClickListener(this);


		
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.close_splash){
			finish();
		}
		
	}

}
