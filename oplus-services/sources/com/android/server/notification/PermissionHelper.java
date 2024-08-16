package com.android.server.notification;

import android.content.Context;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.permission.IPermissionManager;
import android.util.ArrayMap;
import android.util.Pair;
import android.util.Slog;
import com.android.internal.util.ArrayUtils;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class PermissionHelper {
    private static final String NOTIFICATION_PERMISSION = "android.permission.POST_NOTIFICATIONS";
    private static final String TAG = "NotificationService--PermissionHelper";
    private final Context mContext;
    private final IPackageManager mPackageManager;
    private IPermissionHelperWrapper mPerHWrapper = new PermissionHelperWrapper();
    private IPermissionHelperExt mPerHelperExt = (IPermissionHelperExt) ExtLoader.type(IPermissionHelperExt.class).base(this).create();
    private final IPermissionManager mPermManager;

    public PermissionHelper(Context context, IPackageManager iPackageManager, IPermissionManager iPermissionManager) {
        this.mContext = context;
        this.mPackageManager = iPackageManager;
        this.mPermManager = iPermissionManager;
    }

    public boolean hasPermission(int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mContext.checkPermission(NOTIFICATION_PERMISSION, -1, i) == 0;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean hasRequestedPermission(String str, String str2, int i) {
        PackageInfo packageInfo;
        String[] strArr;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                packageInfo = this.mPackageManager.getPackageInfo(str2, 4096L, i);
            } catch (RemoteException e) {
                Slog.d(TAG, "Could not reach system server", e);
            }
            if (packageInfo != null && (strArr = packageInfo.requestedPermissions) != null) {
                for (String str3 : strArr) {
                    if (str.equals(str3)) {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        return true;
                    }
                }
                return false;
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    Set<Pair<Integer, String>> getAppsRequestingPermission(int i) {
        HashSet hashSet = new HashSet();
        for (PackageInfo packageInfo : getInstalledPackages(i)) {
            String[] strArr = packageInfo.requestedPermissions;
            if (strArr != null) {
                int length = strArr.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    if (NOTIFICATION_PERMISSION.equals(strArr[i2])) {
                        hashSet.add(new Pair(Integer.valueOf(packageInfo.applicationInfo.uid), packageInfo.packageName));
                        break;
                    }
                    i2++;
                }
            }
        }
        return hashSet;
    }

    private List<PackageInfo> getInstalledPackages(int i) {
        ParceledListSlice parceledListSlice;
        try {
            parceledListSlice = this.mPackageManager.getInstalledPackages(4096L, i);
        } catch (RemoteException e) {
            Slog.d(TAG, "Could not reach system server", e);
            parceledListSlice = null;
        }
        if (parceledListSlice == null) {
            return Collections.emptyList();
        }
        return parceledListSlice.getList();
    }

    Set<Pair<Integer, String>> getAppsGrantedPermission(int i) {
        ParceledListSlice parceledListSlice;
        HashSet hashSet = new HashSet();
        try {
            parceledListSlice = this.mPackageManager.getPackagesHoldingPermissions(new String[]{NOTIFICATION_PERMISSION}, 0L, i);
        } catch (RemoteException e) {
            Slog.e(TAG, "Could not reach system server", e);
            parceledListSlice = null;
        }
        if (parceledListSlice == null) {
            return hashSet;
        }
        for (PackageInfo packageInfo : parceledListSlice.getList()) {
            hashSet.add(new Pair(Integer.valueOf(packageInfo.applicationInfo.uid), packageInfo.packageName));
        }
        return hashSet;
    }

    public ArrayMap<Pair<Integer, String>, Pair<Boolean, Boolean>> getNotificationPermissionValues(int i) {
        ArrayMap<Pair<Integer, String>, Pair<Boolean, Boolean>> arrayMap = new ArrayMap<>();
        Set<Pair<Integer, String>> appsRequestingPermission = getAppsRequestingPermission(i);
        Set<Pair<Integer, String>> appsGrantedPermission = getAppsGrantedPermission(i);
        for (Pair<Integer, String> pair : appsRequestingPermission) {
            arrayMap.put(pair, new Pair<>(Boolean.valueOf(appsGrantedPermission.contains(pair)), Boolean.valueOf(isPermissionUserSet((String) pair.second, i))));
        }
        return arrayMap;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0048 A[Catch: all -> 0x0064, RemoteException -> 0x0066, TryCatch #1 {RemoteException -> 0x0066, blocks: (B:3:0x0006, B:5:0x000c, B:7:0x0012, B:11:0x001b, B:15:0x002d, B:22:0x0048, B:26:0x0054, B:30:0x0037), top: B:2:0x0006, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0054 A[Catch: all -> 0x0064, RemoteException -> 0x0066, TRY_LEAVE, TryCatch #1 {RemoteException -> 0x0066, blocks: (B:3:0x0006, B:5:0x000c, B:7:0x0012, B:11:0x001b, B:15:0x002d, B:22:0x0048, B:26:0x0054, B:30:0x0037), top: B:2:0x0006, outer: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setNotificationPermission(String str, int i, boolean z, boolean z2) {
        int i2;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
            } catch (RemoteException e) {
                Slog.e(TAG, "Could not reach system server", e);
            }
            if (packageRequestsNotificationPermission(str, i) && !isPermissionFixed(str, i) && (!isPermissionGrantedByDefaultOrRole(str, i) || z2)) {
                boolean hasPermission = hasPermission(this.mPackageManager.getPackageUid(str, 0L, i));
                if (z && !hasPermission) {
                    this.mPermManager.grantRuntimePermission(str, NOTIFICATION_PERMISSION, i);
                } else if (!z && hasPermission) {
                    this.mPermManager.revokeRuntimePermission(str, NOTIFICATION_PERMISSION, i, TAG);
                }
                if (!z2 && z) {
                    i2 = 3;
                    int i3 = i2;
                    if (!z2) {
                        this.mPermManager.updatePermissionFlags(str, NOTIFICATION_PERMISSION, i3, 1, true, i);
                    } else {
                        this.mPermManager.updatePermissionFlags(str, NOTIFICATION_PERMISSION, i3, 0, true, i);
                    }
                }
                i2 = 35;
                int i32 = i2;
                if (!z2) {
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setNotificationPermission(PackagePermission packagePermission) {
        String str;
        if (packagePermission == null || (str = packagePermission.packageName) == null || isPermissionFixed(str, packagePermission.userId)) {
            return;
        }
        setNotificationPermission(packagePermission.packageName, packagePermission.userId, packagePermission.granted, true);
    }

    public boolean isPermissionFixed(String str, int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int permissionFlags = this.mPermManager.getPermissionFlags(str, NOTIFICATION_PERMISSION, i);
            return ((permissionFlags & 16) == 0 && (permissionFlags & 4) == 0) ? false : true;
        } catch (RemoteException e) {
            Slog.e(TAG, "Could not reach system server", e);
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPermissionUserSet(String str, int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return (this.mPermManager.getPermissionFlags(str, NOTIFICATION_PERMISSION, i) & 3) != 0;
        } catch (RemoteException e) {
            Slog.e(TAG, "Could not reach system server", e);
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    boolean isPermissionGrantedByDefaultOrRole(String str, int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return (this.mPermManager.getPermissionFlags(str, NOTIFICATION_PERMISSION, i) & 32800) != 0;
        } catch (RemoteException e) {
            Slog.e(TAG, "Could not reach system server", e);
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean packageRequestsNotificationPermission(String str, int i) {
        try {
            PackageInfo packageInfo = this.mPackageManager.getPackageInfo(str, 4096L, i);
            if (packageInfo != null) {
                return ArrayUtils.contains(packageInfo.requestedPermissions, NOTIFICATION_PERMISSION);
            }
            return false;
        } catch (RemoteException e) {
            Slog.e(TAG, "Could not reach system server", e);
            return false;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PackagePermission {
        public final boolean granted;
        public final String packageName;
        public final int userId;
        public final boolean userModifiedSettings;

        public PackagePermission(String str, int i, boolean z, boolean z2) {
            this.packageName = str;
            this.userId = i;
            this.granted = z;
            this.userModifiedSettings = z2;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            PackagePermission packagePermission = (PackagePermission) obj;
            return this.userId == packagePermission.userId && this.granted == packagePermission.granted && this.userModifiedSettings == packagePermission.userModifiedSettings && Objects.equals(this.packageName, packagePermission.packageName);
        }

        public int hashCode() {
            return Objects.hash(this.packageName, Integer.valueOf(this.userId), Boolean.valueOf(this.granted), Boolean.valueOf(this.userModifiedSettings));
        }

        public String toString() {
            return "PackagePermission{packageName='" + this.packageName + "', userId=" + this.userId + ", granted=" + this.granted + ", userSet=" + this.userModifiedSettings + '}';
        }
    }

    public IPermissionHelperWrapper getWrapper() {
        return this.mPerHWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class PermissionHelperWrapper implements IPermissionHelperWrapper {
        private PermissionHelperWrapper() {
        }

        @Override // com.android.server.notification.IPermissionHelperWrapper
        public IPermissionHelperExt getPermissionHelperExt() {
            return PermissionHelper.this.mPerHelperExt;
        }

        @Override // com.android.server.notification.IPermissionHelperWrapper
        public boolean canModifyNotificationPermissionForPackage(String str, int i) {
            int userId = UserHandle.getUserId(i);
            boolean packageRequestsNotificationPermission = PermissionHelper.this.packageRequestsNotificationPermission(str, userId);
            boolean isPermissionFixed = PermissionHelper.this.isPermissionFixed(str, userId);
            boolean isPermissionGrantedByDefaultOrRole = PermissionHelper.this.isPermissionGrantedByDefaultOrRole(str, userId);
            if (getPermissionHelperExt() != null && getPermissionHelperExt().isLoggable()) {
                Slog.d(PermissionHelper.TAG, "preview of modifying notification permission for pkg: " + str + "; is Pkg requests Notify Permission: " + packageRequestsNotificationPermission + "; is permission fixed for this pkg: " + isPermissionFixed + "; is permission granted by default: " + isPermissionGrantedByDefaultOrRole);
            }
            return packageRequestsNotificationPermission && !isPermissionFixed;
        }
    }
}
