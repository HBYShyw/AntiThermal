package com.android.server;

import android.content.Context;
import android.content.Intent;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorAdditionalInfo;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Slog;
import com.android.server.am.IOplusSceneManager;
import com.android.server.backup.BackupManagerConstants;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SensorNotificationService extends SystemService implements SensorEventListener, LocationListener {
    private static final String ATTRIBUTION_TAG = "SensorNotificationService";
    private static final boolean DBG = false;
    private static final long KM_IN_M = 1000;
    private static final long LOCATION_MIN_DISTANCE = 100000;
    private static final long LOCATION_MIN_TIME = 1800000;
    private static final long MILLIS_2010_1_1 = 1262358000000L;
    private static final long MINUTE_IN_MS = 60000;
    private static final String PROPERTY_USE_MOCKED_LOCATION = "sensor.notification.use_mocked";
    private static final String TAG = "SensorNotificationService";
    private Context mContext;
    private long mLocalGeomagneticFieldUpdateTime;
    private LocationManager mLocationManager;
    private Sensor mMetaSensor;
    private SensorManager mSensorManager;

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override // android.location.LocationListener
    public void onProviderDisabled(String str) {
    }

    @Override // android.location.LocationListener
    public void onProviderEnabled(String str) {
    }

    @Override // android.location.LocationListener
    public void onStatusChanged(String str, int i, Bundle bundle) {
    }

    public SensorNotificationService(Context context) {
        super(context.createAttributionContext("SensorNotificationService"));
        this.mLocalGeomagneticFieldUpdateTime = -1800000L;
        this.mContext = getContext();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        LocalServices.addService(SensorNotificationService.class, this);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int i) {
        if (i == 600) {
            SensorManager sensorManager = (SensorManager) this.mContext.getSystemService(IOplusSceneManager.APP_SCENE_SENSOR);
            this.mSensorManager = sensorManager;
            Sensor defaultSensor = sensorManager.getDefaultSensor(32);
            this.mMetaSensor = defaultSensor;
            if (defaultSensor != null) {
                this.mSensorManager.registerListener(this, defaultSensor, 0);
            }
        }
        if (i == 1000) {
            LocationManager locationManager = (LocationManager) this.mContext.getSystemService("location");
            this.mLocationManager = locationManager;
            if (locationManager == null) {
                return;
            }
            locationManager.requestLocationUpdates("passive", 1800000L, 100000.0f, this);
        }
    }

    private void broadcastDynamicSensorChanged() {
        Intent intent = new Intent("android.intent.action.DYNAMIC_SENSOR_CHANGED");
        intent.setFlags(1073741824);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor == this.mMetaSensor) {
            broadcastDynamicSensorChanged();
        }
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(Location location) {
        if (!(location.getLatitude() == 0.0d && location.getLongitude() == 0.0d) && SystemClock.elapsedRealtime() - this.mLocalGeomagneticFieldUpdateTime >= BackupManagerConstants.DEFAULT_KEY_VALUE_BACKUP_FUZZ_MILLISECONDS) {
            long currentTimeMillis = System.currentTimeMillis();
            if (useMockedLocation() == location.isMock() || currentTimeMillis < MILLIS_2010_1_1) {
                return;
            }
            try {
                SensorAdditionalInfo createLocalGeomagneticField = SensorAdditionalInfo.createLocalGeomagneticField(new GeomagneticField((float) location.getLatitude(), (float) location.getLongitude(), (float) location.getAltitude(), currentTimeMillis).getFieldStrength() / 1000.0f, (float) ((r0.getDeclination() * 3.141592653589793d) / 180.0d), (float) ((r0.getInclination() * 3.141592653589793d) / 180.0d));
                if (createLocalGeomagneticField != null) {
                    this.mSensorManager.setOperationParameter(createLocalGeomagneticField);
                    this.mLocalGeomagneticFieldUpdateTime = SystemClock.elapsedRealtime();
                }
            } catch (IllegalArgumentException unused) {
                Slog.e("SensorNotificationService", "Invalid local geomagnetic field, ignore.");
            }
        }
    }

    private boolean useMockedLocation() {
        return "false".equals(System.getProperty(PROPERTY_USE_MOCKED_LOCATION, "false"));
    }
}
