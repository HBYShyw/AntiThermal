package com.android.server.recoverysystem.hal;

import android.hardware.boot.IBootControl;
import android.hardware.boot.V1_0.CommandResult;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import java.util.NoSuchElementException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class BootControlHIDL implements IBootControl {
    private static final String TAG = "BootControlHIDL";
    final android.hardware.boot.V1_1.IBootControl v1_1_hal;
    final android.hardware.boot.V1_2.IBootControl v1_2_hal;
    final android.hardware.boot.V1_0.IBootControl v1_hal;

    public IBinder asBinder() {
        return null;
    }

    public int getInterfaceVersion() throws RemoteException {
        return 1;
    }

    public static boolean isServicePresent() {
        try {
            android.hardware.boot.V1_0.IBootControl.getService(true);
            return true;
        } catch (RemoteException | NoSuchElementException unused) {
            return false;
        }
    }

    public static boolean isV1_2ServicePresent() {
        try {
            android.hardware.boot.V1_2.IBootControl.getService(true);
            return true;
        } catch (RemoteException | NoSuchElementException unused) {
            return false;
        }
    }

    public static BootControlHIDL getService() throws RemoteException {
        android.hardware.boot.V1_0.IBootControl service = android.hardware.boot.V1_0.IBootControl.getService(true);
        return new BootControlHIDL(service, android.hardware.boot.V1_1.IBootControl.castFrom(service), android.hardware.boot.V1_2.IBootControl.castFrom(service));
    }

    private BootControlHIDL(android.hardware.boot.V1_0.IBootControl iBootControl, android.hardware.boot.V1_1.IBootControl iBootControl2, android.hardware.boot.V1_2.IBootControl iBootControl3) throws RemoteException {
        this.v1_hal = iBootControl;
        this.v1_1_hal = iBootControl2;
        this.v1_2_hal = iBootControl3;
        if (iBootControl == null) {
            throw new RemoteException("Failed to find V1.0 BootControl HIDL");
        }
        if (iBootControl3 != null) {
            Slog.i(TAG, "V1.2 version of BootControl HIDL HAL available, using V1.2");
        } else if (iBootControl2 != null) {
            Slog.i(TAG, "V1.1 version of BootControl HIDL HAL available, using V1.1");
        } else {
            Slog.i(TAG, "V1.0 version of BootControl HIDL HAL available, using V1.0");
        }
    }

    public int getActiveBootSlot() throws RemoteException {
        android.hardware.boot.V1_2.IBootControl iBootControl = this.v1_2_hal;
        if (iBootControl == null) {
            throw new RemoteException("getActiveBootSlot() requires V1.2 BootControl HAL");
        }
        return iBootControl.getActiveBootSlot();
    }

    public int getCurrentSlot() throws RemoteException {
        return this.v1_hal.getCurrentSlot();
    }

    public int getNumberSlots() throws RemoteException {
        return this.v1_hal.getNumberSlots();
    }

    public int getSnapshotMergeStatus() throws RemoteException {
        android.hardware.boot.V1_1.IBootControl iBootControl = this.v1_1_hal;
        if (iBootControl == null) {
            throw new RemoteException("getSnapshotMergeStatus() requires V1.1 BootControl HAL");
        }
        return iBootControl.getSnapshotMergeStatus();
    }

    public String getSuffix(int i) throws RemoteException {
        return this.v1_hal.getSuffix(i);
    }

    public boolean isSlotBootable(int i) throws RemoteException {
        int isSlotBootable = this.v1_hal.isSlotBootable(i);
        if (isSlotBootable != -1) {
            return isSlotBootable != 0;
        }
        throw new RemoteException("isSlotBootable() failed, Slot %d might be invalid.".formatted(Integer.valueOf(i)));
    }

    public boolean isSlotMarkedSuccessful(int i) throws RemoteException {
        int isSlotMarkedSuccessful = this.v1_hal.isSlotMarkedSuccessful(i);
        if (isSlotMarkedSuccessful != -1) {
            return isSlotMarkedSuccessful != 0;
        }
        throw new RemoteException("isSlotMarkedSuccessful() failed, Slot %d might be invalid.".formatted(Integer.valueOf(i)));
    }

    public void markBootSuccessful() throws RemoteException {
        CommandResult markBootSuccessful = this.v1_hal.markBootSuccessful();
        if (markBootSuccessful.success) {
            return;
        }
        throw new RemoteException("Error markBootSuccessful() " + markBootSuccessful.errMsg);
    }

    public void setActiveBootSlot(int i) throws RemoteException {
        CommandResult activeBootSlot = this.v1_hal.setActiveBootSlot(i);
        if (!activeBootSlot.success) {
            throw new RemoteException("Error setActiveBootSlot(%d) %s".formatted(Integer.valueOf(i), activeBootSlot.errMsg));
        }
    }

    public void setSlotAsUnbootable(int i) throws RemoteException {
        CommandResult slotAsUnbootable = this.v1_hal.setSlotAsUnbootable(i);
        if (!slotAsUnbootable.success) {
            throw new RemoteException("Error setSlotAsUnbootable(%d) %s".formatted(Integer.valueOf(i), slotAsUnbootable.errMsg));
        }
    }

    public void setSnapshotMergeStatus(int i) throws RemoteException {
        android.hardware.boot.V1_1.IBootControl iBootControl = this.v1_1_hal;
        if (iBootControl == null) {
            throw new RemoteException("getSnapshotMergeStatus() requires V1.1 BootControl HAL");
        }
        if (!iBootControl.setSnapshotMergeStatus(i)) {
            throw new RemoteException("Error setSnapshotMergeStatus(%d)".formatted(Integer.valueOf(i)));
        }
    }

    public String getInterfaceHash() throws RemoteException {
        return this.v1_hal.interfaceDescriptor();
    }
}
