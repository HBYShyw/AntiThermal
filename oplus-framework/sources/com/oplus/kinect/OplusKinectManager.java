package com.oplus.kinect;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.ArrayMap;
import android.util.Log;
import com.oplus.kinect.IOplusKinectObserver;
import com.oplus.kinect.IOplusKinectService;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusKinectManager {
    public static final int KINECT_FLIP = 2;
    public static final int KINECT_PICK_UP = 1;
    public static final String TAG = "OplusKinectManager";
    private static volatile OplusKinectManager sInstance;
    private static String sServiceName = "KinectService";
    private final Map<OplusKinectObserver, IOplusKinectObserver> mKinectObservers = new ArrayMap();
    private IOplusKinectService mKinectService;

    @Deprecated
    /* loaded from: classes.dex */
    public interface OplusKinectObserver {
        void onKinectDetected(int i);
    }

    @Deprecated
    public static String getServiceName() {
        return sServiceName;
    }

    @Deprecated
    public static OplusKinectManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusKinectManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusKinectManager();
                }
            }
        }
        return sInstance;
    }

    private OplusKinectManager() {
        verifyService();
    }

    private void verifyService() {
        try {
            if (this.mKinectService == null) {
                this.mKinectService = IOplusKinectService.Stub.asInterface(ServiceManager.getService(getServiceName()));
            }
        } catch (Exception e) {
            Log.e(TAG, "verifyService e : " + e);
        }
    }

    @Deprecated
    public boolean registerKinectObserver(Context context, OplusKinectObserver observer, int config) {
        IOplusKinectService iOplusKinectService;
        if (context == null || observer == null || config <= 0) {
            Log.e(TAG, "registerKinectObserver failed, invalid args:" + context + ", " + observer + ", " + config);
            return false;
        }
        synchronized (this.mKinectObservers) {
            if (this.mKinectObservers.get(observer) != null) {
                Log.e(TAG, "already registered before");
                return false;
            }
            OplusKinectObserverDelegate delegate = new OplusKinectObserverDelegate(observer);
            Log.i(TAG, "start registerKinectObserver:" + context.getPackageName() + ", delegate=" + delegate);
            try {
                verifyService();
                iOplusKinectService = this.mKinectService;
            } catch (RemoteException e) {
                Log.e(TAG, "registerKinectObserver RemoteException, err: " + e);
            }
            if (iOplusKinectService == null) {
                return false;
            }
            boolean result = iOplusKinectService.registerKinectObserver(delegate, config);
            if (result) {
                this.mKinectObservers.put(observer, delegate);
            }
            return result;
        }
    }

    @Deprecated
    public boolean unregisterKinectObserver(Context context, OplusKinectObserver observer) {
        if (context == null || observer == null) {
            Log.e(TAG, "unregisterKinectObserver failed, invalid args:" + context + ", " + observer);
            return false;
        }
        synchronized (this.mKinectObservers) {
            IOplusKinectObserver delegate = this.mKinectObservers.get(observer);
            Log.i(TAG, "start unregisterKinectObserver:" + context.getPackageName() + ", delegate=" + delegate);
            if (delegate != null) {
                try {
                    verifyService();
                    if (this.mKinectService != null) {
                        this.mKinectObservers.remove(observer);
                        return this.mKinectService.unregisterKinectObserver(delegate);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "unregisterKinectObserver RemoteException, err: " + e);
                }
            }
            return false;
        }
    }

    /* loaded from: classes.dex */
    private class OplusKinectObserverDelegate extends IOplusKinectObserver.Stub {
        private final OplusKinectObserver mObserver;

        public OplusKinectObserverDelegate(OplusKinectObserver observer) {
            this.mObserver = observer;
        }

        @Override // com.oplus.kinect.IOplusKinectObserver
        public void onKinectDetected(int type) {
            OplusKinectObserver oplusKinectObserver = this.mObserver;
            if (oplusKinectObserver != null) {
                oplusKinectObserver.onKinectDetected(type);
            }
        }
    }
}
