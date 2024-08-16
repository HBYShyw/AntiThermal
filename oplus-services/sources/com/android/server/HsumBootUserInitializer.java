package com.android.server;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.os.UserManager;
import android.provider.Settings;
import com.android.server.am.ActivityManagerService;
import com.android.server.pm.PackageManagerService;
import com.android.server.pm.UserManagerInternal;
import com.android.server.utils.Slogf;
import com.android.server.utils.TimingsTraceAndSlog;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class HsumBootUserInitializer {
    private static final String TAG = "HsumBootUserInitializer";
    private final ActivityManagerService mAms;
    private final ContentResolver mContentResolver;
    private final ContentObserver mDeviceProvisionedObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: com.android.server.HsumBootUserInitializer.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            if (HsumBootUserInitializer.this.isDeviceProvisioned()) {
                Slogf.i(HsumBootUserInitializer.TAG, "Marking USER_SETUP_COMPLETE for system user");
                Settings.Secure.putInt(HsumBootUserInitializer.this.mContentResolver, "user_setup_complete", 1);
                HsumBootUserInitializer.this.mContentResolver.unregisterContentObserver(HsumBootUserInitializer.this.mDeviceProvisionedObserver);
            }
        }
    };
    private final PackageManagerService mPms;
    private final boolean mShouldAlwaysHaveMainUser;
    private final UserManagerInternal mUmi;

    public static HsumBootUserInitializer createInstance(ActivityManagerService activityManagerService, PackageManagerService packageManagerService, ContentResolver contentResolver, boolean z) {
        if (UserManager.isHeadlessSystemUserMode()) {
            return new HsumBootUserInitializer((UserManagerInternal) LocalServices.getService(UserManagerInternal.class), activityManagerService, packageManagerService, contentResolver, z);
        }
        return null;
    }

    private HsumBootUserInitializer(UserManagerInternal userManagerInternal, ActivityManagerService activityManagerService, PackageManagerService packageManagerService, ContentResolver contentResolver, boolean z) {
        this.mUmi = userManagerInternal;
        this.mAms = activityManagerService;
        this.mPms = packageManagerService;
        this.mContentResolver = contentResolver;
        this.mShouldAlwaysHaveMainUser = z;
    }

    public void init(TimingsTraceAndSlog timingsTraceAndSlog) {
        Slogf.i(TAG, "init())");
        if (this.mShouldAlwaysHaveMainUser) {
            timingsTraceAndSlog.traceBegin("createMainUserIfNeeded");
            createMainUserIfNeeded();
            timingsTraceAndSlog.traceEnd();
        }
    }

    private void createMainUserIfNeeded() {
        int mainUserId = this.mUmi.getMainUserId();
        if (mainUserId != -10000) {
            Slogf.d(TAG, "Found existing MainUser, userId=%d", new Object[]{Integer.valueOf(mainUserId)});
            return;
        }
        String str = TAG;
        Slogf.d(str, "Creating a new MainUser");
        try {
            Slogf.i(str, "Successfully created MainUser, userId=%d", new Object[]{Integer.valueOf(this.mUmi.createUserEvenWhenDisallowed((String) null, "android.os.usertype.full.SECONDARY", 16386, (String[]) null, (Object) null).id)});
        } catch (UserManager.CheckedUserOperationException e) {
            Slogf.wtf(TAG, "Initial bootable MainUser creation failed", e);
        }
    }

    public void systemRunning(TimingsTraceAndSlog timingsTraceAndSlog) {
        observeDeviceProvisioning();
        unlockSystemUser(timingsTraceAndSlog);
        try {
            timingsTraceAndSlog.traceBegin("getBootUser");
            int bootUser = this.mUmi.getBootUser(this.mPms.hasSystemFeature("android.hardware.type.automotive", 0));
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("switchToBootUser-" + bootUser);
            switchToBootUser(bootUser);
            timingsTraceAndSlog.traceEnd();
        } catch (UserManager.CheckedUserOperationException unused) {
            Slogf.wtf(TAG, "Failed to switch to boot user since there isn't one.");
        }
    }

    private void observeDeviceProvisioning() {
        if (isDeviceProvisioned()) {
            return;
        }
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor("device_provisioned"), false, this.mDeviceProvisionedObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDeviceProvisioned() {
        try {
            return Settings.Global.getInt(this.mContentResolver, "device_provisioned") == 1;
        } catch (Exception e) {
            Slogf.wtf(TAG, "DEVICE_PROVISIONED setting not found.", e);
            return false;
        }
    }

    private void unlockSystemUser(TimingsTraceAndSlog timingsTraceAndSlog) {
        String str = TAG;
        Slogf.i(str, "Unlocking system user");
        timingsTraceAndSlog.traceBegin("unlock-system-user");
        try {
            timingsTraceAndSlog.traceBegin("am.startUser");
            boolean startUserInBackgroundWithListener = this.mAms.startUserInBackgroundWithListener(0, null);
            timingsTraceAndSlog.traceEnd();
            if (!startUserInBackgroundWithListener) {
                Slogf.w(str, "could not restart system user in background; trying unlock instead");
                timingsTraceAndSlog.traceBegin("am.unlockUser");
                boolean unlockUser = this.mAms.unlockUser(0, null, null, null);
                timingsTraceAndSlog.traceEnd();
                if (!unlockUser) {
                    Slogf.w(str, "could not unlock system user either");
                }
            }
        } finally {
            timingsTraceAndSlog.traceEnd();
        }
    }

    private void switchToBootUser(int i) {
        String str = TAG;
        Slogf.i(str, "Switching to boot user %d", new Object[]{Integer.valueOf(i)});
        if (this.mAms.startUserInForegroundWithListener(i, null)) {
            return;
        }
        Slogf.wtf(str, "Failed to start user %d in foreground", new Object[]{Integer.valueOf(i)});
    }
}
