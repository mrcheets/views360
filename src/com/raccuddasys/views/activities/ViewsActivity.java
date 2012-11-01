package com.raccuddasys.views.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.raccuddasys.views.utilities.ViewsDBAdapter;

public class ViewsActivity extends Activity implements LocationListener, View.OnClickListener, View.OnKeyListener, View.OnTouchListener{
	//View view
	public static Activity me = null;
	public static final int CAMERA_OK = 5;
	public static final int CAMERA_CANCELED = 10;
	public static final int OPEN_OK = 20;
	public static final int NO_LATITUDE = -1;
	public static final int NO_LONGITUDE = -1;
	static int saveProgress = 0;
	LocationManager lm;
	ViewsDBAdapter vdb;
	Cursor c;
	double tempLatitude;
	double tempLongitude;
	int latitude;
	int longitude;
	
	public static final String PREFS_NAME = "MyPrefsFile";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//check if has camera and required sensors
    	//if not disable the CameraActivity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.views_layout);
        Button camera_but = (Button) findViewById(R.id.camera_button);
        camera_but.setOnClickListener(this);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    boolean splash = settings.getBoolean("splash", true);
	    boolean firstOpening = settings.getBoolean("firstOpening", true);
	    lm =(LocationManager) getSystemService( LOCATION_SERVICE );
	    if ( !lm.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
	        buildAlertMessageNoGps();
	    }
	    else{
	    	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	    }
	    latitude = NO_LATITUDE;;
	    longitude = NO_LONGITUDE;
	    
	    vdb = new ViewsDBAdapter(this);
	    vdb.open();
	    c = vdb.fetchAllViews();
	    startManagingCursor(c);
        
    }
    
    public void onResume(){
    	super.onResume();
    	if(vdb == null){
    		vdb = new ViewsDBAdapter(this);
    		c = vdb.fetchAllViews();
    	}
    	//System.gc();
    }
    public void getViewTeasers(String viewLocation){
    	//Do this in a thread
    	if(viewLocation.equals("local")){
    		//Show local view teasers
    	}
    	else{
    		//show remote view teasers
    	}
    }
    
    public void onPause(){
    	super.onPause();
    	vdb.close();
    }
    
    public void getSelectedView(){
    	//Do this in a thread
    	//Show download progress bar
    	//
    }
    public void showWebView(){
    	//Show the view set in the view variable
    }
	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent i = new Intent();
		switch(id){
		case R.id.camera_button:
			i.setClassName("com.raccuddasys.views.activities", "com.raccuddasys.views.activities.CameraActivity");
    		startActivityForResult(i, CAMERA_OK|CAMERA_CANCELED);
    		break;
		
		}
		
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
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		Intent i = new Intent();
		switch (item.getItemId()) {
	    case R.id.quit_view:
	        finish();
	        return true;
	    case R.id.open_view:
	    	i.setClassName("com.raccuddasys.views.activities", "com.raccuddasys.views.activities.OpenActivity");
    		startActivityForResult(i, OPEN_OK);
        	return true;
	    case R.id.upload_view:
	    	i.setClassName("com.raccuddasys.views.activities", "com.raccuddasys.views.activities.UploadActivity");
    		startActivity(i);
        	return true;
	    case R.id.locate_view:
	    	i.setClassName("com.raccuddasys.views.activities", "com.raccuddasys.views.activities.ViewsMapActivity");
    		startActivity(i);
        	return true;
	    case R.id.help_view:
	    	i.setClassName("com.raccuddasys.views.activities", "com.raccuddasys.views.activities.HelpActivity");
    		startActivity(i);
        	return true;
	    /*case R.id.settings_view:
	    	i.setClassName("com.raccuddasys.views.activities", "com.raccuddasys.views.activities.SettingsActivity");
    		startActivityForResult(i, CAMERA_OK|CAMERA_CANCELED);
        	return true;*/
	    }
	
		return false;
		
	}
	public void onDestroy(){
		super.onDestroy();
		SharedPreferences newsettings = getSharedPreferences(PREFS_NAME, 0);

    	SharedPreferences.Editor editor = newsettings.edit();
        
        editor.putBoolean("firstOpening", true);
        editor.commit();
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode){
		case CAMERA_OK:
			//do the necessary
			//combine the images
			//Save images in mediastore
			//getgeodata
			//Put geodata in dbase
			//delete the raw images
			Bundle extras = data.getExtras();
			int imgWidth = extras.getInt("imgWidth");
			int imgHeight = extras.getInt("imgHeight");
			int imageCount = extras.getInt("max_photos");
			Log.i("ViewsActivity.onActivityResult", "image width "+ imgWidth);
			Log.i("ViewsActivity.onActivityResult", "Images received");
			//progress bar to show convert progress of images to rgb
			Toast.makeText(this, "Converting images from raw to rgb", Toast.LENGTH_LONG).show();
			
			
		    if(!lm.isProviderEnabled( LocationManager.GPS_PROVIDER )){
		    	 //Dont change the latitude and longitude values
		    	Log.i("ViewsActivity", "Provider not enabled");
		    }
		    else{
		    	latitude = (int)tempLatitude*1000000;
		    	longitude = (int)tempLongitude*1000000;
		    	Log.i("ViewsActivity.Longitudes", "Longitude: "+longitude+" Latitude "+latitude);
		    }
			//Process Images and combine them
		    break;
		case CAMERA_CANCELED:
			Toast.makeText(this, "sensor_error", Toast.LENGTH_LONG).show();
			break;
		case OPEN_OK:
			//display the image
			break;
		default:
			Toast.makeText(this, "Back from the activity", Toast.LENGTH_LONG).show();
		}
		for(int i = 0; i < CameraActivity.MAX_PHOTOS; i++){
			//this.deleteFile("tempImage"+i);
		}
		
		
	}
	private void buildAlertMessageNoGps() {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Yout GPS seems to be disabled, do you want to enable it?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                   //launchGPSOptions(); 
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                    dialog.cancel();
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	}
	public native int processimage(ByteBuffer buffer, IntBuffer rgbBuffer, int buffLength);
	static {
        System.loadLibrary("processimage");
    }
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		tempLatitude = location.getLatitude();
		tempLongitude = location.getLongitude();
		Log.i("ViewsActivity", "Location lat:"+tempLatitude+" long:"+tempLongitude);
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
}

class SavePNGData implements Runnable{
	Context context;
	int buffLength;
	int width, height;
	private ProgressDialog mProgressDialog;
	private Handler mProgressHandler;
	static int mProgress;
	int imgCount;
	private boolean saveImages = true;
	
	public SavePNGData(Context context, int width, int height, int imgCount){
		Log.i("SavePNGData", "initialization");
		this.context = (ViewsActivity)context;
		this.width = width;
		this.height = height;
		this.imgCount = imgCount;
		mProgressDialog = new ProgressDialog(ViewsActivity.me);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setMax(this.imgCount);
		mProgressDialog.setTitle(ViewsActivity.me.getText(R.string.select_dialog));
		mProgressDialog.setMessage(ViewsActivity.me.getText(R.string.message_dialog));
		mProgressDialog.setButton(ViewsActivity.me.getText(R.string.alert_dialog_cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
	            saveImages = false;
	            deleteImages();
			}
		});
		mProgressDialog.show();
		
		
			/*ProgressDialog.show(ViewsActivity.me, ViewsActivity.me.getText(R.string.select_dialog),ViewsActivity.me.getText(R.string.message_dialog), true);
		//mProgressDialog.setIcon(R.drawable.alert_dialog_icon);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		//mProgressDialog.setMax(this.imgCount);
		mProgressDialog.setButton2(ViewsActivity.me.getText(R.string.alert_dialog_cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

            /* User clicked No so do some stuff */
			//}
		//}); 
		mProgressHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (mProgress >= CameraActivity.MAX_PHOTOS-1) {
                    mProgressDialog.dismiss();
                } else {
                    //mProgress = ViewsActivity.saveProgress;
                    mProgressDialog.incrementProgressBy(1);
                    //mProgressHandler.sendEmptyMessageDelayed(0, 100);
                }
            }
        };
		Thread tt = new Thread(this);
		tt.start();
	}
	private void deleteImages(){
		String[] files = ViewsActivity.me.fileList();
		int count = files.length;
		for(int i = 0; i < count; i++){
			ViewsActivity.me.deleteFile(files[i]);
		}		
	}
	@Override
	public void run() {
		for(int i = 0; i < this.imgCount; i++){
			if(saveImages){
				mProgress = i;
				try{ 
					Log.i("SavePNGData",">>>>>>Crunching Images<<<<<<<<<<");
					Log.i("File path",  context.getFileStreamPath("tempImage"+i).getPath());
				
					File fi = new File(context.getFileStreamPath("tempImage"+i).getPath());
					buffLength = (int)fi.length();
					//ByteBuffer myBuffer = ByteBuffer.allocateDirect(buffLength); 
					//ByteBuffer tempBuffer = ByteBuffer.allocateDirect(buffLength*2);
					//IntBuffer mainBuffer = tempBuffer.asIntBuffer();
				
					byte[] buffer = new byte[buffLength];
					//int[] buffer2 = new int[buffLength];
					//FileInputStream fis = context.openFileInput("tempImage"+i);
					//fis.read(buffer);
					//Bitmap bmp = BitmapFactory.decodeByteArray(buffer, 0, buffLength);
					Bitmap bmp = BitmapFactory.decodeFile("tempImage"+i);
					Log.i("SavePNG Data",  "Filling buffer");
					//for(int j = 0; j < buffLength; j++){
					//	myBuffer.put(j, (byte)buffer[j]);
					//}
					//Log.i("SavePNG Data",  "Buffer filled. Proceeding...");
					//Log.i("SavePNGData()", "myBuffer Size: "+myBuffer.capacity()+ " buffLength: "+buffLength);
					//Log.i("SavePNGdata", "hm"+((ViewsActivity)context).processimage(myBuffer, mainBuffer, buffLength));
					//update progressbar
					//save file
					context.deleteFile("tempImage"+i);
					//Log.i("File path",  context.getFileStreamPath("tempImage"+i).getPath());
					//Log.i("savePNGData", "Capacity "+mainBuffer.capacity());
					//Log.i("SavePNGData", "Size"+mainBuffer.array().length);
					//IntBuffer rgbis = IntBuffer.allocate(mainBuffer.capacity());
					//rgbis.put(mainBuffer);
					/*String storage_state = Environment.getExternalStorageState();
				 	if (storage_state.contains("mounted")) {
					 	Toast.makeText(context, "Sdcard mounted", Toast.LENGTH_SHORT);
				 	}
					File f = Environment.getExternalStorageDirectory();
					File j = new File(f, "tempImage"+i+".png");*/
					//Bitmap scaledBmp = Bitmap.createBitmap(buffer2, width, height, Bitmap.Config.ALPHA_8);
					FileOutputStream out = context.openFileOutput("tempImage"+i+".jpg", Context.MODE_WORLD_READABLE);
					//FileOutputStream out = new FileOutputStream(j);
					if(bmp !=null){
						if(bmp.compress(Bitmap.CompressFormat.JPEG, 100, out)){
							Log.d("ViewsActivity.saveData", "bitmap Compress -> Image saved");
						}
						else{
							Log.d("ViewsActivity.saveData", "bitmap Compress -> Could not save Image");
						}
					}else
						Log.d("ViewsActivity.saveData", "bmp is null");
					out.close();
				}catch(FileNotFoundException fne){
					Toast.makeText(context, "Error:cannot find raw image files", Toast.LENGTH_LONG).show();
					Log.i("ViewsActivity.onActivityResult()", "Raw files missing");
				}catch(IOException ioe){
					Toast.makeText(context, "Error:cannot process raw image", Toast.LENGTH_LONG).show();
					Log.i("ViewsActivity.onActivityResult()", "Raw files present but unprocessed");
				}
				mProgressHandler.sendEmptyMessage(0);
				mProgress++;
			}
			if(i >= this.imgCount - 1){
				ViewsActivity.me = null;
			}
			
		}
			
	}
}