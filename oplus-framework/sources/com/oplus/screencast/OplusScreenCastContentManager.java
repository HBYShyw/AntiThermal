package com.oplus.screencast;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import com.oplus.screencast.IOplusScreenCastStateObserver;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusScreenCastContentManager extends OplusBaseScreenCastContentManager {
    private static final boolean CAST_DEBUG = true;
    public static final int FLAG_MASK = 65535;
    public static final int FLAG_POWER_SAVE = 1;
    public static final int FLAG_PRIVACY_PROTECTION = 16;
    public static final int MODE_MASK = -65536;
    public static final int MODE_MIRROR_CAST = 16777216;
    public static final int MODE_SINGLE_APP_CAST = 33554432;
    private static final String TAG = "OplusScreenCastContentManager";
    private static volatile OplusScreenCastContentManager sInstance;
    private final Map<OnScreenCastStateObserver, IOplusScreenCastStateObserver> mScreencastStateObservers;

    /* loaded from: classes.dex */
    public interface OnScreenCastStateObserver {
        void onContentConfigChanged(OplusScreenCastInfo oplusScreenCastInfo);
    }

    public static OplusScreenCastContentManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusScreenCastContentManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusScreenCastContentManager();
                }
            }
        }
        return sInstance;
    }

    private OplusScreenCastContentManager() {
        super("activity_task");
        this.mScreencastStateObservers = new ArrayMap();
    }

    @Override // com.oplus.screencast.IOplusScreenCastContentManager
    public boolean requestScreenCastContentMode(int castMode, int castFlag) throws RemoteException {
        return requestScreenCastContentMode(castMode, castFlag, null);
    }

    @Override // com.oplus.screencast.IOplusScreenCastContentManager
    public boolean requestScreenCastContentMode(int castMode, int castFlag, Bundle options) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusScreenCastContentManager.DESCRIPTOR);
            data.writeInt(castMode);
            data.writeInt(castFlag);
            if (options != null) {
                data.writeInt(1);
                data.writeBundle(options);
            } else {
                data.writeInt(0);
            }
            this.mRemote.transact(IOplusScreenCastContentManager.REQUEST_SCREEN_CAST_MODE, data, reply, 0);
            reply.readException();
            boolean success = Boolean.valueOf(reply.readString()).booleanValue();
            return success;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @Override // com.oplus.screencast.IOplusScreenCastContentManager
    public void resetScreenCastContentMode() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusScreenCastContentManager.DESCRIPTOR);
            this.mRemote.transact(IOplusScreenCastContentManager.RESET_SCREEN_CAST_MODE, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @Override // com.oplus.screencast.IOplusScreenCastContentManager
    public OplusScreenCastInfo getScreenCastContentMode() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusScreenCastContentManager.DESCRIPTOR);
            this.mRemote.transact(IOplusScreenCastContentManager.GET_SCREEN_CAST_MODE, data, reply, 0);
            reply.readException();
            OplusScreenCastInfo info = OplusScreenCastInfo.CREATOR.createFromParcel(reply);
            return info;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean registerScreenCastStateObserver(Context context, OnScreenCastStateObserver observer) {
        Log.i(TAG, "registerScreenCastStateObserver observer = " + observer);
        if (observer == null || context == null) {
            return false;
        }
        synchronized (this.mScreencastStateObservers) {
            if (this.mScreencastStateObservers.get(observer) != null) {
                Log.e(TAG, "already register before");
                return false;
            }
            OnScreenCastStateObserverDelegate delegate = new OnScreenCastStateObserverDelegate(observer);
            try {
                boolean result = registerScreenCastStateObserverInner(context.getPackageName(), delegate);
                if (result) {
                    this.mScreencastStateObservers.put(observer, delegate);
                }
                return result;
            } catch (RemoteException e) {
                Log.e(TAG, "registerScreenCastStateObserver remoteException ");
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean unregisterScreenCastStateObserver(Context context, OnScreenCastStateObserver observer) {
        Log.i(TAG, "unregisterScreenCastStateObserver observer = " + observer);
        if (observer == null || context == null) {
            return false;
        }
        synchronized (this.mScreencastStateObservers) {
            IOplusScreenCastStateObserver delegate = this.mScreencastStateObservers.get(observer);
            if (delegate != null) {
                try {
                    this.mScreencastStateObservers.remove(observer);
                    return unregisterScreenCastStateObserverInner(context.getPackageName(), delegate);
                } catch (RemoteException e) {
                    Log.e(TAG, "unregisterScreenCastStateObserver remoteException ");
                    e.printStackTrace();
                }
            }
            return false;
        }
    }

    private boolean registerScreenCastStateObserverInner(String pkgName, IOplusScreenCastStateObserver observer) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusScreenCastContentManager.DESCRIPTOR);
            data.writeString(pkgName);
            data.writeStrongBinder(observer != null ? observer.asBinder() : null);
            this.mRemote.transact(IOplusScreenCastContentManager.REGISTER_SCREEN_CAST_OBSERVER, data, reply, 0);
            reply.readException();
            boolean result = Boolean.valueOf(reply.readString()).booleanValue();
            return result;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    private boolean unregisterScreenCastStateObserverInner(String pkgName, IOplusScreenCastStateObserver observer) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IOplusScreenCastContentManager.DESCRIPTOR);
            data.writeString(pkgName);
            data.writeStrongBinder(observer != null ? observer.asBinder() : null);
            this.mRemote.transact(IOplusScreenCastContentManager.UNREGISTER_SCREEN_CAST_OBSERVER, data, reply, 0);
            reply.readException();
            boolean result = Boolean.valueOf(reply.readString()).booleanValue();
            return result;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    /* loaded from: classes.dex */
    private class OnScreenCastStateObserverDelegate extends IOplusScreenCastStateObserver.Stub {
        private final OnScreenCastStateObserver mObserver;

        public OnScreenCastStateObserverDelegate(OnScreenCastStateObserver observer) {
            this.mObserver = observer;
        }

        @Override // com.oplus.screencast.IOplusScreenCastStateObserver
        public void onContentConfigChanged(OplusScreenCastInfo info) {
            Log.d(OplusScreenCastContentManager.TAG, "onContentConfigChanged info = " + info);
            this.mObserver.onContentConfigChanged(info);
        }
    }
}
