package com.oplus.globalgesture;

import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.IOplusExService;
import android.os.OplusExManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.ArrayMap;
import android.util.Log;
import com.oplus.globalgesture.IOplusGlobalGestureObserver;
import com.oplus.neuron.NsConstants;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusGlobalGestureManager {
    public static final int ACTION_ABORT = 3;
    public static final int ACTION_DOWN = 0;
    public static final int ACTION_MOVE = 2;
    public static final int ACTION_UP = 1;
    public static final int GESTURE_FIVE_FINGERS = 8;
    public static final int GESTURE_FORE_FINGERS = 4;
    public static final int GESTURE_MULTI_FINGERS_WITH_MOVE = 268435456;
    public static final int GESTURE_THREE_FINGERS = 2;
    public static final int GESTURE_TWO_FINGERS = 1;
    public static final String TAG = "OplusGlobalGestureManager";
    private static volatile OplusGlobalGestureManager sInstance;
    private static String sServiceName = OplusExManager.SERVICE_NAME;
    private static String sBundleKeyReason = NsConstants.REASON;
    private static String sBundleKeyMotion = "MotionEvent";
    private final Map<OplusGlobalGestureObserver, IOplusGlobalGestureObserver> mGlobalGestureObservers = new ArrayMap();
    private IOplusExService mExService = null;

    /* loaded from: classes.dex */
    public interface OplusGlobalGestureObserver {
        void onGestureDetected(int i, Bundle bundle);
    }

    public static String getServiceName() {
        return sServiceName;
    }

    public static String getBundleKeyReason() {
        return sBundleKeyReason;
    }

    public static String getBundleKeyMotion() {
        return sBundleKeyMotion;
    }

    public static OplusGlobalGestureManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusGlobalGestureManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusGlobalGestureManager();
                }
            }
        }
        return sInstance;
    }

    private OplusGlobalGestureManager() {
        verifyService();
    }

    private void verifyService() {
        try {
            if (this.mExService == null) {
                this.mExService = IOplusExService.Stub.asInterface(ServiceManager.getService(getServiceName()));
            }
        } catch (Exception e) {
            Log.e(TAG, "verifyService e : " + e);
        }
    }

    public boolean registerGestureObserver(Context context, OplusGlobalGestureObserver observer, int config, List<RectF> validRegion) {
        IOplusExService iOplusExService;
        if (context == null || observer == null || config <= 0) {
            Log.e(TAG, "registerGestureObserver failed, invalid args:" + context + ", " + observer + ", " + config);
            return false;
        }
        synchronized (this.mGlobalGestureObservers) {
            if (this.mGlobalGestureObservers.get(observer) != null) {
                Log.e(TAG, "already registered before");
                return false;
            }
            OplusGlobalGestureObserverDelegate delegate = new OplusGlobalGestureObserverDelegate(observer);
            Log.i(TAG, "start registerGestureObserver:" + context.getPackageName() + ", delegate=" + delegate);
            try {
                verifyService();
                iOplusExService = this.mExService;
            } catch (RemoteException e) {
                Log.e(TAG, "registerGestureObserver RemoteException, err: " + e);
            }
            if (iOplusExService == null) {
                return false;
            }
            boolean result = iOplusExService.registerGlobalGestureObserver(delegate, context.getPackageName(), config, validRegion);
            if (result) {
                this.mGlobalGestureObservers.put(observer, delegate);
            }
            return result;
        }
    }

    public boolean unregisterGestureObserver(Context context, OplusGlobalGestureObserver observer) {
        if (context == null || observer == null) {
            Log.e(TAG, "unregisterGestureObserver failed, invalid args:" + context + ", " + observer);
            return false;
        }
        synchronized (this.mGlobalGestureObservers) {
            IOplusGlobalGestureObserver delegate = this.mGlobalGestureObservers.get(observer);
            Log.i(TAG, "start unregisterGestureObserver:" + context.getPackageName() + ", delegate=" + delegate);
            if (delegate != null) {
                try {
                    verifyService();
                    if (this.mExService != null) {
                        this.mGlobalGestureObservers.remove(observer);
                        return this.mExService.unregisterGlobalGestureObserver(delegate);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "unregisterGestureObserver RemoteException, err: " + e);
                }
            }
            return false;
        }
    }

    /* loaded from: classes.dex */
    private class OplusGlobalGestureObserverDelegate extends IOplusGlobalGestureObserver.Stub {
        private final OplusGlobalGestureObserver mObserver;

        public OplusGlobalGestureObserverDelegate(OplusGlobalGestureObserver observer) {
            this.mObserver = observer;
        }

        @Override // com.oplus.globalgesture.IOplusGlobalGestureObserver
        public void onGestureDetected(int type, Bundle extras) {
            OplusGlobalGestureObserver oplusGlobalGestureObserver = this.mObserver;
            if (oplusGlobalGestureObserver != null) {
                oplusGlobalGestureObserver.onGestureDetected(type, extras);
            }
        }
    }
}
