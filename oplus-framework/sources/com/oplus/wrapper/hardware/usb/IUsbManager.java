package com.oplus.wrapper.hardware.usb;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.hardware.usb.IDisplayPortAltModeInfoListener;
import android.hardware.usb.IUsbManager;
import android.hardware.usb.IUsbOperationInternal;
import android.hardware.usb.ParcelableUsbPort;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbPortStatus;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.UserHandle;
import java.util.List;

/* loaded from: classes.dex */
public interface IUsbManager {
    void grantAccessoryPermission(UsbAccessory usbAccessory, int i) throws RemoteException;

    void setAccessoryPackage(UsbAccessory usbAccessory, String str, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IUsbManager {
        private final android.hardware.usb.IUsbManager mTarget = new IUsbManager.Stub() { // from class: com.oplus.wrapper.hardware.usb.IUsbManager.Stub.1
            public void getDeviceList(Bundle bundle) throws RemoteException {
            }

            public ParcelFileDescriptor openDevice(String s, String s1) throws RemoteException {
                return null;
            }

            public UsbAccessory getCurrentAccessory() throws RemoteException {
                return null;
            }

            public ParcelFileDescriptor openAccessory(UsbAccessory usbAccessory) throws RemoteException {
                return null;
            }

            public void setDevicePackage(UsbDevice usbDevice, String s, int i) throws RemoteException {
            }

            public boolean hasAccessoryPermissionWithIdentity(UsbAccessory accessory, int pid, int uid) throws RemoteException {
                return false;
            }

            public void setAccessoryPackage(UsbAccessory usbAccessory, String s, int i) throws RemoteException {
                Stub.this.setAccessoryPackage(usbAccessory, s, i);
            }

            public void addDevicePackagesToPreferenceDenied(UsbDevice usbDevice, String[] strings, UserHandle userHandle) throws RemoteException {
            }

            public void addAccessoryPackagesToPreferenceDenied(UsbAccessory usbAccessory, String[] strings, UserHandle userHandle) throws RemoteException {
            }

            public void removeDevicePackagesFromPreferenceDenied(UsbDevice usbDevice, String[] strings, UserHandle userHandle) throws RemoteException {
            }

            public void removeAccessoryPackagesFromPreferenceDenied(UsbAccessory usbAccessory, String[] strings, UserHandle userHandle) throws RemoteException {
            }

            public boolean hasDevicePermissionWithIdentity(UsbDevice device, String packageName, int pid, int uid) throws RemoteException {
                return false;
            }

            public void setDevicePersistentPermission(UsbDevice usbDevice, int i, UserHandle userHandle, boolean b) throws RemoteException {
            }

            public void setAccessoryPersistentPermission(UsbAccessory usbAccessory, int i, UserHandle userHandle, boolean b) throws RemoteException {
            }

            public boolean hasDevicePermission(UsbDevice usbDevice, String s) throws RemoteException {
                return false;
            }

            public boolean hasAccessoryPermission(UsbAccessory usbAccessory) throws RemoteException {
                return false;
            }

            public void setCurrentFunctions(long functions, int operationId) throws RemoteException {
            }

            public void setCurrentFunction(String function, boolean usbDataUnlocked, int operationId) throws RemoteException {
            }

            public void requestDevicePermission(UsbDevice usbDevice, String s, PendingIntent pendingIntent) throws RemoteException {
            }

            public void requestAccessoryPermission(UsbAccessory usbAccessory, String s, PendingIntent pendingIntent) throws RemoteException {
            }

            public void grantDevicePermission(UsbDevice usbDevice, int i) throws RemoteException {
            }

            public void grantAccessoryPermission(UsbAccessory usbAccessory, int i) throws RemoteException {
                Stub.this.grantAccessoryPermission(usbAccessory, i);
            }

            public boolean hasDefaults(String s, int i) throws RemoteException {
                return false;
            }

            public void clearDefaults(String s, int i) throws RemoteException {
            }

            public boolean isFunctionEnabled(String s) throws RemoteException {
                return false;
            }

            public long getCurrentFunctions() throws RemoteException {
                return 0L;
            }

            public int getCurrentUsbSpeed() throws RemoteException {
                return 0;
            }

            public int getGadgetHalVersion() throws RemoteException {
                return 0;
            }

            public void setScreenUnlockedFunctions(long l) throws RemoteException {
            }

            public long getScreenUnlockedFunctions() throws RemoteException {
                return 0L;
            }

            public void resetUsbGadget() throws RemoteException {
            }

            public void resetUsbPort(String s, int i, IUsbOperationInternal iUsbOperationInternal) throws RemoteException {
            }

            public boolean enableUsbData(String s, boolean b, int i, IUsbOperationInternal iUsbOperationInternal) throws RemoteException {
                return false;
            }

            public void enableUsbDataWhileDocked(String s, int i, IUsbOperationInternal iUsbOperationInternal) throws RemoteException {
            }

            public int getUsbHalVersion() throws RemoteException {
                return 0;
            }

            public ParcelFileDescriptor getControlFd(long l) throws RemoteException {
                return null;
            }

            public List<ParcelableUsbPort> getPorts() throws RemoteException {
                return null;
            }

            public UsbPortStatus getPortStatus(String s) throws RemoteException {
                return null;
            }

            public void setPortRoles(String s, int i, int i1) throws RemoteException {
            }

            public void enableLimitPowerTransfer(String s, boolean b, int i, IUsbOperationInternal iUsbOperationInternal) throws RemoteException {
            }

            public void enableContaminantDetection(String s, boolean b) throws RemoteException {
            }

            public void setUsbDeviceConnectionHandler(ComponentName componentName) throws RemoteException {
            }

            public boolean registerForDisplayPortEvents(IDisplayPortAltModeInfoListener listener) throws RemoteException {
                return false;
            }

            public void unregisterForDisplayPortEvents(IDisplayPortAltModeInfoListener listener) throws RemoteException {
            }
        };

        public static IUsbManager asInterface(IBinder obj) {
            return new Proxy(IUsbManager.Stub.asInterface(obj));
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IUsbManager {
            private final android.hardware.usb.IUsbManager mTarget;

            Proxy(android.hardware.usb.IUsbManager target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.hardware.usb.IUsbManager
            public void setAccessoryPackage(UsbAccessory accessory, String packageName, int userId) throws RemoteException {
                this.mTarget.setAccessoryPackage(accessory, packageName, userId);
            }

            @Override // com.oplus.wrapper.hardware.usb.IUsbManager
            public void grantAccessoryPermission(UsbAccessory accessory, int uid) throws RemoteException {
                this.mTarget.grantAccessoryPermission(accessory, uid);
            }
        }
    }
}
