package com.oplus.network;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import com.oplus.network.IOlkEventChange;
import com.oplus.network.IOlkInternalCallback;
import com.oplus.network.OlkServiceConnector;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class OlkManager {
    private static final long DELAY_INIT_TIME = 10000;
    public static final String SRV_NAME = "opluslinkkit";
    public static final String TAG = "OlkManager";
    private static OlkManager sInstance;
    public Context mContext;
    private Handler mHandler;
    private volatile IOlk mOlkService;
    private boolean showDebug = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private OlkServiceConnector.ConnectorListener mConnectorListener = new OlkServiceConnector.ConnectorListener() { // from class: com.oplus.network.OlkManager.1
        @Override // com.oplus.network.OlkServiceConnector.ConnectorListener
        public void onServiceConnected(IOlk service) {
            OlkManager.this.mOlkService = service;
            try {
                OlkManager.this.log("OlkManager() : registerInit");
                OlkManager.this.mOlkService.registerInternalCallback(OlkManager.this.mInternal);
            } catch (RemoteException e) {
                Log.e(OlkManager.TAG, "onServiceConnected Oops!! register fail, this is unexpected!!");
            }
        }
    };
    private IOlkCallback mCacheCallback = null;
    private final ArrayList<OlkListener> setApStateList = new ArrayList<>();
    private final ArrayList<OlkListener> setApBandwidthList = new ArrayList<>();
    private final ArrayList<OlkListener> setSocketPriorityList = new ArrayList<>();
    private final ArrayList<OlkListener> clearSocketPriorityList = new ArrayList<>();
    private final ArrayList<OlkListener> setRealTimeList = new ArrayList<>();
    private final ArrayList<OlkListener> updateCellularEnableList = new ArrayList<>();
    private EventChangeCb mEventChanged = new EventChangeCb();
    private InternalCallback mInternal = new InternalCallback();

    /* loaded from: classes.dex */
    public interface IOlkCallback {
        void onChanged(Bundle bundle);
    }

    public static OlkManager getInstance(Context c) {
        OlkManager olkManager;
        synchronized (OlkManager.class) {
            if (sInstance == null) {
                sInstance = new OlkManager(c);
                Log.d(TAG, "OlkManager first new!");
            }
            olkManager = sInstance;
        }
        return olkManager;
    }

    private void checkOlkService() {
        if (this.mOlkService == null) {
            Log.e(TAG, "mOlkService is null");
        }
    }

    protected OlkManager(Context context) {
        this.mContext = context;
        OlkServiceConnector.create(this.mContext).connect(this.mConnectorListener);
    }

    protected void finalize() {
        log("finalize()");
        try {
            if (this.mOlkService != null) {
                this.mOlkService.unregisterEventChange(this.mEventChanged);
                this.mOlkService.unregisterInternalCallback(this.mInternal);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "unregister fail!");
        }
    }

    public Bundle request(Bundle bundle) {
        log("request() pkg = " + getOpPackageName());
        Bundle b = new Bundle();
        try {
            checkOlkService();
            if (this.mOlkService == null) {
                b.putInt("result", 500);
                return b;
            }
            return this.mOlkService.request(bundle);
        } catch (RemoteException e) {
            Log.e(TAG, "request fail!");
            b.putInt("result", 500);
            return b;
        }
    }

    public int registerCallback(int event, IOlkCallback cb) {
        log("registerCallback() event = " + event + ", pkg = " + getOpPackageName());
        if (cb == null) {
            return 400;
        }
        try {
            checkOlkService();
            if (this.mOlkService == null) {
                return 500;
            }
            int ret = this.mOlkService.registerEventChange(event, this.mEventChanged);
            if (ret != 0) {
                return ret;
            }
            this.mCacheCallback = cb;
            return 0;
        } catch (Exception e) {
            Log.e(TAG, "registerEventChange exception:" + e.getMessage());
            return 500;
        }
    }

    public int unregisterCallback(IOlkCallback cb) {
        log("unregisterCallback() pkg = " + getOpPackageName());
        if (cb != this.mCacheCallback) {
            return 400;
        }
        try {
            if (this.mOlkService != null) {
                this.mOlkService.unregisterEventChange(this.mEventChanged);
            }
            this.mCacheCallback = null;
            return 0;
        } catch (Exception e) {
            Log.e(TAG, "unregisterEventChange failed!" + e.getMessage());
            return 500;
        }
    }

    /* loaded from: classes.dex */
    private class EventChangeCb extends IOlkEventChange.Stub {
        private EventChangeCb() {
        }

        @Override // com.oplus.network.IOlkEventChange
        public void onChanged(Bundle data) {
            if (OlkManager.this.mCacheCallback != null) {
                Log.d(OlkManager.TAG, "IOlkEventChange data:" + data);
                OlkManager.this.mCacheCallback.onChanged(data);
            }
        }
    }

    public String getVersion() {
        String version = "0.0";
        checkOlkService();
        log("getVersion() pkg = " + getOpPackageName());
        if (this.mOlkService == null) {
            Log.e(TAG, "getVersion() mOlkService is null");
            return "0.0";
        }
        try {
            version = this.mOlkService.getVersion();
            Log.d(TAG, "getVersion() version = " + version);
            return version;
        } catch (RemoteException e) {
            Log.e(TAG, "getVersion fail!");
            return version;
        }
    }

    private String getOpPackageName() {
        Context context = this.mContext;
        if (context != null) {
            return context.getOpPackageName();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String content) {
        boolean debug = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        if (debug) {
            Log.d(TAG, content);
        }
    }

    /* loaded from: classes.dex */
    public interface OlkListener {
        default void setApStateCallback(String pkgName, boolean delay) {
        }

        default void setApBandwithCallback(String pkgName, int rxBw, int txBw) {
        }

        default void setSocketPriorityCallback(String pkgName, OlkStreamParam param) {
        }

        default void clearSocketPriorityCallback(String pkgName, OlkStreamParam param) {
        }

        default void setRealTimeEventCallback(String pkgName, int event) {
        }

        default void setUpdateCellularEnable(String pkgName, boolean enable) {
        }
    }

    /* loaded from: classes.dex */
    private class InternalCallback extends IOlkInternalCallback.Stub {
        private InternalCallback() {
        }

        @Override // com.oplus.network.IOlkInternalCallback
        public void olkSetApStateCallback(String pkgName, boolean delay) throws RemoteException {
            synchronized (OlkManager.this.setApStateList) {
                OlkManager.this.log("setApStateList len = " + OlkManager.this.setApStateList.size());
                OlkManager.this.log("InternalCallback() olkSetApStateCallback() pkgName = " + pkgName + ", delay = " + delay);
                Iterator it = OlkManager.this.setApStateList.iterator();
                while (it.hasNext()) {
                    OlkListener listener = (OlkListener) it.next();
                    listener.setApStateCallback(pkgName, delay);
                }
            }
        }

        @Override // com.oplus.network.IOlkInternalCallback
        public void olkSetApBandwithCallback(String pkgName, int rxBw, int txBw) throws RemoteException {
            synchronized (OlkManager.this.setApBandwidthList) {
                OlkManager.this.log("setApBandwidthList len = " + OlkManager.this.setApBandwidthList.size());
                OlkManager.this.log("InternalCallback() olkSetApBandwithCallback() pkgName = " + pkgName + ", rxBw = " + rxBw + ", txBw = " + txBw);
                Iterator it = OlkManager.this.setApBandwidthList.iterator();
                while (it.hasNext()) {
                    OlkListener listener = (OlkListener) it.next();
                    listener.setApBandwithCallback(pkgName, rxBw, txBw);
                }
            }
        }

        @Override // com.oplus.network.IOlkInternalCallback
        public void olkSetSocketPriorityCallback(String pkgName, OlkStreamParam param) throws RemoteException {
            synchronized (OlkManager.this.setSocketPriorityList) {
                OlkManager.this.log("setSocketPriorityList len = " + OlkManager.this.setSocketPriorityList.size());
                OlkManager.this.log("InternalCallback() olkSetSocketPriorityCallback() pkgName = " + pkgName + ",  param = " + param);
                Iterator it = OlkManager.this.setSocketPriorityList.iterator();
                while (it.hasNext()) {
                    OlkListener listener = (OlkListener) it.next();
                    listener.setSocketPriorityCallback(pkgName, param);
                }
            }
        }

        @Override // com.oplus.network.IOlkInternalCallback
        public void olkClearSocketPriorityCallback(String pkgName, OlkStreamParam param) throws RemoteException {
            synchronized (OlkManager.this.clearSocketPriorityList) {
                OlkManager.this.log("clearSocketPriorityList len = " + OlkManager.this.clearSocketPriorityList.size());
                OlkManager.this.log("InternalCallback() olkClearSocketPriorityCallback() pkgName = " + pkgName + ", param = " + param);
                Iterator it = OlkManager.this.clearSocketPriorityList.iterator();
                while (it.hasNext()) {
                    OlkListener listener = (OlkListener) it.next();
                    listener.clearSocketPriorityCallback(pkgName, param);
                }
            }
        }

        @Override // com.oplus.network.IOlkInternalCallback
        public void olkSetRealTimeEventCallback(String pkgName, int event) throws RemoteException {
            synchronized (OlkManager.this.setRealTimeList) {
                OlkManager.this.log("setRealTimeList len = " + OlkManager.this.setRealTimeList.size());
                OlkManager.this.log("InternalCallback() olkSetRealTimeEventCallback() pkgName = " + pkgName + ", event = " + event);
                Iterator it = OlkManager.this.setRealTimeList.iterator();
                while (it.hasNext()) {
                    OlkListener listener = (OlkListener) it.next();
                    listener.setRealTimeEventCallback(pkgName, event);
                }
            }
        }

        @Override // com.oplus.network.IOlkInternalCallback
        public void olkUpdateCellularEnable(String pkgName, boolean enable) throws RemoteException {
            synchronized (OlkManager.this.updateCellularEnableList) {
                OlkManager.this.log("updateCellularEnableList len = " + OlkManager.this.updateCellularEnableList.size());
                OlkManager.this.log("InternalCallback() olkUpdateCellularEnable() pkgName = " + pkgName + ", event = " + enable);
                Iterator it = OlkManager.this.updateCellularEnableList.iterator();
                while (it.hasNext()) {
                    OlkListener listener = (OlkListener) it.next();
                    listener.setUpdateCellularEnable(pkgName, enable);
                }
            }
        }
    }

    public void registerInterfaceCallback(int event, OlkListener cb) {
        log("registerInterfaceCallback() event = " + event + ", pkg = " + getOpPackageName());
        checkOlkService();
        if ((event & 1) != 0) {
            synchronized (this.setApStateList) {
                if (!this.setApStateList.contains(cb)) {
                    this.setApStateList.add(cb);
                }
            }
        }
        if ((event & 2) != 0) {
            synchronized (this.setApBandwidthList) {
                if (!this.setApBandwidthList.contains(cb)) {
                    this.setApBandwidthList.add(cb);
                }
            }
        }
        if ((event & 4) != 0) {
            synchronized (this.setSocketPriorityList) {
                if (!this.setSocketPriorityList.contains(cb)) {
                    this.setSocketPriorityList.add(cb);
                }
            }
        }
        if ((event & 8) != 0) {
            synchronized (this.clearSocketPriorityList) {
                if (!this.clearSocketPriorityList.contains(cb)) {
                    this.clearSocketPriorityList.add(cb);
                }
            }
        }
        if ((event & 32) != 0) {
            synchronized (this.setRealTimeList) {
                if (!this.setRealTimeList.contains(cb)) {
                    this.setRealTimeList.add(cb);
                }
            }
        }
        if ((event & 64) != 0) {
            synchronized (this.updateCellularEnableList) {
                if (!this.updateCellularEnableList.contains(cb)) {
                    this.updateCellularEnableList.add(cb);
                }
            }
        }
    }

    public void unregisterInterfaceCallback(OlkListener cb) {
        log("unregisterInterfaceCallback() pkg = " + getOpPackageName());
        synchronized (this.setApStateList) {
            if (this.setApStateList.contains(cb)) {
                this.setApStateList.remove(cb);
            }
        }
        synchronized (this.setApBandwidthList) {
            if (this.setApBandwidthList.contains(cb)) {
                this.setApBandwidthList.remove(cb);
            }
        }
        synchronized (this.setSocketPriorityList) {
            if (this.setSocketPriorityList.contains(cb)) {
                this.setSocketPriorityList.remove(cb);
            }
        }
        synchronized (this.clearSocketPriorityList) {
            if (this.clearSocketPriorityList.contains(cb)) {
                this.clearSocketPriorityList.remove(cb);
            }
        }
        synchronized (this.setRealTimeList) {
            if (this.setRealTimeList.contains(cb)) {
                this.setRealTimeList.remove(cb);
            }
        }
    }
}
