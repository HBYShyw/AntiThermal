package com.android.server.pm;

import com.android.internal.util.Preconditions;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class InstallSource {
    static final InstallSource EMPTY = new InstallSource(null, null, null, -1, null, null, false, false, null, 0);
    private static final InstallSource EMPTY_ORPHANED = new InstallSource(null, null, null, -1, null, null, true, false, null, 0);
    final String mInitiatingPackageName;
    final PackageSignatures mInitiatingPackageSignatures;
    final String mInstallerAttributionTag;
    String mInstallerPackageName;
    final int mInstallerPackageUid;
    final boolean mIsInitiatingPackageUninstalled;
    final boolean mIsOrphaned;
    final String mOriginatingPackageName;
    final int mPackageSource;
    final String mUpdateOwnerPackageName;

    static InstallSource create(String str, String str2, String str3, int i, String str4, String str5, boolean z, boolean z2) {
        return create(str, str2, str3, i, str4, str5, 0, z, z2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static InstallSource create(String str, String str2, String str3, int i, String str4, String str5, int i2) {
        return create(str, str2, str3, i, str4, str5, i2, false, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static InstallSource create(String str, String str2, String str3, int i, String str4, String str5, int i2, boolean z, boolean z2) {
        return createInternal(intern(str), intern(str2), intern(str3), i, intern(str4), str5, i2, z, z2, null);
    }

    private static InstallSource createInternal(String str, String str2, String str3, int i, String str4, String str5, int i2, boolean z, boolean z2, PackageSignatures packageSignatures) {
        if (str == null && str2 == null && str3 == null && str4 == null && packageSignatures == null && !z2 && i2 == 0) {
            return z ? EMPTY_ORPHANED : EMPTY;
        }
        return new InstallSource(str, str2, str3, i, str4, str5, z, z2, packageSignatures, i2);
    }

    private InstallSource(String str, String str2, String str3, int i, String str4, String str5, boolean z, boolean z2, PackageSignatures packageSignatures, int i2) {
        if (str == null) {
            Preconditions.checkArgument(packageSignatures == null);
            Preconditions.checkArgument(!z2);
        }
        this.mInitiatingPackageName = str;
        this.mOriginatingPackageName = str2;
        this.mInstallerPackageName = str3;
        this.mInstallerPackageUid = i;
        this.mUpdateOwnerPackageName = str4;
        this.mInstallerAttributionTag = str5;
        this.mIsOrphaned = z;
        this.mIsInitiatingPackageUninstalled = z2;
        this.mInitiatingPackageSignatures = packageSignatures;
        this.mPackageSource = i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InstallSource setInstallerPackage(String str, int i) {
        return Objects.equals(str, this.mInstallerPackageName) ? this : createInternal(this.mInitiatingPackageName, this.mOriginatingPackageName, intern(str), i, this.mUpdateOwnerPackageName, this.mInstallerAttributionTag, this.mPackageSource, this.mIsOrphaned, this.mIsInitiatingPackageUninstalled, this.mInitiatingPackageSignatures);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InstallSource setUpdateOwnerPackageName(String str) {
        return Objects.equals(str, this.mUpdateOwnerPackageName) ? this : createInternal(this.mInitiatingPackageName, this.mOriginatingPackageName, this.mInstallerPackageName, this.mInstallerPackageUid, intern(str), this.mInstallerAttributionTag, this.mPackageSource, this.mIsOrphaned, this.mIsInitiatingPackageUninstalled, this.mInitiatingPackageSignatures);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InstallSource setIsOrphaned(boolean z) {
        return z == this.mIsOrphaned ? this : createInternal(this.mInitiatingPackageName, this.mOriginatingPackageName, this.mInstallerPackageName, this.mInstallerPackageUid, this.mUpdateOwnerPackageName, this.mInstallerAttributionTag, this.mPackageSource, z, this.mIsInitiatingPackageUninstalled, this.mInitiatingPackageSignatures);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InstallSource setInitiatingPackageSignatures(PackageSignatures packageSignatures) {
        return packageSignatures == this.mInitiatingPackageSignatures ? this : createInternal(this.mInitiatingPackageName, this.mOriginatingPackageName, this.mInstallerPackageName, this.mInstallerPackageUid, this.mUpdateOwnerPackageName, this.mInstallerAttributionTag, this.mPackageSource, this.mIsOrphaned, this.mIsInitiatingPackageUninstalled, packageSignatures);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InstallSource removeInstallerPackage(String str) {
        boolean z;
        boolean z2;
        String str2;
        String str3;
        int i;
        boolean z3;
        String str4;
        if (str == null) {
            return this;
        }
        boolean z4 = this.mIsInitiatingPackageUninstalled;
        String str5 = this.mOriginatingPackageName;
        String str6 = this.mInstallerPackageName;
        String str7 = this.mUpdateOwnerPackageName;
        int i2 = this.mInstallerPackageUid;
        boolean z5 = this.mIsOrphaned;
        boolean z6 = true;
        if (!str.equals(this.mInitiatingPackageName) || z4) {
            z = false;
            z2 = z4;
        } else {
            z = true;
            z2 = true;
        }
        if (str.equals(str5)) {
            z = true;
            str2 = null;
        } else {
            str2 = str5;
        }
        if (str.equals(str6)) {
            i = -1;
            z = true;
            z3 = true;
            str3 = null;
        } else {
            str3 = str6;
            i = i2;
            z3 = z5;
        }
        if (str.equals(str7)) {
            str4 = null;
        } else {
            str4 = str7;
            z6 = z;
        }
        return !z6 ? this : createInternal(this.mInitiatingPackageName, str2, str3, i, str4, null, this.mPackageSource, z3, z2, this.mInitiatingPackageSignatures);
    }

    private static String intern(String str) {
        if (str == null) {
            return null;
        }
        return str.intern();
    }
}
