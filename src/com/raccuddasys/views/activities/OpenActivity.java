package com.raccuddasys.views.activities;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

public class OpenActivity extends TabActivity implements View.OnClickListener{
	
	TabHost mTabHost;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.open_layout);
		mTabHost = getTabHost();
	    
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator(getString(R.string.open_dialog_local)).setContent(R.id.textview1));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator(getString(R.string.open_dialog_internet)).setContent(R.id.textview2));
	    	    
	    mTabHost.setCurrentTab(0);

		
	}
	public void onDestroy(){
		super.onDestroy();
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
