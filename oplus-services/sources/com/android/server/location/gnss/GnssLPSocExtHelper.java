package com.android.server.location.gnss;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import java.util.Calendar;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class GnssLPSocExtHelper {
    private static final String AUTO_TIME_GPS = "auto_time_gps";
    private static final int CONVERT_VALUE = 1000;
    private static final int EVENT_GPS_TIME_SYNC_CHANGED = 4;
    private static final int EVENT_SEND_BLUESKY_BROADCAST = 5;
    private static final String TAG = "GnssLocationProvider";
    private final Context mContext;
    private Handler mGpsHandler;
    private GpsTimeSyncObserver mGpsTimeSyncObserver;
    private final Handler mHandler;
    private LocationManager mLocationManager;
    private LocationListener mPassiveLocationListener = new LocationListener() { // from class: com.android.server.location.gnss.GnssLPSocExtHelper.1
        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if ("gps".equals(location.getProvider())) {
                GnssLPSocExtHelper.this.doSystemTimeSyncByGps((location.getLatitude() == 0.0d || location.getLongitude() == 0.0d) ? false : true, location.getTime());
            }
        }
    };

    public GnssLPSocExtHelper(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
        registerIntentReceiver();
        Log.d(TAG, "add GPS time sync handler and looper");
        this.mGpsHandler = new MyHandler(handler.getLooper());
        this.mLocationManager = (LocationManager) context.getSystemService("location");
        GpsTimeSyncObserver gpsTimeSyncObserver = new GpsTimeSyncObserver(this.mGpsHandler, 4);
        this.mGpsTimeSyncObserver = gpsTimeSyncObserver;
        gpsTimeSyncObserver.observe(context);
    }

    private void registerIntentReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        this.mContext.registerReceiverAsUser(new BroadcastReceiver() { // from class: com.android.server.location.gnss.GnssLPSocExtHelper.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
                    boolean gpsTimeSyncState = GnssLPSocExtHelper.this.getGpsTimeSyncState();
                    Log.d(GnssLPSocExtHelper.TAG, "BOOT_COMPLETED, GPS Time sync is set to " + gpsTimeSyncState);
                    GnssLPSocExtHelper.this.setGpsTimeSyncFlag(gpsTimeSyncState);
                    GnssLPSocExtHelper.this.mGpsHandler.obtainMessage(5).sendToTarget();
                }
            }
        }, UserHandle.ALL, intentFilter, null, this.mHandler);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 4) {
                if (i != 5) {
                    return;
                }
                BlueskyUtility.sendBlueskyBroadcast(GnssLPSocExtHelper.this.mContext);
                Log.d(GnssLPSocExtHelper.TAG, "Finish Bluesky broadcast");
                return;
            }
            boolean gpsTimeSyncState = GnssLPSocExtHelper.this.getGpsTimeSyncState();
            Log.d(GnssLPSocExtHelper.TAG, "GPS Time sync is changed to " + gpsTimeSyncState);
            GnssLPSocExtHelper.this.onGpsTimeChanged(gpsTimeSyncState);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getGpsTimeSyncState() {
        try {
            return Settings.Global.getInt(this.mContext.getContentResolver(), AUTO_TIME_GPS) > 0;
        } catch (Settings.SettingNotFoundException unused) {
            return false;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class GpsTimeSyncObserver extends ContentObserver {
        private Handler mHandler;
        private int mMsg;

        GpsTimeSyncObserver(Handler handler, int i) {
            super(handler);
            this.mHandler = handler;
            this.mMsg = i;
        }

        void observe(Context context) {
            context.getContentResolver().registerContentObserver(Settings.Global.getUriFor(GnssLPSocExtHelper.AUTO_TIME_GPS), false, this);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            this.mHandler.obtainMessage(this.mMsg).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onGpsTimeChanged(boolean z) {
        setGpsTimeSyncFlag(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setGpsTimeSyncFlag(boolean z) {
        Log.d(TAG, "setGpsTimeSyncFlag: " + z);
        if (z) {
            this.mLocationManager.requestLocationUpdates("passive", 0L, 0.0f, this.mPassiveLocationListener);
        } else {
            this.mLocationManager.removeUpdates(this.mPassiveLocationListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSystemTimeSyncByGps(boolean z, long j) {
        if (z) {
            Log.d(TAG, " ########## Auto-sync time with GPS: timestamp = " + j + " ########## ");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(j);
            long timeInMillis = calendar.getTimeInMillis();
            if (timeInMillis / 1000 < 2147483647L) {
                SystemClock.setCurrentTimeMillis(timeInMillis);
            }
            this.mLocationManager.removeUpdates(this.mPassiveLocationListener);
        }
    }
}
