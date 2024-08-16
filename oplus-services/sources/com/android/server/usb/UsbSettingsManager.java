package com.android.server.usb;

import android.content.Context;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.dump.DualDumpOutputStream;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UsbSettingsManager {
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "UsbSettingsManager";
    private final Context mContext;
    private UsbHandlerManager mUsbHandlerManager;
    final UsbService mUsbService;
    private UserManager mUserManager;

    @GuardedBy({"mSettingsByUser"})
    private final SparseArray<UsbUserSettingsManager> mSettingsByUser = new SparseArray<>();

    @GuardedBy({"mSettingsByProfileGroup"})
    private final SparseArray<UsbProfileGroupSettingsManager> mSettingsByProfileGroup = new SparseArray<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public UsbSettingsManager(Context context, UsbService usbService) {
        this.mContext = context;
        this.mUsbService = usbService;
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mUsbHandlerManager = new UsbHandlerManager(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UsbUserSettingsManager getSettingsForUser(int i) {
        UsbUserSettingsManager usbUserSettingsManager;
        synchronized (this.mSettingsByUser) {
            usbUserSettingsManager = this.mSettingsByUser.get(i);
            if (usbUserSettingsManager == null) {
                usbUserSettingsManager = new UsbUserSettingsManager(this.mContext, UserHandle.of(i));
                this.mSettingsByUser.put(i, usbUserSettingsManager);
            }
        }
        return usbUserSettingsManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UsbProfileGroupSettingsManager getSettingsForProfileGroup(UserHandle userHandle) {
        UsbProfileGroupSettingsManager usbProfileGroupSettingsManager;
        UserInfo profileParent = this.mUserManager.getProfileParent(userHandle.getIdentifier());
        if (profileParent != null) {
            userHandle = profileParent.getUserHandle();
        }
        synchronized (this.mSettingsByProfileGroup) {
            usbProfileGroupSettingsManager = this.mSettingsByProfileGroup.get(userHandle.getIdentifier());
            if (usbProfileGroupSettingsManager == null) {
                usbProfileGroupSettingsManager = new UsbProfileGroupSettingsManager(this.mContext, userHandle, this, this.mUsbHandlerManager);
                this.mSettingsByProfileGroup.put(userHandle.getIdentifier(), usbProfileGroupSettingsManager);
            }
        }
        return usbProfileGroupSettingsManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void remove(UserHandle userHandle) {
        synchronized (this.mSettingsByUser) {
            this.mSettingsByUser.remove(userHandle.getIdentifier());
        }
        synchronized (this.mSettingsByProfileGroup) {
            if (this.mSettingsByProfileGroup.indexOfKey(userHandle.getIdentifier()) >= 0) {
                this.mSettingsByProfileGroup.get(userHandle.getIdentifier()).unregisterReceivers();
                this.mSettingsByProfileGroup.remove(userHandle.getIdentifier());
            } else {
                int size = this.mSettingsByProfileGroup.size();
                for (int i = 0; i < size; i++) {
                    this.mSettingsByProfileGroup.valueAt(i).removeUser(userHandle);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(DualDumpOutputStream dualDumpOutputStream, String str, long j) {
        int i;
        long start = dualDumpOutputStream.start(str, j);
        synchronized (this.mSettingsByUser) {
            List users = this.mUserManager.getUsers();
            int size = users.size();
            for (int i2 = 0; i2 < size; i2++) {
                getSettingsForUser(((UserInfo) users.get(i2)).id).dump(dualDumpOutputStream, "user_settings", 2246267895809L);
            }
        }
        synchronized (this.mSettingsByProfileGroup) {
            int size2 = this.mSettingsByProfileGroup.size();
            for (i = 0; i < size2; i++) {
                this.mSettingsByProfileGroup.valueAt(i).dump(dualDumpOutputStream, "profile_group_settings", 2246267895810L);
            }
        }
        dualDumpOutputStream.end(start);
    }
}
