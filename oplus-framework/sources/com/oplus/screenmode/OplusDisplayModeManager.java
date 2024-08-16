package com.oplus.screenmode;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import com.oplus.screenmode.IOplusScreenMode;

/* loaded from: classes.dex */
public class OplusDisplayModeManager {
    public static final int DEBUG_CALLER_DEPTH = 8;
    private static final String OPLUS_SCREENMODE_SERVICE_NAME = "oplusscreenmode";
    private static final String TAG = "OplusDisplayModeManager";
    private boolean mInitialized;
    private IOplusScreenMode mScreenModeService;
    private Binder mToken;
    private static final boolean mOplusDebug = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    public static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.compat.debug", false);

    /* loaded from: classes.dex */
    private static class InstanceHolder {
        static final OplusDisplayModeManager INSTANCE = new OplusDisplayModeManager();

        private InstanceHolder() {
        }
    }

    public static OplusDisplayModeManager getInstance() {
        OplusDisplayModeManager instance = InstanceHolder.INSTANCE;
        return instance;
    }

    private OplusDisplayModeManager() {
        this.mScreenModeService = null;
        this.mToken = null;
        this.mInitialized = false;
        init();
    }

    private void init() {
        if (DEBUG) {
            Log.d(TAG, "OplusDisplayModeManager init.");
        }
        synchronized (OplusDisplayModeManager.class) {
            if (this.mInitialized) {
                Log.e(TAG, " mInitialized= " + this.mInitialized);
                return;
            }
            Trace.traceBegin(8L, "initoppposcreenmode");
            if (this.mScreenModeService == null) {
                IBinder binder = ServiceManager.getService(OPLUS_SCREENMODE_SERVICE_NAME);
                if (binder == null) {
                    Log.e(TAG, "failed to get oppposcreenmode service:binder null");
                } else {
                    this.mScreenModeService = IOplusScreenMode.Stub.asInterface(binder);
                }
            }
            boolean z = true;
            this.mInitialized = true;
            if (mOplusDebug) {
                StringBuilder append = new StringBuilder().append(" init end ").append(this.mInitialized).append(",service == null ? ");
                if (this.mScreenModeService != null) {
                    z = false;
                }
                Log.d(TAG, append.append(z).toString());
            }
            Trace.traceEnd(8L);
        }
    }

    public void enterPSMode(boolean enter) {
        enterPSModeOnRate(enter, 7);
    }

    public void enterPSModeOnRate(boolean enter, int rate) {
        if (enter && this.mToken == null) {
            this.mToken = new Binder();
        }
        enterPSModeOnRateWithToken(enter, rate, this.mToken);
    }

    public boolean setHighTemperatureStatus(int status, int rate) {
        Log.d(TAG, "High Temperature Status " + status + "," + rate);
        IOplusScreenMode iOplusScreenMode = this.mScreenModeService;
        if (iOplusScreenMode != null) {
            try {
                return iOplusScreenMode.setHighTemperatureStatus(status, rate);
            } catch (RemoteException e) {
                Log.e(TAG, "requestRefreshRate failed!", e);
                return false;
            }
        }
        if (mOplusDebug) {
            Log.d(TAG, "setHighTemperatureStatus service unavailable!!");
        }
        return false;
    }

    public boolean setAppOverrideRefreshRate(String pkg, int settingMode, int rate) {
        Log.d(TAG, "User Override RefreshRate " + pkg + "," + rate);
        IOplusScreenMode iOplusScreenMode = this.mScreenModeService;
        if (iOplusScreenMode != null) {
            try {
                return iOplusScreenMode.setAppOverrideRefreshRate(pkg, settingMode, rate);
            } catch (RemoteException e) {
                Log.e(TAG, "setAppOverrideRefreshRate failed!", e);
                return false;
            }
        }
        if (mOplusDebug) {
            Log.d(TAG, "setAppOverrideRefreshRate service unavailable!!");
        }
        return false;
    }

    public int getAppOverrideRefreshRate(String pkg, int settingMode) {
        IOplusScreenMode iOplusScreenMode = this.mScreenModeService;
        if (iOplusScreenMode != null) {
            try {
                return iOplusScreenMode.getAppOverrideRefreshRate(pkg, settingMode);
            } catch (RemoteException e) {
                Log.e(TAG, "setAppOverrideRefreshRate failed!", e);
                return 0;
            }
        }
        if (mOplusDebug) {
            Log.d(TAG, "setAppOverrideRefreshRate service unavailable!!");
        }
        return 0;
    }

    public Bundle getAppOverrideRefreshRateList() {
        IOplusScreenMode iOplusScreenMode = this.mScreenModeService;
        if (iOplusScreenMode != null) {
            try {
                return iOplusScreenMode.getAppOverrideRefreshRateList();
            } catch (RemoteException e) {
                Log.e(TAG, "setAppOverrideRefreshRate failed!", e);
                return null;
            }
        }
        if (mOplusDebug) {
            Log.d(TAG, "setAppOverrideRefreshRate service unavailable!!");
        }
        return null;
    }

    public boolean removeCustomizeRefreshRate(String pkg) {
        IOplusScreenMode iOplusScreenMode = this.mScreenModeService;
        if (iOplusScreenMode != null) {
            try {
                return iOplusScreenMode.removeCustomizeRefreshRate(pkg);
            } catch (RemoteException e) {
                Log.e(TAG, "removeCustomizeRefreshRate failed!", e);
                return false;
            }
        }
        if (mOplusDebug) {
            Log.d(TAG, "removeCustomizeRefreshRate service unavailable!!");
        }
        return false;
    }

    public boolean removeAllCustomizeRefreshRate() {
        IOplusScreenMode iOplusScreenMode = this.mScreenModeService;
        if (iOplusScreenMode != null) {
            try {
                return iOplusScreenMode.removeAllCustomizeRefreshRate();
            } catch (RemoteException e) {
                Log.e(TAG, "removeAllCustomizeRefreshRate failed!", e);
                return false;
            }
        }
        if (mOplusDebug) {
            Log.d(TAG, "removeAllCustomizeRefreshRate service unavailable!!");
        }
        return false;
    }

    public void enterPSModeOnRateWithToken(boolean enter, int rate, Binder token) {
        Log.d(TAG, " performance spec mode, " + enter + "," + rate);
        IOplusScreenMode iOplusScreenMode = this.mScreenModeService;
        if (iOplusScreenMode != null) {
            try {
                iOplusScreenMode.enterPSModeOnRateWithToken(enter, rate, token);
                return;
            } catch (RemoteException e) {
                Log.e(TAG, "performance spec mode failed!", e);
                return;
            }
        }
        Log.w(TAG, "performance spec mode service unavailable!!");
    }

    public void overrideWindowRefreshRate(IBinder window, int refreshRateId) {
        IOplusScreenMode iOplusScreenMode = this.mScreenModeService;
        if (iOplusScreenMode != null) {
            try {
                iOplusScreenMode.overrideWindowRefreshRate(window, refreshRateId);
            } catch (RemoteException e) {
                Log.e(TAG, "performance spec mode failed!", e);
            }
        }
    }

    public boolean isDisplayCompat(String packageName, int uid) {
        if (DEBUG) {
            Log.d(TAG, " isDisplayCompat, pkg:" + packageName);
        }
        IOplusScreenMode iOplusScreenMode = this.mScreenModeService;
        if (iOplusScreenMode != null) {
            try {
                return iOplusScreenMode.isDisplayCompat(packageName, uid);
            } catch (RemoteException e) {
                Log.e(TAG, "isDisplayCompat failed!", e);
                return false;
            }
        }
        Log.w(TAG, "isDisplayCompat service unavailable!!");
        return false;
    }
}
