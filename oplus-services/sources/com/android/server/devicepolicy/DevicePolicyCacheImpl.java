package com.android.server.devicepolicy;

import android.app.admin.DevicePolicyCache;
import android.util.ArrayMap;
import android.util.IndentingPrintWriter;
import android.util.SparseIntArray;
import com.android.internal.annotations.GuardedBy;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DevicePolicyCacheImpl extends DevicePolicyCache {
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private int mScreenCaptureDisallowedUser = -10000;

    @GuardedBy({"mLock"})
    private Set<Integer> mScreenCaptureDisallowedUsers = new HashSet();

    @GuardedBy({"mLock"})
    private final SparseIntArray mPasswordQuality = new SparseIntArray();

    @GuardedBy({"mLock"})
    private final SparseIntArray mPermissionPolicy = new SparseIntArray();

    @GuardedBy({"mLock"})
    private ArrayMap<String, String> mLauncherShortcutOverrides = new ArrayMap<>();
    private final AtomicBoolean mCanGrantSensorsPermissions = new AtomicBoolean(false);

    public void onUserRemoved(int i) {
        synchronized (this.mLock) {
            this.mPasswordQuality.delete(i);
            this.mPermissionPolicy.delete(i);
        }
    }

    public boolean isScreenCaptureAllowed(int i) {
        boolean z;
        if (DevicePolicyManagerService.isPolicyEngineForFinanceFlagEnabled()) {
            return isScreenCaptureAllowedInPolicyEngine(i);
        }
        synchronized (this.mLock) {
            int i2 = this.mScreenCaptureDisallowedUser;
            z = (i2 == -1 || i2 == i) ? false : true;
        }
        return z;
    }

    private boolean isScreenCaptureAllowedInPolicyEngine(int i) {
        boolean z;
        synchronized (this.mLock) {
            z = (this.mScreenCaptureDisallowedUsers.contains(Integer.valueOf(i)) || this.mScreenCaptureDisallowedUsers.contains(-1)) ? false : true;
        }
        return z;
    }

    public int getScreenCaptureDisallowedUser() {
        int i;
        synchronized (this.mLock) {
            i = this.mScreenCaptureDisallowedUser;
        }
        return i;
    }

    public void setScreenCaptureDisallowedUser(int i) {
        synchronized (this.mLock) {
            this.mScreenCaptureDisallowedUser = i;
        }
    }

    public void setScreenCaptureDisallowedUser(int i, boolean z) {
        synchronized (this.mLock) {
            if (z) {
                this.mScreenCaptureDisallowedUsers.add(Integer.valueOf(i));
            } else {
                this.mScreenCaptureDisallowedUsers.remove(Integer.valueOf(i));
            }
        }
    }

    public int getPasswordQuality(int i) {
        int i2;
        synchronized (this.mLock) {
            i2 = this.mPasswordQuality.get(i, 0);
        }
        return i2;
    }

    public void setPasswordQuality(int i, int i2) {
        synchronized (this.mLock) {
            this.mPasswordQuality.put(i, i2);
        }
    }

    public int getPermissionPolicy(int i) {
        int i2;
        synchronized (this.mLock) {
            i2 = this.mPermissionPolicy.get(i, 0);
        }
        return i2;
    }

    public void setPermissionPolicy(int i, int i2) {
        synchronized (this.mLock) {
            this.mPermissionPolicy.put(i, i2);
        }
    }

    public boolean canAdminGrantSensorsPermissions() {
        return this.mCanGrantSensorsPermissions.get();
    }

    public void setAdminCanGrantSensorsPermissions(boolean z) {
        this.mCanGrantSensorsPermissions.set(z);
    }

    public Map<String, String> getLauncherShortcutOverrides() {
        ArrayMap arrayMap;
        synchronized (this.mLock) {
            arrayMap = new ArrayMap(this.mLauncherShortcutOverrides);
        }
        return arrayMap;
    }

    public void setLauncherShortcutOverrides(ArrayMap<String, String> arrayMap) {
        synchronized (this.mLock) {
            this.mLauncherShortcutOverrides = new ArrayMap<>(arrayMap);
        }
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        synchronized (this.mLock) {
            indentingPrintWriter.println("Device policy cache:");
            indentingPrintWriter.increaseIndent();
            if (DevicePolicyManagerService.isPolicyEngineForFinanceFlagEnabled()) {
                indentingPrintWriter.println("Screen capture disallowed users: " + this.mScreenCaptureDisallowedUsers);
            } else {
                indentingPrintWriter.println("Screen capture disallowed user: " + this.mScreenCaptureDisallowedUser);
            }
            indentingPrintWriter.println("Password quality: " + this.mPasswordQuality);
            indentingPrintWriter.println("Permission policy: " + this.mPermissionPolicy);
            indentingPrintWriter.println("Admin can grant sensors permission: " + this.mCanGrantSensorsPermissions.get());
            indentingPrintWriter.print("Shortcuts overrides: ");
            indentingPrintWriter.println(this.mLauncherShortcutOverrides);
            indentingPrintWriter.decreaseIndent();
        }
    }
}
