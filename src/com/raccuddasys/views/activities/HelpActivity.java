package com.raccuddasys.views.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

public class HelpActivity extends Activity implements View.OnClickListener, View.OnKeyListener, 
														View.OnTouchListener, View.OnFocusChangeListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//check if has camera and required sensors
    	//if not disable the CameraActivity
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.help_layout);        
        
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, 
                android.R.drawable.arrow_down_float);
                
       
        
       
    }
    public void showCameraHelpPage(){
    	new AlertDialog.Builder(HelpActivity.this)
    		.setTitle(R.string.camera_help_title)
    		.setMessage(R.string.camera_help_text)
    		.setNeutralButton(R.string.alert_dialog_close, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    
                    /* User clicked OK so do some stuff */
                }
            }).show();
    	
    }
    public void showUploadHelpPage(){
    	new AlertDialog.Builder(HelpActivity.this)
		.setTitle(R.string.upload_help_title)
		.setMessage(R.string.upload_help_text)
		.setNeutralButton(R.string.alert_dialog_close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                
                /* User clicked OK so do some stuff */
            }
        }).show();
    	
    }
    public void showSettingsHelpPage(){
    	new AlertDialog.Builder(HelpActivity.this)
		.setTitle(R.string.settings_help_title)
		.setMessage(R.string.settings_help_text)
		.setNeutralButton(R.string.alert_dialog_close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                
                /* User clicked OK so do some stuff */
            }
        }).show();
    	
    }
    public void showViewsHelpPage(){
    	new AlertDialog.Builder(HelpActivity.this)
		.setTitle(R.string.views_help_title)
		.setMessage(R.string.views_help_text)
		.setNeutralButton(R.string.alert_dialog_close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                
                /* User clicked OK so do some stuff */
            }
        }).show();
    	
    }
    public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.help_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		int id = item.getItemId();
		switch(id){
		case R.id.help_camera:
			showCameraHelpPage();
			break;
		case R.id.help_upload:
			showUploadHelpPage();
			break;
		case R.id.help_settings:
			showSettingsHelpPage();
			break;
		case R.id.help_view:
			showViewsHelpPage();
			break;
		case R.id.help_back:
			finish();
			break;
		
		}
		return false;
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}