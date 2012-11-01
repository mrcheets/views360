package com.raccuddasys.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends Activity implements View.OnClickListener, View.OnKeyListener, 
															View.OnTouchListener, View.OnFocusChangeListener{
	public void onCreate(Bundle savedInstanceState){
		//check if has camera and required sensors
    	//if not disable the CameraActivity
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		Button camera_but = (Button) findViewById(R.id.camera_button);
		camera_but.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent i = new Intent();
		switch(id){
		case R.id.camera_button:
			i.setClassName("com.raccuddasys.views.activities", "com.raccuddasys.views.activities.CameraActivity");
    		startActivity(i);
    		break;
		
		}
		finish();
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		
	}
}
