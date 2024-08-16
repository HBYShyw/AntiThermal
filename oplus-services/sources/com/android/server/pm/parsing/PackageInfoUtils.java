package com.android.server.pm.parsing;

import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.Attribution;
import android.content.pm.ComponentInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FallbackCategoryProvider;
import android.content.pm.FeatureGroupInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PathPermission;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProcessInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.Signature;
import android.content.pm.SigningDetails;
import android.content.pm.SigningInfo;
import android.content.pm.overlay.OverlayPaths;
import android.os.Bundle;
import android.os.Environment;
import android.os.PatternMatcher;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Pair;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.server.SystemConfig;
import com.android.server.pm.DumpState;
import com.android.server.pm.PackageManagerService;
import com.android.server.pm.parsing.pkg.AndroidPackageUtils;
import com.android.server.pm.parsing.pkg.PackageImpl;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.PackageStateUnserialized;
import com.android.server.pm.pkg.PackageUserState;
import com.android.server.pm.pkg.PackageUserStateInternal;
import com.android.server.pm.pkg.PackageUserStateUtils;
import com.android.server.pm.pkg.SELinuxUtil;
import com.android.server.pm.pkg.SharedLibraryWrapper;
import com.android.server.pm.pkg.component.ComponentParseUtils;
import com.android.server.pm.pkg.component.ParsedActivity;
import com.android.server.pm.pkg.component.ParsedAttribution;
import com.android.server.pm.pkg.component.ParsedComponent;
import com.android.server.pm.pkg.component.ParsedInstrumentation;
import com.android.server.pm.pkg.component.ParsedMainComponent;
import com.android.server.pm.pkg.component.ParsedPermission;
import com.android.server.pm.pkg.component.ParsedPermissionGroup;
import com.android.server.pm.pkg.component.ParsedProcess;
import com.android.server.pm.pkg.component.ParsedProvider;
import com.android.server.pm.pkg.component.ParsedService;
import com.android.server.pm.pkg.component.ParsedUsesPermission;
import com.android.server.pm.pkg.parsing.IParsingPackageUtilsExt;
import com.android.server.pm.pkg.parsing.ParsingPackageUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageInfoUtils {
    private static final String SYSTEM_DATA_PATH = Environment.getDataDirectoryPath() + File.separator + "system";
    private static final String TAG = "PackageParsing";

    public static int appInfoPrivateFlags(int i, PackageStateInternal packageStateInternal) {
        return i;
    }

    private static int flag(boolean z, int i) {
        if (z) {
            return i;
        }
        return 0;
    }

    public static PackageInfo generate(AndroidPackage androidPackage, int[] iArr, long j, long j2, long j3, Set<String> set, Set<String> set2, PackageUserStateInternal packageUserStateInternal, int i, PackageStateInternal packageStateInternal) {
        return generateWithComponents(androidPackage, iArr, j, j2, j3, set, set2, packageUserStateInternal, i, packageStateInternal);
    }

    private static PackageInfo generateWithComponents(AndroidPackage androidPackage, int[] iArr, long j, long j2, long j3, Set<String> set, Set<String> set2, PackageUserStateInternal packageUserStateInternal, int i, PackageStateInternal packageStateInternal) {
        int size;
        int size2;
        int size3;
        int size4;
        int size5;
        int i2;
        ActivityInfo[] activityInfoArr;
        int i3;
        ApplicationInfo generateApplicationInfo = generateApplicationInfo(androidPackage, j, packageUserStateInternal, i, packageStateInternal);
        if (generateApplicationInfo == null) {
            return null;
        }
        PackageInfo packageInfo = new PackageInfo();
        packageInfo.packageName = androidPackage.getPackageName();
        packageInfo.splitNames = androidPackage.getSplitNames();
        AndroidPackageUtils.fillVersionCodes(androidPackage, packageInfo);
        packageInfo.baseRevisionCode = androidPackage.getBaseRevisionCode();
        packageInfo.splitRevisionCodes = androidPackage.getSplitRevisionCodes();
        packageInfo.versionName = androidPackage.getVersionName();
        packageInfo.sharedUserId = androidPackage.getSharedUserId();
        packageInfo.sharedUserLabel = androidPackage.getSharedUserLabelResourceId();
        packageInfo.applicationInfo = generateApplicationInfo;
        packageInfo.installLocation = androidPackage.getInstallLocation();
        int i4 = packageInfo.applicationInfo.flags;
        if ((i4 & 1) != 0 || (i4 & 128) != 0) {
            packageInfo.requiredForAllUsers = androidPackage.isRequiredForAllUsers();
        }
        packageInfo.restrictedAccountType = androidPackage.getRestrictedAccountType();
        packageInfo.requiredAccountType = androidPackage.getRequiredAccountType();
        packageInfo.overlayTarget = androidPackage.getOverlayTarget();
        packageInfo.targetOverlayableName = androidPackage.getOverlayTargetOverlayableName();
        packageInfo.overlayCategory = androidPackage.getOverlayCategory();
        packageInfo.overlayPriority = androidPackage.getOverlayPriority();
        packageInfo.mOverlayIsStatic = androidPackage.isOverlayIsStatic();
        packageInfo.compileSdkVersion = androidPackage.getCompileSdkVersion();
        packageInfo.compileSdkVersionCodename = androidPackage.getCompileSdkVersionCodeName();
        packageInfo.firstInstallTime = j2;
        packageInfo.lastUpdateTime = j3;
        if ((256 & j) != 0) {
            packageInfo.gids = iArr;
        }
        if ((16384 & j) != 0) {
            int size6 = androidPackage.getConfigPreferences().size();
            if (size6 > 0) {
                packageInfo.configPreferences = new ConfigurationInfo[size6];
                androidPackage.getConfigPreferences().toArray(packageInfo.configPreferences);
            }
            int size7 = androidPackage.getRequestedFeatures().size();
            if (size7 > 0) {
                packageInfo.reqFeatures = new FeatureInfo[size7];
                androidPackage.getRequestedFeatures().toArray(packageInfo.reqFeatures);
            }
            int size8 = androidPackage.getFeatureGroups().size();
            if (size8 > 0) {
                packageInfo.featureGroups = new FeatureGroupInfo[size8];
                androidPackage.getFeatureGroups().toArray(packageInfo.featureGroups);
            }
        }
        if ((4096 & j) != 0) {
            int size9 = ArrayUtils.size(androidPackage.getPermissions());
            if (size9 > 0) {
                packageInfo.permissions = new PermissionInfo[size9];
                for (int i5 = 0; i5 < size9; i5++) {
                    ParsedPermission parsedPermission = androidPackage.getPermissions().get(i5);
                    PermissionInfo generatePermissionInfo = generatePermissionInfo(parsedPermission, j);
                    if (set.contains(parsedPermission.getName())) {
                        generatePermissionInfo.flags |= 1073741824;
                    }
                    packageInfo.permissions[i5] = generatePermissionInfo;
                }
            }
            List<ParsedUsesPermission> usesPermissions = androidPackage.getUsesPermissions();
            int size10 = usesPermissions.size();
            if (size10 > 0) {
                packageInfo.requestedPermissions = new String[size10];
                packageInfo.requestedPermissionsFlags = new int[size10];
                for (int i6 = 0; i6 < size10; i6++) {
                    ParsedUsesPermission parsedUsesPermission = usesPermissions.get(i6);
                    packageInfo.requestedPermissions[i6] = parsedUsesPermission.getName();
                    int[] iArr2 = packageInfo.requestedPermissionsFlags;
                    iArr2[i6] = iArr2[i6] | 1;
                    if (set2 != null && set2.contains(parsedUsesPermission.getName())) {
                        int[] iArr3 = packageInfo.requestedPermissionsFlags;
                        iArr3[i6] = iArr3[i6] | 2;
                    }
                    if ((parsedUsesPermission.getUsesPermissionFlags() & 65536) != 0) {
                        int[] iArr4 = packageInfo.requestedPermissionsFlags;
                        iArr4[i6] = iArr4[i6] | 65536;
                    }
                    if (androidPackage.getImplicitPermissions().contains(packageInfo.requestedPermissions[i6])) {
                        int[] iArr5 = packageInfo.requestedPermissionsFlags;
                        iArr5[i6] = iArr5[i6] | 4;
                    }
                }
            }
        }
        if ((2147483648L & j) != 0) {
            int size11 = ArrayUtils.size(androidPackage.getAttributions());
            if (size11 > 0) {
                packageInfo.attributions = new Attribution[size11];
                for (int i7 = 0; i7 < size11; i7++) {
                    ParsedAttribution parsedAttribution = androidPackage.getAttributions().get(i7);
                    if (parsedAttribution != null) {
                        packageInfo.attributions[i7] = new Attribution(parsedAttribution.getTag(), parsedAttribution.getLabel());
                    }
                }
            }
            if (androidPackage.isAttributionsUserVisible()) {
                packageInfo.applicationInfo.privateFlagsExt |= 4;
            } else {
                packageInfo.applicationInfo.privateFlagsExt &= -5;
            }
        } else {
            packageInfo.applicationInfo.privateFlagsExt &= -5;
        }
        SigningDetails signingDetails = androidPackage.getSigningDetails();
        if ((64 & j) != 0) {
            if (signingDetails.hasPastSigningCertificates()) {
                packageInfo.signatures = r2;
                Signature[] signatureArr = {signingDetails.getPastSigningCertificates()[0]};
            } else if (signingDetails.hasSignatures()) {
                int length = signingDetails.getSignatures().length;
                packageInfo.signatures = new Signature[length];
                System.arraycopy(signingDetails.getSignatures(), 0, packageInfo.signatures, 0, length);
            }
        }
        if ((134217728 & j) != 0) {
            if (signingDetails != SigningDetails.UNKNOWN) {
                packageInfo.signingInfo = new SigningInfo(signingDetails);
            } else {
                packageInfo.signingInfo = null;
            }
        }
        packageInfo.isStub = androidPackage.isStub();
        packageInfo.coreApp = androidPackage.isCoreApp();
        packageInfo.isApex = androidPackage.isApex();
        if (!packageStateInternal.hasSharedUser()) {
            packageInfo.sharedUserId = null;
            packageInfo.sharedUserLabel = 0;
        }
        if ((1 & j) != 0 && (size5 = androidPackage.getActivities().size()) > 0) {
            ActivityInfo[] activityInfoArr2 = new ActivityInfo[size5];
            int i8 = 0;
            int i9 = 0;
            while (i9 < size5 && i9 <= 2000) {
                ParsedActivity parsedActivity = androidPackage.getActivities().get(i9);
                if (!ComponentParseUtils.isMatch(packageUserStateInternal, packageStateInternal.isSystem(), androidPackage.isEnabled(), parsedActivity, j) || PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME.equals(parsedActivity.getName())) {
                    i2 = i9;
                    activityInfoArr = activityInfoArr2;
                    i3 = size5;
                    i8 = i8;
                } else {
                    i2 = i9;
                    activityInfoArr = activityInfoArr2;
                    i3 = size5;
                    activityInfoArr[i8] = generateActivityInfo(androidPackage, parsedActivity, j, packageUserStateInternal, generateApplicationInfo, i, packageStateInternal);
                    i8++;
                }
                i9 = i2 + 1;
                activityInfoArr2 = activityInfoArr;
                size5 = i3;
            }
            packageInfo.activities = (ActivityInfo[]) ArrayUtils.trimToSize(activityInfoArr2, i8);
        }
        if ((2 & j) != 0 && (size4 = androidPackage.getReceivers().size()) > 0) {
            ActivityInfo[] activityInfoArr3 = new ActivityInfo[size4];
            int i10 = 0;
            for (int i11 = 0; i11 < size4; i11++) {
                ParsedActivity parsedActivity2 = androidPackage.getReceivers().get(i11);
                if (ComponentParseUtils.isMatch(packageUserStateInternal, packageStateInternal.isSystem(), androidPackage.isEnabled(), parsedActivity2, j)) {
                    activityInfoArr3[i10] = generateActivityInfo(androidPackage, parsedActivity2, j, packageUserStateInternal, generateApplicationInfo, i, packageStateInternal);
                    i10++;
                }
            }
            packageInfo.receivers = (ActivityInfo[]) ArrayUtils.trimToSize(activityInfoArr3, i10);
        }
        if ((4 & j) != 0 && (size3 = androidPackage.getServices().size()) > 0) {
            ServiceInfo[] serviceInfoArr = new ServiceInfo[size3];
            int i12 = 0;
            for (int i13 = 0; i13 < size3; i13++) {
                ParsedService parsedService = androidPackage.getServices().get(i13);
                if (ComponentParseUtils.isMatch(packageUserStateInternal, packageStateInternal.isSystem(), androidPackage.isEnabled(), parsedService, j)) {
                    serviceInfoArr[i12] = generateServiceInfo(androidPackage, parsedService, j, packageUserStateInternal, generateApplicationInfo, i, packageStateInternal);
                    i12++;
                }
            }
            packageInfo.services = (ServiceInfo[]) ArrayUtils.trimToSize(serviceInfoArr, i12);
        }
        if ((8 & j) != 0 && (size2 = androidPackage.getProviders().size()) > 0) {
            ProviderInfo[] providerInfoArr = new ProviderInfo[size2];
            int i14 = 0;
            for (int i15 = 0; i15 < size2; i15++) {
                ParsedProvider parsedProvider = androidPackage.getProviders().get(i15);
                if (ComponentParseUtils.isMatch(packageUserStateInternal, packageStateInternal.isSystem(), androidPackage.isEnabled(), parsedProvider, j)) {
                    providerInfoArr[i14] = generateProviderInfo(androidPackage, parsedProvider, j, packageUserStateInternal, generateApplicationInfo, i, packageStateInternal);
                    i14++;
                }
            }
            packageInfo.providers = (ProviderInfo[]) ArrayUtils.trimToSize(providerInfoArr, i14);
        }
        if ((16 & j) != 0 && (size = androidPackage.getInstrumentations().size()) > 0) {
            packageInfo.instrumentation = new InstrumentationInfo[size];
            for (int i16 = 0; i16 < size; i16++) {
                packageInfo.instrumentation[i16] = generateInstrumentationInfo(androidPackage.getInstrumentations().get(i16), androidPackage, j, packageUserStateInternal, i, packageStateInternal);
            }
        }
        return packageInfo;
    }

    private static void updateApplicationInfo(ApplicationInfo applicationInfo, long j, PackageUserState packageUserState) {
        if ((128 & j) == 0) {
            applicationInfo.metaData = null;
        }
        if ((1024 & j) == 0) {
            applicationInfo.sharedLibraryFiles = null;
            applicationInfo.sharedLibraryInfos = null;
        }
        if (!ParsingPackageUtils.sCompatibilityModeEnabled) {
            applicationInfo.disableCompatibilityMode();
        }
        applicationInfo.flags |= flag(packageUserState.isStopped(), DumpState.DUMP_COMPILER_STATS) | flag(packageUserState.isInstalled(), 8388608) | flag(packageUserState.isSuspended(), 1073741824);
        applicationInfo.privateFlags |= flag(packageUserState.isInstantApp(), 128) | flag(packageUserState.isVirtualPreload(), 65536) | flag(packageUserState.isHidden(), 1);
        if (packageUserState.getEnabledState() == 1) {
            applicationInfo.enabled = true;
        } else if (packageUserState.getEnabledState() == 4) {
            applicationInfo.enabled = (j & 32768) != 0;
        } else if (packageUserState.getEnabledState() == 2 || packageUserState.getEnabledState() == 3) {
            applicationInfo.enabled = false;
        }
        applicationInfo.enabledSetting = packageUserState.getEnabledState();
        if (applicationInfo.category == -1) {
            applicationInfo.category = FallbackCategoryProvider.getFallbackCategory(applicationInfo.packageName);
        }
        applicationInfo.seInfoUser = SELinuxUtil.getSeinfoUser(packageUserState);
        OverlayPaths allOverlayPaths = packageUserState.getAllOverlayPaths();
        if (allOverlayPaths != null) {
            applicationInfo.resourceDirs = (String[]) allOverlayPaths.getResourceDirs().toArray(new String[0]);
            applicationInfo.overlayPaths = (String[]) allOverlayPaths.getOverlayPaths().toArray(new String[0]);
        }
        ((IParsingPackageUtilsExt) ExtLoader.type(IParsingPackageUtilsExt.class).create()).adjustResultInGenerateApplicationInfoUnchecked(applicationInfo, packageUserState, applicationInfo.mApplicationInfoExt);
    }

    public static ApplicationInfo generateDelegateApplicationInfo(ApplicationInfo applicationInfo, long j, PackageUserState packageUserState, int i) {
        int i2;
        if (applicationInfo == null || !checkUseInstalledOrHidden(j, packageUserState, applicationInfo)) {
            return null;
        }
        ApplicationInfo applicationInfo2 = new ApplicationInfo(applicationInfo);
        applicationInfo2.initForUser(i);
        if (!ParsingPackageUtils.sUseRoundIcon || (i2 = applicationInfo2.roundIconRes) == 0) {
            i2 = applicationInfo2.iconRes;
        }
        applicationInfo2.icon = i2;
        updateApplicationInfo(applicationInfo2, j, packageUserState);
        return applicationInfo2;
    }

    @VisibleForTesting
    public static ApplicationInfo generateApplicationInfo(AndroidPackage androidPackage, long j, PackageUserStateInternal packageUserStateInternal, int i, PackageStateInternal packageStateInternal) {
        if (androidPackage == null || !checkUseInstalledOrHidden(androidPackage, packageStateInternal, packageUserStateInternal, j) || !AndroidPackageUtils.isMatchForSystemOnly(packageStateInternal, j)) {
            return null;
        }
        ApplicationInfo generateAppInfoWithoutState = AndroidPackageUtils.generateAppInfoWithoutState(androidPackage);
        updateApplicationInfo(generateAppInfoWithoutState, j, packageUserStateInternal);
        initForUser(generateAppInfoWithoutState, androidPackage, i);
        PackageStateUnserialized transientState = packageStateInternal.getTransientState();
        generateAppInfoWithoutState.hiddenUntilInstalled = transientState.isHiddenUntilInstalled();
        List<String> usesLibraryFiles = transientState.getUsesLibraryFiles();
        List<SharedLibraryWrapper> usesLibraryInfos = transientState.getUsesLibraryInfos();
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < usesLibraryInfos.size(); i2++) {
            arrayList.add(usesLibraryInfos.get(i2).getInfo());
        }
        generateAppInfoWithoutState.sharedLibraryFiles = usesLibraryFiles.isEmpty() ? null : (String[]) usesLibraryFiles.toArray(new String[0]);
        generateAppInfoWithoutState.sharedLibraryInfos = arrayList.isEmpty() ? null : arrayList;
        if (generateAppInfoWithoutState.category == -1) {
            generateAppInfoWithoutState.category = packageStateInternal.getCategoryOverride();
        }
        generateAppInfoWithoutState.seInfo = packageStateInternal.getSeInfo();
        generateAppInfoWithoutState.primaryCpuAbi = packageStateInternal.getPrimaryCpuAbi();
        generateAppInfoWithoutState.secondaryCpuAbi = packageStateInternal.getSecondaryCpuAbi();
        int i3 = generateAppInfoWithoutState.flags;
        generateAppInfoWithoutState.flags = i3 | appInfoFlags(i3, packageStateInternal);
        int i4 = generateAppInfoWithoutState.privateFlags;
        generateAppInfoWithoutState.privateFlags = i4 | appInfoPrivateFlags(i4, packageStateInternal);
        int i5 = generateAppInfoWithoutState.privateFlagsExt;
        generateAppInfoWithoutState.privateFlagsExt = i5 | appInfoPrivateFlagsExt(i5, packageStateInternal);
        return generateAppInfoWithoutState;
    }

    @VisibleForTesting
    public static ActivityInfo generateActivityInfo(AndroidPackage androidPackage, ParsedActivity parsedActivity, long j, PackageUserStateInternal packageUserStateInternal, int i, PackageStateInternal packageStateInternal) {
        return generateActivityInfo(androidPackage, parsedActivity, j, packageUserStateInternal, null, i, packageStateInternal);
    }

    @VisibleForTesting
    public static ActivityInfo generateActivityInfo(AndroidPackage androidPackage, ParsedActivity parsedActivity, long j, PackageUserStateInternal packageUserStateInternal, ApplicationInfo applicationInfo, int i, PackageStateInternal packageStateInternal) {
        if (parsedActivity == null || !checkUseInstalledOrHidden(androidPackage, packageStateInternal, packageUserStateInternal, j)) {
            return null;
        }
        if (applicationInfo == null) {
            applicationInfo = generateApplicationInfo(androidPackage, j, packageUserStateInternal, i, packageStateInternal);
        }
        if (applicationInfo == null) {
            return null;
        }
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.targetActivity = parsedActivity.getTargetActivity();
        activityInfo.processName = parsedActivity.getProcessName();
        activityInfo.exported = parsedActivity.isExported();
        activityInfo.theme = parsedActivity.getTheme();
        activityInfo.uiOptions = parsedActivity.getUiOptions();
        activityInfo.parentActivityName = parsedActivity.getParentActivityName();
        activityInfo.permission = parsedActivity.getPermission();
        activityInfo.taskAffinity = parsedActivity.getTaskAffinity();
        activityInfo.flags = parsedActivity.getFlags();
        activityInfo.privateFlags = parsedActivity.getPrivateFlags();
        activityInfo.launchMode = parsedActivity.getLaunchMode();
        activityInfo.documentLaunchMode = parsedActivity.getDocumentLaunchMode();
        activityInfo.maxRecents = parsedActivity.getMaxRecents();
        activityInfo.configChanges = parsedActivity.getConfigChanges();
        activityInfo.softInputMode = parsedActivity.getSoftInputMode();
        activityInfo.persistableMode = parsedActivity.getPersistableMode();
        activityInfo.lockTaskLaunchMode = parsedActivity.getLockTaskLaunchMode();
        activityInfo.screenOrientation = parsedActivity.getScreenOrientation();
        activityInfo.resizeMode = parsedActivity.getResizeMode();
        activityInfo.setMaxAspectRatio(parsedActivity.getMaxAspectRatio());
        activityInfo.setMinAspectRatio(parsedActivity.getMinAspectRatio());
        activityInfo.supportsSizeChanges = parsedActivity.isSupportsSizeChanges();
        activityInfo.requestedVrComponent = parsedActivity.getRequestedVrComponent();
        activityInfo.rotationAnimation = parsedActivity.getRotationAnimation();
        activityInfo.colorMode = parsedActivity.getColorMode();
        activityInfo.windowLayout = parsedActivity.getWindowLayout();
        activityInfo.attributionTags = parsedActivity.getAttributionTags();
        if ((j & 128) != 0) {
            Bundle metaData = parsedActivity.getMetaData();
            activityInfo.metaData = metaData.isEmpty() ? null : metaData;
        } else {
            activityInfo.metaData = null;
        }
        activityInfo.applicationInfo = applicationInfo;
        activityInfo.requiredDisplayCategory = parsedActivity.getRequiredDisplayCategory();
        activityInfo.setKnownActivityEmbeddingCerts(parsedActivity.getKnownActivityEmbeddingCerts());
        assignFieldsComponentInfoParsedMainComponent(activityInfo, parsedActivity, packageStateInternal, i);
        return activityInfo;
    }

    public static ActivityInfo generateDelegateActivityInfo(ActivityInfo activityInfo, long j, PackageUserState packageUserState, int i) {
        if (activityInfo == null || !checkUseInstalledOrHidden(j, packageUserState, activityInfo.applicationInfo)) {
            return null;
        }
        ActivityInfo activityInfo2 = new ActivityInfo(activityInfo);
        activityInfo2.applicationInfo = generateDelegateApplicationInfo(activityInfo2.applicationInfo, j, packageUserState, i);
        return activityInfo2;
    }

    public static ServiceInfo generateServiceInfo(AndroidPackage androidPackage, ParsedService parsedService, long j, PackageUserStateInternal packageUserStateInternal, int i, PackageStateInternal packageStateInternal) {
        return generateServiceInfo(androidPackage, parsedService, j, packageUserStateInternal, null, i, packageStateInternal);
    }

    @VisibleForTesting
    public static ServiceInfo generateServiceInfo(AndroidPackage androidPackage, ParsedService parsedService, long j, PackageUserStateInternal packageUserStateInternal, ApplicationInfo applicationInfo, int i, PackageStateInternal packageStateInternal) {
        if (parsedService == null || !checkUseInstalledOrHidden(androidPackage, packageStateInternal, packageUserStateInternal, j)) {
            return null;
        }
        if (applicationInfo == null) {
            applicationInfo = generateApplicationInfo(androidPackage, j, packageUserStateInternal, i, packageStateInternal);
        }
        if (applicationInfo == null) {
            return null;
        }
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.exported = parsedService.isExported();
        serviceInfo.flags = parsedService.getFlags();
        serviceInfo.permission = parsedService.getPermission();
        serviceInfo.processName = parsedService.getProcessName();
        serviceInfo.mForegroundServiceType = parsedService.getForegroundServiceType();
        serviceInfo.applicationInfo = applicationInfo;
        if ((j & 128) != 0) {
            Bundle metaData = parsedService.getMetaData();
            serviceInfo.metaData = metaData.isEmpty() ? null : metaData;
        }
        assignFieldsComponentInfoParsedMainComponent(serviceInfo, parsedService, packageStateInternal, i);
        return serviceInfo;
    }

    @VisibleForTesting
    public static ProviderInfo generateProviderInfo(AndroidPackage androidPackage, ParsedProvider parsedProvider, long j, PackageUserStateInternal packageUserStateInternal, ApplicationInfo applicationInfo, int i, PackageStateInternal packageStateInternal) {
        if (parsedProvider == null || !checkUseInstalledOrHidden(androidPackage, packageStateInternal, packageUserStateInternal, j)) {
            return null;
        }
        if (applicationInfo == null || !androidPackage.getPackageName().equals(applicationInfo.packageName)) {
            StringBuilder sb = new StringBuilder();
            sb.append("AppInfo's package name is different. Expected=");
            sb.append(androidPackage.getPackageName());
            sb.append(" actual=");
            sb.append(applicationInfo == null ? "(null AppInfo)" : applicationInfo.packageName);
            Slog.wtf("PackageParsing", sb.toString());
            applicationInfo = generateApplicationInfo(androidPackage, j, packageUserStateInternal, i, packageStateInternal);
        }
        if (applicationInfo == null) {
            return null;
        }
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.exported = parsedProvider.isExported();
        providerInfo.flags = parsedProvider.getFlags();
        providerInfo.processName = parsedProvider.getProcessName();
        providerInfo.authority = parsedProvider.getAuthority();
        providerInfo.isSyncable = parsedProvider.isSyncable();
        providerInfo.readPermission = parsedProvider.getReadPermission();
        providerInfo.writePermission = parsedProvider.getWritePermission();
        providerInfo.grantUriPermissions = parsedProvider.isGrantUriPermissions();
        providerInfo.forceUriPermissions = parsedProvider.isForceUriPermissions();
        providerInfo.multiprocess = parsedProvider.isMultiProcess();
        providerInfo.initOrder = parsedProvider.getInitOrder();
        providerInfo.uriPermissionPatterns = (PatternMatcher[]) parsedProvider.getUriPermissionPatterns().toArray(new PatternMatcher[0]);
        providerInfo.pathPermissions = (PathPermission[]) parsedProvider.getPathPermissions().toArray(new PathPermission[0]);
        if ((2048 & j) == 0) {
            providerInfo.uriPermissionPatterns = null;
        }
        if ((j & 128) != 0) {
            Bundle metaData = parsedProvider.getMetaData();
            providerInfo.metaData = metaData.isEmpty() ? null : metaData;
        }
        providerInfo.applicationInfo = applicationInfo;
        assignFieldsComponentInfoParsedMainComponent(providerInfo, parsedProvider, packageStateInternal, i);
        return providerInfo;
    }

    public static InstrumentationInfo generateInstrumentationInfo(ParsedInstrumentation parsedInstrumentation, AndroidPackage androidPackage, long j, PackageUserStateInternal packageUserStateInternal, int i, PackageStateInternal packageStateInternal) {
        if (parsedInstrumentation == null || !checkUseInstalledOrHidden(androidPackage, packageStateInternal, packageUserStateInternal, j)) {
            return null;
        }
        InstrumentationInfo instrumentationInfo = new InstrumentationInfo();
        instrumentationInfo.targetPackage = parsedInstrumentation.getTargetPackage();
        instrumentationInfo.targetProcesses = parsedInstrumentation.getTargetProcesses();
        instrumentationInfo.handleProfiling = parsedInstrumentation.isHandleProfiling();
        instrumentationInfo.functionalTest = parsedInstrumentation.isFunctionalTest();
        instrumentationInfo.sourceDir = androidPackage.getBaseApkPath();
        instrumentationInfo.publicSourceDir = androidPackage.getBaseApkPath();
        instrumentationInfo.splitNames = androidPackage.getSplitNames();
        instrumentationInfo.splitSourceDirs = androidPackage.getSplitCodePaths().length == 0 ? null : androidPackage.getSplitCodePaths();
        instrumentationInfo.splitPublicSourceDirs = androidPackage.getSplitCodePaths().length == 0 ? null : androidPackage.getSplitCodePaths();
        instrumentationInfo.splitDependencies = androidPackage.getSplitDependencies().size() == 0 ? null : androidPackage.getSplitDependencies();
        initForUser(instrumentationInfo, androidPackage, i);
        instrumentationInfo.primaryCpuAbi = packageStateInternal.getPrimaryCpuAbi();
        instrumentationInfo.secondaryCpuAbi = packageStateInternal.getSecondaryCpuAbi();
        instrumentationInfo.nativeLibraryDir = androidPackage.getNativeLibraryDir();
        instrumentationInfo.secondaryNativeLibraryDir = androidPackage.getSecondaryNativeLibraryDir();
        assignFieldsPackageItemInfoParsedComponent(instrumentationInfo, parsedInstrumentation, packageStateInternal, i);
        if ((j & 128) == 0) {
            instrumentationInfo.metaData = null;
        } else {
            Bundle metaData = parsedInstrumentation.getMetaData();
            instrumentationInfo.metaData = metaData.isEmpty() ? null : metaData;
        }
        return instrumentationInfo;
    }

    public static PermissionInfo generatePermissionInfo(ParsedPermission parsedPermission, long j) {
        if (parsedPermission == null) {
            return null;
        }
        PermissionInfo permissionInfo = new PermissionInfo(parsedPermission.getBackgroundPermission());
        assignFieldsPackageItemInfoParsedComponent(permissionInfo, parsedPermission);
        permissionInfo.group = parsedPermission.getGroup();
        permissionInfo.requestRes = parsedPermission.getRequestRes();
        permissionInfo.protectionLevel = parsedPermission.getProtectionLevel();
        permissionInfo.descriptionRes = parsedPermission.getDescriptionRes();
        permissionInfo.flags = parsedPermission.getFlags();
        permissionInfo.knownCerts = parsedPermission.getKnownCerts();
        if ((j & 128) == 0) {
            permissionInfo.metaData = null;
        } else {
            Bundle metaData = parsedPermission.getMetaData();
            permissionInfo.metaData = metaData.isEmpty() ? null : metaData;
        }
        return permissionInfo;
    }

    public static PermissionGroupInfo generatePermissionGroupInfo(ParsedPermissionGroup parsedPermissionGroup, long j) {
        if (parsedPermissionGroup == null) {
            return null;
        }
        PermissionGroupInfo permissionGroupInfo = new PermissionGroupInfo(parsedPermissionGroup.getRequestDetailRes(), parsedPermissionGroup.getBackgroundRequestRes(), parsedPermissionGroup.getBackgroundRequestDetailRes());
        assignFieldsPackageItemInfoParsedComponent(permissionGroupInfo, parsedPermissionGroup);
        permissionGroupInfo.descriptionRes = parsedPermissionGroup.getDescriptionRes();
        permissionGroupInfo.priority = parsedPermissionGroup.getPriority();
        permissionGroupInfo.requestRes = parsedPermissionGroup.getRequestRes();
        permissionGroupInfo.flags = parsedPermissionGroup.getFlags();
        if ((j & 128) == 0) {
            permissionGroupInfo.metaData = null;
        } else {
            Bundle metaData = parsedPermissionGroup.getMetaData();
            permissionGroupInfo.metaData = metaData.isEmpty() ? null : metaData;
        }
        return permissionGroupInfo;
    }

    public static ArrayMap<String, ProcessInfo> generateProcessInfo(Map<String, ParsedProcess> map, long j) {
        if (map == null) {
            return null;
        }
        ArrayMap<String, ProcessInfo> arrayMap = new ArrayMap<>(map.size());
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            ParsedProcess parsedProcess = map.get(it.next());
            arrayMap.put(parsedProcess.getName(), new ProcessInfo(parsedProcess.getName(), new ArraySet(parsedProcess.getDeniedPermissions()), parsedProcess.getGwpAsanMode(), parsedProcess.getMemtagMode(), parsedProcess.getNativeHeapZeroInitialized()));
        }
        return arrayMap;
    }

    public static boolean checkUseInstalledOrHidden(AndroidPackage androidPackage, PackageStateInternal packageStateInternal, PackageUserStateInternal packageUserStateInternal, long j) {
        long j2 = 536870912 & j;
        if (j2 == 0 && !packageUserStateInternal.isInstalled() && packageStateInternal.getTransientState().isHiddenUntilInstalled()) {
            return false;
        }
        if (!PackageUserStateUtils.isAvailable(packageUserStateInternal, j)) {
            if (!packageStateInternal.isSystem()) {
                return false;
            }
            if ((4202496 & j) == 0 && j2 == 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkUseInstalledOrHidden(long j, PackageUserState packageUserState, ApplicationInfo applicationInfo) {
        long j2 = 536870912 & j;
        if (j2 == 0 && !packageUserState.isInstalled() && applicationInfo != null && applicationInfo.hiddenUntilInstalled) {
            return false;
        }
        if (!PackageUserStateUtils.isAvailable(packageUserState, j)) {
            if (applicationInfo == null || !applicationInfo.isSystemApp()) {
                return false;
            }
            if ((j & 4202496) == 0 && j2 == 0) {
                return false;
            }
        }
        return true;
    }

    private static void assignFieldsComponentInfoParsedMainComponent(ComponentInfo componentInfo, ParsedMainComponent parsedMainComponent) {
        assignFieldsPackageItemInfoParsedComponent(componentInfo, parsedMainComponent);
        componentInfo.descriptionRes = parsedMainComponent.getDescriptionRes();
        componentInfo.directBootAware = parsedMainComponent.isDirectBootAware();
        componentInfo.enabled = parsedMainComponent.isEnabled();
        componentInfo.splitName = parsedMainComponent.getSplitName();
        componentInfo.attributionTags = parsedMainComponent.getAttributionTags();
    }

    private static void assignFieldsPackageItemInfoParsedComponent(PackageItemInfo packageItemInfo, ParsedComponent parsedComponent) {
        packageItemInfo.nonLocalizedLabel = ComponentParseUtils.getNonLocalizedLabel(parsedComponent);
        packageItemInfo.icon = ComponentParseUtils.getIcon(parsedComponent);
        packageItemInfo.banner = parsedComponent.getBanner();
        packageItemInfo.labelRes = parsedComponent.getLabelRes();
        packageItemInfo.logo = parsedComponent.getLogo();
        packageItemInfo.name = parsedComponent.getName();
        packageItemInfo.packageName = parsedComponent.getPackageName();
    }

    private static void assignFieldsComponentInfoParsedMainComponent(ComponentInfo componentInfo, ParsedMainComponent parsedMainComponent, PackageStateInternal packageStateInternal, int i) {
        assignFieldsComponentInfoParsedMainComponent(componentInfo, parsedMainComponent);
        Pair<CharSequence, Integer> nonLocalizedLabelAndIcon = ParsedComponentStateUtils.getNonLocalizedLabelAndIcon(parsedMainComponent, packageStateInternal, i);
        componentInfo.nonLocalizedLabel = (CharSequence) nonLocalizedLabelAndIcon.first;
        componentInfo.icon = ((Integer) nonLocalizedLabelAndIcon.second).intValue();
    }

    private static void assignFieldsPackageItemInfoParsedComponent(PackageItemInfo packageItemInfo, ParsedComponent parsedComponent, PackageStateInternal packageStateInternal, int i) {
        assignFieldsPackageItemInfoParsedComponent(packageItemInfo, parsedComponent);
        Pair<CharSequence, Integer> nonLocalizedLabelAndIcon = ParsedComponentStateUtils.getNonLocalizedLabelAndIcon(parsedComponent, packageStateInternal, i);
        packageItemInfo.nonLocalizedLabel = (CharSequence) nonLocalizedLabelAndIcon.first;
        packageItemInfo.icon = ((Integer) nonLocalizedLabelAndIcon.second).intValue();
    }

    public static int appInfoFlags(AndroidPackage androidPackage, PackageStateInternal packageStateInternal) {
        return appInfoFlags(flag(androidPackage.isFactoryTest(), 16) | flag(androidPackage.isExternalStorage(), DumpState.DUMP_DOMAIN_PREFERRED) | flag(androidPackage.isHardwareAccelerated(), 536870912) | flag(androidPackage.isBackupAllowed(), 32768) | flag(androidPackage.isKillAfterRestoreAllowed(), 65536) | flag(androidPackage.isRestoreAnyVersion(), 131072) | flag(androidPackage.isFullBackupOnly(), 67108864) | flag(androidPackage.isPersistent(), 8) | flag(androidPackage.isDebuggable(), 2) | flag(androidPackage.isVmSafeMode(), 16384) | flag(androidPackage.isDeclaredHavingCode(), 4) | flag(androidPackage.isTaskReparentingAllowed(), 32) | flag(androidPackage.isClearUserDataAllowed(), 64) | flag(androidPackage.isLargeHeap(), 1048576) | flag(androidPackage.isCleartextTrafficAllowed(), DumpState.DUMP_KNOWN_PACKAGES) | flag(androidPackage.isRtlSupported(), DumpState.DUMP_CHANGES) | flag(androidPackage.isTestOnly(), 256) | flag(androidPackage.isMultiArch(), Integer.MIN_VALUE) | flag(androidPackage.isExtractNativeLibrariesRequested(), 268435456) | flag(androidPackage.isGame(), DumpState.DUMP_APEX) | flag(androidPackage.isSmallScreensSupported(), 512) | flag(androidPackage.isNormalScreensSupported(), 1024) | flag(androidPackage.isLargeScreensSupported(), 2048) | flag(androidPackage.isExtraLargeScreensSupported(), 524288) | flag(androidPackage.isResizeable(), 4096) | flag(androidPackage.isAnyDensity(), 8192) | flag(AndroidPackageUtils.isSystem(androidPackage), 1), packageStateInternal);
    }

    public static int appInfoFlags(int i, PackageStateInternal packageStateInternal) {
        return packageStateInternal != null ? i | flag(packageStateInternal.isUpdatedSystemApp(), 128) : i;
    }

    public static int appInfoPrivateFlags(AndroidPackage androidPackage, PackageStateInternal packageStateInternal) {
        int flag = flag(androidPackage.isStaticSharedLibrary(), 16384) | flag(androidPackage.isResourceOverlay(), 268435456) | flag(androidPackage.isIsolatedSplitLoading(), 32768) | flag(androidPackage.isHasDomainUrls(), 16) | flag(androidPackage.isProfileableByShell(), 8388608) | flag(androidPackage.isBackupInForeground(), 8192) | flag(androidPackage.isUseEmbeddedDex(), DumpState.DUMP_APEX) | flag(androidPackage.isDefaultToDeviceProtectedStorage(), 32) | flag(androidPackage.isDirectBootAware(), 64) | flag(androidPackage.isPartiallyDirectBootAware(), 256) | flag(androidPackage.isClearUserDataOnFailedRestoreAllowed(), 67108864) | flag(androidPackage.isAllowAudioPlaybackCapture(), DumpState.DUMP_KNOWN_PACKAGES) | flag(androidPackage.isRequestLegacyExternalStorage(), 536870912) | flag(androidPackage.isNonSdkApiRequested(), DumpState.DUMP_CHANGES) | flag(androidPackage.isUserDataFragile(), DumpState.DUMP_SERVICE_PERMISSIONS) | flag(androidPackage.isSaveStateDisallowed(), 2) | flag(androidPackage.isResizeableActivityViaSdkVersion(), 4096) | flag(androidPackage.isAllowNativeHeapPointerTagging(), Integer.MIN_VALUE) | flag(AndroidPackageUtils.isSystemExt(androidPackage), DumpState.DUMP_COMPILER_STATS) | flag(AndroidPackageUtils.isPrivileged(androidPackage), 8) | flag(AndroidPackageUtils.isOem(androidPackage), 131072) | flag(AndroidPackageUtils.isVendor(androidPackage), DumpState.DUMP_DOMAIN_PREFERRED) | flag(AndroidPackageUtils.isProduct(androidPackage), 524288) | flag(AndroidPackageUtils.isOdm(androidPackage), 1073741824) | flag(androidPackage.isSignedWithPlatformKey(), 1048576);
        Boolean resizeableActivity = androidPackage.getResizeableActivity();
        if (resizeableActivity != null) {
            flag = resizeableActivity.booleanValue() ? flag | 1024 : flag | 2048;
        }
        return appInfoPrivateFlags(flag, packageStateInternal);
    }

    public static int appInfoPrivateFlagsExt(AndroidPackage androidPackage, PackageStateInternal packageStateInternal) {
        boolean contains = SystemConfig.getInstance().getHiddenApiWhitelistedApps().contains(androidPackage.getPackageName());
        return appInfoPrivateFlagsExt(flag(androidPackage.isOnBackInvokedCallbackEnabled(), 8) | flag(androidPackage.isProfileable(), 1) | flag(androidPackage.hasRequestForegroundServiceExemption(), 2) | flag(androidPackage.isAttributionsUserVisible(), 4) | flag(contains, 16), packageStateInternal);
    }

    private static int appInfoPrivateFlagsExt(int i, PackageStateInternal packageStateInternal) {
        if (packageStateInternal != null) {
            return i | flag(packageStateInternal.getCpuAbiOverride() != null, 32);
        }
        return i;
    }

    private static void initForUser(ApplicationInfo applicationInfo, AndroidPackage androidPackage, int i) {
        PackageImpl packageImpl = (PackageImpl) androidPackage;
        String packageName = androidPackage.getPackageName();
        applicationInfo.uid = UserHandle.getUid(i, UserHandle.getAppId(androidPackage.getUid()));
        if (PackageManagerService.PLATFORM_PACKAGE_NAME.equals(packageName)) {
            applicationInfo.dataDir = SYSTEM_DATA_PATH;
            return;
        }
        if (i == 0) {
            applicationInfo.credentialProtectedDataDir = packageImpl.getBaseAppDataCredentialProtectedDirForSystemUser() + packageName;
            applicationInfo.deviceProtectedDataDir = packageImpl.getBaseAppDataDeviceProtectedDirForSystemUser() + packageName;
        } else {
            String valueOf = String.valueOf(i);
            int length = packageImpl.getBaseAppDataCredentialProtectedDirForSystemUser().length();
            StringBuilder replace = new StringBuilder(packageImpl.getBaseAppDataCredentialProtectedDirForSystemUser()).replace(length - 2, length - 1, valueOf);
            replace.append(packageName);
            applicationInfo.credentialProtectedDataDir = replace.toString();
            int length2 = packageImpl.getBaseAppDataDeviceProtectedDirForSystemUser().length();
            StringBuilder replace2 = new StringBuilder(packageImpl.getBaseAppDataDeviceProtectedDirForSystemUser()).replace(length2 - 2, length2 - 1, valueOf);
            replace2.append(packageName);
            applicationInfo.deviceProtectedDataDir = replace2.toString();
        }
        if (androidPackage.isDefaultToDeviceProtectedStorage()) {
            applicationInfo.dataDir = applicationInfo.deviceProtectedDataDir;
        } else {
            applicationInfo.dataDir = applicationInfo.credentialProtectedDataDir;
        }
    }

    private static void initForUser(InstrumentationInfo instrumentationInfo, AndroidPackage androidPackage, int i) {
        PackageImpl packageImpl = (PackageImpl) androidPackage;
        String packageName = androidPackage.getPackageName();
        if (PackageManagerService.PLATFORM_PACKAGE_NAME.equals(packageName)) {
            instrumentationInfo.dataDir = SYSTEM_DATA_PATH;
            return;
        }
        if (i == 0) {
            instrumentationInfo.credentialProtectedDataDir = packageImpl.getBaseAppDataCredentialProtectedDirForSystemUser() + packageName;
            instrumentationInfo.deviceProtectedDataDir = packageImpl.getBaseAppDataDeviceProtectedDirForSystemUser() + packageName;
        } else {
            String valueOf = String.valueOf(i);
            int length = packageImpl.getBaseAppDataCredentialProtectedDirForSystemUser().length();
            StringBuilder replace = new StringBuilder(packageImpl.getBaseAppDataCredentialProtectedDirForSystemUser()).replace(length - 2, length - 1, valueOf);
            replace.append(packageName);
            instrumentationInfo.credentialProtectedDataDir = replace.toString();
            int length2 = packageImpl.getBaseAppDataDeviceProtectedDirForSystemUser().length();
            StringBuilder replace2 = new StringBuilder(packageImpl.getBaseAppDataDeviceProtectedDirForSystemUser()).replace(length2 - 2, length2 - 1, valueOf);
            replace2.append(packageName);
            instrumentationInfo.deviceProtectedDataDir = replace2.toString();
        }
        if (androidPackage.isDefaultToDeviceProtectedStorage()) {
            instrumentationInfo.dataDir = instrumentationInfo.deviceProtectedDataDir;
        } else {
            instrumentationInfo.dataDir = instrumentationInfo.credentialProtectedDataDir;
        }
    }

    public static File getDataDir(AndroidPackage androidPackage, int i) {
        if (PackageManagerService.PLATFORM_PACKAGE_NAME.equals(androidPackage.getPackageName())) {
            return Environment.getDataSystemDirectory();
        }
        if (androidPackage.isDefaultToDeviceProtectedStorage()) {
            return Environment.getDataUserDePackageDirectory(androidPackage.getVolumeUuid(), i, androidPackage.getPackageName());
        }
        return Environment.getDataUserCePackageDirectory(androidPackage.getVolumeUuid(), i, androidPackage.getPackageName());
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class CachedApplicationInfoGenerator {
        private ArrayMap<String, ApplicationInfo> mCache = new ArrayMap<>();

        public ApplicationInfo generate(AndroidPackage androidPackage, long j, PackageUserStateInternal packageUserStateInternal, int i, PackageStateInternal packageStateInternal) {
            ApplicationInfo applicationInfo = this.mCache.get(androidPackage.getPackageName());
            if (applicationInfo != null) {
                return applicationInfo;
            }
            ApplicationInfo generateApplicationInfo = PackageInfoUtils.generateApplicationInfo(androidPackage, j, packageUserStateInternal, i, packageStateInternal);
            this.mCache.put(androidPackage.getPackageName(), generateApplicationInfo);
            return generateApplicationInfo;
        }
    }
}
