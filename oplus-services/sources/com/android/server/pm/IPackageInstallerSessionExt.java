package com.android.server.pm;

import android.content.pm.parsing.ApkLite;
import android.content.pm.parsing.result.ParseResult;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import com.android.server.pm.StagingManager;
import java.io.File;
import java.io.IOException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPackageInstallerSessionExt {
    default void adjustAssertShellOrSystemCallingThrowException(PackageManagerService packageManagerService, String str) {
    }

    default ParcelFileDescriptor adjustResultInOpenWrite(ParcelFileDescriptor parcelFileDescriptor, String str, Handler handler, PackageManagerService packageManagerService, int i, File file) throws IOException {
        return parcelFileDescriptor;
    }

    default boolean adjustUserActionPendingInComputeUserActionRequirement() {
        return true;
    }

    default void afterDispatchSessionFinished(PackageInstallerSession packageInstallerSession, PackageManagerService packageManagerService) {
    }

    default void afterWrite(String str, Handler handler, PackageManagerService packageManagerService, int i, File file) {
    }

    default void beforeCreateOatDirs() {
    }

    default void beforeDispatchSessionFinished(PackageInstallerSession packageInstallerSession) {
    }

    default void beforeHandleStreamValidateAndCommit() {
    }

    default void beforeOpenInDoWriteInternal(File file) {
    }

    default void beforeRequestUserPreapprovalAvailable(String str) {
    }

    default void beforeSessionCommit(PackageInstallerSession packageInstallerSession) {
    }

    default void checkMainlineLimited(PackageInstallerSession packageInstallerSession) throws PackageManagerException {
    }

    default ParseResult<ApkLite> getPreParseRetInValidateApkInstall(File file) {
        return null;
    }

    default void handleInHandlerCallback(Message message, PackageInstallerSession packageInstallerSession, PackageManagerService packageManagerService) {
    }

    default boolean hasPreExtractNativeLibsFinished() {
        return false;
    }

    default boolean hasSilentInstallationPermissions(Computer computer, String str, int i) {
        return false;
    }

    default void onStagedSessionVerificationComplete(PackageInstallerSession packageInstallerSession, StagingManager.StagedSession stagedSession, PackageManagerService packageManagerService) {
    }

    default void recordSotaAppResult(StagingManager.StagedSession stagedSession, int i, String str) {
    }
}
