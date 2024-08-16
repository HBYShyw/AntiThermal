package com.oplus.content;

import android.content.pm.OplusPackageManager;
import android.os.Debug;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.ArrayMap;
import android.util.Log;
import com.oplus.content.IOplusFeatureActionObserver;
import com.oplus.content.IOplusFeatureConfigManager;
import com.oplus.content.IOplusFeatureMapObserver;
import com.oplus.content.IOplusFeatureObserver;
import java.util.List;

/* loaded from: classes.dex */
public class OplusFeatureConfigManager extends OplusBaseFeatureConfigManager implements IOplusFeatureConfigList {
    private static final int STACK_SIZE = 6;
    private static final String TAG = "OplusFeatureConfigManager";
    private static volatile OplusFeatureConfigManager sInstance = null;
    private OplusFeatureCache mCache;
    private final ArrayMap<OnFeatureActionObserver, IOplusFeatureActionObserver> mFeatureActionObservers;
    private final ArrayMap<Integer, ArrayMap<OnFeatureMapObserver, IOplusFeatureMapObserver>> mFeatureMapObservers;
    private final ArrayMap<OnFeatureObserver, IOplusFeatureObserver> mFeatureObservers;
    private OplusPackageManager mOplusPackageManager;

    /* loaded from: classes.dex */
    public interface OnFeatureActionObserver {
        void onFeaturesActionUpdate(String str, String str2, IOplusFeatureConfigManager.FeatureID featureID);
    }

    /* loaded from: classes.dex */
    public interface OnFeatureMapObserver {
        void onFeatureUpdate(List<String> list, IOplusFeatureConfigManager.FeatureID featureID);
    }

    /* loaded from: classes.dex */
    public interface OnFeatureObserver {
        void onFeatureUpdate(List<String> list);
    }

    private OplusFeatureConfigManager() {
        super("package");
        this.mFeatureObservers = new ArrayMap<>();
        this.mFeatureMapObservers = new ArrayMap<>();
        this.mFeatureActionObservers = new ArrayMap<>();
        this.mCache = new OplusFeatureCache();
        this.mOplusPackageManager = new OplusPackageManager();
    }

    @Deprecated
    public static OplusFeatureConfigManager getInstacne() {
        if (sInstance == null) {
            synchronized (OplusFeatureConfigManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusFeatureConfigManager();
                }
            }
        } else if (sInstance.mRemote == null) {
            IBinder binder = ServiceManager.getService(sInstance.mName);
            if (binder == null) {
                Log.e(TAG, "remote is still null");
            } else {
                Log.e(TAG, "remote is not null, update remote");
                sInstance.mRemote = binder;
            }
        }
        return sInstance;
    }

    public boolean isRemoteReady() {
        return this.mRemote != null;
    }

    public static OplusFeatureConfigManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusFeatureConfigManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusFeatureConfigManager();
                }
            }
        } else if (sInstance.mRemote == null) {
            IBinder binder = ServiceManager.getService(sInstance.mName);
            if (binder == null) {
                Log.e(TAG, "remote is still null");
            } else {
                Log.e(TAG, "remote is not null, update remote");
                sInstance.mRemote = binder;
            }
        }
        return sInstance;
    }

    public boolean hasFeatureIPC(String featureName) throws RemoteException {
        if (this.mRemote == null) {
            Log.e(TAG, "hasFeatureIPC " + featureName + " failed , pms not initialized." + Debug.getCallers(6));
            return false;
        }
        return this.mOplusPackageManager.hasFeatureIPC(featureName, IOplusFeatureConfigManager.FeatureID.STATIC_COMPONENT.ordinal());
    }

    public boolean hasFeatureIPC(String featureName, int featureID) throws RemoteException {
        if (this.mRemote == null) {
            Log.e(TAG, "hasFeatureIPC " + featureName + " ID " + featureID + " failed , pms not initialized." + Debug.getCallers(6));
            return false;
        }
        return this.mOplusPackageManager.hasFeatureIPC(featureName, featureID);
    }

    @Override // com.oplus.content.IOplusFeatureConfigManager
    public boolean hasFeature(String name) {
        return this.mCache.query(name);
    }

    @Override // com.oplus.content.IOplusFeatureConfigManager
    public boolean hasFeature(String name, IOplusFeatureConfigManager.FeatureID featureID) {
        return this.mCache.query(name, featureID.ordinal());
    }

    @Override // com.oplus.content.IOplusFeatureConfigManager
    public boolean enableFeature(String featureName) throws RemoteException {
        return this.mOplusPackageManager.enableFeature(featureName);
    }

    @Override // com.oplus.content.IOplusFeatureConfigManager
    public boolean disableFeature(String featureName) throws RemoteException {
        return this.mOplusPackageManager.disableFeature(featureName);
    }

    @Override // com.oplus.content.IOplusFeatureConfigManager
    public boolean enableFeature(String featureName, IOplusFeatureConfigManager.FeatureID featureID) throws RemoteException {
        return this.mOplusPackageManager.enableFeatureMap(featureName, featureID.ordinal());
    }

    @Override // com.oplus.content.IOplusFeatureConfigManager
    public boolean disableFeature(String featureName, IOplusFeatureConfigManager.FeatureID featureID) throws RemoteException {
        return this.mOplusPackageManager.disableFeatureMap(featureName, featureID.ordinal());
    }

    @Override // com.oplus.content.IOplusFeatureConfigManager
    public void notifyFeaturesUpdate(String action, String actionValue) throws RemoteException {
        this.mOplusPackageManager.notifyFeaturesUpdate(action, actionValue);
    }

    @Override // com.oplus.content.IOplusFeatureConfigManager
    public void notifyFeaturesUpdate(String action, String actionValue, IOplusFeatureConfigManager.FeatureID featureID) throws RemoteException {
        this.mOplusPackageManager.notifyFeaturesMapUpdate(action, actionValue, featureID.ordinal());
    }

    public boolean registerFeatureObserver(List<String> features, OnFeatureObserver observer) {
        if (observer == null) {
            Log.e(TAG, " registerFeatureObserver null observer");
            return false;
        }
        synchronized (this.mFeatureObservers) {
            if (this.mFeatureObservers.get(observer) != null) {
                Log.e(TAG, "already regiter before");
                return false;
            }
            OnFeatureObserverDelegate delegate = new OnFeatureObserverDelegate(observer);
            try {
                boolean result = registerFeatureObserverInner(features, delegate);
                if (result) {
                    this.mFeatureObservers.put(observer, delegate);
                }
                return result;
            } catch (RemoteException e) {
                Log.e(TAG, "registerFeatureObserver failed!!");
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean unregisterFeatureObserver(OnFeatureObserver observer) {
        if (observer == null) {
            Log.i(TAG, "unregisterFeatureObserver null observer");
            return false;
        }
        synchronized (this.mFeatureObservers) {
            IOplusFeatureObserver delegate = this.mFeatureObservers.get(observer);
            if (delegate != null) {
                try {
                    this.mFeatureObservers.remove(observer);
                    return unregisterFeatureObserverInner(delegate);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }

    private boolean registerFeatureObserverInner(List<String> features, IOplusFeatureObserver observer) throws RemoteException {
        return this.mOplusPackageManager.registerFeatureObserverInner(features, observer);
    }

    private boolean unregisterFeatureObserverInner(IOplusFeatureObserver observer) throws RemoteException {
        return this.mOplusPackageManager.unregisterFeatureObserverInner(observer);
    }

    /* loaded from: classes.dex */
    private class OnFeatureObserverDelegate extends IOplusFeatureObserver.Stub {
        private final OnFeatureObserver mObserver;

        public OnFeatureObserverDelegate(OnFeatureObserver observer) {
            this.mObserver = observer;
        }

        @Override // com.oplus.content.IOplusFeatureObserver
        public void onFeatureUpdate(List<String> featureList) {
            this.mObserver.onFeatureUpdate(featureList);
        }
    }

    public boolean registerFeatureObserver(List<String> features, IOplusFeatureConfigManager.FeatureID featureID, OnFeatureMapObserver observer) {
        if (observer == null) {
            Log.e(TAG, " registerFeatureMapObserver null observer");
            return false;
        }
        synchronized (this.mFeatureMapObservers) {
            ArrayMap<OnFeatureMapObserver, IOplusFeatureMapObserver> featureMapObservers = this.mFeatureMapObservers.get(Integer.valueOf(featureID.ordinal()));
            if (featureMapObservers == null) {
                featureMapObservers = new ArrayMap<>();
                this.mFeatureMapObservers.put(Integer.valueOf(featureID.ordinal()), featureMapObservers);
            }
            if (featureMapObservers.get(observer) != null) {
                Log.e(TAG, "already regiter before");
                return false;
            }
            OnFeatureMapObserverDelegate delegate = new OnFeatureMapObserverDelegate(observer);
            try {
                boolean result = registerFeatureObserverInner(features, featureID, delegate);
                if (result) {
                    featureMapObservers.put(observer, delegate);
                }
                return result;
            } catch (RemoteException e) {
                Log.e(TAG, "registerFeatureMapObserver failed!!");
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean unregisterFeatureObserver(IOplusFeatureConfigManager.FeatureID featureID, OnFeatureMapObserver observer) {
        if (observer == null) {
            Log.i(TAG, "unregisterFeatureMapObserver null observer");
            return false;
        }
        synchronized (this.mFeatureMapObservers) {
            ArrayMap<OnFeatureMapObserver, IOplusFeatureMapObserver> featureMapObservers = this.mFeatureMapObservers.get(Integer.valueOf(featureID.ordinal()));
            if (featureMapObservers == null) {
                Log.i(TAG, "unregisterFeatureMapObserver not register observer");
                return false;
            }
            IOplusFeatureMapObserver delegate = featureMapObservers.get(observer);
            if (delegate != null) {
                try {
                    featureMapObservers.remove(observer);
                    return unregisterFeatureObserverInner(featureID, delegate);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }

    private boolean registerFeatureObserverInner(List<String> featureList, IOplusFeatureConfigManager.FeatureID featureID, IOplusFeatureMapObserver observer) throws RemoteException {
        return this.mOplusPackageManager.registerFeatureMapObserverInner(featureList, featureID.ordinal(), observer);
    }

    private boolean unregisterFeatureObserverInner(IOplusFeatureConfigManager.FeatureID featureID, IOplusFeatureMapObserver observer) throws RemoteException {
        return this.mOplusPackageManager.unregisterFeatureMapObserverInner(featureID.ordinal(), observer);
    }

    /* loaded from: classes.dex */
    private class OnFeatureMapObserverDelegate extends IOplusFeatureMapObserver.Stub {
        private final OnFeatureMapObserver mObserver;

        public OnFeatureMapObserverDelegate(OnFeatureMapObserver observer) {
            this.mObserver = observer;
        }

        @Override // com.oplus.content.IOplusFeatureMapObserver
        public void onFeatureUpdate(List<String> featureList, int id) {
            if (id < IOplusFeatureConfigManager.FeatureID.STATIC_COMPONENT.ordinal() || id >= IOplusFeatureConfigManager.FeatureID.INVALID.ordinal()) {
                Log.i(OplusFeatureConfigManager.TAG, "invalid feature id" + id);
            } else {
                IOplusFeatureConfigManager.FeatureID featureID = IOplusFeatureConfigManager.FeatureID.values()[id];
                this.mObserver.onFeatureUpdate(featureList, featureID);
            }
        }
    }

    public boolean registerFeatureActionObserver(OnFeatureActionObserver observer) {
        if (observer == null) {
            Log.e(TAG, " registerFeatureActionObserver null observer");
            return false;
        }
        synchronized (this.mFeatureActionObservers) {
            if (this.mFeatureActionObservers.get(observer) != null) {
                Log.e(TAG, "already regiter before");
                return false;
            }
            OnFeatureActionObserverDelegate delegate = new OnFeatureActionObserverDelegate(observer);
            try {
                boolean result = registerFeatureActionObserverInner(delegate);
                if (result) {
                    this.mFeatureActionObservers.put(observer, delegate);
                }
                return result;
            } catch (RemoteException e) {
                Log.e(TAG, "registerFeatureActionObserver failed!!");
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean unregisterFeatureActionObserver(OnFeatureActionObserver observer) {
        if (observer == null) {
            Log.i(TAG, "unregisterFeatureActionObserver null observer");
            return false;
        }
        synchronized (this.mFeatureActionObservers) {
            IOplusFeatureActionObserver delegate = this.mFeatureActionObservers.get(observer);
            if (delegate != null) {
                try {
                    this.mFeatureActionObservers.remove(observer);
                    return unregisterFeatureActionObserverInner(delegate);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }

    private boolean registerFeatureActionObserverInner(IOplusFeatureActionObserver observer) throws RemoteException {
        return this.mOplusPackageManager.registerFeatureActionObserverInner(observer);
    }

    private boolean unregisterFeatureActionObserverInner(IOplusFeatureActionObserver observer) throws RemoteException {
        return this.mOplusPackageManager.unregisterFeatureActionObserverInner(observer);
    }

    /* loaded from: classes.dex */
    private class OnFeatureActionObserverDelegate extends IOplusFeatureActionObserver.Stub {
        private final OnFeatureActionObserver mObserver;

        public OnFeatureActionObserverDelegate(OnFeatureActionObserver observer) {
            this.mObserver = observer;
        }

        @Override // com.oplus.content.IOplusFeatureActionObserver
        public void onFeaturesActionUpdate(String action, String actionValue, int id) {
            if (id < IOplusFeatureConfigManager.FeatureID.STATIC_COMPONENT.ordinal() || id >= IOplusFeatureConfigManager.FeatureID.INVALID.ordinal()) {
                Log.i(OplusFeatureConfigManager.TAG, "invalid feature id" + id);
            } else {
                IOplusFeatureConfigManager.FeatureID featureID = IOplusFeatureConfigManager.FeatureID.values()[id];
                this.mObserver.onFeaturesActionUpdate(action, actionValue, featureID);
            }
        }
    }
}
