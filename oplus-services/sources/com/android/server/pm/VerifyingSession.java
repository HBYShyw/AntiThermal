package com.android.server.pm;

import android.app.AppOpsManager;
import android.app.BroadcastOptions;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.DataLoaderParams;
import android.content.pm.IPackageInstallObserver2;
import android.content.pm.PackageInfoLite;
import android.content.pm.PackageInstaller;
import android.content.pm.ParceledListSlice;
import android.content.pm.ResolveInfo;
import android.content.pm.SigningDetails;
import android.content.pm.VerifierInfo;
import android.content.pm.parsing.PackageLite;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.incremental.IncrementalManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Slog;
import com.android.server.DeviceIdleInternal;
import com.android.server.tare.AlarmManagerEconomicPolicy;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class VerifyingSession {
    private static final long DEFAULT_ENABLE_ROLLBACK_TIMEOUT_MILLIS = 10000;
    private static final long DEFAULT_INTEGRITY_VERIFICATION_TIMEOUT = 30000;
    private static final boolean DEFAULT_INTEGRITY_VERIFY_ENABLE = true;
    private static final boolean DEFAULT_VERIFY_ENABLE = true;
    private static final String PROPERTY_ENABLE_ROLLBACK_TIMEOUT_MILLIS = "enable_rollback_timeout";
    private final int mDataLoaderType;
    private final int mInstallFlags;
    private final InstallPackageHelper mInstallPackageHelper;
    private final InstallSource mInstallSource;
    private final boolean mIsInherit;
    private final boolean mIsStaged;
    final IPackageInstallObserver2 mObserver;
    final OriginInfo mOriginInfo;
    private final String mPackageAbiOverride;
    private final PackageLite mPackageLite;
    MultiPackageVerifyingSession mParentVerifyingSession;
    private final PackageManagerService mPm;
    private final long mRequiredInstalledVersionCode;
    private final int mSessionId;
    private final SigningDetails mSigningDetails;
    private final UserHandle mUser;
    private final boolean mUserActionRequired;
    private final int mUserActionRequiredType;
    private final VerificationInfo mVerificationInfo;
    private boolean mWaitForEnableRollbackToComplete;
    private boolean mWaitForIntegrityVerificationToComplete;
    private boolean mWaitForVerificationToComplete;
    private int mRet = 1;
    private String mErrorMessage = null;
    private final IVerifyingSessionWrapper mWrapper = new VerifyingSessionWrapper();

    private boolean isIntegrityVerificationEnabled() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VerifyingSession(UserHandle userHandle, File file, IPackageInstallObserver2 iPackageInstallObserver2, PackageInstaller.SessionParams sessionParams, InstallSource installSource, int i, SigningDetails signingDetails, int i2, PackageLite packageLite, boolean z, PackageManagerService packageManagerService) {
        this.mPm = packageManagerService;
        this.mUser = userHandle;
        this.mInstallPackageHelper = new InstallPackageHelper(packageManagerService);
        this.mOriginInfo = OriginInfo.fromStagedFile(file);
        this.mObserver = iPackageInstallObserver2;
        this.mInstallFlags = sessionParams.installFlags;
        this.mInstallSource = installSource;
        this.mPackageAbiOverride = sessionParams.abiOverride;
        this.mVerificationInfo = new VerificationInfo(sessionParams.originatingUri, sessionParams.referrerUri, sessionParams.originatingUid, i);
        this.mSigningDetails = signingDetails;
        this.mRequiredInstalledVersionCode = sessionParams.requiredInstalledVersionCode;
        DataLoaderParams dataLoaderParams = sessionParams.dataLoaderParams;
        this.mDataLoaderType = dataLoaderParams != null ? dataLoaderParams.getType() : 0;
        this.mSessionId = i2;
        this.mPackageLite = packageLite;
        this.mUserActionRequired = z;
        this.mUserActionRequiredType = sessionParams.requireUserAction;
        this.mIsInherit = sessionParams.mode == 2;
        this.mIsStaged = sessionParams.isStaged;
    }

    public String toString() {
        return "VerifyingSession{" + Integer.toHexString(System.identityHashCode(this)) + " file=" + this.mOriginInfo.mFile + "}";
    }

    public void handleStartVerify() {
        if (PackageManagerService.DEBUG_INSTALL) {
            StringBuilder sb = new StringBuilder();
            sb.append("handleStartCopy in VerificationParams: ");
            PackageLite packageLite = this.mPackageLite;
            sb.append(packageLite == null ? null : packageLite.getPackageName());
            Slog.d("PackageManager", sb.toString());
        }
        PackageInfoLite minimalPackageInfo = PackageManagerServiceUtils.getMinimalPackageInfo(this.mPm.mContext, this.mPackageLite, this.mOriginInfo.mResolvedPath, this.mInstallFlags, this.mPackageAbiOverride);
        Pair<Integer, String> verifyReplacingVersionCode = this.mInstallPackageHelper.verifyReplacingVersionCode(minimalPackageInfo, this.mRequiredInstalledVersionCode, this.mInstallFlags);
        setReturnCode(((Integer) verifyReplacingVersionCode.first).intValue(), (String) verifyReplacingVersionCode.second);
        int modifyRetInHandleStartCopyOfVerificationParams = this.mPm.mPackageManagerServiceExt.modifyRetInHandleStartCopyOfVerificationParams(this.mRet, this.mInstallSource, minimalPackageInfo, this.mObserver);
        this.mRet = modifyRetInHandleStartCopyOfVerificationParams;
        if (modifyRetInHandleStartCopyOfVerificationParams != 1) {
            this.mPm.mPackageManagerServiceExt.beforeFailReturnInHandleStartCopyOfVerificationParams(modifyRetInHandleStartCopyOfVerificationParams, minimalPackageInfo, this.mInstallSource, getUser() != null ? getUser().getIdentifier() : 0);
            return;
        }
        if (this.mOriginInfo.mExisting) {
            return;
        }
        if (!isApex()) {
            sendApkVerificationRequest(minimalPackageInfo);
        }
        if ((this.mInstallFlags & DumpState.DUMP_DOMAIN_PREFERRED) != 0) {
            sendEnableRollbackRequest();
        }
    }

    private void sendApkVerificationRequest(PackageInfoLite packageInfoLite) {
        PackageManagerService packageManagerService = this.mPm;
        int i = packageManagerService.mPendingVerificationToken;
        packageManagerService.mPendingVerificationToken = i + 1;
        PackageVerificationState packageVerificationState = new PackageVerificationState(this);
        this.mPm.mPendingVerification.append(i, packageVerificationState);
        sendIntegrityVerificationRequest(i, packageInfoLite, packageVerificationState);
        sendPackageVerificationRequest(i, packageInfoLite, packageVerificationState);
        if (packageVerificationState.areAllVerificationsComplete()) {
            this.mPm.mPendingVerification.remove(i);
        }
    }

    void sendEnableRollbackRequest() {
        PackageManagerService packageManagerService = this.mPm;
        int i = packageManagerService.mPendingEnableRollbackToken;
        packageManagerService.mPendingEnableRollbackToken = i + 1;
        Trace.asyncTraceBegin(262144L, "enable_rollback", i);
        this.mPm.mPendingEnableRollback.append(i, this);
        Intent intent = new Intent("android.intent.action.PACKAGE_ENABLE_ROLLBACK");
        intent.putExtra("android.content.pm.extra.ENABLE_ROLLBACK_TOKEN", i);
        intent.putExtra("android.content.pm.extra.ENABLE_ROLLBACK_SESSION_ID", this.mSessionId);
        intent.setType("application/vnd.android.package-archive");
        intent.addFlags(268435457);
        intent.addFlags(67108864);
        this.mPm.mContext.sendBroadcastAsUser(intent, UserHandle.SYSTEM, "android.permission.PACKAGE_ROLLBACK_AGENT");
        this.mWaitForEnableRollbackToComplete = true;
        long j = DeviceConfig.getLong("rollback", PROPERTY_ENABLE_ROLLBACK_TIMEOUT_MILLIS, 10000L);
        long j2 = j >= 0 ? j : 10000L;
        Message obtainMessage = this.mPm.mHandler.obtainMessage(22);
        obtainMessage.arg1 = i;
        obtainMessage.arg2 = this.mSessionId;
        this.mPm.mHandler.sendMessageDelayed(obtainMessage, j2);
    }

    void sendIntegrityVerificationRequest(final int i, final PackageInfoLite packageInfoLite, PackageVerificationState packageVerificationState) {
        if (!isIntegrityVerificationEnabled()) {
            packageVerificationState.setIntegrityVerificationResult(1);
            return;
        }
        Intent intent = new Intent("android.intent.action.PACKAGE_NEEDS_INTEGRITY_VERIFICATION");
        intent.setDataAndType(Uri.fromFile(new File(this.mOriginInfo.mResolvedPath)), "application/vnd.android.package-archive");
        intent.addFlags(AlarmManagerEconomicPolicy.ACTION_ALARM_WAKEUP_EXACT);
        intent.putExtra("android.content.pm.extra.VERIFICATION_ID", i);
        intent.putExtra("android.intent.extra.PACKAGE_NAME", packageInfoLite.packageName);
        intent.putExtra("android.intent.extra.VERSION_CODE", packageInfoLite.versionCode);
        intent.putExtra("android.intent.extra.LONG_VERSION_CODE", packageInfoLite.getLongVersionCode());
        populateInstallerExtras(intent);
        intent.setPackage(PackageManagerService.PLATFORM_PACKAGE_NAME);
        this.mPm.mContext.sendOrderedBroadcastAsUser(intent, UserHandle.SYSTEM, null, -1, BroadcastOptions.makeBasic().toBundle(), new BroadcastReceiver() { // from class: com.android.server.pm.VerifyingSession.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent2) {
                if (PackageManagerService.DEBUG_INSTALL) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("on IntegrityVerification callback: ");
                    PackageInfoLite packageInfoLite2 = packageInfoLite;
                    sb.append(packageInfoLite2 == null ? null : packageInfoLite2.packageName);
                    Slog.d("PackageManager", sb.toString());
                }
                Message obtainMessage = VerifyingSession.this.mPm.mHandler.obtainMessage(26);
                obtainMessage.arg1 = i;
                VerifyingSession.this.mPm.mHandler.sendMessageDelayed(obtainMessage, VerifyingSession.this.getIntegrityVerificationTimeout());
            }
        }, null, 0, null, null);
        Trace.asyncTraceBegin(262144L, "integrity_verification", i);
        this.mWaitForIntegrityVerificationToComplete = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getIntegrityVerificationTimeout() {
        return Math.max(Settings.Global.getLong(this.mPm.mContext.getContentResolver(), "app_integrity_verification_timeout", DEFAULT_INTEGRITY_VERIFICATION_TIMEOUT), DEFAULT_INTEGRITY_VERIFICATION_TIMEOUT);
    }

    private void sendPackageVerificationRequest(final int i, final PackageInfoLite packageInfoLite, PackageVerificationState packageVerificationState) {
        ArrayList arrayList;
        boolean z;
        String str;
        String str2;
        String str3;
        Intent intent;
        String str4;
        String str5;
        Intent intent2;
        String str6;
        String str7;
        String str8;
        Object obj;
        String str9;
        UserHandle user = getUser();
        if (user == UserHandle.ALL) {
            user = UserHandle.of(this.mPm.mUserManager.getCurrentUserId());
        }
        UserHandle userHandle = user;
        int identifier = userHandle.getIdentifier();
        ArrayList arrayList2 = new ArrayList(Arrays.asList(this.mPm.mRequiredVerifierPackages));
        int i2 = this.mInstallFlags;
        if ((i2 & 32) != 0 && (i2 & 524288) == 0) {
            String str10 = SystemProperties.get("debug.pm.adb_verifier_override_packages", "");
            if (!TextUtils.isEmpty(str10)) {
                String[] split = str10.split(";");
                ArrayList arrayList3 = new ArrayList();
                for (String str11 : split) {
                    if (!TextUtils.isEmpty(str11) && packageExists(str11)) {
                        arrayList3.add(str11);
                    }
                }
                if (arrayList3.size() > 0 && !isAdbVerificationEnabled(packageInfoLite, identifier, true)) {
                    arrayList = arrayList3;
                    z = true;
                    if (!this.mOriginInfo.mExisting || !isVerificationEnabled(packageInfoLite, identifier, arrayList) || this.mPm.mPackageManagerServiceExt.skipVerifyInSendPackageVerificationRequest(arrayList)) {
                        packageVerificationState.passRequiredVerification();
                    }
                    Computer snapshotComputer = this.mPm.snapshotComputer();
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        if (!snapshotComputer.isApplicationEffectivelyEnabled(arrayList.get(size), userHandle)) {
                            Slog.w("PackageManager", "Required verifier: " + arrayList.get(size) + " is disabled");
                            arrayList.remove(size);
                        }
                    }
                    Iterator<String> it = arrayList.iterator();
                    while (it.hasNext()) {
                        packageVerificationState.addRequiredVerifierUid(snapshotComputer.getPackageUid(it.next(), 268435456L, identifier));
                    }
                    Intent intent3 = new Intent("android.intent.action.PACKAGE_NEEDS_VERIFICATION");
                    intent3.addFlags(268435456);
                    intent3.setDataAndType(Uri.fromFile(new File(this.mOriginInfo.mResolvedPath)), "application/vnd.android.package-archive");
                    intent3.addFlags(1);
                    String str12 = "application/vnd.android.package-archive";
                    Computer computer = snapshotComputer;
                    ParceledListSlice<ResolveInfo> queryIntentReceivers = this.mPm.queryIntentReceivers(snapshotComputer, intent3, "application/vnd.android.package-archive", 0L, identifier);
                    if (PackageManagerService.DEBUG_VERIFY) {
                        Slog.d("PackageManager", "Found " + queryIntentReceivers.getList().size() + " verifiers for intent " + intent3.toString() + " with " + packageInfoLite.verifiers.length + " optional verifiers");
                    }
                    Intent intent4 = intent3;
                    intent4.putExtra("android.content.pm.extra.VERIFICATION_ID", i);
                    intent4.putExtra("android.content.pm.extra.VERIFICATION_INSTALL_FLAGS", this.mInstallFlags);
                    intent4.putExtra("android.content.pm.extra.VERIFICATION_PACKAGE_NAME", packageInfoLite.packageName);
                    intent4.putExtra("android.content.pm.extra.VERIFICATION_VERSION_CODE", packageInfoLite.versionCode);
                    intent4.putExtra("android.content.pm.extra.VERIFICATION_LONG_VERSION_CODE", packageInfoLite.getLongVersionCode());
                    String baseApkPath = this.mPackageLite.getBaseApkPath();
                    String[] splitApkPaths = this.mPackageLite.getSplitApkPaths();
                    String str13 = "android.content.pm.extra.VERIFICATION_ROOT_HASH";
                    if (IncrementalManager.isIncrementalPath(baseApkPath)) {
                        String buildVerificationRootHashString = PackageManagerServiceUtils.buildVerificationRootHashString(baseApkPath, splitApkPaths);
                        intent4.putExtra("android.content.pm.extra.VERIFICATION_ROOT_HASH", buildVerificationRootHashString);
                        str = buildVerificationRootHashString;
                    } else {
                        str = null;
                    }
                    String str14 = "android.content.pm.extra.DATA_LOADER_TYPE";
                    intent4.putExtra("android.content.pm.extra.DATA_LOADER_TYPE", this.mDataLoaderType);
                    String str15 = "android.content.pm.extra.SESSION_ID";
                    intent4.putExtra("android.content.pm.extra.SESSION_ID", this.mSessionId);
                    intent4.putExtra("android.content.pm.extra.USER_ACTION_REQUIRED", this.mUserActionRequired);
                    populateInstallerExtras(intent4);
                    boolean z2 = this.mDataLoaderType == 2 && this.mSigningDetails.getSignatureSchemeVersion() == 4 && getDefaultVerificationResponse() == 1;
                    final long verificationTimeout = VerificationUtils.getVerificationTimeout(this.mPm.mContext, z2);
                    List<ComponentName> matchVerifiers = matchVerifiers(packageInfoLite, queryIntentReceivers.getList(), packageVerificationState);
                    final boolean z3 = z2;
                    if (packageInfoLite.isSdkLibrary) {
                        if (matchVerifiers == null) {
                            matchVerifiers = new ArrayList<>();
                        }
                        str2 = "android.content.pm.extra.VERIFICATION_ID";
                        matchVerifiers.add(new ComponentName(PackageManagerService.PLATFORM_PACKAGE_NAME, "com.android.server.sdksandbox.SdkSandboxVerifierReceiver"));
                        packageVerificationState.addSufficientVerifier(Process.myUid());
                    } else {
                        str2 = "android.content.pm.extra.VERIFICATION_ID";
                    }
                    DeviceIdleInternal deviceIdleInternal = (DeviceIdleInternal) this.mPm.mInjector.getLocalService(DeviceIdleInternal.class);
                    BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
                    makeBasic.setTemporaryAppAllowlist(verificationTimeout, 0, 305, "");
                    if (matchVerifiers != null) {
                        int size2 = matchVerifiers.size();
                        if (size2 == 0) {
                            Slog.i("PackageManager", "Additional verifiers required, but none installed.");
                            setReturnCode(-22, "Additional verifiers required, but none installed.");
                        } else {
                            int i3 = 0;
                            while (i3 < size2) {
                                ComponentName componentName = matchVerifiers.get(i3);
                                deviceIdleInternal.addPowerSaveTempWhitelistApp(Process.myUid(), componentName.getPackageName(), verificationTimeout, identifier, false, 305, "package verifier");
                                int i4 = size2;
                                Intent intent5 = new Intent(intent4);
                                intent5.setComponent(componentName);
                                this.mPm.mContext.sendBroadcastAsUser(intent5, userHandle, null, makeBasic.toBundle());
                                i3++;
                                size2 = i4;
                                matchVerifiers = matchVerifiers;
                                deviceIdleInternal = deviceIdleInternal;
                            }
                        }
                    }
                    DeviceIdleInternal deviceIdleInternal2 = deviceIdleInternal;
                    if (arrayList.size() == 0) {
                        Slog.e("PackageManager", "No required verifiers");
                        return;
                    }
                    int i5 = getDefaultVerificationResponse() == 1 ? 2 : -1;
                    for (String str16 : arrayList) {
                        Intent intent6 = intent4;
                        Computer computer2 = computer;
                        int packageUid = computer2.getPackageUid(str16, 268435456L, identifier);
                        if (!z || arrayList.size() == 1) {
                            str3 = str2;
                            intent = intent6;
                            Intent intent7 = new Intent(intent);
                            if (!z) {
                                str4 = str14;
                                intent7.setComponent(matchComponentForVerifier(str16, queryIntentReceivers.getList()));
                            } else {
                                str4 = str14;
                                intent7.setPackage(str16);
                            }
                            str5 = "android.permission.PACKAGE_VERIFICATION_AGENT";
                            intent2 = intent7;
                        } else {
                            Intent intent8 = new Intent("android.intent.action.PACKAGE_NEEDS_VERIFICATION");
                            intent8.addFlags(1);
                            intent8.addFlags(268435456);
                            intent8.addFlags(32);
                            String str17 = str12;
                            intent8.setDataAndType(Uri.fromFile(new File(this.mOriginInfo.mResolvedPath)), str17);
                            intent8.putExtra(str15, this.mSessionId);
                            intent8.putExtra(str14, this.mDataLoaderType);
                            if (str != null) {
                                intent8.putExtra(str13, str);
                            }
                            intent8.setPackage(str16);
                            str12 = str17;
                            str3 = str2;
                            intent8.putExtra(str3, -i);
                            intent2 = intent8;
                            intent = intent6;
                            str5 = null;
                            str4 = str14;
                        }
                        deviceIdleInternal2.addPowerSaveTempWhitelistApp(Process.myUid(), str16, verificationTimeout, identifier, false, 305, "package verifier");
                        final PackageVerificationResponse packageVerificationResponse = new PackageVerificationResponse(i5, packageUid);
                        if (z3) {
                            str8 = str4;
                            str6 = str;
                            str9 = str13;
                            str7 = str15;
                            obj = null;
                            startVerificationTimeoutCountdown(i, z3, packageVerificationResponse, verificationTimeout);
                        } else {
                            str6 = str;
                            str7 = str15;
                            str8 = str4;
                            obj = null;
                            str9 = str13;
                        }
                        this.mPm.mContext.sendOrderedBroadcastAsUser(intent2, userHandle, str5, -1, makeBasic.toBundle(), new BroadcastReceiver() { // from class: com.android.server.pm.VerifyingSession.2
                            @Override // android.content.BroadcastReceiver
                            public void onReceive(Context context, Intent intent9) {
                                if (PackageManagerService.DEBUG_INSTALL) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("on PackageVerification callback: ");
                                    PackageInfoLite packageInfoLite2 = packageInfoLite;
                                    sb.append(packageInfoLite2 == null ? null : packageInfoLite2.packageName);
                                    Slog.d("PackageManager", sb.toString());
                                }
                                boolean z4 = z3;
                                if (z4) {
                                    return;
                                }
                                VerifyingSession.this.startVerificationTimeoutCountdown(i, z4, packageVerificationResponse, verificationTimeout);
                            }
                        }, null, 0, null, null);
                        arrayList = arrayList;
                        i5 = i5;
                        intent4 = intent;
                        str14 = str8;
                        str15 = str7;
                        str13 = str9;
                        identifier = identifier;
                        str2 = str3;
                        userHandle = userHandle;
                        computer = computer2;
                        str = str6;
                    }
                    Trace.asyncTraceBegin(262144L, "verification", i);
                    this.mWaitForVerificationToComplete = true;
                    return;
                }
            }
        }
        arrayList = arrayList2;
        z = false;
        if (!this.mOriginInfo.mExisting) {
        }
        packageVerificationState.passRequiredVerification();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startVerificationTimeoutCountdown(int i, boolean z, PackageVerificationResponse packageVerificationResponse, long j) {
        Message obtainMessage = this.mPm.mHandler.obtainMessage(16);
        obtainMessage.arg1 = i;
        obtainMessage.arg2 = z ? 1 : 0;
        obtainMessage.obj = packageVerificationResponse;
        this.mPm.mHandler.sendMessageDelayed(obtainMessage, j);
    }

    int getDefaultVerificationResponse() {
        if (this.mPm.mUserManager.hasUserRestriction("ensure_verify_apps", getUser().getIdentifier())) {
            return -1;
        }
        return Settings.Global.getInt(this.mPm.mContext.getContentResolver(), "verifier_default_response", 1);
    }

    private boolean packageExists(String str) {
        return this.mPm.snapshotComputer().getPackageStateInternal(str) != null;
    }

    private boolean isAdbVerificationEnabled(PackageInfoLite packageInfoLite, int i, boolean z) {
        boolean z2 = Settings.Global.getInt(this.mPm.mContext.getContentResolver(), "verifier_verify_adb_installs", 1) != 0;
        if (this.mPm.isUserRestricted(i, "ensure_verify_apps")) {
            if (!z2) {
                Slog.w("PackageManager", "Force verification of ADB install because of user restriction.");
            }
            return true;
        }
        if (!z2) {
            return false;
        }
        if (z && packageExists(packageInfoLite.packageName)) {
            return !packageInfoLite.debuggable;
        }
        return true;
    }

    private boolean isVerificationEnabled(PackageInfoLite packageInfoLite, int i, List<String> list) {
        ActivityInfo activityInfo;
        VerificationInfo verificationInfo = this.mVerificationInfo;
        int i2 = verificationInfo == null ? -1 : verificationInfo.mInstallerUid;
        int i3 = this.mInstallFlags;
        boolean z = (524288 & i3) != 0;
        if ((i3 & 32) != 0) {
            return isAdbVerificationEnabled(packageInfoLite, i, z);
        }
        if (z) {
            return false;
        }
        if (isInstant() && (activityInfo = this.mPm.mInstantAppInstallerActivity) != null) {
            String str = activityInfo.packageName;
            for (String str2 : list) {
                if (str.equals(str2)) {
                    try {
                        ((AppOpsManager) this.mPm.mInjector.getSystemService(AppOpsManager.class)).checkPackage(i2, str2);
                        if (PackageManagerService.DEBUG_VERIFY) {
                            Slog.i("PackageManager", "disable verification for instant app");
                        }
                        return false;
                    } catch (SecurityException unused) {
                        continue;
                    }
                }
            }
        }
        return true;
    }

    private List<ComponentName> matchVerifiers(PackageInfoLite packageInfoLite, List<ResolveInfo> list, PackageVerificationState packageVerificationState) {
        int uidForVerifier;
        VerifierInfo[] verifierInfoArr = packageInfoLite.verifiers;
        if (verifierInfoArr == null || verifierInfoArr.length == 0) {
            return null;
        }
        int length = verifierInfoArr.length;
        ArrayList arrayList = new ArrayList(length + 1);
        for (int i = 0; i < length; i++) {
            VerifierInfo verifierInfo = packageInfoLite.verifiers[i];
            ComponentName matchComponentForVerifier = matchComponentForVerifier(verifierInfo.packageName, list);
            if (matchComponentForVerifier != null && (uidForVerifier = this.mInstallPackageHelper.getUidForVerifier(verifierInfo)) != -1) {
                if (PackageManagerService.DEBUG_VERIFY) {
                    Slog.d("PackageManager", "Added sufficient verifier " + verifierInfo.packageName + " with the correct signature");
                }
                arrayList.add(matchComponentForVerifier);
                packageVerificationState.addSufficientVerifier(uidForVerifier);
            }
        }
        return arrayList;
    }

    private static ComponentName matchComponentForVerifier(String str, List<ResolveInfo> list) {
        ActivityInfo activityInfo;
        int size = list.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                activityInfo = null;
                break;
            }
            ResolveInfo resolveInfo = list.get(i);
            ActivityInfo activityInfo2 = resolveInfo.activityInfo;
            if (activityInfo2 != null && str.equals(activityInfo2.packageName)) {
                activityInfo = resolveInfo.activityInfo;
                break;
            }
            i++;
        }
        if (activityInfo == null) {
            return null;
        }
        return new ComponentName(activityInfo.packageName, activityInfo.name);
    }

    void populateInstallerExtras(Intent intent) {
        intent.putExtra("android.content.pm.extra.VERIFICATION_INSTALLER_PACKAGE", this.mInstallSource.mInitiatingPackageName);
        VerificationInfo verificationInfo = this.mVerificationInfo;
        if (verificationInfo != null) {
            Uri uri = verificationInfo.mOriginatingUri;
            if (uri != null) {
                intent.putExtra("android.intent.extra.ORIGINATING_URI", uri);
            }
            Uri uri2 = this.mVerificationInfo.mReferrer;
            if (uri2 != null) {
                intent.putExtra("android.intent.extra.REFERRER", uri2);
            }
            int i = this.mVerificationInfo.mOriginatingUid;
            if (i >= 0) {
                intent.putExtra("android.intent.extra.ORIGINATING_UID", i);
            }
            int i2 = this.mVerificationInfo.mInstallerUid;
            if (i2 >= 0) {
                intent.putExtra("android.content.pm.extra.VERIFICATION_INSTALLER_UID", i2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setReturnCode(int i, String str) {
        if (this.mRet == 1) {
            this.mRet = i;
            this.mErrorMessage = str;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleVerificationFinished() {
        this.mWaitForVerificationToComplete = false;
        handleReturnCode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleIntegrityVerificationFinished() {
        this.mWaitForIntegrityVerificationToComplete = false;
        handleReturnCode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleRollbackEnabled() {
        this.mWaitForEnableRollbackToComplete = false;
        handleReturnCode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleReturnCode() {
        if (PackageManagerService.DEBUG_INSTALL) {
            Slog.d("PackageManager", "handleReturnCode in VerificationParams: " + this.mWaitForVerificationToComplete + ", " + this.mWaitForIntegrityVerificationToComplete + ", " + this.mWaitForEnableRollbackToComplete);
        }
        if (this.mWaitForVerificationToComplete || this.mWaitForIntegrityVerificationToComplete || this.mWaitForEnableRollbackToComplete) {
            return;
        }
        sendVerificationCompleteNotification();
        if (this.mRet != 1) {
            PackageMetrics.onVerificationFailed(this);
        }
    }

    private void sendVerificationCompleteNotification() {
        MultiPackageVerifyingSession multiPackageVerifyingSession = this.mParentVerifyingSession;
        if (multiPackageVerifyingSession != null) {
            multiPackageVerifyingSession.trySendVerificationCompleteNotification(this);
            return;
        }
        try {
            this.mObserver.onPackageInstalled((String) null, this.mRet, this.mErrorMessage, new Bundle());
        } catch (RemoteException unused) {
            Slog.i("PackageManager", "Observer no longer exists.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void start() {
        if (PackageManagerService.DEBUG_INSTALL) {
            Slog.i("PackageManager", "start " + this.mUser + ": " + this);
        }
        Trace.asyncTraceEnd(262144L, "queueVerify", System.identityHashCode(this));
        Trace.traceBegin(262144L, "start");
        handleStartVerify();
        handleReturnCode();
        Trace.traceEnd(262144L);
    }

    public void verifyStage() {
        Trace.asyncTraceBegin(262144L, "queueVerify", System.identityHashCode(this));
        this.mPm.mHandler.post(new Runnable() { // from class: com.android.server.pm.VerifyingSession$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                VerifyingSession.this.start();
            }
        });
    }

    public void verifyStage(List<VerifyingSession> list) throws PackageManagerException {
        final MultiPackageVerifyingSession multiPackageVerifyingSession = new MultiPackageVerifyingSession(this, list);
        this.mPm.mHandler.post(new Runnable() { // from class: com.android.server.pm.VerifyingSession$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                MultiPackageVerifyingSession.this.start();
            }
        });
    }

    public int getRet() {
        return this.mRet;
    }

    public String getErrorMessage() {
        return this.mErrorMessage;
    }

    public UserHandle getUser() {
        return this.mUser;
    }

    public int getSessionId() {
        return this.mSessionId;
    }

    public int getDataLoaderType() {
        return this.mDataLoaderType;
    }

    public int getUserActionRequiredType() {
        return this.mUserActionRequiredType;
    }

    public boolean isInstant() {
        return (this.mInstallFlags & 2048) != 0;
    }

    public boolean isInherit() {
        return this.mIsInherit;
    }

    public int getInstallerPackageUid() {
        return this.mInstallSource.mInstallerPackageUid;
    }

    public boolean isApex() {
        return (this.mInstallFlags & 131072) != 0;
    }

    public boolean isStaged() {
        return this.mIsStaged;
    }

    public IVerifyingSessionWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class VerifyingSessionWrapper implements IVerifyingSessionWrapper {
        private VerifyingSessionWrapper() {
        }

        @Override // com.android.server.pm.IVerifyingSessionWrapper
        public int getRet() {
            return VerifyingSession.this.mRet;
        }

        @Override // com.android.server.pm.IVerifyingSessionWrapper
        public PackageLite getPackageLite() {
            return VerifyingSession.this.mPackageLite;
        }

        @Override // com.android.server.pm.IVerifyingSessionWrapper
        public InstallSource getInstallSource() {
            return VerifyingSession.this.mInstallSource;
        }

        @Override // com.android.server.pm.IVerifyingSessionWrapper
        public UserHandle getUser() {
            return VerifyingSession.this.mUser;
        }
    }
}
