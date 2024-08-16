package android.os;

import android.os.IOplusExInputCallBack;
import android.os.IOplusExService;
import android.os.IOplusGestureCallBack;
import android.util.ArrayMap;
import android.util.Log;
import android.view.InputEvent;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusGestureMonitorManager {
    private static final String TAG = "OplusGestureMonitorManager";
    public static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static volatile OplusGestureMonitorManager sInstance = null;
    private IOplusExService mExService = null;
    private final Map<OnPointerEventObserver, IOplusExInputCallBack> mPointersEventObservers = new ArrayMap();
    private final Map<OnGestureObserver, IOplusGestureCallBack> mGestureObservers = new ArrayMap();

    /* loaded from: classes.dex */
    public interface OnGestureObserver {
        void onDealGesture(int i);
    }

    /* loaded from: classes.dex */
    public interface OnPointerEventObserver {
        void onInputEvent(InputEvent inputEvent);
    }

    public static OplusGestureMonitorManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusGestureMonitorManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusGestureMonitorManager();
                }
            }
        }
        return sInstance;
    }

    private OplusGestureMonitorManager() {
        checkExService();
    }

    public boolean registerInputEvent(OnPointerEventObserver observer) {
        if (DEBUG) {
            Log.i(TAG, "start registerInputEvent");
        }
        if (observer == null) {
            Log.e(TAG, "observer is null, registerInputEvent failed");
            return false;
        }
        synchronized (this.mPointersEventObservers) {
            if (this.mPointersEventObservers.get(observer) != null) {
                Log.e(TAG, "OnPointerEventObserver already register before");
                return false;
            }
            checkExService();
            IOplusExInputCallBack delegate = new OnPointerEventObserverDelegate(observer);
            try {
                boolean result = this.mExService.registerInputEvent(delegate);
                if (result) {
                    this.mPointersEventObservers.put(observer, delegate);
                    Log.d(TAG, "registerInputEvent succeed, " + this.mPointersEventObservers.size() + " observer left.");
                }
                return result;
            } catch (RemoteException e) {
                Log.e(TAG, "registerInputEvent failed, e: " + e);
                return false;
            }
        }
    }

    public boolean registerRawInputEvent(OnPointerEventObserver observer) {
        if (DEBUG) {
            Log.i(TAG, "start registerRawInputEvent");
        }
        if (observer == null) {
            Log.e(TAG, "observer is null, registerRawInputEvent failed");
            return false;
        }
        synchronized (this.mPointersEventObservers) {
            if (this.mPointersEventObservers.get(observer) != null) {
                Log.e(TAG, "raw OnPointerEventObserver already register before");
                return false;
            }
            checkExService();
            IOplusExInputCallBack delegate = new OnPointerEventObserverDelegate(observer);
            try {
                boolean result = this.mExService.registerRawInputEvent(delegate);
                if (result) {
                    this.mPointersEventObservers.put(observer, delegate);
                    Log.d(TAG, "registerRawInputEvent succeed, " + this.mPointersEventObservers.size() + " observer left.");
                }
                return result;
            } catch (RemoteException e) {
                Log.e(TAG, "registerRawInputEvent failed, e: " + e);
                return false;
            }
        }
    }

    public void unregisterInputEvent(OnPointerEventObserver observer) {
        if (DEBUG) {
            Log.i(TAG, "start unregisterInputEvent");
        }
        if (observer == null) {
            Log.e(TAG, "observer is null, unregisterInputEvent failed");
            return;
        }
        synchronized (this.mPointersEventObservers) {
            IOplusExInputCallBack delegate = this.mPointersEventObservers.get(observer);
            if (delegate != null) {
                checkExService();
                try {
                    this.mExService.unregisterInputEvent(delegate);
                    this.mPointersEventObservers.remove(observer);
                    Log.d(TAG, "unregisterInputEvent succeed, " + this.mPointersEventObservers.size() + " observer left.");
                } catch (RemoteException e) {
                    Log.e(TAG, "unregisterInputEvent failed, e: " + e);
                }
            }
        }
    }

    public void pauseExInputEvent() {
        checkExService();
        IOplusExService iOplusExService = this.mExService;
        if (iOplusExService != null) {
            try {
                iOplusExService.pauseExInputEvent();
            } catch (RemoteException e) {
                Log.e(TAG, "pauseExInputEvent failed, e: " + e);
            }
        }
    }

    public void resumeExInputEvent() {
        checkExService();
        IOplusExService iOplusExService = this.mExService;
        if (iOplusExService != null) {
            try {
                iOplusExService.resumeExInputEvent();
            } catch (RemoteException e) {
                Log.e(TAG, "resumeExInputEvent failed, e: " + e);
            }
        }
    }

    public boolean registerScreenoffGesture(OnGestureObserver observer) {
        if (DEBUG) {
            Log.i(TAG, "start registerScreenoffGesture");
        }
        if (observer == null) {
            Log.e(TAG, "observer is null, registerScreenoffGesture failed");
            return false;
        }
        synchronized (this.mGestureObservers) {
            if (this.mGestureObservers.get(observer) != null) {
                Log.e(TAG, "OnGestureObserver already register before");
                return false;
            }
            checkExService();
            IOplusGestureCallBack delegate = new OnGestureObserverDelegate(observer);
            try {
                boolean result = this.mExService.registerScreenoffGesture(delegate);
                if (result) {
                    this.mGestureObservers.put(observer, delegate);
                    Log.d(TAG, "registerScreenoffGesture succeed, " + this.mGestureObservers.size() + " observer left.");
                }
                return result;
            } catch (RemoteException e) {
                Log.e(TAG, "registerScreenoffGesture failed, e: " + e);
                return false;
            }
        }
    }

    public void unregisterScreenoffGesture(OnGestureObserver observer) {
        if (DEBUG) {
            Log.i(TAG, "start unregisterScreenoffGesture");
        }
        if (observer == null) {
            Log.e(TAG, "observer is null, unregisterScreenoffGesture failed");
            return;
        }
        synchronized (this.mGestureObservers) {
            IOplusGestureCallBack delegate = this.mGestureObservers.get(observer);
            if (delegate != null) {
                checkExService();
                try {
                    this.mExService.unregisterScreenoffGesture(delegate);
                    this.mGestureObservers.remove(observer);
                    Log.d(TAG, "unregisterScreenoffGesture succeed, " + this.mGestureObservers.size() + " observer left.");
                } catch (RemoteException e) {
                    Log.e(TAG, "unregisterScreenoffGesture failed, e: " + e);
                }
            }
        }
    }

    public void dealScreenoffGesture(int nGesture) {
        checkExService();
        IOplusExService iOplusExService = this.mExService;
        if (iOplusExService != null) {
            try {
                iOplusExService.dealScreenoffGesture(nGesture);
            } catch (RemoteException e) {
                Log.e(TAG, "dealScreenoffGesture failed, e: " + e);
            }
        }
    }

    public void setGestureState(int nGesture, boolean isOpen) {
        checkExService();
        IOplusExService iOplusExService = this.mExService;
        if (iOplusExService != null) {
            try {
                iOplusExService.setGestureState(nGesture, isOpen);
            } catch (RemoteException e) {
                Log.e(TAG, "setGestureState failed, e: " + e);
            }
        }
    }

    public boolean getGestureState(int nGesture) {
        checkExService();
        IOplusExService iOplusExService = this.mExService;
        if (iOplusExService != null) {
            try {
                return iOplusExService.getGestureState(nGesture);
            } catch (RemoteException e) {
                Log.e(TAG, "getGestureState failed, e: " + e);
                return false;
            }
        }
        return false;
    }

    public void pilferPointers() {
        checkExService();
        IOplusExService iOplusExService = this.mExService;
        if (iOplusExService != null) {
            try {
                iOplusExService.pilferPointers();
            } catch (RemoteException e) {
                Log.e(TAG, "pilferPointers failed, e: " + e);
            }
        }
    }

    private void checkExService() {
        if (this.mExService == null) {
            this.mExService = IOplusExService.Stub.asInterface(ServiceManager.getService(OplusExManager.SERVICE_NAME));
        }
    }

    /* loaded from: classes.dex */
    private class OnPointerEventObserverDelegate extends IOplusExInputCallBack.Stub {
        private OnPointerEventObserver mObserver;

        public OnPointerEventObserverDelegate(OnPointerEventObserver observer) {
            if (OplusGestureMonitorManager.DEBUG) {
                Log.d("Binder", "new OnPointerEventObserverDelegate");
            }
            this.mObserver = observer;
        }

        @Override // android.os.IOplusExInputCallBack
        public void onInputEvent(InputEvent event) {
            this.mObserver.onInputEvent(event);
        }
    }

    /* loaded from: classes.dex */
    private class OnGestureObserverDelegate extends IOplusGestureCallBack.Stub {
        private OnGestureObserver mObserver;

        public OnGestureObserverDelegate(OnGestureObserver observer) {
            if (OplusGestureMonitorManager.DEBUG) {
                Log.d("Binder", "new OnGestureObserverDelegate");
            }
            this.mObserver = observer;
        }

        @Override // android.os.IOplusGestureCallBack
        public void onDealGesture(int nGesture) {
            this.mObserver.onDealGesture(nGesture);
        }
    }
}
