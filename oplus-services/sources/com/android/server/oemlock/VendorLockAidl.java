package com.android.server.oemlock;

import android.content.Context;
import android.hardware.oemlock.IOemLock;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class VendorLockAidl extends OemLock {
    private static final String TAG = "OemLock";
    private IOemLock mOemLock = getOemLockHalService();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static IOemLock getOemLockHalService() {
        return IOemLock.Stub.asInterface(ServiceManager.waitForDeclaredService(IOemLock.DESCRIPTOR + "/default"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VendorLockAidl(Context context) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.oemlock.OemLock
    public String getLockName() {
        try {
            return this.mOemLock.getName();
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to get name from HAL", e);
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.oemlock.OemLock
    public void setOemUnlockAllowedByCarrier(boolean z, byte[] bArr) {
        int oemUnlockAllowedByCarrier;
        try {
            if (bArr == null) {
                oemUnlockAllowedByCarrier = this.mOemLock.setOemUnlockAllowedByCarrier(z, new byte[0]);
            } else {
                oemUnlockAllowedByCarrier = this.mOemLock.setOemUnlockAllowedByCarrier(z, bArr);
            }
            if (oemUnlockAllowedByCarrier == 0) {
                Slog.i(TAG, "Updated carrier allows OEM lock state to: " + z);
                return;
            }
            if (oemUnlockAllowedByCarrier != 1) {
                if (oemUnlockAllowedByCarrier == 2) {
                    if (bArr == null) {
                        throw new IllegalArgumentException("Signature required for carrier unlock");
                    }
                    throw new SecurityException("Invalid signature used in attempt to carrier unlock");
                }
                Slog.e(TAG, "Unknown return value indicates code is out of sync with HAL");
            }
            throw new RuntimeException("Failed to set carrier OEM unlock state");
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to set carrier state with HAL", e);
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.oemlock.OemLock
    public boolean isOemUnlockAllowedByCarrier() {
        try {
            return this.mOemLock.isOemUnlockAllowedByCarrier();
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to get carrier state from HAL");
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.oemlock.OemLock
    public void setOemUnlockAllowedByDevice(boolean z) {
        try {
            this.mOemLock.setOemUnlockAllowedByDevice(z);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to set device state with HAL", e);
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.oemlock.OemLock
    public boolean isOemUnlockAllowedByDevice() {
        try {
            return this.mOemLock.isOemUnlockAllowedByDevice();
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to get devie state from HAL");
            throw e.rethrowFromSystemServer();
        }
    }
}
