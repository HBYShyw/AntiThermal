package android.os;

import android.app.OplusActivityTaskManager;
import android.content.Context;
import android.os.IOplusKeyEventObserver;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusKeyEventManager {
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    public static final int INTERCEPT_ALWAYS = 0;
    public static final int INTERCEPT_ONCE = 1;
    public static final int LISTEN_ALL_KEY_EVENT = 0;
    public static final int LISTEN_APP_SWITCH_KEY_EVENT = 4096;
    public static final int LISTEN_BACK_KEY_EVENT = 32;
    public static final int LISTEN_BRIGHTNESS_DOWN_KEY_EVENT = 32768;
    public static final int LISTEN_BRIGHTNESS_UP_KEY_EVENT = 16384;
    public static final int LISTEN_CAMERA_KEY_EVENT = 128;
    public static final int LISTEN_ENDCALL_KEY_EVENT = 65536;
    public static final int LISTEN_F4_KEY_EVENT = 64;
    public static final int LISTEN_HEADSETHOOK_KEY_EVENT = 1024;
    public static final int LISTEN_HOME_KEY_EVENT = 16;
    public static final int LISTEN_MENU_KEY_EVENT = 8;
    public static final int LISTEN_POWER_KEY_EVENT = 1;
    public static final int LISTEN_SHOULDER_DOWN_KEY_EVENT = 33554432;
    public static final int LISTEN_SHOULDER_UP_KEY_EVENT = 67108864;
    public static final int LISTEN_SLEEP_KEY_EVENT = 131072;
    public static final int LISTEN_VOLUME_DOWN_KEY_EVENT = 4;
    public static final int LISTEN_VOLUME_MUTE_KEY_EVENT = 2048;
    public static final int LISTEN_VOLUME_UP_KEY_EVENT = 2;
    public static final int LISTEN_WAKEUP_KEY_EVENT = 8192;
    public static final String TAG = "OplusKeyEventManager";
    private static volatile OplusKeyEventManager sInstance;
    public int mVersion = 1;
    private final Map<OnKeyEventObserver, IOplusKeyEventObserver> mKeyEventObservers = new ArrayMap();
    private final Map<String, IOplusKeyEventObserver> mKeyEventInterceptors = new ArrayMap();
    private OplusActivityTaskManager mOAms = OplusActivityTaskManager.getInstance();

    /* loaded from: classes.dex */
    public interface OnKeyEventObserver {
        void onKeyEvent(KeyEvent keyEvent);
    }

    public static OplusKeyEventManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusKeyEventManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusKeyEventManager();
                }
            }
        }
        return sInstance;
    }

    private OplusKeyEventManager() {
    }

    public boolean registerKeyEventObserver(Context context, OnKeyEventObserver observer, int listenFlag) {
        if (observer == null || context == null) {
            Log.e(TAG, "context is null or observer is null, registerKeyEventObserver failed.");
            return false;
        }
        Log.i(TAG, "start registerKeyEventObserver, pkg: " + context.getPackageName());
        synchronized (this.mKeyEventObservers) {
            if (this.mKeyEventObservers.get(observer) != null) {
                Log.e(TAG, "already registered before");
                return false;
            }
            OnKeyEventObserverDelegate delegate = new OnKeyEventObserverDelegate(observer);
            try {
            } catch (RemoteException e) {
                Log.e(TAG, "registerKeyEventObserver RemoteException, err: " + e);
            }
            if (this.mOAms == null) {
                return false;
            }
            String observerFingerPrint = observer.hashCode() + context.getPackageName();
            boolean result = this.mOAms.registerKeyEventObserver(observerFingerPrint, delegate, listenFlag);
            if (result) {
                this.mKeyEventObservers.put(observer, delegate);
            }
            return result;
        }
    }

    public boolean unregisterKeyEventObserver(Context context, OnKeyEventObserver observer) {
        if (observer == null || context == null) {
            Log.e(TAG, "context is null or observer is null, unregisterKeyEventObserver failed.");
            return false;
        }
        Log.i(TAG, "start unregisterKeyEventObserver, pkg: " + context.getPackageName());
        synchronized (this.mKeyEventObservers) {
            IOplusKeyEventObserver delegate = this.mKeyEventObservers.get(observer);
            if (delegate != null) {
                try {
                    if (this.mOAms != null) {
                        this.mKeyEventObservers.remove(observer);
                        String observerFingerPrint = observer.hashCode() + context.getPackageName();
                        return this.mOAms.unregisterKeyEventObserver(observerFingerPrint);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "unregisterKeyEventObserver RemoteException, err: " + e);
                }
            }
            return false;
        }
    }

    public int getVersion() {
        return this.mVersion;
    }

    public boolean registerKeyEventInterceptor(Context context, String interceptorKey, OnKeyEventObserver observer, ArrayMap<Integer, Integer> configs) {
        OplusActivityTaskManager oplusActivityTaskManager;
        if (context == null || TextUtils.isEmpty(interceptorKey) || configs == null || configs.isEmpty()) {
            Log.e(TAG, "registerKeyEventInterceptor failed, interceptorKey: " + interceptorKey + ", configs: " + configs);
            return false;
        }
        Log.i(TAG, "start registerKeyEventInterceptor, pkg: " + context.getPackageName() + ", interceptorKey: " + interceptorKey);
        synchronized (this.mKeyEventInterceptors) {
            if (observer != null) {
                try {
                    if (this.mKeyEventInterceptors.containsKey(interceptorKey)) {
                        Log.e(TAG, "interceptor already registered before, interceptorFingerPrint: " + interceptorKey);
                        return false;
                    }
                } finally {
                }
            }
            OnKeyEventObserverDelegate delegate = null;
            if (observer != null) {
                delegate = new OnKeyEventObserverDelegate(observer);
            }
            try {
                oplusActivityTaskManager = this.mOAms;
            } catch (RemoteException e) {
                Log.e(TAG, "registerKeyEventInterceptor RemoteException, err: " + e);
            }
            if (oplusActivityTaskManager == null) {
                return false;
            }
            boolean result = oplusActivityTaskManager.registerKeyEventInterceptor(interceptorKey, delegate, configs);
            if (result) {
                this.mKeyEventInterceptors.put(interceptorKey, delegate);
            }
            return result;
        }
    }

    public boolean unregisterKeyEventInterceptor(Context context, String interceptorKey, OnKeyEventObserver observer) {
        if (context == null || TextUtils.isEmpty(interceptorKey)) {
            Log.e(TAG, "context is null, unregisterKeyEventInterceptor failed, interceptorKey: " + interceptorKey);
            return false;
        }
        Log.i(TAG, "start unregisterKeyEventInterceptor, pkg: " + context.getPackageName() + ", interceptorKey: " + interceptorKey);
        synchronized (this.mKeyEventInterceptors) {
            if (this.mKeyEventInterceptors.containsKey(interceptorKey)) {
                try {
                    if (this.mOAms != null) {
                        this.mKeyEventInterceptors.remove(interceptorKey);
                        boolean result = this.mOAms.unregisterKeyEventInterceptor(interceptorKey);
                        return result;
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "unregisterKeyEventInterceptor RemoteException, err: " + e);
                }
            }
            return false;
        }
    }

    /* loaded from: classes.dex */
    private class OnKeyEventObserverDelegate extends IOplusKeyEventObserver.Stub {
        private final OnKeyEventObserver mObserver;

        public OnKeyEventObserverDelegate(OnKeyEventObserver observer) {
            this.mObserver = observer;
        }

        @Override // android.os.IOplusKeyEventObserver
        public void onKeyEvent(KeyEvent event) {
            OnKeyEventObserver onKeyEventObserver = this.mObserver;
            if (onKeyEventObserver != null) {
                onKeyEventObserver.onKeyEvent(event);
            }
        }
    }
}
