package com.oplus.hardware.devicecase;

import android.graphics.Rect;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.View;
import com.oplus.hardware.devicecase.IOplusDeviceCaseManager;
import com.oplus.hardware.devicecase.IOplusDeviceCaseStateCallback;
import com.oplus.hardware.devicecase.OplusDeviceCaseManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class OplusDeviceCaseManager {
    public static final int FLAG_CONTENT_IN_FULL_SCREEN = 1;
    public static final int FLAG_CONTENT_IN_VIEW_PORT = 0;
    public static final int STATE_CLOSE = 1;
    public static final int STATE_OPEN = 0;
    private static final String TAG = "OplusDeviceCaseManager";
    public static final int TYPE_CASE_PANEL = 2100;
    public static final int TYPE_SUB_CASE_PANEL = 2101;
    private final ArrayList<OplusDeviceCaseStateCallbackWrapper> mCallbacks;
    private IOplusDeviceCaseManager mDeviceCaseService;
    private final OplusDeviceCaseManagerGlobal mGlobal;
    private int mLastDeviceCaseState;
    private final Object mLock;
    private OplusDeviceCaseStateCallbackWrapper mOplusDeviceCaseStateCallbackWrapper;
    private DeviceCaseStateCallback mServerCallback;

    private OplusDeviceCaseManager() {
        this.mGlobal = OplusDeviceCaseManagerGlobal.getInstance();
        this.mLock = new Object();
        this.mCallbacks = new ArrayList<>();
        this.mLastDeviceCaseState = -1;
        try {
            this.mDeviceCaseService = IOplusDeviceCaseManager.Stub.asInterface(ServiceManager.getServiceOrThrow("oplus_devicecase"));
        } catch (ServiceManager.ServiceNotFoundException e) {
            this.mDeviceCaseService = null;
            Log.d(TAG, "device case manager service not found");
        }
    }

    /* loaded from: classes.dex */
    private static class LazyHolder {
        private static final OplusDeviceCaseManager INSTANCE = new OplusDeviceCaseManager();

        private LazyHolder() {
        }
    }

    public static OplusDeviceCaseManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    public boolean isSupported() {
        IOplusDeviceCaseManager iOplusDeviceCaseManager = this.mDeviceCaseService;
        if (iOplusDeviceCaseManager != null) {
            try {
                return iOplusDeviceCaseManager.isSupported();
            } catch (RemoteException e) {
                Log.d(TAG, "Remote exception in isSupported: ", e);
                return false;
            }
        }
        return false;
    }

    public boolean isEnabled() {
        IOplusDeviceCaseManager iOplusDeviceCaseManager = this.mDeviceCaseService;
        if (iOplusDeviceCaseManager != null) {
            try {
                return iOplusDeviceCaseManager.isEnabled();
            } catch (RemoteException e) {
                Log.d(TAG, "Remote exception in isEnabled: ", e);
                return false;
            }
        }
        return false;
    }

    public List<Rect> getViewPorts() {
        IOplusDeviceCaseManager iOplusDeviceCaseManager = this.mDeviceCaseService;
        if (iOplusDeviceCaseManager != null) {
            try {
                return iOplusDeviceCaseManager.getViewPorts();
            } catch (RemoteException e) {
                Log.d(TAG, "Remote exception in getViewPorts: ", e);
                return null;
            }
        }
        return null;
    }

    public void registerCallback(Executor executor, OplusDeviceCaseStateCallback callback) {
        if (callback == null || executor == null) {
            Log.e(TAG, "registerCallback invalid parameters");
            return;
        }
        Log.i(TAG, "registerCallback callback = " + callback);
        synchronized (this.mLock) {
            int index = findCallbackLocked(callback);
            if (index != -1) {
                return;
            }
            OplusDeviceCaseStateCallbackWrapper wrapper = new OplusDeviceCaseStateCallbackWrapper(executor, callback);
            this.mCallbacks.add(wrapper);
            int i = this.mLastDeviceCaseState;
            if (i != -1) {
                wrapper.notifyDeviceCaseStateChanged(i);
            }
            if (this.mDeviceCaseService != null && this.mServerCallback == null) {
                DeviceCaseStateCallback deviceCaseStateCallback = new DeviceCaseStateCallback();
                this.mServerCallback = deviceCaseStateCallback;
                try {
                    this.mDeviceCaseService.registerCallback(deviceCaseStateCallback);
                } catch (RemoteException e) {
                    this.mServerCallback = null;
                    synchronized (this.mLock) {
                        this.mCallbacks.clear();
                        Log.d(TAG, "Remote exception in registerCallback: ", e);
                    }
                }
            }
        }
    }

    public void unregisterCallback() {
        DeviceCaseStateCallback deviceCaseStateCallback;
        Log.i(TAG, "unregisterCallback() called");
        IOplusDeviceCaseManager iOplusDeviceCaseManager = this.mDeviceCaseService;
        if (iOplusDeviceCaseManager == null || (deviceCaseStateCallback = this.mServerCallback) == null) {
            return;
        }
        try {
            try {
                iOplusDeviceCaseManager.unregisterCallback(deviceCaseStateCallback);
                this.mServerCallback = null;
                synchronized (this.mLock) {
                    this.mCallbacks.clear();
                }
            } catch (RemoteException e) {
                Log.d(TAG, "Remote exception in unregisterCallback: ", e);
                this.mServerCallback = null;
                synchronized (this.mLock) {
                    this.mCallbacks.clear();
                }
            }
            this.mLastDeviceCaseState = -1;
        } catch (Throwable th) {
            this.mServerCallback = null;
            synchronized (this.mLock) {
                this.mCallbacks.clear();
                this.mLastDeviceCaseState = -1;
                throw th;
            }
        }
    }

    public void unregisterCallback(OplusDeviceCaseStateCallback callback) {
        boolean callbackEmpty;
        if (callback == null) {
            Log.e(TAG, "unregisterCallback, callback is null, do nothing");
            return;
        }
        Log.i(TAG, "unregisterCallback callback = " + callback);
        synchronized (this.mLock) {
            int indexToRemove = findCallbackLocked(callback);
            if (indexToRemove != -1) {
                this.mCallbacks.remove(indexToRemove);
            }
            callbackEmpty = this.mCallbacks.isEmpty();
        }
        if (callbackEmpty) {
            unregisterCallback();
        }
    }

    public void showContentView(View view) {
        showContentView(view, TYPE_SUB_CASE_PANEL, 0);
    }

    public void showContentView(View view, int flag) {
        showContentView(view, TYPE_SUB_CASE_PANEL, flag);
    }

    public void showContentView(View view, int type, int flag) {
        IOplusDeviceCaseManager iOplusDeviceCaseManager;
        if (isSupported() && isEnabled() && view != null && (iOplusDeviceCaseManager = this.mDeviceCaseService) != null) {
            try {
                List<Rect> viewPorts = iOplusDeviceCaseManager.getViewPorts();
                this.mGlobal.showContentView(view, type, flag, viewPorts);
            } catch (RemoteException e) {
                Log.d(TAG, "Remote exception in getViewPorts: ", e);
            }
        }
    }

    public void showContentView(List<View> views) {
        if (!isSupported() || !isEnabled() || views == null) {
            return;
        }
        Log.e(TAG, "current version not support multi view ports");
    }

    public void hideContentView(View view) {
        if (!isSupported() || !isEnabled() || view == null) {
            return;
        }
        this.mGlobal.hideContentView(view);
    }

    private int findCallbackLocked(OplusDeviceCaseStateCallback callback) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            if (this.mCallbacks.get(i).mOplusDeviceCaseStateCallback.equals(callback)) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class DeviceCaseStateCallback extends IOplusDeviceCaseStateCallback.Stub {
        private DeviceCaseStateCallback() {
        }

        @Override // com.oplus.hardware.devicecase.IOplusDeviceCaseStateCallback
        public void onStateChanged(int state) {
            synchronized (OplusDeviceCaseManager.this.mLock) {
                if (!OplusDeviceCaseManager.this.mCallbacks.isEmpty()) {
                    Iterator it = OplusDeviceCaseManager.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        OplusDeviceCaseStateCallbackWrapper wrapper = (OplusDeviceCaseStateCallbackWrapper) it.next();
                        wrapper.notifyDeviceCaseStateChanged(state);
                    }
                }
            }
            OplusDeviceCaseManager.this.mLastDeviceCaseState = state;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class OplusDeviceCaseStateCallbackWrapper {
        private final Executor mExecutor;
        private final OplusDeviceCaseStateCallback mOplusDeviceCaseStateCallback;

        OplusDeviceCaseStateCallbackWrapper(Executor executor, OplusDeviceCaseStateCallback callback) {
            this.mOplusDeviceCaseStateCallback = callback;
            this.mExecutor = executor;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyDeviceCaseStateChanged$0(int newState) {
            this.mOplusDeviceCaseStateCallback.onStateChanged(newState);
        }

        void notifyDeviceCaseStateChanged(final int newState) {
            this.mExecutor.execute(new Runnable() { // from class: com.oplus.hardware.devicecase.OplusDeviceCaseManager$OplusDeviceCaseStateCallbackWrapper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    OplusDeviceCaseManager.OplusDeviceCaseStateCallbackWrapper.this.lambda$notifyDeviceCaseStateChanged$0(newState);
                }
            });
        }
    }
}
