package com.raccuddasys.views.utilities;

import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MySensorManagerWrapper implements MySensorManager{

	SensorManager mSensorManager;
	
	public MySensorManagerWrapper(SensorManager sensorManager){
		super();
		mSensorManager = sensorManager;
	}
	@Override
	public List<Sensor> getSensorList(int type) {
		return mSensorManager.getSensorList(type);
	}
	@Override
	public boolean registerListener(SensorEventListener listener, Sensor sensor, int rate) {
		return mSensorManager.registerListener(listener, sensor, rate);
	}
	
	public void unregisterListener(SensorEventListener sel){
		mSensorManager.unregisterListener(sel);
	}

}
