package android.hardware;

import android.app.ActivityThread;
import android.app.IOplusCompactWindowAppManager;
import android.common.OplusFeatureCache;
import android.content.Context;
import android.hardware.SystemSensorManager;
import android.os.Handler;
import android.os.SystemProperties;
import android.util.Log;
import com.aiunit.aon.AonSmartRotation;
import com.oplus.android.internal.util.OplusFrameworkStatsLog;
import java.util.HashMap;

/* loaded from: classes.dex */
public class SystemSensorManagerExtImpl implements ISystemSensorManagerExt {
    private static final boolean DEBUG_DYNAMIC_SENSOR = false;
    private static final float FLOAT_DIFF = 0.0f;
    private static final int MESSAGE_WHAT = 1001;
    private static final int POST_DELAYED_INTERVEL = 2000;
    private static final int SENSOR_VALUE_STEP = 100;
    private static final int STATSLOG_CONSTANT = 1;
    private static final String TAG = "SystemSensorManagerExtImpl";
    public static final int TYPE_PEDOMETER_MINUTE = 33171034;
    public static boolean sInZoomWindow;
    private Context mContext;
    private SensorEvent mSensorevent;
    private Handler mTimerHandler;
    public static float mLastStep = 0.0f;
    public static float mPedoLastStep = 0.0f;
    public static int MAX_STEP_INTERVAL = 100;
    public static AonSmartRotation mAonSmartRotation = null;
    public static boolean haveAonSmartRotation = false;
    public static float hardwareValue = 0.0f;
    public static int mLastSmartRotationStatus = 0;
    public static int mCurrentSmartRotationStatus = 0;
    private static float sSensorEventValueLight = 1.0f;
    private static long sTimestamp = 0;
    private static int sOldProxStatus = 1;
    private static int sProxStatus = 1;
    private static boolean sStepDectorFlag = false;
    private Runnable mTimerThread = new Runnable() { // from class: android.hardware.SystemSensorManagerExtImpl.1
        @Override // java.lang.Runnable
        public void run() {
            SystemSensorManagerExtImpl.sStepDectorFlag = false;
            long duration = SystemSensorManagerExtImpl.this.mSensorevent.timestamp - SystemSensorManagerExtImpl.sTimestamp;
            OplusFrameworkStatsLog.write(100098, SystemSensorManagerExtImpl.sTimestamp, SystemSensorManagerExtImpl.this.mSensorevent.timestamp, duration, SystemSensorManagerExtImpl.this.mSensorevent.sensor.getHandle());
        }
    };
    public IOplusCompactWindowAppManager mCompactWindowAppManager = OplusFeatureCache.getOrCreate(IOplusCompactWindowAppManager.DEFAULT, new Object[0]);

    public SystemSensorManagerExtImpl(Object base) {
    }

    public boolean registerListenerImplHook(Context context, SensorEventListener listener, Sensor sensor, int delayUs) {
        this.mContext = context;
        if (listener != null) {
            if (sensor == null) {
                Log.v(TAG, "RegisterListener by " + listener.toString() + " from " + context.getPackageName());
            } else {
                Log.v(TAG, "RegisterListener " + sensor.getName() + " type:" + sensor.getType() + " delay:" + delayUs + "us by " + listener.toString() + " from " + context.getPackageName());
            }
            if (listener.getClass().getName() != null && listener.getClass().getName().indexOf("com.tencent.beacon") != -1) {
                Log.w(TAG, "Block tencent beacon for using sensor.");
                return true;
            }
        }
        if (sensor != null) {
            try {
                if (sensor.getType() == 27 && mAonSmartRotation == null) {
                    boolean equals = SystemProperties.get("persist.sys.oplus.smartrotation", "false").equals("true");
                    haveAonSmartRotation = equals;
                    if (equals) {
                        mAonSmartRotation = new AonSmartRotation(context);
                        if (listener != null) {
                            Log.d(TAG, "SmartRotationDebug, Init AonSmartRotation by " + listener.toString());
                            return false;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            } catch (Exception e) {
                Log.e(TAG, "SmartRotation got exception, e = " + e.toString());
                return false;
            }
        }
        return false;
    }

    public boolean unregisterListenerImplHook(SensorEventListener listener, Sensor sensor) {
        if (listener != null) {
            if (sensor != null) {
                Log.v(TAG, "unRegisterListener by " + listener.toString() + " and its name is " + sensor.getName());
            } else {
                Log.v(TAG, "unRegisterListener by " + listener.toString());
            }
        }
        if (sensor != null) {
            try {
                if (sensor.getType() == 27 && mAonSmartRotation != null) {
                    mAonSmartRotation = null;
                    if (listener != null) {
                        Log.d(TAG, "SmartRotationDebug, Destroy AonSmartRotation by " + listener.toString());
                        return false;
                    }
                    return false;
                }
                return false;
            } catch (Exception e) {
                Log.e(TAG, "SmartRotation got exception, e = " + e.toString());
                return false;
            }
        }
        return false;
    }

    private boolean blockDispatchEvent(SensorEventListener listener, Sensor sensor, SensorEvent events) {
        if (sensor == null || sensor.getType() != 27 || events.values[0] <= 4.0d) {
            return sensor != null && (sensor.getType() == 27 || sensor.getType() == 8 || sensor.getType() == 1) && sInZoomWindow;
        }
        return true;
    }

    private boolean needToAdjustEvent(SensorEventListener listener, Sensor sensor, SensorEvent events) {
        if (sensor == null) {
            return false;
        }
        try {
            if (sensor.getType() == 19) {
                if (mLastStep == 0.0f || Math.abs(events.values[0] - mLastStep) >= MAX_STEP_INTERVAL) {
                    Log.v(TAG, "step counter dispatchSensorEvent step " + events.values[0] + " to + " + listener);
                    mLastStep = events.values[0];
                }
            } else if (sensor.getType() == 33171034 && (mPedoLastStep == 0.0f || Math.abs(events.values[0] - mPedoLastStep) >= MAX_STEP_INTERVAL)) {
                Log.v(TAG, "Pedometer minute step counter dispatchSensorEvent step " + events.values[0] + " to + " + listener);
                mPedoLastStep = events.values[0];
            }
        } catch (Exception e) {
            Log.e(TAG, "step counter error e = " + e.toString());
        }
        return sensor.getType() == 27 && haveAonSmartRotation && mAonSmartRotation != null;
    }

    public SensorEvent dispatchEventDataAdjust(SensorEventListener listener, Sensor sensor, SensorEvent events) {
        AonSmartRotation aonSmartRotation;
        if (sensor != null) {
            try {
                if (sensor.getType() == 27 && haveAonSmartRotation && (aonSmartRotation = mAonSmartRotation) != null) {
                    int status = aonSmartRotation.getStatus();
                    mCurrentSmartRotationStatus = status;
                    if (status != mLastSmartRotationStatus) {
                        if (status == 1) {
                            Log.d(TAG, "SmartRotationDebug, SmartRotation switch On.");
                            mAonSmartRotation.createSmartRotationConnection();
                        } else if (status == 0) {
                            Log.d(TAG, "SmartRotationDebug, SmartRotation switch Off.");
                            mAonSmartRotation.destroySmartRotationConnection();
                        } else {
                            Log.w(TAG, "SmartRotationDebug, SmartRotation switch Invalid, reset Status value.");
                            mCurrentSmartRotationStatus = 0;
                            mLastSmartRotationStatus = 0;
                        }
                    }
                    mLastSmartRotationStatus = mCurrentSmartRotationStatus;
                    hardwareValue = events.values[0];
                    events.values[0] = mAonSmartRotation.makeDecisionBySmartRotation(events.values);
                    if (hardwareValue != events.values[0]) {
                        Log.d(TAG, "SmartRotationDebug, Device Orientation changed by SmartRotation, t.value[0] is " + events.values[0] + ", and Hardware Value is " + hardwareValue + "to: " + listener);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "SmartRotation got exception, e = " + e.toString());
            }
        }
        return events;
    }

    public SensorEvent dispatchSensorEventHook(SensorEventListener listener, Sensor sensor, SensorEvent events, boolean inMirage, int cmdArgs, Context context) {
        IOplusCompactWindowAppManager iOplusCompactWindowAppManager;
        SensorEvent t = events;
        int type = sensor != null ? sensor.getType() : -1;
        if (needToAdjustEvent(listener, sensor, events)) {
            t = dispatchEventDataAdjust(listener, sensor, events);
        }
        if (blockDispatchEvent(listener, sensor, events)) {
            t = null;
        }
        if (inMirage) {
            t = null;
        }
        if (cmdArgs == 1) {
            Log.d(TAG, "dispatchSensorEventHook newEvent=" + t + ",oldEvent=" + events + ",type=" + type + ",sInZoomWindow=" + sInZoomWindow + ",inMirage=" + inMirage + ",listener=" + listener);
        }
        if (!ActivityThread.isSystem() && (iOplusCompactWindowAppManager = this.mCompactWindowAppManager) != null && iOplusCompactWindowAppManager.blockOrientationSensorEventInCompactWindowMode(context, sensor.getType(), ActivityThread.currentPackageName())) {
            return null;
        }
        return t;
    }

    public void dispatchSensorEventEnd(int res, int handle, float[] values, int accuracy, int type, int cmdArgs, String client) {
        if (cmdArgs == 1) {
            Log.d(TAG, "dispatchSensorEvent_end  reportedRes=" + res + ",type=" + type + ",handle=data[" + values[0] + "," + values[1] + "," + values[2] + "],accuracy=" + accuracy + handle + ",client=" + client);
        }
    }

    public void printSensorListenersWhileExceededLocked(Context context, HashMap<SensorEventListener, SystemSensorManager.SensorEventQueue> sensorListeners) {
        if (sensorListeners != null) {
            HashMap<SensorEventListener, SystemSensorManager.SensorEventQueue> listeners = new HashMap<>(sensorListeners);
            Log.e(TAG, "sensorListeners exceeded, size = " + listeners.size() + " in " + context.getPackageName());
            for (SensorEventListener l : listeners.keySet()) {
                Log.e(TAG, "listener : " + l.toString());
            }
        }
    }

    public void enableSensorStatsLog(int enbale, int handler, int sensorType, String opPackageName, String packageName) {
        switch (sensorType) {
            case 5:
            case 8:
            case 18:
            case 30:
                OplusFrameworkStatsLog.write(100038, handler, opPackageName, sensorType, packageName, enbale);
                return;
            default:
                return;
        }
    }

    public void sensorDispatchEventStasLog(SensorEvent event, float[] values) {
        float max = event.sensor.getMaximumRange();
        switch (event.sensor.getType()) {
            case 5:
                float eventValue = Math.round((values[0] * 100.0f) / max);
                if (Math.abs(sSensorEventValueLight - eventValue) > 1.0f) {
                    sSensorEventValueLight = eventValue;
                    OplusFrameworkStatsLog.write(100039, event.sensor.getName(), event.sensor.getHandle(), event.accuracy, eventValue);
                    return;
                }
                return;
            case 8:
                float distance = values[0];
                if (distance >= 0.0f && distance < max) {
                    sProxStatus = 1;
                } else {
                    sProxStatus = 0;
                }
                int i = sOldProxStatus;
                int i2 = sProxStatus;
                if (i != i2) {
                    sOldProxStatus = i2;
                    OplusFrameworkStatsLog.write(100039, event.sensor.getName(), event.sensor.getHandle(), event.accuracy, values[0]);
                    return;
                }
                return;
            case 18:
                this.mSensorevent = event;
                if (this.mContext != null) {
                    if (this.mTimerHandler == null) {
                        this.mTimerHandler = new Handler(this.mContext.getMainLooper());
                    }
                    if (!sStepDectorFlag) {
                        sTimestamp = event.timestamp;
                        sStepDectorFlag = true;
                        this.mTimerHandler.postDelayed(this.mTimerThread, 1001, 2000L);
                        return;
                    } else {
                        this.mTimerHandler.removeMessages(1001);
                        this.mTimerHandler.postDelayed(this.mTimerThread, 1001, 2000L);
                        return;
                    }
                }
                return;
            default:
                return;
        }
    }
}
