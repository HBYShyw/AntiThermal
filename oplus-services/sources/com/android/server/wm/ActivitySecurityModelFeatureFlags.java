package com.android.server.wm;

import android.app.compat.CompatChanges;
import android.content.pm.PackageManager;
import android.provider.DeviceConfig;
import com.android.internal.annotations.GuardedBy;
import java.util.HashSet;
import java.util.concurrent.Executor;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivitySecurityModelFeatureFlags {
    static final int ASM_VERSION = 7;
    private static final String DEFAULT_EXCEPTION_LIST = "";
    private static final int DEFAULT_VALUE = 0;
    static final String DOC_LINK = "go/android-asm";
    private static final String KEY_ASM_EXEMPTED_PACKAGES = "ActivitySecurity__asm_exempted_packages";
    private static final String KEY_ASM_PREFIX = "ActivitySecurity__";
    private static final String KEY_ASM_RESTRICTIONS_ENABLED = "ActivitySecurity__asm_restrictions_enabled";
    private static final String KEY_ASM_TOASTS_ENABLED = "ActivitySecurity__asm_toasts_enabled";
    private static final String NAMESPACE = "window_manager";
    private static final int VALUE_DISABLE = 0;
    private static final int VALUE_ENABLE_FOR_ALL = 2;
    private static final int VALUE_ENABLE_FOR_U = 1;
    private static int sAsmRestrictionsEnabled;
    private static int sAsmToastsEnabled;
    private static final HashSet<String> sExcludedPackageNames = new HashSet<>();
    private static PackageManager sPm;

    ActivitySecurityModelFeatureFlags() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ActivityTaskManagerService.mGlobalLock"})
    public static void initialize(Executor executor, PackageManager packageManager) {
        updateFromDeviceConfig();
        DeviceConfig.addOnPropertiesChangedListener(NAMESPACE, executor, new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.wm.ActivitySecurityModelFeatureFlags$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                ActivitySecurityModelFeatureFlags.updateFromDeviceConfig();
            }
        });
        sPm = packageManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ActivityTaskManagerService.mGlobalLock"})
    public static boolean shouldShowToast(int i) {
        return flagEnabledForUid(sAsmToastsEnabled, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ActivityTaskManagerService.mGlobalLock"})
    public static boolean shouldRestrictActivitySwitch(int i) {
        return flagEnabledForUid(sAsmRestrictionsEnabled, i);
    }

    private static boolean flagEnabledForUid(int i, int i2) {
        if (!(i == 2 || (i == 1 && CompatChanges.isChangeEnabled(230590090L, i2)))) {
            return false;
        }
        String[] packagesForUid = sPm.getPackagesForUid(i2);
        if (packagesForUid == null) {
            return true;
        }
        for (String str : packagesForUid) {
            if (sExcludedPackageNames.contains(str)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateFromDeviceConfig() {
        sAsmToastsEnabled = DeviceConfig.getInt(NAMESPACE, KEY_ASM_TOASTS_ENABLED, 0);
        sAsmRestrictionsEnabled = DeviceConfig.getInt(NAMESPACE, KEY_ASM_RESTRICTIONS_ENABLED, 0);
        String string = DeviceConfig.getString(NAMESPACE, KEY_ASM_EXEMPTED_PACKAGES, DEFAULT_EXCEPTION_LIST);
        sExcludedPackageNames.clear();
        for (String str : string.split(",")) {
            String trim = str.trim();
            if (!trim.isEmpty()) {
                sExcludedPackageNames.add(trim);
            }
        }
    }
}
