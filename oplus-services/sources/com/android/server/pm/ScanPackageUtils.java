package com.android.server.pm;

import android.content.pm.SharedLibraryInfo;
import android.content.pm.SigningDetails;
import android.content.pm.parsing.result.ParseInput;
import android.content.pm.parsing.result.ParseResult;
import android.content.pm.parsing.result.ParseTypeImpl;
import android.os.Build;
import android.os.Environment;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.util.apk.ApkSignatureVerifier;
import android.util.jar.StrictJarFile;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.server.SystemConfig;
import com.android.server.location.gnss.hal.GnssNative;
import com.android.server.pm.PackageAbiHelper;
import com.android.server.pm.Settings;
import com.android.server.pm.parsing.PackageInfoUtils;
import com.android.server.pm.parsing.library.PackageBackwardCompatibility;
import com.android.server.pm.parsing.pkg.AndroidPackageUtils;
import com.android.server.pm.parsing.pkg.ParsedPackage;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.PackageStateUtils;
import com.android.server.pm.pkg.component.ComponentMutateUtils;
import com.android.server.pm.pkg.component.ParsedActivity;
import com.android.server.pm.pkg.component.ParsedMainComponent;
import com.android.server.pm.pkg.component.ParsedProcess;
import com.android.server.pm.pkg.component.ParsedProvider;
import com.android.server.pm.pkg.component.ParsedService;
import com.android.server.pm.pkg.parsing.ParsingPackageUtils;
import com.android.server.utils.WatchedArraySet;
import dalvik.system.VMRuntime;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class ScanPackageUtils {
    ScanPackageUtils() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:107:0x03b5  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x03fc  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0465  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0470  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x048a  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x04db  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x04f0  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x053c  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x054e  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0560  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x058b  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x0554  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0542  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x049e  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x0479  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x0310  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x0232  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x0215  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x0197  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x011c  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x010b  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x00e8  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00b7  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00d4  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00f5  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0119  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0120  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0212  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x021c  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0261  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x027a  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0332  */
    /* JADX WARN: Type inference failed for: r3v3, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r4v38 */
    /* JADX WARN: Type inference failed for: r4v39, types: [com.android.server.pm.pkg.PackageStateInternal] */
    /* JADX WARN: Type inference failed for: r4v40 */
    /* JADX WARN: Type inference failed for: r8v3 */
    /* JADX WARN: Type inference failed for: r8v4, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r8v5 */
    @GuardedBy({"mPm.mInstallLock"})
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ScanResult scanPackageOnlyLI(ScanRequest scanRequest, PackageManagerServiceInjector packageManagerServiceInjector, boolean z, long j) throws PackageManagerException {
        boolean z2;
        String str;
        String str2;
        String str3;
        String[] strArr;
        boolean z3;
        String[] strArr2;
        boolean z4;
        String str4;
        UserHandle userHandle;
        SharedUserSetting sharedUserSetting;
        String str5;
        boolean z5;
        SharedLibraryInfo sharedLibraryInfo;
        PackageSetting packageSetting;
        int identifier;
        boolean isSystem;
        int i;
        SharedUserSetting sharedUserSetting2;
        int i2;
        String str6;
        int i3;
        long firstInstallTimeMillis;
        String volumeUuid;
        ?? r8;
        boolean z6;
        ?? r4;
        String str7;
        PackageAbiHelper abiHelper = packageManagerServiceInjector.getAbiHelper();
        ParsedPackage parsedPackage = scanRequest.mParsedPackage;
        PackageSetting packageSetting2 = scanRequest.mPkgSetting;
        PackageSetting packageSetting3 = scanRequest.mDisabledPkgSetting;
        PackageSetting packageSetting4 = scanRequest.mOriginalPkgSetting;
        int i4 = scanRequest.mParseFlags;
        int i5 = scanRequest.mScanFlags;
        String str8 = scanRequest.mRealPkgName;
        SharedUserSetting sharedUserSetting3 = scanRequest.mOldSharedUserSetting;
        SharedUserSetting sharedUserSetting4 = scanRequest.mSharedUserSetting;
        UserHandle userHandle2 = scanRequest.mUser;
        boolean z7 = scanRequest.mIsPlatformPackage;
        if (PackageManagerService.DEBUG_PACKAGE_SCANNING && (Integer.MIN_VALUE & i4) != 0) {
            Log.d("PackageManager", "Scanning package " + parsedPackage.getPackageName());
        }
        File file = new File(parsedPackage.getPath());
        boolean z8 = (i5 & 4096) != 0;
        if (z8) {
            z2 = z8;
            str = null;
        } else {
            if (packageSetting2 != null && (packageSetting2.getPkg() == null || !packageSetting2.getPkg().isStub())) {
                z2 = z8;
                str = packageSetting2.getPrimaryCpuAbiLegacy();
                str2 = packageSetting2.getSecondaryCpuAbiLegacy();
                if (packageSetting2 != null && sharedUserSetting3 != sharedUserSetting4) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Package ");
                    sb.append(parsedPackage.getPackageName());
                    sb.append(" shared user changed from ");
                    String str9 = "<nothing>";
                    if (sharedUserSetting3 == null) {
                        str7 = "<nothing>";
                        str9 = sharedUserSetting3.name;
                    } else {
                        str7 = "<nothing>";
                    }
                    sb.append(str9);
                    sb.append(" to ");
                    sb.append(sharedUserSetting4 == null ? sharedUserSetting4.name : str7);
                    sb.append("; replacing with new");
                    PackageManagerService.reportSettingsProblem(5, sb.toString());
                    packageSetting2 = null;
                }
                if (parsedPackage.getUsesSdkLibraries().isEmpty()) {
                    strArr = new String[parsedPackage.getUsesSdkLibraries().size()];
                    str3 = str;
                    parsedPackage.getUsesSdkLibraries().toArray(strArr);
                } else {
                    str3 = str;
                    strArr = null;
                }
                if (parsedPackage.getUsesStaticLibraries().isEmpty()) {
                    String[] strArr3 = new String[parsedPackage.getUsesStaticLibraries().size()];
                    z3 = z7;
                    parsedPackage.getUsesStaticLibraries().toArray(strArr3);
                    strArr2 = strArr3;
                } else {
                    z3 = z7;
                    strArr2 = null;
                }
                UUID generateNewId = packageManagerServiceInjector.getDomainVerificationManagerInternal().generateNewId();
                z4 = packageSetting2 != null;
                if (!z4) {
                    boolean z9 = (i5 & 8192) != 0;
                    boolean z10 = (32768 & i5) != 0;
                    if ((134217728 & i5) != 0) {
                        r4 = 0;
                        z6 = true;
                    } else {
                        z6 = false;
                        r4 = 0;
                    }
                    str4 = " to ";
                    str5 = str3;
                    z5 = z3;
                    userHandle = userHandle2;
                    sharedUserSetting = sharedUserSetting4;
                    packageSetting = Settings.createNewSetting(parsedPackage.getPackageName(), packageSetting4, packageSetting3, str8, sharedUserSetting4, file, parsedPackage.getNativeLibraryRootDir(), AndroidPackageUtils.getRawPrimaryCpuAbi(parsedPackage), AndroidPackageUtils.getRawSecondaryCpuAbi(parsedPackage), parsedPackage.getLongVersionCode(), PackageInfoUtils.appInfoFlags(parsedPackage, (PackageStateInternal) r4), PackageInfoUtils.appInfoPrivateFlags(parsedPackage, (PackageStateInternal) r4), userHandle, true, z9, z10, z6, UserManagerService.getInstance(), strArr, parsedPackage.getUsesSdkLibrariesVersionsMajor(), strArr2, parsedPackage.getUsesStaticLibrariesVersions(), parsedPackage.getMimeGroups(), generateNewId);
                    ((IPackageManagerServiceUtilsExt) ExtLoader.type(IPackageManagerServiceUtilsExt.class).create()).afterCreateNewSettingInScanPackageOnlyLI(parsedPackage, packageSetting, scanRequest);
                    sharedLibraryInfo = r4;
                } else {
                    str4 = " to ";
                    userHandle = userHandle2;
                    sharedUserSetting = sharedUserSetting4;
                    str5 = str3;
                    z5 = z3;
                    sharedLibraryInfo = null;
                    PackageSetting packageSetting5 = new PackageSetting(packageSetting2);
                    packageSetting5.setPkg(parsedPackage);
                    Settings.updatePackageSetting(packageSetting5, packageSetting3, sharedUserSetting3, sharedUserSetting, file, parsedPackage.getNativeLibraryDir(), packageSetting5.getPrimaryCpuAbi(), packageSetting5.getSecondaryCpuAbi(), PackageInfoUtils.appInfoFlags(parsedPackage, packageSetting5), PackageInfoUtils.appInfoPrivateFlags(parsedPackage, packageSetting5), UserManagerService.getInstance(), strArr, parsedPackage.getUsesSdkLibrariesVersionsMajor(), strArr2, parsedPackage.getUsesStaticLibrariesVersions(), parsedPackage.getMimeGroups(), generateNewId);
                    packageSetting = packageSetting5;
                }
                if (z4 && packageSetting4 != null) {
                    parsedPackage.setPackageName(packageSetting4.getPackageName());
                    PackageManagerService.reportSettingsProblem(5, "New package " + packageSetting.getRealName() + " renamed to replace old package " + packageSetting.getPackageName());
                }
                identifier = userHandle != null ? 0 : userHandle.getIdentifier();
                if (!z4) {
                    setInstantAppForUser(packageManagerServiceInjector, packageSetting, identifier, (i5 & 8192) != 0, (i5 & 16384) != 0);
                }
                if (packageSetting3 == null || ((i5 & 4) != 0 && packageSetting != null && packageSetting.isSystem())) {
                    packageSetting.getPkgState().setUpdatedSystemApp(true);
                }
                packageSetting.getTransientState().setSeInfo(SELinuxMMAC.getSeInfo(packageSetting, parsedPackage, sharedUserSetting, packageManagerServiceInjector.getCompatibility()));
                if (packageSetting.isSystem()) {
                    configurePackageComponents(parsedPackage);
                }
                String deriveAbiOverride = PackageManagerServiceUtils.deriveAbiOverride(scanRequest.mCpuAbiOverride);
                isSystem = packageSetting.isSystem();
                boolean isUpdatedSystemApp = packageSetting.isUpdatedSystemApp();
                File appLib32InstallDir = getAppLib32InstallDir();
                i = i5 & 4;
                if (i != 0) {
                    if (z2) {
                        Trace.traceBegin(262144L, "derivePackageAbi");
                        i2 = i5;
                        i3 = i4;
                        Pair<PackageAbiHelper.Abis, PackageAbiHelper.NativeLibraryPaths> derivePackageAbi = abiHelper.derivePackageAbi(parsedPackage, isSystem, isUpdatedSystemApp, deriveAbiOverride, appLib32InstallDir);
                        ((PackageAbiHelper.Abis) derivePackageAbi.first).applyTo(parsedPackage);
                        ((PackageAbiHelper.NativeLibraryPaths) derivePackageAbi.second).applyTo(parsedPackage);
                        Trace.traceEnd(262144L);
                        String rawPrimaryCpuAbi = AndroidPackageUtils.getRawPrimaryCpuAbi(parsedPackage);
                        if (isSystem && !isUpdatedSystemApp && rawPrimaryCpuAbi == null) {
                            PackageAbiHelper.Abis bundledAppAbis = abiHelper.getBundledAppAbis(parsedPackage);
                            bundledAppAbis.applyTo(parsedPackage);
                            bundledAppAbis.applyTo(packageSetting);
                            abiHelper.deriveNativeLibraryPaths(parsedPackage, isSystem, isUpdatedSystemApp, appLib32InstallDir).applyTo(parsedPackage);
                        }
                        sharedUserSetting2 = sharedUserSetting3;
                    } else {
                        sharedUserSetting2 = sharedUserSetting3;
                        i2 = i5;
                        i3 = i4;
                        parsedPackage.setPrimaryCpuAbi(str5).setSecondaryCpuAbi(str2);
                        abiHelper.deriveNativeLibraryPaths(parsedPackage, isSystem, isUpdatedSystemApp, appLib32InstallDir).applyTo(parsedPackage);
                        if (PackageManagerService.DEBUG_ABI_SELECTION) {
                            str6 = "PackageManager";
                            Slog.i(str6, "Using ABIS and native lib paths from settings : " + parsedPackage.getPackageName() + " " + AndroidPackageUtils.getRawPrimaryCpuAbi(parsedPackage) + ", " + AndroidPackageUtils.getRawSecondaryCpuAbi(parsedPackage));
                        }
                    }
                    str6 = "PackageManager";
                } else {
                    sharedUserSetting2 = sharedUserSetting3;
                    i2 = i5;
                    str6 = "PackageManager";
                    i3 = i4;
                    if ((i2 & 256) != 0) {
                        parsedPackage.setPrimaryCpuAbi(packageSetting.getPrimaryCpuAbiLegacy()).setSecondaryCpuAbi(packageSetting.getSecondaryCpuAbiLegacy());
                    }
                    abiHelper.deriveNativeLibraryPaths(parsedPackage, isSystem, isUpdatedSystemApp, appLib32InstallDir).applyTo(parsedPackage);
                }
                if (z5) {
                    if (Build.MTK_HBT_ON_64BIT_ONLY_CHIP) {
                        parsedPackage.setPrimaryCpuAbi(VMRuntime.getRuntime().is64Bit() ? Build.SUPPORTED_64_BIT_ABIS[0] : Build.MTK_HBT_SUPPORTED_32_BIT_ABIS[0]);
                    } else if (Build.QCOM_TANGO_ON_64BIT_ONLY_CHIP) {
                        parsedPackage.setPrimaryCpuAbi(VMRuntime.getRuntime().is64Bit() ? Build.SUPPORTED_64_BIT_ABIS[0] : Build.QCOM_TANGO_SUPPORTED_32_BIT_ABIS[0]);
                    } else {
                        parsedPackage.setPrimaryCpuAbi(VMRuntime.getRuntime().is64Bit() ? Build.SUPPORTED_64_BIT_ABIS[0] : Build.SUPPORTED_32_BIT_ABIS[0]);
                    }
                }
                if ((i2 & 1) == 0 && i != 0 && deriveAbiOverride == null) {
                    Slog.w(str6, "Ignoring persisted ABI override for package " + parsedPackage.getPackageName());
                }
                packageSetting.setPrimaryCpuAbi(AndroidPackageUtils.getRawPrimaryCpuAbi(parsedPackage)).setSecondaryCpuAbi(AndroidPackageUtils.getRawSecondaryCpuAbi(parsedPackage)).setCpuAbiOverride(deriveAbiOverride);
                if (PackageManagerService.DEBUG_ABI_SELECTION) {
                    Slog.d(str6, "Resolved nativeLibraryRoot for " + parsedPackage.getPackageName() + " to root=" + parsedPackage.getNativeLibraryRootDir() + ", to dir=" + parsedPackage.getNativeLibraryDir() + ", isa=" + parsedPackage.isNativeLibraryRootRequiresIsa());
                }
                packageSetting.setLegacyNativeLibraryPath(parsedPackage.getNativeLibraryRootDir());
                if (PackageManagerService.DEBUG_ABI_SELECTION) {
                    Log.d(str6, "Abis for package[" + parsedPackage.getPackageName() + "] are primary=" + packageSetting.getPrimaryCpuAbiLegacy() + " secondary=" + packageSetting.getSecondaryCpuAbiLegacy() + " abiOverride=" + packageSetting.getCpuAbiOverride());
                }
                Object applyAdjustedAbiToSharedUser = ((i2 & 16) == 0 || sharedUserSetting2 == null) ? sharedLibraryInfo : applyAdjustedAbiToSharedUser(sharedUserSetting2, parsedPackage, abiHelper.getAdjustedAbiForSharedUser(sharedUserSetting2.getPackageStates(), parsedPackage));
                parsedPackage.setFactoryTest(!z && parsedPackage.getRequestedPermissions().contains("android.permission.FACTORY_TEST"));
                if (isSystem) {
                    packageSetting.setIsOrphaned(true);
                }
                long lastModifiedTime = PackageManagerServiceUtils.getLastModifiedTime(parsedPackage);
                if (identifier != -1) {
                    firstInstallTimeMillis = PackageStateUtils.getEarliestFirstInstallTime(packageSetting.getUserStates());
                } else {
                    firstInstallTimeMillis = packageSetting.readUserState(identifier).getFirstInstallTimeMillis();
                }
                String str10 = str6;
                if (j == 0) {
                    if (firstInstallTimeMillis == 0) {
                        packageSetting.setFirstInstallTime(j, identifier).setLastUpdateTime(j);
                    } else if ((i2 & 8) != 0) {
                        packageSetting.setLastUpdateTime(j);
                    }
                } else if (firstInstallTimeMillis == 0) {
                    packageSetting.setFirstInstallTime(lastModifiedTime, identifier).setLastUpdateTime(lastModifiedTime);
                } else if ((i3 & 16) != 0 && lastModifiedTime != packageSetting.getLastModifiedTime()) {
                    packageSetting.setLastUpdateTime(lastModifiedTime);
                }
                packageSetting.setLastModifiedTime(lastModifiedTime);
                packageSetting.setPkg(parsedPackage).setFlags(PackageInfoUtils.appInfoFlags(parsedPackage, packageSetting)).setPrivateFlags(PackageInfoUtils.appInfoPrivateFlags(parsedPackage, packageSetting));
                if (parsedPackage.getLongVersionCode() != packageSetting.getVersionCode()) {
                    packageSetting.setLongVersionCode(parsedPackage.getLongVersionCode());
                }
                volumeUuid = parsedPackage.getVolumeUuid();
                if (!Objects.equals(volumeUuid, packageSetting.getVolumeUuid())) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Update");
                    sb2.append(packageSetting.isSystem() ? " system" : "");
                    sb2.append(" package ");
                    sb2.append(parsedPackage.getPackageName());
                    sb2.append(" volume from ");
                    sb2.append(packageSetting.getVolumeUuid());
                    sb2.append(str4);
                    sb2.append(volumeUuid);
                    Slog.i(str10, sb2.toString());
                    packageSetting.setVolumeUuid(volumeUuid);
                }
                SharedLibraryInfo createSharedLibraryForSdk = TextUtils.isEmpty(parsedPackage.getSdkLibraryName()) ? AndroidPackageUtils.createSharedLibraryForSdk(parsedPackage) : sharedLibraryInfo;
                SharedLibraryInfo createSharedLibraryForStatic = TextUtils.isEmpty(parsedPackage.getStaticSharedLibraryName()) ? AndroidPackageUtils.createSharedLibraryForStatic(parsedPackage) : sharedLibraryInfo;
                if (ArrayUtils.isEmpty(parsedPackage.getLibraryNames())) {
                    ArrayList arrayList = new ArrayList(parsedPackage.getLibraryNames().size());
                    Iterator<String> it = parsedPackage.getLibraryNames().iterator();
                    while (it.hasNext()) {
                        arrayList.add(AndroidPackageUtils.createSharedLibraryForDynamic(parsedPackage, it.next()));
                    }
                    r8 = arrayList;
                } else {
                    r8 = sharedLibraryInfo;
                }
                return new ScanResult(scanRequest, packageSetting, applyAdjustedAbiToSharedUser, !z4, -1, createSharedLibraryForSdk, createSharedLibraryForStatic, r8);
            }
            str = null;
            z2 = true;
        }
        str2 = null;
        if (packageSetting2 != null) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Package ");
            sb3.append(parsedPackage.getPackageName());
            sb3.append(" shared user changed from ");
            String str92 = "<nothing>";
            if (sharedUserSetting3 == null) {
            }
            sb3.append(str92);
            sb3.append(" to ");
            sb3.append(sharedUserSetting4 == null ? sharedUserSetting4.name : str7);
            sb3.append("; replacing with new");
            PackageManagerService.reportSettingsProblem(5, sb3.toString());
            packageSetting2 = null;
        }
        if (parsedPackage.getUsesSdkLibraries().isEmpty()) {
        }
        if (parsedPackage.getUsesStaticLibraries().isEmpty()) {
        }
        UUID generateNewId2 = packageManagerServiceInjector.getDomainVerificationManagerInternal().generateNewId();
        if (packageSetting2 != null) {
        }
        if (!z4) {
        }
        if (z4) {
            parsedPackage.setPackageName(packageSetting4.getPackageName());
            PackageManagerService.reportSettingsProblem(5, "New package " + packageSetting.getRealName() + " renamed to replace old package " + packageSetting.getPackageName());
        }
        if (userHandle != null) {
        }
        if (!z4) {
        }
        if (packageSetting3 == null) {
        }
        packageSetting.getPkgState().setUpdatedSystemApp(true);
        packageSetting.getTransientState().setSeInfo(SELinuxMMAC.getSeInfo(packageSetting, parsedPackage, sharedUserSetting, packageManagerServiceInjector.getCompatibility()));
        if (packageSetting.isSystem()) {
        }
        String deriveAbiOverride2 = PackageManagerServiceUtils.deriveAbiOverride(scanRequest.mCpuAbiOverride);
        isSystem = packageSetting.isSystem();
        boolean isUpdatedSystemApp2 = packageSetting.isUpdatedSystemApp();
        File appLib32InstallDir2 = getAppLib32InstallDir();
        i = i5 & 4;
        if (i != 0) {
        }
        if (z5) {
        }
        if ((i2 & 1) == 0) {
            Slog.w(str6, "Ignoring persisted ABI override for package " + parsedPackage.getPackageName());
        }
        packageSetting.setPrimaryCpuAbi(AndroidPackageUtils.getRawPrimaryCpuAbi(parsedPackage)).setSecondaryCpuAbi(AndroidPackageUtils.getRawSecondaryCpuAbi(parsedPackage)).setCpuAbiOverride(deriveAbiOverride2);
        if (PackageManagerService.DEBUG_ABI_SELECTION) {
        }
        packageSetting.setLegacyNativeLibraryPath(parsedPackage.getNativeLibraryRootDir());
        if (PackageManagerService.DEBUG_ABI_SELECTION) {
        }
        if ((i2 & 16) == 0) {
        }
        parsedPackage.setFactoryTest(!z && parsedPackage.getRequestedPermissions().contains("android.permission.FACTORY_TEST"));
        if (isSystem) {
        }
        long lastModifiedTime2 = PackageManagerServiceUtils.getLastModifiedTime(parsedPackage);
        if (identifier != -1) {
        }
        String str102 = str6;
        if (j == 0) {
        }
        packageSetting.setLastModifiedTime(lastModifiedTime2);
        packageSetting.setPkg(parsedPackage).setFlags(PackageInfoUtils.appInfoFlags(parsedPackage, packageSetting)).setPrivateFlags(PackageInfoUtils.appInfoPrivateFlags(parsedPackage, packageSetting));
        if (parsedPackage.getLongVersionCode() != packageSetting.getVersionCode()) {
        }
        volumeUuid = parsedPackage.getVolumeUuid();
        if (!Objects.equals(volumeUuid, packageSetting.getVolumeUuid())) {
        }
        if (TextUtils.isEmpty(parsedPackage.getSdkLibraryName())) {
        }
        if (TextUtils.isEmpty(parsedPackage.getStaticSharedLibraryName())) {
        }
        if (ArrayUtils.isEmpty(parsedPackage.getLibraryNames())) {
        }
        return new ScanResult(scanRequest, packageSetting, applyAdjustedAbiToSharedUser, !z4, -1, createSharedLibraryForSdk, createSharedLibraryForStatic, r8);
    }

    public static int adjustScanFlagsWithPackageSetting(int i, PackageSetting packageSetting, PackageSetting packageSetting2, UserHandle userHandle) {
        if ((i & 4) != 0 && packageSetting2 == null && packageSetting != null && packageSetting.isSystem()) {
            packageSetting2 = packageSetting;
        }
        if (packageSetting2 != null) {
            i |= 65536;
            if ((packageSetting2.getPrivateFlags() & 8) != 0) {
                i |= 131072;
            }
            if ((packageSetting2.getPrivateFlags() & 131072) != 0) {
                i |= DumpState.DUMP_DOMAIN_PREFERRED;
            }
            if ((packageSetting2.getPrivateFlags() & DumpState.DUMP_DOMAIN_PREFERRED) != 0) {
                i |= 524288;
            }
            if ((packageSetting2.getPrivateFlags() & 524288) != 0) {
                i |= 1048576;
            }
            if ((packageSetting2.getPrivateFlags() & DumpState.DUMP_COMPILER_STATS) != 0) {
                i |= DumpState.DUMP_COMPILER_STATS;
            }
            if ((packageSetting2.getPrivateFlags() & 1073741824) != 0) {
                i |= DumpState.DUMP_CHANGES;
            }
        }
        if (packageSetting == null) {
            return i;
        }
        int identifier = userHandle == null ? 0 : userHandle.getIdentifier();
        if (packageSetting.getInstantApp(identifier)) {
            i |= 8192;
        }
        return packageSetting.getVirtualPreload(identifier) ? i | 32768 : i;
    }

    public static void assertCodePolicy(AndroidPackage androidPackage) throws PackageManagerException {
        if (androidPackage.isDeclaredHavingCode() && !apkHasCode(androidPackage.getBaseApkPath())) {
            throw new PackageManagerException(-2, "Package " + androidPackage.getBaseApkPath() + " code is missing");
        }
        if (ArrayUtils.isEmpty(androidPackage.getSplitCodePaths())) {
            return;
        }
        for (int i = 0; i < androidPackage.getSplitCodePaths().length; i++) {
            if (((androidPackage.getSplitFlags()[i] & 4) != 0) && !apkHasCode(androidPackage.getSplitCodePaths()[i])) {
                throw new PackageManagerException(-2, "Package " + androidPackage.getSplitCodePaths()[i] + " code is missing");
            }
        }
    }

    public static void assertStaticSharedLibraryIsValid(AndroidPackage androidPackage, int i) throws PackageManagerException {
        if (androidPackage.getTargetSdkVersion() < 26) {
            throw PackageManagerException.ofInternalError("Packages declaring static-shared libs must target O SDK or higher", -22);
        }
        if ((i & 8192) != 0) {
            throw PackageManagerException.ofInternalError("Packages declaring static-shared libs cannot be instant apps", -23);
        }
        if (!ArrayUtils.isEmpty(androidPackage.getOriginalPackages())) {
            throw PackageManagerException.ofInternalError("Packages declaring static-shared libs cannot be renamed", -24);
        }
        if (!ArrayUtils.isEmpty(androidPackage.getLibraryNames())) {
            throw PackageManagerException.ofInternalError("Packages declaring static-shared libs cannot declare dynamic libs", -25);
        }
        if (androidPackage.getSharedUserId() != null) {
            throw PackageManagerException.ofInternalError("Packages declaring static-shared libs cannot declare shared users", -26);
        }
        if (!androidPackage.getActivities().isEmpty()) {
            throw PackageManagerException.ofInternalError("Static shared libs cannot declare activities", -27);
        }
        if (!androidPackage.getServices().isEmpty()) {
            throw PackageManagerException.ofInternalError("Static shared libs cannot declare services", -28);
        }
        if (!androidPackage.getProviders().isEmpty()) {
            throw PackageManagerException.ofInternalError("Static shared libs cannot declare content providers", -29);
        }
        if (!androidPackage.getReceivers().isEmpty()) {
            throw PackageManagerException.ofInternalError("Static shared libs cannot declare broadcast receivers", -30);
        }
        if (!androidPackage.getPermissionGroups().isEmpty()) {
            throw PackageManagerException.ofInternalError("Static shared libs cannot declare permission groups", -31);
        }
        if (!androidPackage.getAttributions().isEmpty()) {
            throw PackageManagerException.ofInternalError("Static shared libs cannot declare features", -32);
        }
        if (!androidPackage.getPermissions().isEmpty()) {
            throw PackageManagerException.ofInternalError("Static shared libs cannot declare permissions", -33);
        }
        if (!androidPackage.getProtectedBroadcasts().isEmpty()) {
            throw PackageManagerException.ofInternalError("Static shared libs cannot declare protected broadcasts", -34);
        }
        if (androidPackage.getOverlayTarget() != null) {
            throw PackageManagerException.ofInternalError("Static shared libs cannot be overlay targets", -35);
        }
    }

    public static void assertProcessesAreValid(AndroidPackage androidPackage) throws PackageManagerException {
        Map<String, ParsedProcess> processes = androidPackage.getProcesses();
        if (processes.isEmpty()) {
            return;
        }
        if (!processes.containsKey(androidPackage.getProcessName())) {
            throw new PackageManagerException(-122, "Can't install because application tag's process attribute " + androidPackage.getProcessName() + " (in package " + androidPackage.getPackageName() + ") is not included in the <processes> list");
        }
        assertPackageProcesses(androidPackage, androidPackage.getActivities(), processes, "activity");
        assertPackageProcesses(androidPackage, androidPackage.getServices(), processes, "service");
        assertPackageProcesses(androidPackage, androidPackage.getReceivers(), processes, ParsingPackageUtils.TAG_RECEIVER);
        assertPackageProcesses(androidPackage, androidPackage.getProviders(), processes, "provider");
    }

    private static <T extends ParsedMainComponent> void assertPackageProcesses(AndroidPackage androidPackage, List<T> list, Map<String, ParsedProcess> map, String str) throws PackageManagerException {
        if (list == null) {
            return;
        }
        for (int size = list.size() - 1; size >= 0; size--) {
            T t = list.get(size);
            if (!map.containsKey(t.getProcessName())) {
                throw new PackageManagerException(-122, "Can't install because " + str + " " + t.getClassName() + "'s process attribute " + t.getProcessName() + " (in package " + androidPackage.getPackageName() + ") is not included in the <processes> list");
            }
        }
    }

    public static void assertMinSignatureSchemeIsValid(AndroidPackage androidPackage, int i) throws PackageManagerException {
        int minimumSignatureSchemeVersionForTargetSdk = ApkSignatureVerifier.getMinimumSignatureSchemeVersionForTargetSdk(androidPackage.getTargetSdkVersion());
        if (androidPackage.getSigningDetails().getSignatureSchemeVersion() >= minimumSignatureSchemeVersionForTargetSdk) {
            return;
        }
        throw new PackageManagerException(GnssNative.GeofenceCallbacks.GEOFENCE_STATUS_ERROR_INVALID_TRANSITION, "No signature found in package of version " + minimumSignatureSchemeVersionForTargetSdk + " or newer for package " + androidPackage.getPackageName());
    }

    public static String getRealPackageName(AndroidPackage androidPackage, String str, boolean z) {
        if (isPackageRenamed(androidPackage, str)) {
            return AndroidPackageUtils.getRealPackageOrNull(androidPackage, z);
        }
        return null;
    }

    public static boolean isPackageRenamed(AndroidPackage androidPackage, String str) {
        return androidPackage.getOriginalPackages().contains(str);
    }

    public static void ensurePackageRenamed(ParsedPackage parsedPackage, String str) {
        if (!parsedPackage.getOriginalPackages().contains(str) || parsedPackage.getPackageName().equals(str)) {
            return;
        }
        parsedPackage.setPackageName(str);
    }

    public static boolean apkHasCode(String str) {
        StrictJarFile strictJarFile;
        StrictJarFile strictJarFile2 = null;
        try {
            strictJarFile = new StrictJarFile(str, false, false);
        } catch (IOException unused) {
        } catch (Throwable th) {
            th = th;
        }
        try {
            boolean z = strictJarFile.findEntry("classes.dex") != null;
            try {
                strictJarFile.close();
            } catch (IOException unused2) {
            }
            return z;
        } catch (IOException unused3) {
            strictJarFile2 = strictJarFile;
            if (strictJarFile2 != null) {
                try {
                    strictJarFile2.close();
                } catch (IOException unused4) {
                }
            }
            return false;
        } catch (Throwable th2) {
            th = th2;
            strictJarFile2 = strictJarFile;
            if (strictJarFile2 != null) {
                try {
                    strictJarFile2.close();
                } catch (IOException unused5) {
                }
            }
            throw th;
        }
    }

    public static void configurePackageComponents(AndroidPackage androidPackage) {
        ArrayMap componentsEnabledStates = SystemConfig.getInstance().getComponentsEnabledStates(androidPackage.getPackageName());
        if (componentsEnabledStates == null) {
            return;
        }
        for (int size = ArrayUtils.size(androidPackage.getActivities()) - 1; size >= 0; size--) {
            ParsedActivity parsedActivity = androidPackage.getActivities().get(size);
            Boolean bool = (Boolean) componentsEnabledStates.get(parsedActivity.getName());
            if (bool != null) {
                ComponentMutateUtils.setEnabled(parsedActivity, bool.booleanValue());
            }
        }
        for (int size2 = ArrayUtils.size(androidPackage.getReceivers()) - 1; size2 >= 0; size2--) {
            ParsedActivity parsedActivity2 = androidPackage.getReceivers().get(size2);
            Boolean bool2 = (Boolean) componentsEnabledStates.get(parsedActivity2.getName());
            if (bool2 != null) {
                ComponentMutateUtils.setEnabled(parsedActivity2, bool2.booleanValue());
            }
        }
        for (int size3 = ArrayUtils.size(androidPackage.getProviders()) - 1; size3 >= 0; size3--) {
            ParsedProvider parsedProvider = androidPackage.getProviders().get(size3);
            Boolean bool3 = (Boolean) componentsEnabledStates.get(parsedProvider.getName());
            if (bool3 != null) {
                ComponentMutateUtils.setEnabled(parsedProvider, bool3.booleanValue());
            }
        }
        for (int size4 = ArrayUtils.size(androidPackage.getServices()) - 1; size4 >= 0; size4--) {
            ParsedService parsedService = androidPackage.getServices().get(size4);
            Boolean bool4 = (Boolean) componentsEnabledStates.get(parsedService.getName());
            if (bool4 != null) {
                ComponentMutateUtils.setEnabled(parsedService, bool4.booleanValue());
            }
        }
    }

    public static int getVendorPartitionVersion() {
        String str = SystemProperties.get("ro.vndk.version");
        if (str.isEmpty()) {
            return 28;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return ArrayUtils.contains(Build.VERSION.ACTIVE_CODENAMES, str) ? 10000 : 28;
        }
    }

    public static void applyPolicy(ParsedPackage parsedPackage, int i, AndroidPackage androidPackage, boolean z) {
        boolean z2;
        boolean z3 = true;
        if ((65536 & i) != 0) {
            parsedPackage.setSystem(true);
            if (parsedPackage.isDirectBootAware()) {
                parsedPackage.setAllComponentsDirectBootAware(true);
            }
            if (PackageManagerServiceUtils.compressedFileExists(parsedPackage.getPath())) {
                parsedPackage.setStub(true);
            }
            z2 = true;
        } else {
            parsedPackage.clearProtectedBroadcasts().setCoreApp(false).setPersistent(false).setDefaultToDeviceProtectedStorage(false).setDirectBootAware(false).capPermissionPriorities();
            z2 = z;
        }
        int i2 = 131072 & i;
        if (i2 == 0) {
            parsedPackage.markNotActivitiesAsNotExportedIfSingleUser();
        }
        parsedPackage.setApex((67108864 & i) != 0);
        parsedPackage.setPrivileged(i2 != 0).setOem((262144 & i) != 0).setVendor((524288 & i) != 0).setProduct((1048576 & i) != 0).setSystemExt((2097152 & i) != 0).setOdm((i & DumpState.DUMP_CHANGES) != 0);
        if (!PackageManagerService.PLATFORM_PACKAGE_NAME.equals(parsedPackage.getPackageName()) && (androidPackage == null || PackageManagerServiceUtils.compareSignatures(androidPackage.getSigningDetails().getSignatures(), parsedPackage.getSigningDetails().getSignatures()) != 0)) {
            z3 = false;
        }
        parsedPackage.setSignedWithPlatformKey(z3);
        if (!z2) {
            parsedPackage.clearOriginalPackages().clearAdoptPermissions();
        }
        PackageBackwardCompatibility.modifySharedLibraries(parsedPackage, z2, z);
    }

    public static List<String> applyAdjustedAbiToSharedUser(SharedUserSetting sharedUserSetting, ParsedPackage parsedPackage, String str) {
        if (parsedPackage != null) {
            parsedPackage.setPrimaryCpuAbi(str);
        }
        WatchedArraySet<PackageSetting> packageSettings = sharedUserSetting.getPackageSettings();
        ArrayList arrayList = null;
        for (int i = 0; i < packageSettings.size(); i++) {
            PackageSetting valueAt = packageSettings.valueAt(i);
            if ((parsedPackage == null || !parsedPackage.getPackageName().equals(valueAt.getPackageName())) && valueAt.getPrimaryCpuAbiLegacy() == null) {
                valueAt.setPrimaryCpuAbi(str);
                valueAt.onChanged();
                if (valueAt.getPkg() != null && !TextUtils.equals(str, AndroidPackageUtils.getRawPrimaryCpuAbi(valueAt.getPkg()))) {
                    if (PackageManagerService.DEBUG_ABI_SELECTION) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Adjusting ABI for ");
                        sb.append(valueAt.getPackageName());
                        sb.append(" to ");
                        sb.append(str);
                        sb.append(" (scannedPackage=");
                        sb.append(parsedPackage != null ? parsedPackage : "null");
                        sb.append(")");
                        Slog.i("PackageManager", sb.toString());
                    }
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(valueAt.getPathString());
                }
            }
        }
        return arrayList;
    }

    public static void collectCertificatesLI(PackageSetting packageSetting, ParsedPackage parsedPackage, Settings.VersionInfo versionInfo, boolean z, boolean z2, boolean z3) throws PackageManagerException {
        long lastModifiedTime;
        if (z3) {
            lastModifiedTime = new File(parsedPackage.getPath()).lastModified();
        } else {
            lastModifiedTime = PackageManagerServiceUtils.getLastModifiedTime(parsedPackage);
        }
        if (packageSetting != null && !z && packageSetting.getPathString().equals(parsedPackage.getPath()) && packageSetting.getLastModifiedTime() == lastModifiedTime && !ReconcilePackageUtils.isCompatSignatureUpdateNeeded(versionInfo) && !ReconcilePackageUtils.isRecoverSignatureUpdateNeeded(versionInfo)) {
            if (packageSetting.getSigningDetails().getSignatures() != null && packageSetting.getSigningDetails().getSignatures().length != 0 && packageSetting.getSigningDetails().getSignatureSchemeVersion() != 0) {
                parsedPackage.setSigningDetails(new SigningDetails(packageSetting.getSigningDetails()));
                return;
            }
            Slog.w("PackageManager", "PackageSetting for " + packageSetting.getPackageName() + " is missing signatures.  Collecting certs again to recover them.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(parsedPackage.getPath());
            sb.append(" changed; collecting certs");
            sb.append(z ? " (forced)" : "");
            Slog.i("PackageManager", sb.toString());
        }
        try {
            Trace.traceBegin(262144L, "collectCertificates");
            ParseResult<SigningDetails> signingDetails = ParsingPackageUtils.getSigningDetails((ParseInput) ParseTypeImpl.forDefaultParsing(), parsedPackage, z2);
            if (signingDetails.isError()) {
                throw new PackageManagerException(signingDetails.getErrorCode(), signingDetails.getErrorMessage(), signingDetails.getException());
            }
            parsedPackage.setSigningDetails((SigningDetails) signingDetails.getResult());
        } finally {
            Trace.traceEnd(262144L);
        }
    }

    public static void setInstantAppForUser(PackageManagerServiceInjector packageManagerServiceInjector, PackageSetting packageSetting, int i, boolean z, boolean z2) {
        if (z || z2) {
            if (i != -1) {
                if (z && !packageSetting.getInstantApp(i)) {
                    packageSetting.setInstantApp(true, i);
                    return;
                } else {
                    if (z2 && packageSetting.getInstantApp(i)) {
                        packageSetting.setInstantApp(false, i);
                        return;
                    }
                    return;
                }
            }
            for (int i2 : packageManagerServiceInjector.getUserManagerInternal().getUserIds()) {
                if (z && !packageSetting.getInstantApp(i2)) {
                    packageSetting.setInstantApp(true, i2);
                } else if (z2 && packageSetting.getInstantApp(i2)) {
                    packageSetting.setInstantApp(false, i2);
                }
            }
        }
    }

    public static File getAppLib32InstallDir() {
        return new File(Environment.getDataDirectory(), "app-lib");
    }
}
