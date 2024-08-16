package android.os.customize;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.customize.IOplusCustomizeModuleControlManagerService;
import android.util.Slog;

/* loaded from: classes.dex */
public class OplusCustomizeModuleControlManager {
    private static final String SERVICE_NAME = "OplusCustomizeModuleControlManagerService";
    private static final String TAG = "OplusCustomizeModuleControlManager";
    private static final Object mLock = new Object();
    private static final Object mServiceLock = new Object();
    private static volatile OplusCustomizeModuleControlManager sInstance = null;
    private IOplusCustomizeModuleControlManagerService mOplusCustomizeModuleControlManagerService;

    private OplusCustomizeModuleControlManager() {
    }

    public static final OplusCustomizeModuleControlManager getInstance(Context context) {
        OplusCustomizeModuleControlManager oplusCustomizeModuleControlManager;
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new OplusCustomizeModuleControlManager();
                }
                oplusCustomizeModuleControlManager = sInstance;
            }
            return oplusCustomizeModuleControlManager;
        }
        return sInstance;
    }

    private IOplusCustomizeModuleControlManagerService getOplusCustomizeModuleControlManagerService() {
        IOplusCustomizeModuleControlManagerService iOplusCustomizeModuleControlManagerService;
        synchronized (mServiceLock) {
            if (this.mOplusCustomizeModuleControlManagerService == null) {
                this.mOplusCustomizeModuleControlManagerService = IOplusCustomizeModuleControlManagerService.Stub.asInterface(OplusCustomizeManager.getInstance().getDeviceManagerServiceByName(SERVICE_NAME));
            }
            iOplusCustomizeModuleControlManagerService = this.mOplusCustomizeModuleControlManagerService;
        }
        return iOplusCustomizeModuleControlManagerService;
    }

    public boolean getModuleControlStateByType(int moduleType) {
        boolean result = true;
        try {
            result = getOplusCustomizeModuleControlManagerService().getModuleControlStateByType(moduleType);
            Slog.d(TAG, "getApplicationControlState is: " + result);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getApplicationControlState", e);
            return result;
        } catch (Exception e2) {
            Slog.e(TAG, "getApplicationControlState fail", e2);
            return result;
        }
    }

    public boolean getModuleControlStateByTypeBundle(int moduleType, Bundle bundle) {
        boolean result = true;
        try {
            result = getOplusCustomizeModuleControlManagerService().getModuleControlStateByTypeBundle(moduleType, bundle);
            Slog.d(TAG, "getApplicationControlState is: " + result);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getApplicationControlState", e);
            return result;
        } catch (Exception e2) {
            Slog.e(TAG, "getApplicationControlState fail", e2);
            return result;
        }
    }

    public boolean getModuleControlStateByTypeAdmin(int moduleType, ComponentName admin, Bundle bundle) {
        boolean result = true;
        try {
            result = getOplusCustomizeModuleControlManagerService().getModuleControlStateByTypeAdmin(moduleType, admin, bundle);
            Slog.d(TAG, "getApplicationControlState is: " + result);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getApplicationControlState", e);
            return result;
        } catch (Exception e2) {
            Slog.e(TAG, "getApplicationControlState fail", e2);
            return result;
        }
    }
}
