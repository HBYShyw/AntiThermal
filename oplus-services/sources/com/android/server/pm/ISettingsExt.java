package com.android.server.pm;

import android.content.pm.UserInfo;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.pm.pkg.PackageUserStateInternal;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CompletableFuture;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ISettingsExt {
    default void adjustBackupFileDelete(int i, File file) {
    }

    default void adjustInstalledWhenCreateNewUserLI(PackageSetting packageSetting, int i) {
    }

    default boolean adjustInstalledWhenNoFileInRPRLPr(boolean z, int i) {
        return z;
    }

    default void adjustPsAfterReadPackageInRPRLPr(PackageSetting packageSetting, String str, int i, String str2) {
    }

    default boolean adjustShouldReallyInstallInCreateNewUserLI(boolean z, int i, String str, boolean z2) {
        return z;
    }

    default boolean adjustStrategyBeforeFileSync() {
        return true;
    }

    default void afterCreateFutureInCreateNewUserLI(CompletableFuture<Long> completableFuture, PackageSetting packageSetting, int i) {
    }

    default boolean fixDisableStateWhenCreateUser(PackageSetting packageSetting, int i, boolean z) {
        return false;
    }

    default boolean getUserPendingMig(int i) {
        return false;
    }

    default void initPMS(PackageManagerService packageManagerService) {
    }

    default boolean isCustomTagInRPRLPr(String str) {
        return false;
    }

    default void onPrintPackageAttrInDumpPackageLPr(PrintWriter printWriter, PackageSetting packageSetting, UserInfo userInfo) {
    }

    default void onRemoveUserLPw(int i) {
    }

    default void readAndSetCustomPackageAttrInRPRLPr(TypedXmlPullParser typedXmlPullParser, PackageSetting packageSetting, int i) {
    }

    default void readAndSetCustomTagInRPRLPr(String str, TypedXmlPullParser typedXmlPullParser, int i) {
    }

    default void setUserPendingMig(int i, boolean z) {
    }

    default boolean skipCreateAppDataInCreateNewUserLI(boolean z) {
        return false;
    }

    default boolean tryFixWhenUserPackagesStateFileNotFound(int i, File file) {
        return false;
    }

    default void writeCustomPackageAttrInWPRLPr(PackageUserStateInternal packageUserStateInternal, TypedXmlSerializer typedXmlSerializer) throws IOException {
    }

    default void writeCustomTagInWPRLPr(TypedXmlSerializer typedXmlSerializer, int i) throws IOException {
    }
}
