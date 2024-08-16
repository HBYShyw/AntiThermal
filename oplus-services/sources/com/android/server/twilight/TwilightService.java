package com.android.server.twilight;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.server.SystemService;
import com.android.server.hdmi.HdmiCecKeycode;
import com.ibm.icu.impl.CalendarAstronomer;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class TwilightService extends SystemService implements AlarmManager.OnAlarmListener, Handler.Callback, LocationListener {
    private static final String ATTRIBUTION_TAG = "TwilightService";
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final int MSG_START_LISTENING = 1;
    private static final int MSG_STOP_LISTENING = 2;
    private static final String TAG = "TwilightService";
    protected AlarmManager mAlarmManager;
    private boolean mBootCompleted;
    private final Handler mHandler;
    private boolean mHasListeners;
    protected Location mLastLocation;

    @GuardedBy({"mListeners"})
    protected TwilightState mLastTwilightState;

    @GuardedBy({"mListeners"})
    private final ArrayMap<TwilightListener, Handler> mListeners;
    private LocationManager mLocationManager;
    private BroadcastReceiver mTimeChangedReceiver;

    @Override // android.location.LocationListener
    public void onStatusChanged(String str, int i, Bundle bundle) {
    }

    public TwilightService(Context context) {
        super(context.createAttributionContext("TwilightService"));
        this.mListeners = new ArrayMap<>();
        this.mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public void onStart() {
        publishLocalService(TwilightManager.class, new TwilightManager() { // from class: com.android.server.twilight.TwilightService.1
            @Override // com.android.server.twilight.TwilightManager
            public void registerListener(TwilightListener twilightListener, Handler handler) {
                synchronized (TwilightService.this.mListeners) {
                    boolean isEmpty = TwilightService.this.mListeners.isEmpty();
                    TwilightService.this.mListeners.put(twilightListener, handler);
                    if (isEmpty && !TwilightService.this.mListeners.isEmpty()) {
                        TwilightService.this.mHandler.sendEmptyMessage(1);
                    }
                }
            }

            @Override // com.android.server.twilight.TwilightManager
            public void unregisterListener(TwilightListener twilightListener) {
                synchronized (TwilightService.this.mListeners) {
                    boolean isEmpty = TwilightService.this.mListeners.isEmpty();
                    TwilightService.this.mListeners.remove(twilightListener);
                    if (!isEmpty && TwilightService.this.mListeners.isEmpty()) {
                        TwilightService.this.mHandler.sendEmptyMessage(2);
                    }
                }
            }

            @Override // com.android.server.twilight.TwilightManager
            public TwilightState getLastTwilightState() {
                TwilightState twilightState;
                synchronized (TwilightService.this.mListeners) {
                    twilightState = TwilightService.this.mLastTwilightState;
                }
                return twilightState;
            }
        });
    }

    public void onBootPhase(int i) {
        if (i == 1000) {
            Context context = getContext();
            this.mAlarmManager = (AlarmManager) context.getSystemService("alarm");
            this.mLocationManager = (LocationManager) context.getSystemService("location");
            this.mBootCompleted = true;
            if (this.mHasListeners) {
                startListening();
            }
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            if (!this.mHasListeners) {
                this.mHasListeners = true;
                if (this.mBootCompleted) {
                    startListening();
                }
            }
            return true;
        }
        if (i != 2) {
            return false;
        }
        if (this.mHasListeners) {
            this.mHasListeners = false;
            if (this.mBootCompleted) {
                stopListening();
            }
        }
        return true;
    }

    private void startListening() {
        Slog.d("TwilightService", "startListening");
        if (!this.mLocationManager.hasProvider("fused")) {
            if (this.mListeners.isEmpty()) {
                return;
            }
            this.mListeners.clear();
            return;
        }
        this.mLocationManager.requestLocationUpdates(LocationRequest.create().setQuality(HdmiCecKeycode.CEC_KEYCODE_SELECT_MEDIA_FUNCTION), this, Looper.getMainLooper());
        if (this.mLocationManager.getLastLocation() == null) {
            if (this.mLocationManager.isProviderEnabled("network")) {
                this.mLocationManager.getCurrentLocation("network", null, getContext().getMainExecutor(), new TwilightService$$ExternalSyntheticLambda0(this));
                if (DEBUG) {
                    Slog.d("TwilightService", "startListening: NETWORK_PROVIDER true");
                }
            } else if (this.mLocationManager.isProviderEnabled("gps")) {
                this.mLocationManager.getCurrentLocation("gps", null, getContext().getMainExecutor(), new TwilightService$$ExternalSyntheticLambda0(this));
                if (DEBUG) {
                    Slog.d("TwilightService", "startListening: GPS_PROVIDER true");
                }
            }
        }
        if (this.mTimeChangedReceiver == null) {
            this.mTimeChangedReceiver = new BroadcastReceiver() { // from class: com.android.server.twilight.TwilightService.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    Slog.d("TwilightService", "onReceive: " + intent);
                    TwilightService.this.updateTwilightState();
                }
            };
            IntentFilter intentFilter = new IntentFilter("android.intent.action.TIME_SET");
            intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            getContext().registerReceiver(this.mTimeChangedReceiver, intentFilter);
        }
        updateTwilightState();
    }

    private void stopListening() {
        Slog.d("TwilightService", "stopListening");
        if (this.mTimeChangedReceiver != null) {
            getContext().unregisterReceiver(this.mTimeChangedReceiver);
            this.mTimeChangedReceiver = null;
        }
        if (this.mLastTwilightState != null) {
            this.mAlarmManager.cancel(this);
        }
        this.mLocationManager.removeUpdates(this);
        this.mLastLocation = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTwilightState() {
        long currentTimeMillis = System.currentTimeMillis();
        Location location = this.mLastLocation;
        if (location == null) {
            location = this.mLocationManager.getLastLocation();
        }
        final TwilightState calculateTwilightState = calculateTwilightState(location, currentTimeMillis);
        if (DEBUG) {
            Slog.d("TwilightService", "updateTwilightState: " + calculateTwilightState);
        }
        synchronized (this.mListeners) {
            if (!Objects.equals(this.mLastTwilightState, calculateTwilightState)) {
                this.mLastTwilightState = calculateTwilightState;
                for (int size = this.mListeners.size() - 1; size >= 0; size--) {
                    final TwilightListener keyAt = this.mListeners.keyAt(size);
                    this.mListeners.valueAt(size).post(new Runnable() { // from class: com.android.server.twilight.TwilightService$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            TwilightListener.this.onTwilightStateChanged(calculateTwilightState);
                        }
                    });
                }
            }
        }
        if (calculateTwilightState != null) {
            this.mAlarmManager.setExact(1, calculateTwilightState.isNight() ? calculateTwilightState.sunriseTimeMillis() : calculateTwilightState.sunsetTimeMillis(), "TwilightService", this, this.mHandler);
        }
    }

    @Override // android.app.AlarmManager.OnAlarmListener
    public void onAlarm() {
        Slog.d("TwilightService", "onAlarm");
        updateTwilightState();
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(Location location) {
        if (location != null) {
            Slog.d("TwilightService", "onLocationChanged: provider=" + location.getProvider() + " accuracy=" + location.getAccuracy() + " time=" + location.getTime());
            this.mLastLocation = location;
            updateTwilightState();
        }
    }

    @Override // android.location.LocationListener
    public void onProviderEnabled(String str) {
        if (DEBUG) {
            Slog.d("TwilightService", "onProviderEnabled: provider=" + str);
        }
        LocationManager locationManager = this.mLocationManager;
        if (locationManager != null) {
            locationManager.getCurrentLocation(str, null, getContext().getMainExecutor(), new TwilightService$$ExternalSyntheticLambda0(this));
        }
    }

    @Override // android.location.LocationListener
    public void onProviderDisabled(String str) {
        if (DEBUG) {
            Slog.d("TwilightService", "onProviderDisabled");
        }
    }

    private static TwilightState calculateTwilightState(Location location, long j) {
        if (location == null) {
            return null;
        }
        CalendarAstronomer calendarAstronomer = new CalendarAstronomer(location.getLongitude(), location.getLatitude());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        calendar.set(11, 12);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendarAstronomer.setTime(calendar.getTimeInMillis());
        long sunRiseSet = calendarAstronomer.getSunRiseSet(true);
        long sunRiseSet2 = calendarAstronomer.getSunRiseSet(false);
        if (sunRiseSet2 < j) {
            calendar.add(5, 1);
            calendarAstronomer.setTime(calendar.getTimeInMillis());
            sunRiseSet = calendarAstronomer.getSunRiseSet(true);
        } else if (sunRiseSet > j) {
            calendar.add(5, -1);
            calendarAstronomer.setTime(calendar.getTimeInMillis());
            sunRiseSet2 = calendarAstronomer.getSunRiseSet(false);
        }
        return new TwilightState(sunRiseSet, sunRiseSet2);
    }
}
