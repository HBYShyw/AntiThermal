package android.os.customize;

import android.content.Context;
import android.os.RemoteException;
import android.os.customize.IOplusCustomizeInputMethodManagerService;
import android.util.Slog;

/* loaded from: classes.dex */
public class OplusCustomizeInputMethodManager {
    private static final String SERVICE_NAME = "OplusCustomizeInputMethodManagerService";
    private static final String TAG = "OplusCustomizeInputMethodManager";
    private static final Object mLock = new Object();
    private static final Object mServiceLock = new Object();
    private static volatile OplusCustomizeInputMethodManager sInstance;
    private IOplusCustomizeInputMethodManagerService mIOplusCustomizeInputMethodManagerService;

    private OplusCustomizeInputMethodManager() {
        getOplusCustomizeInputMethodManagerService();
    }

    private IOplusCustomizeInputMethodManagerService getOplusCustomizeInputMethodManagerService() {
        IOplusCustomizeInputMethodManagerService iOplusCustomizeInputMethodManagerService;
        synchronized (mServiceLock) {
            if (this.mIOplusCustomizeInputMethodManagerService == null) {
                this.mIOplusCustomizeInputMethodManagerService = IOplusCustomizeInputMethodManagerService.Stub.asInterface(OplusCustomizeManager.getInstance().getDeviceManagerServiceByName(SERVICE_NAME));
            }
            if (this.mIOplusCustomizeInputMethodManagerService == null) {
                Slog.e(TAG, "mIOplusCustomizeInputMethodService is null");
            }
            iOplusCustomizeInputMethodManagerService = this.mIOplusCustomizeInputMethodManagerService;
        }
        return iOplusCustomizeInputMethodManagerService;
    }

    public static final OplusCustomizeInputMethodManager getInstance(Context context) {
        OplusCustomizeInputMethodManager oplusCustomizeInputMethodManager;
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new OplusCustomizeInputMethodManager();
                }
                oplusCustomizeInputMethodManager = sInstance;
            }
            return oplusCustomizeInputMethodManager;
        }
        return sInstance;
    }

    public boolean setDefaultInputMethod(String packageName) {
        try {
            return getOplusCustomizeInputMethodManagerService().setDefaultInputMethod(packageName);
        } catch (RemoteException e) {
            Slog.d(TAG, "setDefaultInputMethod: fail");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setDefaultInputMethod Error");
            return false;
        }
    }

    public String getDefaultInputMethod() {
        try {
            return getOplusCustomizeInputMethodManagerService().getDefaultInputMethod();
        } catch (RemoteException e) {
            Slog.d(TAG, "getDefaultInputMethod: fail");
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getDefaultInputMethod Error");
            return null;
        }
    }

    public boolean clearDefaultInputMethod() {
        try {
            return getOplusCustomizeInputMethodManagerService().clearDefaultInputMethod();
        } catch (RemoteException e) {
            Slog.d(TAG, "clearDefaultInputMethod: fail");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "clearDefaultInputMethod Error");
            return false;
        }
    }
}
