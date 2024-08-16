package com.android.server.oemlock;

import android.content.Context;
import android.hardware.oemlock.V1_0.IOemLock;
import android.os.RemoteException;
import android.util.Slog;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class VendorLockHidl extends OemLock {
    private static final String TAG = "OemLock";
    private Context mContext;
    private IOemLock mOemLock = getOemLockHalService();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static IOemLock getOemLockHalService() {
        try {
            return IOemLock.getService(true);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        } catch (NoSuchElementException unused) {
            Slog.i(TAG, "OemLock Hidl HAL not present on device");
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VendorLockHidl(Context context) {
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.oemlock.OemLock
    public String getLockName() {
        final String[] strArr = new String[1];
        final Integer[] numArr = new Integer[1];
        try {
            this.mOemLock.getName(new IOemLock.getNameCallback() { // from class: com.android.server.oemlock.VendorLockHidl$$ExternalSyntheticLambda0
                public final void onValues(int i, String str) {
                    VendorLockHidl.lambda$getLockName$0(numArr, strArr, i, str);
                }
            });
            int intValue = numArr[0].intValue();
            if (intValue == 0) {
                return strArr[0];
            }
            if (intValue == 1) {
                Slog.e(TAG, "Failed to get OEM lock name.");
                return null;
            }
            Slog.e(TAG, "Unknown return value indicates code is out of sync with HAL");
            return null;
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to get name from HAL", e);
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getLockName$0(Integer[] numArr, String[] strArr, int i, String str) {
        numArr[0] = Integer.valueOf(i);
        strArr[0] = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.oemlock.OemLock
    public void setOemUnlockAllowedByCarrier(boolean z, byte[] bArr) {
        try {
            ArrayList<Byte> byteArrayList = toByteArrayList(bArr);
            int oemUnlockAllowedByCarrier = this.mOemLock.setOemUnlockAllowedByCarrier(z, byteArrayList);
            if (oemUnlockAllowedByCarrier == 0) {
                Slog.i(TAG, "Updated carrier allows OEM lock state to: " + z);
                return;
            }
            if (oemUnlockAllowedByCarrier != 1) {
                if (oemUnlockAllowedByCarrier == 2) {
                    if (byteArrayList.isEmpty()) {
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
        final Boolean[] boolArr = new Boolean[1];
        final Integer[] numArr = new Integer[1];
        try {
            this.mOemLock.isOemUnlockAllowedByCarrier(new IOemLock.isOemUnlockAllowedByCarrierCallback() { // from class: com.android.server.oemlock.VendorLockHidl$$ExternalSyntheticLambda2
                public final void onValues(int i, boolean z) {
                    VendorLockHidl.lambda$isOemUnlockAllowedByCarrier$1(numArr, boolArr, i, z);
                }
            });
            int intValue = numArr[0].intValue();
            if (intValue == 0) {
                return boolArr[0].booleanValue();
            }
            if (intValue != 1) {
                Slog.e(TAG, "Unknown return value indicates code is out of sync with HAL");
            }
            throw new RuntimeException("Failed to get carrier OEM unlock state");
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to get carrier state from HAL");
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$isOemUnlockAllowedByCarrier$1(Integer[] numArr, Boolean[] boolArr, int i, boolean z) {
        numArr[0] = Integer.valueOf(i);
        boolArr[0] = Boolean.valueOf(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.oemlock.OemLock
    public void setOemUnlockAllowedByDevice(boolean z) {
        try {
            int oemUnlockAllowedByDevice = this.mOemLock.setOemUnlockAllowedByDevice(z);
            if (oemUnlockAllowedByDevice == 0) {
                Slog.i(TAG, "Updated device allows OEM lock state to: " + z);
                return;
            }
            if (oemUnlockAllowedByDevice != 1) {
                Slog.e(TAG, "Unknown return value indicates code is out of sync with HAL");
            }
            throw new RuntimeException("Failed to set device OEM unlock state");
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to set device state with HAL", e);
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.oemlock.OemLock
    public boolean isOemUnlockAllowedByDevice() {
        final Boolean[] boolArr = new Boolean[1];
        final Integer[] numArr = new Integer[1];
        try {
            this.mOemLock.isOemUnlockAllowedByDevice(new IOemLock.isOemUnlockAllowedByDeviceCallback() { // from class: com.android.server.oemlock.VendorLockHidl$$ExternalSyntheticLambda1
                public final void onValues(int i, boolean z) {
                    VendorLockHidl.lambda$isOemUnlockAllowedByDevice$2(numArr, boolArr, i, z);
                }
            });
            int intValue = numArr[0].intValue();
            if (intValue == 0) {
                return boolArr[0].booleanValue();
            }
            if (intValue != 1) {
                Slog.e(TAG, "Unknown return value indicates code is out of sync with HAL");
            }
            throw new RuntimeException("Failed to get device OEM unlock state");
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to get devie state from HAL");
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$isOemUnlockAllowedByDevice$2(Integer[] numArr, Boolean[] boolArr, int i, boolean z) {
        numArr[0] = Integer.valueOf(i);
        boolArr[0] = Boolean.valueOf(z);
    }

    private ArrayList<Byte> toByteArrayList(byte[] bArr) {
        if (bArr == null) {
            return new ArrayList<>();
        }
        ArrayList<Byte> arrayList = new ArrayList<>(bArr.length);
        for (byte b : bArr) {
            arrayList.add(Byte.valueOf(b));
        }
        return arrayList;
    }
}
