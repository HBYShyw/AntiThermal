package com.android.server.usb;

import android.content.Context;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.dump.DualDumpOutputStream;
import com.android.server.pm.DumpState;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UsbPermissionManager {
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "UsbPermissionManager";
    private final Context mContext;

    @GuardedBy({"mPermissionsByUser"})
    private final SparseArray<UsbUserPermissionManager> mPermissionsByUser = new SparseArray<>();
    final UsbService mUsbService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public UsbPermissionManager(Context context, UsbService usbService) {
        this.mContext = context;
        this.mUsbService = usbService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UsbUserPermissionManager getPermissionsForUser(int i) {
        UsbUserPermissionManager usbUserPermissionManager;
        synchronized (this.mPermissionsByUser) {
            usbUserPermissionManager = this.mPermissionsByUser.get(i);
            if (usbUserPermissionManager == null) {
                usbUserPermissionManager = new UsbUserPermissionManager(this.mContext.createContextAsUser(UserHandle.of(i), 0), this.mUsbService.getSettingsForUser(i));
                this.mPermissionsByUser.put(i, usbUserPermissionManager);
            }
        }
        return usbUserPermissionManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UsbUserPermissionManager getPermissionsForUser(UserHandle userHandle) {
        return getPermissionsForUser(userHandle.getIdentifier());
    }

    void remove(UserHandle userHandle) {
        synchronized (this.mPermissionsByUser) {
            this.mPermissionsByUser.remove(userHandle.getIdentifier());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void usbDeviceRemoved(UsbDevice usbDevice) {
        synchronized (this.mPermissionsByUser) {
            for (int i = 0; i < this.mPermissionsByUser.size(); i++) {
                this.mPermissionsByUser.valueAt(i).removeDevicePermissions(usbDevice);
            }
        }
        Intent intent = new Intent("android.hardware.usb.action.USB_DEVICE_DETACHED");
        intent.addFlags(DumpState.DUMP_SERVICE_PERMISSIONS);
        intent.putExtra("device", usbDevice);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void usbAccessoryRemoved(UsbAccessory usbAccessory) {
        synchronized (this.mPermissionsByUser) {
            for (int i = 0; i < this.mPermissionsByUser.size(); i++) {
                this.mPermissionsByUser.valueAt(i).removeAccessoryPermissions(usbAccessory);
            }
        }
        Intent intent = new Intent("android.hardware.usb.action.USB_ACCESSORY_DETACHED");
        intent.addFlags(DumpState.DUMP_SERVICE_PERMISSIONS);
        intent.putExtra("accessory", usbAccessory);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(DualDumpOutputStream dualDumpOutputStream, String str, long j) {
        long start = dualDumpOutputStream.start(str, j);
        UserManager userManager = (UserManager) this.mContext.getSystemService(UserManager.class);
        synchronized (this.mPermissionsByUser) {
            List users = userManager.getUsers();
            int size = users.size();
            for (int i = 0; i < size; i++) {
                getPermissionsForUser(((UserInfo) users.get(i)).id).dump(dualDumpOutputStream, "user_permissions", 2246267895809L);
            }
        }
        dualDumpOutputStream.end(start);
    }
}
