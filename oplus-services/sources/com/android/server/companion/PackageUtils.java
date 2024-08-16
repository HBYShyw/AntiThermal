package com.android.server.companion;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.util.Slog;
import com.android.internal.util.FunctionalUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class PackageUtils {
    private static final Intent COMPANION_SERVICE_INTENT = new Intent("android.companion.CompanionDeviceService");
    private static final String PROPERTY_PRIMARY_TAG = "android.companion.PROPERTY_PRIMARY_COMPANION_DEVICE_SERVICE";

    PackageUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PackageInfo getPackageInfo(Context context, final int i, final String str) {
        final PackageManager packageManager = context.getPackageManager();
        final PackageManager.PackageInfoFlags of = PackageManager.PackageInfoFlags.of(20480L);
        return (PackageInfo) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.companion.PackageUtils$$ExternalSyntheticLambda1
            public final Object getOrThrow() {
                PackageInfo lambda$getPackageInfo$0;
                lambda$getPackageInfo$0 = PackageUtils.lambda$getPackageInfo$0(packageManager, str, of, i);
                return lambda$getPackageInfo$0;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ PackageInfo lambda$getPackageInfo$0(PackageManager packageManager, String str, PackageManager.PackageInfoFlags packageInfoFlags, int i) throws Exception {
        try {
            return packageManager.getPackageInfoAsUser(str, packageInfoFlags, i);
        } catch (PackageManager.NameNotFoundException unused) {
            Slog.e("CDM_CompanionDeviceManagerService", "Package [" + str + "] is not found.");
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void enforceUsesCompanionDeviceFeature(Context context, int i, String str) {
        FeatureInfo[] featureInfoArr = getPackageInfo(context, i, str).reqFeatures;
        if (featureInfoArr != null) {
            for (FeatureInfo featureInfo : featureInfoArr) {
                if ("android.software.companion_device_setup".equals(featureInfo.name)) {
                    return;
                }
            }
        }
        throw new IllegalStateException("Must declare uses-feature android.software.companion_device_setup in manifest to use this API");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Map<String, List<ComponentName>> getCompanionServicesForUser(Context context, int i) {
        PackageManager packageManager = context.getPackageManager();
        List queryIntentServicesAsUser = packageManager.queryIntentServicesAsUser(COMPANION_SERVICE_INTENT, PackageManager.ResolveInfoFlags.of(0L), i);
        HashMap hashMap = new HashMap(queryIntentServicesAsUser.size());
        Iterator it = queryIntentServicesAsUser.iterator();
        while (it.hasNext()) {
            ServiceInfo serviceInfo = ((ResolveInfo) it.next()).serviceInfo;
            if (!"android.permission.BIND_COMPANION_DEVICE_SERVICE".equals(serviceInfo.permission)) {
                Slog.w("CDM_CompanionDeviceManagerService", "CompanionDeviceService " + serviceInfo.getComponentName().flattenToShortString() + " must require android.permission.BIND_COMPANION_DEVICE_SERVICE");
            } else {
                ArrayList arrayList = (ArrayList) hashMap.computeIfAbsent(serviceInfo.packageName, new Function() { // from class: com.android.server.companion.PackageUtils$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        List lambda$getCompanionServicesForUser$1;
                        lambda$getCompanionServicesForUser$1 = PackageUtils.lambda$getCompanionServicesForUser$1((String) obj);
                        return lambda$getCompanionServicesForUser$1;
                    }
                });
                ComponentName componentName = serviceInfo.getComponentName();
                if (isPrimaryCompanionDeviceService(packageManager, componentName, i)) {
                    arrayList.add(0, componentName);
                } else {
                    arrayList.add(componentName);
                }
            }
        }
        return hashMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ List lambda$getCompanionServicesForUser$1(String str) {
        return new ArrayList(1);
    }

    private static boolean isPrimaryCompanionDeviceService(PackageManager packageManager, ComponentName componentName, int i) {
        try {
            return packageManager.getPropertyAsUser(PROPERTY_PRIMARY_TAG, componentName.getPackageName(), componentName.getClassName(), i).getBoolean();
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }
}
