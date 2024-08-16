package com.android.server.am;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.content.PackageMonitor;
import com.android.server.FgThread;
import com.android.server.LocalServices;
import com.android.server.compat.PlatformCompat;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Deprecated
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ComponentAliasResolver {
    private static final String ALIAS_FILTER_ACTION = "com.android.intent.action.EXPERIMENTAL_IS_ALIAS";
    private static final String ALIAS_FILTER_ACTION_ALT = "android.intent.action.EXPERIMENTAL_IS_ALIAS";
    private static final boolean DEBUG = true;
    private static final String META_DATA_ALIAS_TARGET = "alias_target";
    private static final String OPT_IN_PROPERTY = "com.android.EXPERIMENTAL_COMPONENT_ALIAS_OPT_IN";
    private static final int PACKAGE_QUERY_FLAGS = 4989056;
    private static final String TAG = "ComponentAliasResolver";
    public static final long USE_EXPERIMENTAL_COMPONENT_ALIAS = 196254758;
    private final ActivityManagerService mAm;
    private final Context mContext;

    @GuardedBy({"mLock"})
    private boolean mEnabled;

    @GuardedBy({"mLock"})
    private boolean mEnabledByDeviceConfig;

    @GuardedBy({"mLock"})
    private String mOverrideString;

    @GuardedBy({"mLock"})
    private PlatformCompat mPlatformCompat;
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private final ArrayMap<ComponentName, ComponentName> mFromTo = new ArrayMap<>();
    final PackageMonitor mPackageMonitor = new PackageMonitor() { // from class: com.android.server.am.ComponentAliasResolver.1
        public void onPackageModified(String str) {
            ComponentAliasResolver.this.refresh();
        }

        public void onPackageAdded(String str, int i) {
            ComponentAliasResolver.this.refresh();
        }

        public void onPackageRemoved(String str, int i) {
            ComponentAliasResolver.this.refresh();
        }
    };

    public ComponentAliasResolver(ActivityManagerService activityManagerService) {
        this.mAm = activityManagerService;
        this.mContext = activityManagerService.mContext;
    }

    public boolean isEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mEnabled;
        }
        return z;
    }

    public void onSystemReady(boolean z, String str) {
        synchronized (this.mLock) {
            this.mPlatformCompat = (PlatformCompat) ServiceManager.getService("platform_compat");
        }
        Slog.d(TAG, "Compat listener set.");
        update(z, str);
    }

    public void update(boolean z, String str) {
        synchronized (this.mLock) {
            if (this.mPlatformCompat == null) {
                return;
            }
            if (this.mEnabled) {
                Slog.i(TAG, "Disabling component aliases...");
                FgThread.getHandler().post(new Runnable() { // from class: com.android.server.am.ComponentAliasResolver$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ComponentAliasResolver.this.lambda$update$0();
                    }
                });
            }
            this.mEnabled = false;
            this.mEnabledByDeviceConfig = z;
            this.mOverrideString = str;
            this.mFromTo.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$update$0() {
        this.mPackageMonitor.unregister();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refresh() {
        synchronized (this.mLock) {
            update(this.mEnabledByDeviceConfig, this.mOverrideString);
        }
    }

    @GuardedBy({"mLock"})
    private void refreshLocked() {
        Slog.d(TAG, "Refreshing aliases...");
        this.mFromTo.clear();
        loadFromMetadataLocked();
        loadOverridesLocked();
    }

    @GuardedBy({"mLock"})
    private void loadFromMetadataLocked() {
        Slog.d(TAG, "Scanning service aliases...");
        loadFromMetadataLockedInner(new Intent(ALIAS_FILTER_ACTION_ALT));
        loadFromMetadataLockedInner(new Intent(ALIAS_FILTER_ACTION));
    }

    private void loadFromMetadataLockedInner(Intent intent) {
        extractAliasesLocked(this.mContext.getPackageManager().queryIntentServicesAsUser(intent, PACKAGE_QUERY_FLAGS, 0));
        Slog.d(TAG, "Scanning receiver aliases...");
        extractAliasesLocked(this.mContext.getPackageManager().queryBroadcastReceiversAsUser(intent, PACKAGE_QUERY_FLAGS, 0));
    }

    @GuardedBy({"mLock"})
    private boolean isEnabledForPackageLocked(String str) {
        boolean z;
        try {
            z = this.mContext.getPackageManager().getProperty(OPT_IN_PROPERTY, str).getBoolean();
        } catch (PackageManager.NameNotFoundException unused) {
            z = false;
        }
        if (!z) {
            Slog.w(TAG, "USE_EXPERIMENTAL_COMPONENT_ALIAS not enabled for " + str);
        }
        return z;
    }

    private static boolean validateAlias(ComponentName componentName, ComponentName componentName2) {
        String packageName = componentName.getPackageName();
        String packageName2 = componentName2.getPackageName();
        if (Objects.equals(packageName, packageName2)) {
            return true;
        }
        if (packageName2.startsWith(packageName + ".")) {
            return true;
        }
        Slog.w(TAG, "Invalid alias: " + componentName.flattenToShortString() + " -> " + componentName2.flattenToShortString());
        return false;
    }

    @GuardedBy({"mLock"})
    private void validateAndAddAliasLocked(ComponentName componentName, ComponentName componentName2) {
        Slog.d(TAG, "" + componentName.flattenToShortString() + " -> " + componentName2.flattenToShortString());
        if (validateAlias(componentName, componentName2) && isEnabledForPackageLocked(componentName.getPackageName()) && isEnabledForPackageLocked(componentName2.getPackageName())) {
            this.mFromTo.put(componentName, componentName2);
        }
    }

    @GuardedBy({"mLock"})
    private void extractAliasesLocked(List<ResolveInfo> list) {
        Iterator<ResolveInfo> it = list.iterator();
        while (it.hasNext()) {
            ComponentInfo componentInfo = it.next().getComponentInfo();
            ComponentName componentName = componentInfo.getComponentName();
            ComponentName unflatten = unflatten(componentInfo.metaData.getString(META_DATA_ALIAS_TARGET));
            if (unflatten != null) {
                validateAndAddAliasLocked(componentName, unflatten);
            }
        }
    }

    @GuardedBy({"mLock"})
    private void loadOverridesLocked() {
        ComponentName unflatten;
        Slog.d(TAG, "Loading aliases overrides ...");
        for (String str : this.mOverrideString.split("\\,+")) {
            String[] split = str.split("\\:+", 2);
            if (!TextUtils.isEmpty(split[0]) && (unflatten = unflatten(split[0])) != null) {
                if (split.length == 1) {
                    Slog.d(TAG, "" + unflatten.flattenToShortString() + " [removed]");
                    this.mFromTo.remove(unflatten);
                } else {
                    ComponentName unflatten2 = unflatten(split[1]);
                    if (unflatten2 != null) {
                        validateAndAddAliasLocked(unflatten, unflatten2);
                    }
                }
            }
        }
    }

    private static ComponentName unflatten(String str) {
        ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
        if (unflattenFromString != null) {
            return unflattenFromString;
        }
        Slog.e(TAG, "Invalid component name detected: " + str);
        return null;
    }

    public void dump(PrintWriter printWriter) {
        synchronized (this.mLock) {
            printWriter.println("ACTIVITY MANAGER COMPONENT-ALIAS (dumpsys activity component-alias)");
            printWriter.print("  Enabled: ");
            printWriter.println(this.mEnabled);
            printWriter.println("  Aliases:");
            for (int i = 0; i < this.mFromTo.size(); i++) {
                ComponentName keyAt = this.mFromTo.keyAt(i);
                ComponentName valueAt = this.mFromTo.valueAt(i);
                printWriter.print("    ");
                printWriter.print(keyAt.flattenToShortString());
                printWriter.print(" -> ");
                printWriter.print(valueAt.flattenToShortString());
                printWriter.println();
            }
            printWriter.println();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Resolution<T> {
        public final T resolved;
        public final T source;

        public Resolution(T t, T t2) {
            this.source = t;
            this.resolved = t2;
        }

        public boolean isAlias() {
            return this.resolved != null;
        }

        public T getAlias() {
            if (isAlias()) {
                return this.source;
            }
            return null;
        }

        public T getTarget() {
            if (isAlias()) {
                return this.resolved;
            }
            return null;
        }
    }

    public Resolution<ComponentName> resolveComponentAlias(Supplier<ComponentName> supplier) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                if (!this.mEnabled) {
                    return new Resolution<>(null, null);
                }
                ComponentName componentName = supplier.get();
                ComponentName componentName2 = this.mFromTo.get(componentName);
                if (componentName2 != null) {
                    Slog.d(TAG, "Alias resolved: " + componentName.flattenToShortString() + " -> " + componentName2.flattenToShortString(), Log.isLoggable(TAG, 2) ? new RuntimeException("STACKTRACE") : null);
                }
                return new Resolution<>(componentName, componentName2);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public Resolution<ComponentName> resolveService(final Intent intent, final String str, final int i, final int i2, final int i3) {
        Resolution<ComponentName> resolveComponentAlias = resolveComponentAlias(new Supplier() { // from class: com.android.server.am.ComponentAliasResolver$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final Object get() {
                ComponentName lambda$resolveService$1;
                lambda$resolveService$1 = ComponentAliasResolver.lambda$resolveService$1(intent, str, i, i2, i3);
                return lambda$resolveService$1;
            }
        });
        if (resolveComponentAlias != null && resolveComponentAlias.isAlias()) {
            intent.setOriginalIntent(new Intent(intent));
            intent.setPackage(null);
            intent.setComponent(resolveComponentAlias.getTarget());
        }
        return resolveComponentAlias;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ ComponentName lambda$resolveService$1(Intent intent, String str, int i, int i2, int i3) {
        ResolveInfo resolveService = ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).resolveService(intent, str, i, i2, i3);
        ServiceInfo serviceInfo = resolveService != null ? resolveService.serviceInfo : null;
        if (serviceInfo == null) {
            return null;
        }
        return new ComponentName(serviceInfo.applicationInfo.packageName, serviceInfo.name);
    }

    public Resolution<ResolveInfo> resolveReceiver(Intent intent, final ResolveInfo resolveInfo, String str, int i, int i2, int i3, boolean z) {
        Resolution<ComponentName> resolveComponentAlias = resolveComponentAlias(new Supplier() { // from class: com.android.server.am.ComponentAliasResolver$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                ComponentName lambda$resolveReceiver$2;
                lambda$resolveReceiver$2 = ComponentAliasResolver.lambda$resolveReceiver$2(resolveInfo);
                return lambda$resolveReceiver$2;
            }
        });
        ComponentName target = resolveComponentAlias.getTarget();
        if (target == null) {
            return new Resolution<>(resolveInfo, null);
        }
        PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        Intent intent2 = new Intent(intent);
        intent2.setPackage(null);
        intent2.setComponent(resolveComponentAlias.getTarget());
        List<ResolveInfo> queryIntentReceivers = packageManagerInternal.queryIntentReceivers(intent2, str, i, i3, i2, z);
        if (queryIntentReceivers == null || queryIntentReceivers.size() == 0) {
            Slog.w(TAG, "Alias target " + target.flattenToShortString() + " not found");
            return null;
        }
        return new Resolution<>(resolveInfo, queryIntentReceivers.get(0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ ComponentName lambda$resolveReceiver$2(ResolveInfo resolveInfo) {
        return resolveInfo.activityInfo.getComponentName();
    }
}
