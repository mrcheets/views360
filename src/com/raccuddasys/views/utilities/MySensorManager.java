package com.raccuddasys.views.utilities;

import java.io.IOException;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;

public interface MySensorManager {
	List<Sensor> getSensorList(int type);
	boolean registerListener(SensorEventListener listener, Sensor sensor, int rate);
	public void unregisterListener(SensorEventListener sel);

}
