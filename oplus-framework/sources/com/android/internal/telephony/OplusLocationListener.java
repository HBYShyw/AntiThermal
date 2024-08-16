package com.android.internal.telephony;

import android.content.Context;
import android.database.ContentObserver;
import android.hardware.display.DisplayManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RegistrantList;
import android.provider.Settings;
import android.telephony.Rlog;
import android.view.Display;
import com.android.internal.telephony.nrNetwork.OplusNrModeConstant;
import com.android.internal.telephony.nrNetwork.OplusNrUtils;
import com.oplus.network.OlkConstants;
import java.util.List;

/* loaded from: classes.dex */
public class OplusLocationListener implements LocationListener {
    private static final int EVENT_GET_LOC_TIMEOUT = 203;
    private static final int EVENT_INITIAL_LOC_INFO = 202;
    private static final int EVENT_INIT_LOCATION_ADD = 204;
    private static final int EVENT_LOCATION_CHANGED_CB = 201;
    private static final int EVENT_REQUEST_LOC_UPDATE = 206;
    private static final int EVENT_SCREEN_STATE_CHANGED = 205;
    private static final int INTERVAL_NLP_REPORT_TIMEOUT = 60000;
    private static final String TAG = "OplusLocationListener";
    private static HandlerThread mHt;
    private static OplusLocationListener sInstance = null;
    private int MAX_LOCATION_RETRY_TIMES;
    private int MAX_RETRY_TIMES;
    private Address mAdd;
    private String mCity;
    private Context mContext;
    private final DisplayManager.DisplayListener mDisplayListener;
    private Geocoder mGeocoder;
    private MyHandler mHandler;
    private Runnable mInitailLocationInfo;
    private boolean mIsScreenOn;
    private final RegistrantList mLocRegistrants;
    private Location mLocation;
    private LocationManager mLocationManager;
    private String mProvince;
    private int mRetryCounter;
    private int mRetryLocCounter;
    private boolean mScreenFirstState;
    private boolean mScreenLastState;
    private boolean mScreenOnRetry;

    public void registerForLocChanged(Handler h, int what, Object obj) {
        this.mLocRegistrants.addUnique(h, what, obj);
    }

    public void unregisterForLocChanged(Handler h) {
        this.mLocRegistrants.remove(h);
    }

    protected void notifyLocChanged(Location location) {
        logd("notifyLocChanged");
        AsyncResult ar = new AsyncResult((Object) null, location, (Throwable) null);
        this.mLocRegistrants.notifyRegistrants(ar);
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(Location location) {
        logd("onLocationChanged().");
        this.mHandler.obtainMessage(201, location).sendToTarget();
    }

    public void onLocationChangedCallback(Location location) {
        logd("onLocationChangedCallback().");
        this.mLocationManager.removeUpdates(this);
        if (this.mHandler.hasMessages(203)) {
            this.mHandler.removeMessages(203);
        }
        if (location != null) {
            this.mLocation = location;
            Address updateAddFromLocation = updateAddFromLocation(location);
            this.mAdd = updateAddFromLocation;
            this.mProvince = updateProvinceFromAdd(updateAddFromLocation);
            this.mCity = updateCityFromAdd(this.mAdd);
            this.mRetryLocCounter = 0;
            notifyLocChanged(this.mLocation);
            scheduleNextPeriodLocationUpdate();
            return;
        }
        logd("onLocationChangedCallback : location=null");
        retryLocationRequest();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationTimeout() {
        logd("onLocationTimeout screenOn=" + this.mIsScreenOn);
        this.mLocationManager.removeUpdates(this);
        if (this.mIsScreenOn) {
            retryLocationRequest();
        } else {
            this.mScreenOnRetry = true;
            scheduleNextPeriodLocationUpdate();
        }
    }

    private void scheduleNextPeriodLocationUpdate() {
        logd("scheduleNextPeriodLocationUpdate after 2 hours");
        this.mHandler.sendEmptyMessageDelayed(206, OplusNrModeConstant.LONG_lOC_PERIOD);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestLocationUpdates() {
        logd("requestLocationUpdates");
        if (this.mHandler.hasMessages(206)) {
            this.mHandler.removeMessages(206);
        }
        this.mScreenOnRetry = false;
        try {
            this.mLocationManager.requestLocationUpdates(OlkConstants.EXT_NETWORK, 1000L, 50.0f, this);
            this.mHandler.sendEmptyMessageDelayed(203, 60000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onScreenStateChanged() {
        logd("onScreenStateChanged : screenOn=" + this.mIsScreenOn + ",screenOnRetry=" + this.mScreenOnRetry);
        if (this.mScreenOnRetry && this.mIsScreenOn) {
            requestLocationUpdates();
        }
    }

    private void retryLocationRequest() {
        int i = this.mRetryLocCounter;
        if (i > this.MAX_LOCATION_RETRY_TIMES) {
            logd("loc request retry max time hit");
            this.mRetryLocCounter = 0;
            scheduleNextPeriodLocationUpdate();
        } else {
            this.mRetryLocCounter = i + 1;
            this.mHandler.sendEmptyMessageDelayed(206, getRetryTimeout(r0));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            OplusLocationListener.this.logd("handleMessage : " + msg.what);
            switch (msg.what) {
                case 201:
                    OplusLocationListener.this.onLocationChangedCallback((Location) msg.obj);
                    return;
                case 202:
                    OplusLocationListener.this.onInitailLocationInfo();
                    return;
                case 203:
                    OplusLocationListener.this.onLocationTimeout();
                    return;
                case 204:
                    OplusLocationListener.this.initLocationAdd();
                    return;
                case 205:
                    OplusLocationListener.this.onScreenStateChanged();
                    return;
                case 206:
                    OplusLocationListener.this.requestLocationUpdates();
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initLocationAdd() {
        try {
            logd("initLocation().");
            Location location = this.mLocation;
            if (location != null) {
                Address updateAddFromLocation = updateAddFromLocation(location);
                this.mAdd = updateAddFromLocation;
                this.mProvince = updateProvinceFromAdd(updateAddFromLocation);
                this.mCity = updateCityFromAdd(this.mAdd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getRetryTimeout(int counter) {
        return (1 << counter) * 500;
    }

    private void retryInitailLocationInfo() {
        int i = this.mRetryCounter;
        if (i > this.MAX_RETRY_TIMES) {
            logd("retry max time was hit.");
            return;
        }
        this.mRetryCounter = i + 1;
        this.mHandler.postDelayed(this.mInitailLocationInfo, getRetryTimeout(r0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        try {
            logd("mInitailLocationInfo mRetryCounter = " + this.mRetryCounter + " getRetryTimeout = " + getRetryTimeout(this.mRetryCounter));
            if (!this.mLocationManager.isProviderEnabled(OlkConstants.EXT_NETWORK)) {
                logd("NETWORK_PROVIDER not ready yet : mRetryCounter = " + this.mRetryCounter);
                retryInitailLocationInfo();
            } else {
                this.mHandler.sendEmptyMessageDelayed(202, 10000L);
            }
        } catch (IllegalArgumentException ex) {
            logd("OplusLocationListener ex = " + ex.getMessage());
            retryInitailLocationInfo();
        }
    }

    public OplusLocationListener(Context context) {
        this(context, getHandlerThread().getLooper());
    }

    public OplusLocationListener(Context context, Looper looper) {
        this.mLocRegistrants = new RegistrantList();
        this.mScreenFirstState = true;
        this.mScreenOnRetry = false;
        this.MAX_RETRY_TIMES = 10;
        this.mRetryCounter = 0;
        this.MAX_LOCATION_RETRY_TIMES = 5;
        this.mRetryLocCounter = 0;
        this.mInitailLocationInfo = new Runnable() { // from class: com.android.internal.telephony.OplusLocationListener$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                OplusLocationListener.this.lambda$new$0();
            }
        };
        this.mDisplayListener = new DisplayManager.DisplayListener() { // from class: com.android.internal.telephony.OplusLocationListener.2
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int displayId) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int displayId) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int displayId) {
                OplusLocationListener.this.updateScreenState();
                if (OplusLocationListener.this.mScreenFirstState || OplusLocationListener.this.mIsScreenOn != OplusLocationListener.this.mScreenLastState) {
                    OplusLocationListener.this.mScreenFirstState = false;
                    OplusLocationListener oplusLocationListener = OplusLocationListener.this;
                    oplusLocationListener.mScreenLastState = oplusLocationListener.mIsScreenOn;
                    OplusLocationListener.this.mHandler.sendEmptyMessage(205);
                }
            }
        };
        this.mContext = context;
        this.mLocationManager = (LocationManager) context.getSystemService("location");
        this.mHandler = new MyHandler(looper);
        int isProvisioned = Settings.Global.getInt(this.mContext.getContentResolver(), "device_provisioned", 0);
        if (isProvisioned == 1) {
            registerforScreenStateChanged();
            this.mInitailLocationInfo.run();
        } else {
            ContentObserver observer = new ContentObserver(this.mHandler) { // from class: com.android.internal.telephony.OplusLocationListener.1
                @Override // android.database.ContentObserver
                public void onChange(boolean b) {
                    int isProvisioned2 = Settings.Global.getInt(OplusLocationListener.this.mContext.getContentResolver(), "device_provisioned", 0);
                    OplusLocationListener.this.logd("onChanged DeviceProvisioned=" + isProvisioned2);
                    if (isProvisioned2 == 1) {
                        OplusLocationListener.this.registerforScreenStateChanged();
                        OplusLocationListener.this.mInitailLocationInfo.run();
                        OplusLocationListener.this.mContext.getContentResolver().unregisterContentObserver(this);
                    }
                }
            };
            this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("device_provisioned"), false, observer);
        }
        logd("OplusLocationListener Created");
    }

    public static OplusLocationListener getInstance() {
        OplusLocationListener oplusLocationListener;
        synchronized (OplusLocationListener.class) {
            oplusLocationListener = sInstance;
        }
        return oplusLocationListener;
    }

    public static OplusLocationListener create(Context context) {
        OplusLocationListener oplusLocationListener;
        synchronized (OplusLocationListener.class) {
            if (sInstance == null) {
                sInstance = new OplusLocationListener(context);
            }
            oplusLocationListener = sInstance;
        }
        return oplusLocationListener;
    }

    public static OplusLocationListener create(Context context, Looper looper) {
        OplusLocationListener oplusLocationListener;
        synchronized (OplusLocationListener.class) {
            if (sInstance == null) {
                sInstance = new OplusLocationListener(context, looper);
            }
            oplusLocationListener = sInstance;
        }
        return oplusLocationListener;
    }

    private static HandlerThread getHandlerThread() {
        if (mHt == null) {
            HandlerThread handlerThread = new HandlerThread(TAG);
            mHt = handlerThread;
            handlerThread.start();
        }
        return mHt;
    }

    private String updateCityFromAdd(Address add) {
        String cityName = add != null ? add.getLocality() : "";
        if (cityName == null || cityName.length() == 0) {
            return cityName;
        }
        return OplusNrUtils.isChinese(cityName) ? OplusNrUtils.stringToUnicode(cityName.substring(0, cityName.length() - 1)) : cityName;
    }

    private String updateProvinceFromAdd(Address add) {
        String provinceName = add != null ? add.getAdminArea() : "";
        if (provinceName == null || provinceName.length() == 0) {
            return provinceName;
        }
        return OplusNrUtils.isChinese(provinceName) ? OplusNrUtils.stringToUnicode(provinceName.substring(0, provinceName.length() - 1)) : provinceName;
    }

    private Address updateAddFromLocation(Location location) {
        List<Address> addList = null;
        Address add = null;
        if (location != null) {
            try {
                addList = this.mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (addList != null && addList.size() > 0) {
            add = addList.get(0);
        }
        return add != null ? add : this.mAdd;
    }

    private Location getCurrLocation() {
        try {
            if (this.mLocationManager.isProviderEnabled(OlkConstants.EXT_NETWORK)) {
                Location location = this.mLocationManager.getLastKnownLocation(OlkConstants.EXT_NETWORK);
                return location;
            }
            logd("getCurrLocation do not support gps");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onInitailLocationInfo() {
        logd("onInitailLocationInfo : begin");
        this.mGeocoder = new Geocoder(this.mContext);
        this.mLocation = getCurrLocation();
        this.mHandler.sendEmptyMessage(204);
        requestLocationUpdates();
        logd("onInitailLocationInfo : end");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateScreenState() {
        DisplayManager dm = (DisplayManager) this.mContext.getSystemService("display");
        Display[] displays = dm.getDisplays();
        if (displays != null) {
            for (Display display : displays) {
                if (display.getState() == 2) {
                    this.mIsScreenOn = true;
                    return;
                }
            }
            this.mIsScreenOn = false;
            return;
        }
        logd("No displays found");
        this.mIsScreenOn = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerforScreenStateChanged() {
        logd("registerforScreenStateChanged");
        DisplayManager dm = (DisplayManager) this.mContext.getSystemService("display");
        dm.registerDisplayListener(this.mDisplayListener, null);
    }

    public String getProvince() {
        return this.mProvince;
    }

    public String getCity() {
        return this.mCity;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String msg) {
        Rlog.d(TAG, msg);
    }
}
