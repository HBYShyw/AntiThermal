package com.android.server.pm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class VerificationUtils {
    private static final long DEFAULT_STREAMING_VERIFICATION_TIMEOUT = 3000;
    private static final long DEFAULT_VERIFICATION_TIMEOUT = 10000;

    VerificationUtils() {
    }

    public static long getVerificationTimeout(Context context, boolean z) {
        if (z) {
            return getDefaultStreamingVerificationTimeout(context);
        }
        return getDefaultVerificationTimeout(context);
    }

    public static long getDefaultVerificationTimeout(Context context) {
        return Math.max(Settings.Global.getLong(context.getContentResolver(), "verifier_timeout", 10000L), 10000L);
    }

    public static long getDefaultStreamingVerificationTimeout(Context context) {
        return Math.max(Settings.Global.getLong(context.getContentResolver(), "streaming_verifier_timeout", DEFAULT_STREAMING_VERIFICATION_TIMEOUT), DEFAULT_STREAMING_VERIFICATION_TIMEOUT);
    }

    public static void broadcastPackageVerified(int i, Uri uri, int i2, String str, int i3, UserHandle userHandle, Context context) {
        Intent intent = new Intent("android.intent.action.PACKAGE_VERIFIED");
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(1);
        intent.putExtra("android.content.pm.extra.VERIFICATION_ID", i);
        intent.putExtra("android.content.pm.extra.VERIFICATION_RESULT", i2);
        if (str != null) {
            intent.putExtra("android.content.pm.extra.VERIFICATION_ROOT_HASH", str);
        }
        intent.putExtra("android.content.pm.extra.DATA_LOADER_TYPE", i3);
        context.sendBroadcastAsUser(intent, userHandle, "android.permission.PACKAGE_VERIFICATION_AGENT");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public static void processVerificationResponseOnTimeout(int i, PackageVerificationState packageVerificationState, PackageVerificationResponse packageVerificationResponse, PackageManagerService packageManagerService) {
        packageVerificationState.setVerifierResponseOnTimeout(packageVerificationResponse.callerUid, packageVerificationResponse.code);
        processVerificationResponse(i, packageVerificationState, packageVerificationResponse.code, "Verification timed out", packageManagerService);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public static void processVerificationResponse(int i, PackageVerificationState packageVerificationState, PackageVerificationResponse packageVerificationResponse, PackageManagerService packageManagerService) {
        packageVerificationState.setVerifierResponse(packageVerificationResponse.callerUid, packageVerificationResponse.code);
        processVerificationResponse(i, packageVerificationState, packageVerificationResponse.code, "Install not allowed", packageManagerService);
    }

    private static void processVerificationResponse(int i, PackageVerificationState packageVerificationState, int i2, String str, PackageManagerService packageManagerService) {
        if (packageVerificationState.isVerificationComplete()) {
            VerifyingSession verifyingSession = packageVerificationState.getVerifyingSession();
            Uri fromFile = verifyingSession != null ? Uri.fromFile(verifyingSession.mOriginInfo.mResolvedFile) : null;
            if (!packageVerificationState.isInstallAllowed()) {
                i2 = -1;
            }
            int i3 = i2;
            if (packageManagerService != null && verifyingSession != null) {
                broadcastPackageVerified(i, fromFile, i3, null, verifyingSession.getDataLoaderType(), verifyingSession.getUser(), packageManagerService.mContext);
            }
            if (packageVerificationState.isInstallAllowed()) {
                Slog.i("PackageManager", "Continuing with installation of " + fromFile);
            } else {
                String str2 = str + " for " + fromFile;
                Slog.i("PackageManager", str2);
                if (verifyingSession != null) {
                    verifyingSession.setReturnCode(-22, str2);
                    packageManagerService.mPackageManagerServiceExt.afterSetVerifyFailInCasePackageVerified(verifyingSession);
                }
            }
            if (packageManagerService != null && packageVerificationState.areAllVerificationsComplete()) {
                packageManagerService.mPendingVerification.remove(i);
            }
            Trace.asyncTraceEnd(262144L, "verification", i);
            if (verifyingSession != null) {
                verifyingSession.handleVerificationFinished();
            }
        }
    }
}
