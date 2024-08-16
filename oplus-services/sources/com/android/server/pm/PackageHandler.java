package com.android.server.pm;

import android.content.Context;
import android.content.Intent;
import android.content.pm.InstantAppRequest;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.util.Slog;
import com.android.server.hdmi.HdmiCecKeycode;
import com.android.server.job.controllers.JobStatus;
import java.io.IOException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class PackageHandler extends Handler {
    private final InstallPackageHelper mInstallPackageHelper;
    private final PackageManagerService mPm;
    private final RemovePackageHelper mRemovePackageHelper;

    private int getDefaultIntegrityVerificationResponse() {
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PackageHandler(Looper looper, PackageManagerService packageManagerService) {
        super(looper);
        this.mPm = packageManagerService;
        this.mInstallPackageHelper = new InstallPackageHelper(packageManagerService);
        this.mRemovePackageHelper = new RemovePackageHelper(packageManagerService);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        try {
            doHandleMessage(message);
        } finally {
            Process.setThreadPriority(0);
        }
    }

    void doHandleMessage(Message message) {
        if (PackageManagerService.DEBUG_INSTALL) {
            Slog.d("PackageManager", "doHandleMessage: " + message.what);
        }
        int i = message.what;
        if (i == 1) {
            this.mInstallPackageHelper.sendPendingBroadcasts();
            return;
        }
        if (i == 9) {
            if (PackageManagerService.DEBUG_INSTALL) {
                Log.v("PackageManager", "Handling post-install for " + message.arg1);
            }
            InstallRequest installRequest = this.mPm.mRunningInstalls.get(message.arg1);
            boolean z = message.arg2 != 0;
            this.mPm.mRunningInstalls.delete(message.arg1);
            installRequest.closeFreezer();
            installRequest.runPostInstallRunnable();
            if (!installRequest.isInstallExistingForUser()) {
                this.mInstallPackageHelper.handlePackagePostInstall(installRequest, z);
                this.mPm.mPackageManagerServiceExt.writeMdmLog("005", installRequest.getReturnCode() == 1 ? "1" : "0", installRequest.getPkg() != null ? installRequest.getPkg().getPackageName() : installRequest.getName());
                this.mPm.mPackageManagerServiceExt.afterHandlePackagePostInstallInCasePostInstall(installRequest);
            } else if (PackageManagerService.DEBUG_INSTALL) {
                Slog.i("PackageManager", "Nothing to do for post-install token " + message.arg1);
            }
            Trace.asyncTraceEnd(262144L, "postInstall", message.arg1);
            return;
        }
        if (i == 13) {
            this.mPm.writeSettings(false);
            return;
        }
        if (i == 15) {
            int i2 = message.arg1;
            PackageVerificationState packageVerificationState = this.mPm.mPendingVerification.get(i2);
            if (packageVerificationState == null) {
                Slog.w("PackageManager", "Verification with id " + i2 + " not found. It may be invalid or overridden by integrity verification");
                return;
            }
            if (packageVerificationState.isVerificationComplete()) {
                Slog.w("PackageManager", "Verification with id " + i2 + " already complete.");
                return;
            }
            VerificationUtils.processVerificationResponse(i2, packageVerificationState, (PackageVerificationResponse) message.obj, this.mPm);
            return;
        }
        if (i != 16) {
            switch (i) {
                case 19:
                    this.mPm.writePackageList(message.arg1);
                    return;
                case 20:
                    PackageManagerService packageManagerService = this.mPm;
                    Context context = packageManagerService.mContext;
                    Computer snapshotComputer = packageManagerService.snapshotComputer();
                    PackageManagerService packageManagerService2 = this.mPm;
                    InstantAppResolver.doInstantAppResolutionPhaseTwo(context, snapshotComputer, packageManagerService2.mUserManager, packageManagerService2.mInstantAppResolverConnection, (InstantAppRequest) message.obj, packageManagerService2.mInstantAppInstallerActivity, packageManagerService2.mHandler);
                    return;
                case 21:
                    int i3 = message.arg1;
                    int i4 = message.arg2;
                    VerifyingSession verifyingSession = this.mPm.mPendingEnableRollback.get(i3);
                    if (verifyingSession == null) {
                        Slog.w("PackageManager", "Invalid rollback enabled token " + i3 + " received");
                        return;
                    }
                    this.mPm.mPendingEnableRollback.remove(i3);
                    if (i4 != 1) {
                        Uri fromFile = Uri.fromFile(verifyingSession.mOriginInfo.mResolvedFile);
                        Slog.w("PackageManager", "Failed to enable rollback for " + fromFile);
                        Slog.w("PackageManager", "Continuing with installation of " + fromFile);
                    }
                    Trace.asyncTraceEnd(262144L, "enable_rollback", i3);
                    verifyingSession.handleRollbackEnabled();
                    return;
                case 22:
                    int i5 = message.arg1;
                    int i6 = message.arg2;
                    VerifyingSession verifyingSession2 = this.mPm.mPendingEnableRollback.get(i5);
                    if (verifyingSession2 != null) {
                        Uri fromFile2 = Uri.fromFile(verifyingSession2.mOriginInfo.mResolvedFile);
                        Slog.w("PackageManager", "Enable rollback timed out for " + fromFile2);
                        this.mPm.mPendingEnableRollback.remove(i5);
                        Slog.w("PackageManager", "Continuing with installation of " + fromFile2);
                        Trace.asyncTraceEnd(262144L, "enable_rollback", i5);
                        verifyingSession2.handleRollbackEnabled();
                        Intent intent = new Intent("android.intent.action.CANCEL_ENABLE_ROLLBACK");
                        intent.putExtra("android.content.pm.extra.ENABLE_ROLLBACK_SESSION_ID", i6);
                        intent.addFlags(335544320);
                        this.mPm.mContext.sendBroadcastAsUser(intent, UserHandle.SYSTEM, "android.permission.PACKAGE_ROLLBACK_AGENT");
                        return;
                    }
                    return;
                case PackageManagerService.MIN_INSTALLABLE_TARGET_SDK /* 23 */:
                    InstallArgs installArgs = (InstallArgs) message.obj;
                    if (installArgs != null) {
                        this.mRemovePackageHelper.cleanUpResources(installArgs.mCodeFile, installArgs.mInstructionSets);
                        return;
                    }
                    return;
                case 24:
                case HdmiCecKeycode.CEC_KEYCODE_NUMBER_ENTRY_MODE /* 29 */:
                    String str = (String) message.obj;
                    if (str != null) {
                        this.mPm.notifyInstallObserver(str, i == 29);
                        return;
                    }
                    return;
                case 25:
                    int i7 = message.arg1;
                    PackageVerificationState packageVerificationState2 = this.mPm.mPendingVerification.get(i7);
                    if (packageVerificationState2 == null) {
                        Slog.w("PackageManager", "Integrity verification with id " + i7 + " not found. It may be invalid or overridden by verifier");
                        return;
                    }
                    int intValue = ((Integer) message.obj).intValue();
                    VerifyingSession verifyingSession3 = packageVerificationState2.getVerifyingSession();
                    Uri fromFile3 = Uri.fromFile(verifyingSession3.mOriginInfo.mResolvedFile);
                    packageVerificationState2.setIntegrityVerificationResult(intValue);
                    if (intValue == 1) {
                        Slog.i("PackageManager", "Integrity check passed for " + fromFile3);
                    } else {
                        verifyingSession3.setReturnCode(-22, "Integrity check failed for " + fromFile3);
                    }
                    if (packageVerificationState2.areAllVerificationsComplete()) {
                        this.mPm.mPendingVerification.remove(i7);
                    }
                    Trace.asyncTraceEnd(262144L, "integrity_verification", i7);
                    verifyingSession3.handleIntegrityVerificationFinished();
                    return;
                case 26:
                    int i8 = message.arg1;
                    PackageVerificationState packageVerificationState3 = this.mPm.mPendingVerification.get(i8);
                    if (packageVerificationState3 == null || packageVerificationState3.isIntegrityVerificationComplete()) {
                        return;
                    }
                    VerifyingSession verifyingSession4 = packageVerificationState3.getVerifyingSession();
                    Uri fromFile4 = Uri.fromFile(verifyingSession4.mOriginInfo.mResolvedFile);
                    String str2 = "Integrity verification timed out for " + fromFile4;
                    Slog.i("PackageManager", str2);
                    packageVerificationState3.setIntegrityVerificationResult(getDefaultIntegrityVerificationResponse());
                    if (getDefaultIntegrityVerificationResponse() == 1) {
                        Slog.i("PackageManager", "Integrity check times out, continuing with " + fromFile4);
                    } else {
                        verifyingSession4.setReturnCode(-22, str2);
                    }
                    if (packageVerificationState3.areAllVerificationsComplete()) {
                        this.mPm.mPendingVerification.remove(i8);
                    }
                    Trace.asyncTraceEnd(262144L, "integrity_verification", i8);
                    verifyingSession4.handleIntegrityVerificationFinished();
                    return;
                case 27:
                    this.mPm.mDomainVerificationManager.runMessage(message.arg1, message.obj);
                    return;
                case 28:
                    try {
                        this.mPm.mInjector.getSharedLibrariesImpl().pruneUnusedStaticSharedLibraries(this.mPm.snapshotComputer(), JobStatus.NO_LATEST_RUNTIME, Settings.Global.getLong(this.mPm.mContext.getContentResolver(), "unused_static_shared_lib_min_cache_period", PackageManagerService.DEFAULT_UNUSED_STATIC_SHARED_LIB_MIN_CACHE_PERIOD));
                        return;
                    } catch (IOException e) {
                        Log.w("PackageManager", "Failed to prune unused static shared libraries :" + e.getMessage());
                        return;
                    }
                default:
                    this.mPm.mPackageManagerServiceExt.customHandleMsgInPackageHandler(message);
                    return;
            }
        }
        int i9 = message.arg1;
        boolean z2 = message.arg2 != 0;
        PackageVerificationState packageVerificationState4 = this.mPm.mPendingVerification.get(i9);
        if (packageVerificationState4 == null || packageVerificationState4.isVerificationComplete()) {
            return;
        }
        PackageVerificationResponse packageVerificationResponse = (PackageVerificationResponse) message.obj;
        if (z2 || !packageVerificationState4.timeoutExtended(packageVerificationResponse.callerUid)) {
            VerificationUtils.processVerificationResponseOnTimeout(i9, packageVerificationState4, packageVerificationResponse, this.mPm);
        }
    }
}
