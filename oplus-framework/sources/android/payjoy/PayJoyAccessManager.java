package android.payjoy;

import android.content.Context;
import android.os.RemoteException;

/* loaded from: classes.dex */
public class PayJoyAccessManager {
    public static final int PAYJOY_CONTROL_STATE_FALSE = -1;
    public static final int PAYJOY_CONTROL_STATE_TRUE = 1;
    public static final int PAYJOY_CONTROL_STATE_UNKNOWN = 0;
    private IPayJoyAccessService mService;

    public static PayJoyAccessManager getInstance(Context context) {
        PayJoyAccessManager manager = (PayJoyAccessManager) context.getSystemService("payjoy_access_service");
        if (manager == null) {
            throw new RuntimeException("Failed to get PayJoyAccessService");
        }
        return manager;
    }

    public PayJoyAccessManager(IPayJoyAccessService service) {
        this.mService = service;
    }

    public String getVersion() {
        try {
            return this.mService.getVersion();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getPayJoyControlState() {
        try {
            return this.mService.getPayJoyControlState();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int setPayJoyControlState(int state) {
        try {
            return this.mService.setPayJoyControlState(state);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getPersistedImei(int slot) {
        try {
            return this.mService.getPersistedImei(slot);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean setPayJoyAppAsDeviceOwner(String packageName, String className, String ownerName) {
        try {
            return this.mService.setPayJoyAppAsDeviceOwner(packageName, className, ownerName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String[] getAppOpPermissionPackages(String permissionName) {
        try {
            return this.mService.getAppOpPermissionPackages(permissionName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void enableDebuggingFeature() {
        try {
            this.mService.enableDebuggingFeature();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void dumpPayJoyDataBlock() {
        try {
            this.mService.dumpPayJoyDataBlock();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void erasePayJoyDataBlock() {
        try {
            this.mService.erasePayJoyDataBlock();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getPayJoyDataVersion() {
        try {
            return this.mService.getPayJoyDataVersion();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getPayJoyDataLocation() {
        try {
            return this.mService.getPayJoyDataLocation();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void switchPayjoyLockStatus(int status) {
        try {
            this.mService.switchPayjoyLockStatus(status);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
