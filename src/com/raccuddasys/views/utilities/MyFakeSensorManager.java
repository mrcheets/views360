package com.raccuddasys.views.utilities;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MyFakeSensorManager implements MySensorManager{
	Socket mSocket;
	List<Sensor> sensors;
	SensorManager mSensorManager;

    public MyFakeSensorManager(SensorManager sensorManager) throws UnknownHostException, IOException {
    	super();
    	sensors = new LinkedList<Sensor>();
    	mSensorManager = sensorManager;
    	//mSocket = new Socket("192.168.0.2", 9000);
    }
    
	@Override
	public List<Sensor> getSensorList(int type) {
		sensors.add(mSensorManager.getDefaultSensor(type));
		return sensors;
	}

	@Override
	public boolean registerListener(SensorEventListener listener, Sensor sensor, int rate) {
		return false;
	}
	
	public void unregisterListener(SensorEventListener sel){
		
	}

}
