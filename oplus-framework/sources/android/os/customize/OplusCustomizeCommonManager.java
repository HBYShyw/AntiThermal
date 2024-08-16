package android.os.customize;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.customize.IOplusCustomizeCommonManagerService;
import android.util.Slog;

/* loaded from: classes.dex */
public class OplusCustomizeCommonManager {
    private static final String SERVICE_NAME = "OplusCustomizeCommonManagerService";
    private static final String TAG = "OplusCustomizeCommonManager";
    private static final Object mLock = new Object();
    private static final Object mServiceLock = new Object();
    private static volatile OplusCustomizeCommonManager sInstance;
    private IOplusCustomizeCommonManagerService mOplusCustomizeCommonManagerService;

    private OplusCustomizeCommonManager() {
        getOplusCustomizeCommonManagerService();
    }

    public static final OplusCustomizeCommonManager getInstance(Context context) {
        OplusCustomizeCommonManager oplusCustomizeCommonManager;
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new OplusCustomizeCommonManager();
                }
                oplusCustomizeCommonManager = sInstance;
            }
            return oplusCustomizeCommonManager;
        }
        return sInstance;
    }

    private IOplusCustomizeCommonManagerService getOplusCustomizeCommonManagerService() {
        IOplusCustomizeCommonManagerService iOplusCustomizeCommonManagerService;
        synchronized (mServiceLock) {
            if (this.mOplusCustomizeCommonManagerService == null) {
                this.mOplusCustomizeCommonManagerService = IOplusCustomizeCommonManagerService.Stub.asInterface(OplusCustomizeManager.getInstance().getDeviceManagerServiceByName(SERVICE_NAME));
            }
            iOplusCustomizeCommonManagerService = this.mOplusCustomizeCommonManagerService;
        }
        return iOplusCustomizeCommonManagerService;
    }

    public void handleCmd(int cmd, Bundle param) {
        try {
            getOplusCustomizeCommonManagerService().handleCmd(cmd, param);
            Slog.d(TAG, "handleCmd: succeeded");
        } catch (RemoteException e) {
            Slog.e(TAG, "handleCmd fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "handleCmd fail!", e2);
        }
    }

    public Bundle handleCmdExt(int cmd, Bundle param) {
        Bundle bundle = new Bundle();
        try {
            bundle = getOplusCustomizeCommonManagerService().handleCmdExt(cmd, param);
            Slog.d(TAG, "handleCmdExt: succeeded");
            return bundle;
        } catch (RemoteException e) {
            Slog.e(TAG, "handleCmdExt fail!", e);
            return bundle;
        } catch (Exception e2) {
            Slog.e(TAG, "handleCmdExt fail!", e2);
            return bundle;
        }
    }

    public Bundle getPolicy(int cmd, Bundle param) {
        Bundle bundle = new Bundle();
        try {
            bundle = getOplusCustomizeCommonManagerService().getPolicy(cmd, param);
            Slog.d(TAG, "getPolicy: succeeded");
            return bundle;
        } catch (RemoteException e) {
            Slog.e(TAG, "getPolicy fail!", e);
            return bundle;
        } catch (Exception e2) {
            Slog.e(TAG, "getPolicy fail!", e2);
            return bundle;
        }
    }
}
