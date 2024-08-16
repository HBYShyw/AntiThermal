package com.android.server.pm;

import android.content.pm.IPackageInstallObserver2;
import android.content.pm.SigningDetails;
import android.os.UserHandle;
import android.util.ArrayMap;
import com.android.internal.util.Preconditions;
import java.io.File;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class InstallArgs {
    final String mAbiOverride;
    final List<String> mAllowlistedRestrictedPermissions;
    final boolean mApplicationEnabledSettingPersistent;
    final int mAutoRevokePermissionsMode;
    File mCodeFile;
    final int mDataLoaderType;
    final boolean mForceQueryableOverride;
    IInstallArgsExt mInstallArgsExt;
    final int mInstallFlags;
    final int mInstallReason;
    final int mInstallScenario;
    final InstallSource mInstallSource;
    String[] mInstructionSets;
    final MoveInfo mMoveInfo;
    final IPackageInstallObserver2 mObserver;
    final OriginInfo mOriginInfo;
    final int mPackageSource;
    final ArrayMap<String, Integer> mPermissionStates;
    final SigningDetails mSigningDetails;
    final int mTraceCookie;
    final String mTraceMethod;
    final UserHandle mUser;
    final String mVolumeUuid;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InstallArgs(OriginInfo originInfo, MoveInfo moveInfo, IPackageInstallObserver2 iPackageInstallObserver2, int i, InstallSource installSource, String str, UserHandle userHandle, String[] strArr, String str2, ArrayMap<String, Integer> arrayMap, List<String> list, int i2, String str3, int i3, SigningDetails signingDetails, int i4, int i5, boolean z, int i6, int i7, boolean z2) {
        this.mOriginInfo = originInfo;
        this.mMoveInfo = moveInfo;
        this.mInstallFlags = i;
        this.mObserver = iPackageInstallObserver2;
        this.mInstallSource = (InstallSource) Preconditions.checkNotNull(installSource);
        this.mVolumeUuid = str;
        this.mUser = userHandle;
        this.mInstructionSets = strArr;
        this.mAbiOverride = str2;
        this.mPermissionStates = arrayMap;
        this.mAllowlistedRestrictedPermissions = list;
        this.mAutoRevokePermissionsMode = i2;
        this.mTraceMethod = str3;
        this.mTraceCookie = i3;
        this.mSigningDetails = signingDetails;
        this.mInstallReason = i4;
        this.mInstallScenario = i5;
        this.mForceQueryableOverride = z;
        this.mDataLoaderType = i6;
        this.mPackageSource = i7;
        this.mApplicationEnabledSettingPersistent = z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InstallArgs(String str, String[] strArr) {
        this(OriginInfo.fromNothing(), null, null, 0, InstallSource.EMPTY, null, null, strArr, null, new ArrayMap(), null, 3, null, 0, SigningDetails.UNKNOWN, 0, 0, false, 0, 0, false);
        this.mCodeFile = str != null ? new File(str) : null;
    }

    String getCodePath() {
        File file = this.mCodeFile;
        if (file != null) {
            return file.getAbsolutePath();
        }
        return null;
    }
}
