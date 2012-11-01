package com.raccuddasys.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class ViewsMapActivity extends MapActivity implements View.OnClickListener, View.OnKeyListener, 
														View.OnTouchListener, View.OnFocusChangeListener{
	MapView mapView;
	
	//Use intents as much as possible
	public void onCreate(Bundle savedInstanceState){
		//check if camera has and required sensors
    	//if not disable the CameraActivity
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);        	
		
	}
	public ViewsMapActivity(){
		//Comes only with continental views
		//show current camera position
		//show views on map if user clicks on a view pick and download
	}
	public ViewsMapActivity(long latitude, long longitude){
		//show given map position (used especially where views were taken
		//If view, place view icon
	}
	public void pickView(){
		
	}
	@Override
	public void onClick(View v) {
		
		
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
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	

}
