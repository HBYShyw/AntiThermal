package com.android.server.pm;

import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManagerNative;
import android.content.pm.IStagedApexObserver;
import android.content.pm.PackageInfo;
import android.content.pm.StagedApexInfo;
import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Slog;
import java.util.Arrays;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class PackageManagerNative extends IPackageManagerNative.Stub {
    private final IPackageManagerNativeExt mPackageManagerNativeExt;
    private final PackageManagerService mPm;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PackageManagerNative(PackageManagerService packageManagerService) {
        this.mPm = packageManagerService;
        IPackageManagerNativeExt iPackageManagerNativeExt = (IPackageManagerNativeExt) ExtLoader.type(IPackageManagerNativeExt.class).create();
        this.mPackageManagerNativeExt = iPackageManagerNativeExt;
        iPackageManagerNativeExt.init(packageManagerService, packageManagerService.mContext);
    }

    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (super.onTransact(i, parcel, parcel2, i2)) {
            return true;
        }
        return this.mPackageManagerNativeExt.onTransact(i, parcel, parcel2, i2);
    }

    public String[] getNamesForUids(int[] iArr) throws RemoteException {
        String[] strArr;
        String[] strArr2 = null;
        if (iArr != null) {
            try {
                if (iArr.length != 0) {
                    String[] namesForUids = this.mPm.snapshotComputer().getNamesForUids(iArr);
                    if (namesForUids != null) {
                        strArr2 = namesForUids;
                    } else {
                        try {
                            strArr2 = new String[iArr.length];
                        } catch (Throwable th) {
                            th = th;
                            String[] strArr3 = strArr2;
                            strArr2 = namesForUids;
                            strArr = strArr3;
                            Slog.e("PackageManager", "uids: " + Arrays.toString(iArr));
                            Slog.e("PackageManager", "names: " + Arrays.toString(strArr2));
                            Slog.e("PackageManager", "results: " + Arrays.toString(strArr));
                            Slog.e("PackageManager", "throwing exception", th);
                            throw th;
                        }
                    }
                    for (int length = strArr2.length - 1; length >= 0; length--) {
                        if (strArr2[length] == null) {
                            strArr2[length] = "";
                        }
                    }
                    return strArr2;
                }
            } catch (Throwable th2) {
                th = th2;
                strArr = null;
            }
        }
        return null;
    }

    public String getInstallerForPackage(String str) throws RemoteException {
        Computer snapshotComputer = this.mPm.snapshotComputer();
        int userId = UserHandle.getUserId(Binder.getCallingUid());
        String installerPackageName = snapshotComputer.getInstallerPackageName(str, userId);
        if (!TextUtils.isEmpty(installerPackageName)) {
            return installerPackageName;
        }
        ApplicationInfo applicationInfo = snapshotComputer.getApplicationInfo(str, 0L, userId);
        return (applicationInfo == null || (applicationInfo.flags & 1) == 0) ? "" : "preload";
    }

    public long getVersionCodeForPackage(String str) throws RemoteException {
        try {
            PackageInfo packageInfo = this.mPm.snapshotComputer().getPackageInfo(str, 0L, UserHandle.getUserId(Binder.getCallingUid()));
            if (packageInfo != null) {
                return packageInfo.getLongVersionCode();
            }
        } catch (Exception unused) {
        }
        return 0L;
    }

    public int getTargetSdkVersionForPackage(String str) throws RemoteException {
        int targetSdkVersion = this.mPm.snapshotComputer().getTargetSdkVersion(str);
        if (targetSdkVersion != -1) {
            return targetSdkVersion;
        }
        throw new RemoteException("Couldn't get targetSdkVersion for package " + str);
    }

    public boolean isPackageDebuggable(String str) throws RemoteException {
        ApplicationInfo applicationInfo = this.mPm.snapshotComputer().getApplicationInfo(str, 0L, UserHandle.getCallingUserId());
        if (applicationInfo != null) {
            return (applicationInfo.flags & 2) != 0;
        }
        throw new RemoteException("Couldn't get debug flag for package " + str);
    }

    public boolean[] isAudioPlaybackCaptureAllowed(String[] strArr) throws RemoteException {
        int userId = UserHandle.getUserId(Binder.getCallingUid());
        Computer snapshotComputer = this.mPm.snapshotComputer();
        int length = strArr.length;
        boolean[] zArr = new boolean[length];
        for (int i = length - 1; i >= 0; i--) {
            ApplicationInfo applicationInfo = snapshotComputer.getApplicationInfo(strArr[i], 0L, userId);
            zArr[i] = applicationInfo != null && applicationInfo.isAudioPlaybackCaptureAllowed();
        }
        return zArr;
    }

    public int getLocationFlags(String str) throws RemoteException {
        ApplicationInfo applicationInfo = this.mPm.snapshotComputer().getApplicationInfo(str, 0L, UserHandle.getUserId(Binder.getCallingUid()));
        if (applicationInfo == null) {
            throw new RemoteException("Couldn't get ApplicationInfo for package " + str);
        }
        return (applicationInfo.isSystemApp() ? 1 : 0) | (applicationInfo.isVendor() ? 2 : 0) | (applicationInfo.isProduct() ? 4 : 0);
    }

    public String getModuleMetadataPackageName() throws RemoteException {
        return this.mPm.getModuleMetadataPackageName();
    }

    public boolean hasSha256SigningCertificate(String str, byte[] bArr) throws RemoteException {
        return this.mPm.snapshotComputer().hasSigningCertificate(str, bArr, 1);
    }

    public boolean hasSystemFeature(String str, int i) {
        return this.mPm.hasSystemFeature(str, i);
    }

    public void registerStagedApexObserver(IStagedApexObserver iStagedApexObserver) {
        this.mPm.mInstallerService.getStagingManager().registerStagedApexObserver(iStagedApexObserver);
    }

    public void unregisterStagedApexObserver(IStagedApexObserver iStagedApexObserver) {
        this.mPm.mInstallerService.getStagingManager().unregisterStagedApexObserver(iStagedApexObserver);
    }

    public String[] getStagedApexModuleNames() {
        return (String[]) this.mPm.mInstallerService.getStagingManager().getStagedApexModuleNames().toArray(new String[0]);
    }

    public StagedApexInfo getStagedApexInfo(String str) {
        return this.mPm.mInstallerService.getStagingManager().getStagedApexInfo(str);
    }
}
