package com.raccuddasys.views.activities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.graphics.Matrix;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.raccuddasys.views.utilities.MyFakeSensorManager;
import com.raccuddasys.views.utilities.MySensorManager;
import com.raccuddasys.views.utilities.MySensorManagerWrapper;

public class CameraActivity extends Activity implements View.OnClickListener, 
														View.OnKeyListener, View.OnTouchListener, View.OnFocusChangeListener, SensorEventListener, LocationListener{
	//public static Activity me = null;
	public static int MAX_PHOTOS = 15;
	static int photoNumber = 0;
	public static boolean savedata = false;
	public static boolean processdata = false;
	boolean useSensorEmulator = false;
	Preview mPreview;
	MySensorManager sm;
	static boolean cameraOk = true;
	static boolean moveLeft = false;
	static boolean moveRight = false;
	static boolean moveBack = false;
	static boolean moveFront = false;
	static boolean rotateLeft = false;
	static boolean rotateRight = false;
	static boolean rollLeft = false;
	static boolean rollRight = false;
	static boolean pitchFront = false;
	static boolean pitchBack = false;
	static int imgWidth;
	static int imgHeight;
	static int STARTING_LOWER = 3;
	static int STARTING_UPPER = 20;
	boolean started = false;
	public static float angles[] = new float[MAX_PHOTOS];
	public static float compassValues[] = new float[3];
	List<Sensor> sensorList;
	//Sensoreventlistener variable
	private final SensorEventListener sel = new SensorEventListener(){

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if(!started && event.values[0] > STARTING_UPPER){
				CameraActivity.rotateLeft = true;
				if(event.values[0] < STARTING_UPPER && event.values[0] > STARTING_LOWER){
					started = true;
				}
			}else{
				if(!started && (event.values[0] < STARTING_UPPER && event.values[0] > STARTING_LOWER)){
					CameraActivity.rotateLeft = false;
				}
			}
			
			if(photoNumber == 0 && event.values[0] > STARTING_UPPER){
				started = false;
				CameraActivity.rotateLeft = true;
			}
						
			compassValues[0] = event.values[0];
			compassValues[1] = event.values[1];
			compassValues[2] = event.values[2];
			//Log.i("CameraActivity.onSensorChanged()", "[0] "+event.values[0]+"[1] "+event.values[1]+"[2] "+event.values[2]);
			final int ERROR_ZONE = 5;
			float checkZero;
			if(photoNumber > 0){
				CameraActivity.rotateLeft = false;
			}
			
			CameraActivity.rotateRight = false;
			
			
			CameraActivity.rollLeft = false;
			CameraActivity.rollRight = false;
			CameraActivity.pitchBack = false;
			CameraActivity.pitchFront = false;
			if(event.values[1] > -80){
				CameraActivity.pitchFront = true;
			}
			if(event.values[1] < -100){
				CameraActivity.pitchBack = true;
			}
			if(event.values[2] < -10){
				CameraActivity.rollLeft = true;
			}
			if(event.values[2] > 10){
				CameraActivity.rollRight = true;
			}
			
			if(photoNumber <= MAX_PHOTOS - 1) {
				if(event.values[0] < (angles[photoNumber] - ERROR_ZONE)){
					CameraActivity.rotateRight = true;
				}
			}
			if(photoNumber <= MAX_PHOTOS - 1){
				if (started && (event.values[0] > (angles[photoNumber] + ERROR_ZONE))){
					CameraActivity.rotateLeft = true;
				}
			}
			
			if(photoNumber == 0){
				CameraActivity.rotateRight = false;
			}
							
			
		}
	};
	
	
	//~sensoreventlistener variable
	
	
	public void onCreate(Bundle savedInstanceState) {
		System.gc();
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if(useSensorEmulator){
			System.out.println("Using fake sensor manager");
			try{
				sm = new MyFakeSensorManager((SensorManager)getSystemService(SENSOR_SERVICE));
				Log.i("CameraActivity.onCreate", "fake sensor manager startup successful");
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		else{
			sm = new MySensorManagerWrapper((SensorManager)getSystemService(SENSOR_SERVICE));
		}
		
		setContentView(R.layout.camera_layout);
		Button actionButton = (Button)findViewById(R.id.action_button);
		actionButton.setOnClickListener(this);
		mPreview = (Preview) findViewById(R.id.myCamera);
		if(mPreview == null){
			System.out.println("Preview is null!");
		}
	
		sensorList = sm.getSensorList(Sensor.TYPE_ORIENTATION);
		       
	}
	
	public void onPause(){
		super.onPause();
		sm.unregisterListener(sel);
	}
	public void onResume(){
		super.onResume();
		if(sensorList != null){
			if(sensorList.size() > 0){
				sm.registerListener(sel, sensorList.get(0), SensorManager.SENSOR_DELAY_NORMAL);
				//Toast.makeText(this, "has orientation sensors", Toast.LENGTH_LONG).show();
			}
		}
		else{
			System.out.println("Has no sensors");
			Bundle bundle = new Bundle();
	        
			bundle.putString("sensor_error", this.getString(R.string.orientation_sensors_error));
	       	        
			Intent mIntent = new Intent();
	        mIntent.putExtras(bundle);
	        setResult(ViewsActivity.CAMERA_CANCELED, mIntent);
			finish();
		}
	}
	public void onDestroy(){
		mPreview = null;
		//CameraActivity.me = null;
		photoNumber = 0;
		processdata = false;
		Log.i("CameraActivity.onDestroy()", "Camera activity destroyed");
		super.onDestroy();
		sm.unregisterListener(sel);
	}
	
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		//Intent i = new Intent();
		//i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		switch(id){
		case R.id.action_button:
			//Toast.makeText(this, "starting photo shoot photos:"+CameraActivity.getPhotoCount(), Toast.LENGTH_LONG).show();
			if(compassValues[0] < STARTING_UPPER && compassValues[0] > STARTING_LOWER){
				angles[0] = compassValues[0];
				v.setEnabled(false);
				float camAngles = (float)Math.floor(360/MAX_PHOTOS);
				for(int j = 1; j < MAX_PHOTOS; j++){
					//if(angles[j - 1]+camAngles > 359){
					//	float p = 359 - angles[j - 1];
					//	angles[j] = camAngles - p -1;// -1 error correction to put it to 0 - 359 instead of 1 - 360
					//}
					//else{
					angles[j] = angles[j - 1]+camAngles;
					//}
					Log.i("CameraActiviy Angles Init", "The angle:"+angles[j]);
				}
				CameraActivity.savedata = true;
			}else{
				Toast.makeText(this, "Please rotate left", Toast.LENGTH_LONG);
			}
			
			break;
		
		}
		//finish();
		
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
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized static void addPhotoCount(){
		++photoNumber;
	}
	public synchronized static int getPhotoCount(){
		return photoNumber;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
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

class Preview extends SurfaceView implements SurfaceHolder.Callback {
	SurfaceHolder mHolder;
	Camera mCamera;
	String attributeNamespace = "http://schemas.android.com/apk/res/com.raccuddasys.views.activities.Preview";
	Context context;
	public Preview(Context context){
		super(context);
		setFocusable(true);
		mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.context = context;
	}
	public Preview(Context context, AttributeSet attrs){
		super(context, attrs);
		setFocusable(true);
		mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.context = context;
		
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// Now that the size is known, set up the camera parameters and begin
        // the preview.
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(width, height);
        parameters.setPreviewFormat(PixelFormat.YCbCr_422_SP);
        parameters.setPreviewFrameRate(24);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
        parameters.setPictureFormat(PixelFormat.JPEG);
        parameters.setPictureSize(400, 300);
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try{
			mCamera = Camera.open();
			mCamera.setPreviewDisplay(holder);
			mCamera.setPreviewCallback(new CamPreviewCallback(context));
			CameraActivity.imgHeight = mCamera.getParameters().getPreviewSize().height;
			CameraActivity.imgWidth = mCamera.getParameters().getPreviewSize().width;
			
			Log.i("CameraActivity.surfaceCreated", "Camera created");
		    
		}catch(IOException e){
			//Toast.makeText(CameraActivity.me, "Error: Out of memory", Toast.LENGTH_LONG);
			mCamera.release();
	        mCamera = null;
	        e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

	
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
		
	}
	public void release(){
		//mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
	}
	
}

class CamPreviewCallback implements Camera.PreviewCallback{
	Context context;
	
	public CamPreviewCallback(Context context){
		this.context = context;
	}
	Camera.PictureCallback myPictureCallBack = new Camera.PictureCallback(){
		
		public void onPictureTaken(byte[] data, Camera camera){
			
			Log.i("CameraActivity.PictureCallback", "picture taken");
			
			int newWidth = 400;
			int newHeight = 300;
			
			//File myFile = new File("/sdcard", "myImage"+CameraActivity.photoNumber+".jpg");
			//Toast.makeText(context, "Saving data....", Toast.LENGTH_LONG).show();
			Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
			int width = bmp.getWidth();
			int height = bmp.getHeight();
			float scaleWidth = (float)newWidth/width;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleWidth); //We do it in the same ratio for both sides
			matrix.postRotate(90);
			Bitmap bmp2 = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
			
			new SaveData(bmp2, context);
			
			bmp = null;
			data = null;
			//bmp2 = null;
			matrix = null;
				
			
			System.gc();
			try{
				Thread.sleep(10);
			}catch(Exception e){
				e.printStackTrace();
			}
			camera.startPreview();
		}
	};

	public void onPreviewFrame(byte[] data, Camera camera) {
		int photoCount = CameraActivity.getPhotoCount();
		//if(CameraActivity.savedata){
			tellSetCamera();
			TextView tv = (TextView) ((Activity)context).findViewById(R.id.pitch);
			tv.setText("[0] "+(int)CameraActivity.compassValues[0]);
			TextView tv2 = (TextView) ((Activity)context).findViewById(R.id.roll);
			tv2.setText("[1] "+(int)CameraActivity.compassValues[1]);
			TextView tv3 = (TextView) ((Activity)context).findViewById(R.id.rotate);
			tv3.setText("[2] "+(int)CameraActivity.compassValues[2]);
		//}
		//System.out.println("preview frame "+photoCount);
		if(CameraActivity.savedata == true && CameraActivity.cameraOk && photoCount < CameraActivity.MAX_PHOTOS){
			//Thread th = new Thread(new SaveData(data, camera.getParameters().getPreviewSize().width,camera.getParameters().getPreviewSize().height, photoCount, context));
			//th.start();
			try{
				camera.takePicture(null, null, myPictureCallBack);
				CameraActivity.cameraOk = false;
				TextView photos = (TextView)((Activity)context).findViewById(R.id.photos);
				photos.setText("Saved: "+CameraActivity.photoNumber);
				CameraActivity.addPhotoCount();
			}catch(Exception e){
				//Toast.makeText(context, "Repeat Photo", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			
		}
		else{
			if(CameraActivity.savedata == true && photoCount >= CameraActivity.MAX_PHOTOS){
				CameraActivity.savedata = false;
			}
			
		}
		if(CameraActivity.processdata == false && (photoCount >=CameraActivity.MAX_PHOTOS)){
			CameraActivity.processdata = true;
			//this takes the raw data and uses this to generate the final image
			//take the geocordinates for the location
			Bundle bundle = new Bundle();
	        
	        bundle.putString("image", "myImage");
	        bundle.putInt("imgWidth", CameraActivity.imgWidth);
	        bundle.putInt("imgHeight", CameraActivity.imgHeight);
	        bundle.putInt("max_photos", CameraActivity.MAX_PHOTOS);
	        
	        Intent mIntent = new Intent();
	        mIntent.putExtras(bundle);
	        ((Activity)context).setResult(ViewsActivity.CAMERA_OK, mIntent);
	        ((Activity)context).finish();
		}
		
	}

	public void tellSetCamera(){
		//Inform the user to set the camera straight, no rolls or pitches
		//If camera position is ok, inform user to click button
		
		CameraActivity.cameraOk = false;
		
		if(!CameraActivity.cameraOk){
			//Log.i("CameraActivity.tellSetCamera()", "Camera not ok");
		}
		
		if(CameraActivity.moveLeft){
			//Log.i("CameraActivity.tellSetCamera", "moving left");
			CameraActivity.moveLeft = false;
		}
		if(CameraActivity.moveRight){
			//Log.i("CameraActivity.tellSetCamera", "moving right");
			CameraActivity.moveRight = false;
		}
		if(CameraActivity.moveBack){
			//Log.i("CameraActivity.tellSetCamera", "moving back");
			CameraActivity.moveBack = false;
		}
		if(CameraActivity.moveFront){
			//Log.i("CameraActivity.tellSetCamera", "moving forward");
			CameraActivity.moveFront = false;
		}
		
		if(CameraActivity.rotateLeft){
			//Log.i("CameraActivity.tellSetCamera", "rotating left");
			TextView rotate2 = (TextView)((Activity)context).findViewById(R.id.rotl);
			rotate2.setText("rotl");
		}
		else{
			TextView rotate2 = (TextView)((Activity)context).findViewById(R.id.rotl);
			rotate2.setText("");
		}
		
		if(CameraActivity.rotateRight){
			//Log.i("CameraActivity.tellSetCamera", "rotating right");
			TextView rotate2 = (TextView)((Activity)context).findViewById(R.id.rotr);
			rotate2.setText("rotr");
		}
		else{
			TextView rotate2 = (TextView)((Activity)context).findViewById(R.id.rotr);
			rotate2.setText("");
		}
		if(CameraActivity.rollLeft){
			//Log.i("CameraActivity.tellSetCamera", "rolling left");
			TextView roll2 = (TextView)((Activity)context).findViewById(R.id.rl);
			roll2.setText("rl");
		}
		else{
			TextView roll2 = (TextView)((Activity)context).findViewById(R.id.rl);
			roll2.setText("");
		}
		if(CameraActivity.rollRight){
			//Log.i("CameraActivity.tellSetCamera", "rolling right");
			TextView roll2 = (TextView)((Activity)context).findViewById(R.id.rr);
			roll2.setText("rr");
		}
		else{
			TextView roll2 = (TextView)((Activity)context).findViewById(R.id.rr);
			roll2.setText("");
		}
		if(CameraActivity.pitchBack){
			//Log.i("CameraActivity.tellPitchBack()", "pitching back");
			TextView pitch2 = (TextView)((Activity)context).findViewById(R.id.pb);
			pitch2.setText("pb");
		}
		else{
			TextView pitch2 = (TextView)((Activity)context).findViewById(R.id.pb);
			pitch2.setText("");
		}
		if(CameraActivity.pitchFront){
			//Log.i("CameraActivity.tellPitchFront()", "pitching forwards");
			TextView pitch2 = (TextView)((Activity)context).findViewById(R.id.pf);
			pitch2.setText("pf");
		}
		else{
			TextView pitch2 = (TextView)((Activity)context).findViewById(R.id.pf);
			pitch2.setText("");
		}
		if(!CameraActivity.moveLeft && !CameraActivity.moveRight && !CameraActivity.moveBack &&	!CameraActivity.moveFront
				&& !CameraActivity.rotateLeft && !CameraActivity.rotateRight && !CameraActivity.rollLeft
				&& !CameraActivity.rollRight && !CameraActivity.pitchBack && !CameraActivity.pitchFront){
			Log.i("CameraActivity.tellSetCamera()", "camera Ok, Saving data");
			CameraActivity.cameraOk = true;
		}
		
	}
}

class SaveData implements Runnable{

	BufferedOutputStream fos;
	Bitmap bitmap;
	Context context;
	
	public SaveData(Bitmap bitmap, Context context){
		this.bitmap = bitmap;
		this.context = context;
		
	}
	public void run() {
		File myFile = new File("/sdcard", "myImage"+CameraActivity.photoNumber+".jpg");
		try{
			BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(myFile));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			bitmap = null;
			
		}catch(FileNotFoundException fne){
			Toast.makeText(context, "File not found Could not save Image", Toast.LENGTH_LONG);
		}catch(IOException ioe){
			Toast.makeText(context, "IOException Could not save Image", Toast.LENGTH_LONG);
		}
	}
}




