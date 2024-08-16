package com.android.server.devicepolicy;

import android.app.role.RoleManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import android.view.inputmethod.InputMethodInfo;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FunctionalUtils;
import com.android.internal.util.Preconditions;
import com.android.server.devicepolicy.OverlayPackagesProvider;
import com.android.server.inputmethod.InputMethodManagerInternal;
import com.android.server.pm.ApexManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class OverlayPackagesProvider {
    protected static final String TAG = "OverlayPackagesProvider";
    private static final Map<String, String> sActionToMetadataKeyMap = new HashMap();
    private static final Set<String> sAllowedActions = new HashSet();
    private final Context mContext;
    private final Injector mInjector;
    private final PackageManager mPm;

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Injector {
        String getActiveApexPackageNameContainingPackage(String str);

        String getDevicePolicyManagementRoleHolderPackageName(Context context);

        List<InputMethodInfo> getInputMethodListAsUser(int i);
    }

    public OverlayPackagesProvider(Context context) {
        this(context, new DefaultInjector());
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static final class DefaultInjector implements Injector {
        private DefaultInjector() {
        }

        @Override // com.android.server.devicepolicy.OverlayPackagesProvider.Injector
        public List<InputMethodInfo> getInputMethodListAsUser(int i) {
            return InputMethodManagerInternal.get().getInputMethodListAsUser(i);
        }

        @Override // com.android.server.devicepolicy.OverlayPackagesProvider.Injector
        public String getActiveApexPackageNameContainingPackage(String str) {
            return ApexManager.getInstance().getActiveApexPackageNameContainingPackage(str);
        }

        @Override // com.android.server.devicepolicy.OverlayPackagesProvider.Injector
        public String getDevicePolicyManagementRoleHolderPackageName(final Context context) {
            return (String) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.devicepolicy.OverlayPackagesProvider$DefaultInjector$$ExternalSyntheticLambda0
                public final Object getOrThrow() {
                    String lambda$getDevicePolicyManagementRoleHolderPackageName$0;
                    lambda$getDevicePolicyManagementRoleHolderPackageName$0 = OverlayPackagesProvider.DefaultInjector.lambda$getDevicePolicyManagementRoleHolderPackageName$0(context);
                    return lambda$getDevicePolicyManagementRoleHolderPackageName$0;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ String lambda$getDevicePolicyManagementRoleHolderPackageName$0(Context context) throws Exception {
            List roleHolders = ((RoleManager) context.getSystemService(RoleManager.class)).getRoleHolders("android.app.role.DEVICE_POLICY_MANAGEMENT");
            if (roleHolders.isEmpty()) {
                return null;
            }
            return (String) roleHolders.get(0);
        }
    }

    @VisibleForTesting
    OverlayPackagesProvider(Context context, Injector injector) {
        Map<String, String> map = sActionToMetadataKeyMap;
        map.put("android.app.action.PROVISION_MANAGED_USER", "android.app.REQUIRED_APP_MANAGED_USER");
        map.put("android.app.action.PROVISION_MANAGED_PROFILE", "android.app.REQUIRED_APP_MANAGED_PROFILE");
        map.put("android.app.action.PROVISION_MANAGED_DEVICE", "android.app.REQUIRED_APP_MANAGED_DEVICE");
        Set<String> set = sAllowedActions;
        set.add("android.app.action.PROVISION_MANAGED_USER");
        set.add("android.app.action.PROVISION_MANAGED_PROFILE");
        set.add("android.app.action.PROVISION_MANAGED_DEVICE");
        this.mContext = context;
        this.mPm = (PackageManager) Preconditions.checkNotNull(context.getPackageManager());
        this.mInjector = (Injector) Preconditions.checkNotNull(injector);
    }

    public Set<String> getNonRequiredApps(ComponentName componentName, int i, String str) {
        Objects.requireNonNull(componentName);
        Preconditions.checkArgument(sAllowedActions.contains(str));
        Set<String> launchableApps = getLaunchableApps(i);
        launchableApps.removeAll(getRequiredApps(str, componentName.getPackageName()));
        launchableApps.removeAll(getSystemInputMethods(i));
        launchableApps.addAll(getDisallowedApps(str));
        launchableApps.removeAll(getRequiredAppsMainlineModules(launchableApps, str));
        launchableApps.removeAll(getDeviceManagerRoleHolders());
        return launchableApps;
    }

    private Set<String> getDeviceManagerRoleHolders() {
        HashSet hashSet = new HashSet();
        String devicePolicyManagementRoleHolderPackageName = this.mInjector.getDevicePolicyManagementRoleHolderPackageName(this.mContext);
        if (devicePolicyManagementRoleHolderPackageName != null) {
            hashSet.add(devicePolicyManagementRoleHolderPackageName);
        }
        return hashSet;
    }

    private Set<String> getRequiredAppsMainlineModules(Set<String> set, String str) {
        HashSet hashSet = new HashSet();
        for (String str2 : set) {
            if (isMainlineModule(str2) && isRequiredAppDeclaredInMetadata(str2, str)) {
                hashSet.add(str2);
            }
        }
        return hashSet;
    }

    private boolean isRequiredAppDeclaredInMetadata(String str, String str2) {
        try {
            PackageInfo packageInfo = this.mPm.getPackageInfo(str, 128);
            return packageInfo.applicationInfo.metaData.getBoolean(sActionToMetadataKeyMap.get(str2));
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private boolean isMainlineModule(String str) {
        return isRegularMainlineModule(str) || isApkInApexMainlineModule(str);
    }

    private boolean isRegularMainlineModule(String str) {
        try {
            this.mPm.getModuleInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private boolean isApkInApexMainlineModule(String str) {
        return this.mInjector.getActiveApexPackageNameContainingPackage(str) != null;
    }

    private Set<String> getLaunchableApps(int i) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        List queryIntentActivitiesAsUser = this.mPm.queryIntentActivitiesAsUser(intent, 795136, i);
        ArraySet arraySet = new ArraySet();
        Iterator it = queryIntentActivitiesAsUser.iterator();
        while (it.hasNext()) {
            arraySet.add(((ResolveInfo) it.next()).activityInfo.packageName);
        }
        return arraySet;
    }

    private Set<String> getSystemInputMethods(int i) {
        List<InputMethodInfo> inputMethodListAsUser = this.mInjector.getInputMethodListAsUser(i);
        ArraySet arraySet = new ArraySet();
        for (InputMethodInfo inputMethodInfo : inputMethodListAsUser) {
            if (inputMethodInfo.getServiceInfo().applicationInfo.isSystemApp()) {
                arraySet.add(inputMethodInfo.getPackageName());
            }
        }
        return arraySet;
    }

    private Set<String> getRequiredApps(String str, String str2) {
        ArraySet arraySet = new ArraySet();
        arraySet.addAll(getRequiredAppsSet(str));
        arraySet.addAll(getVendorRequiredAppsSet(str));
        arraySet.add(str2);
        return arraySet;
    }

    private Set<String> getDisallowedApps(String str) {
        ArraySet arraySet = new ArraySet();
        arraySet.addAll(getDisallowedAppsSet(str));
        arraySet.addAll(getVendorDisallowedAppsSet(str));
        return arraySet;
    }

    private Set<String> getRequiredAppsSet(String str) {
        int i;
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -920528692:
                if (str.equals("android.app.action.PROVISION_MANAGED_DEVICE")) {
                    c = 0;
                    break;
                }
                break;
            case -514404415:
                if (str.equals("android.app.action.PROVISION_MANAGED_USER")) {
                    c = 1;
                    break;
                }
                break;
            case -340845101:
                if (str.equals("android.app.action.PROVISION_MANAGED_PROFILE")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                i = 17236201;
                break;
            case 1:
                i = 17236203;
                break;
            case 2:
                i = 17236202;
                break;
            default:
                throw new IllegalArgumentException("Provisioning type " + str + " not supported.");
        }
        return new ArraySet(Arrays.asList(this.mContext.getResources().getStringArray(i)));
    }

    private Set<String> getDisallowedAppsSet(String str) {
        int i;
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -920528692:
                if (str.equals("android.app.action.PROVISION_MANAGED_DEVICE")) {
                    c = 0;
                    break;
                }
                break;
            case -514404415:
                if (str.equals("android.app.action.PROVISION_MANAGED_USER")) {
                    c = 1;
                    break;
                }
                break;
            case -340845101:
                if (str.equals("android.app.action.PROVISION_MANAGED_PROFILE")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                i = 17236181;
                break;
            case 1:
                i = 17236183;
                break;
            case 2:
                i = 17236182;
                break;
            default:
                throw new IllegalArgumentException("Provisioning type " + str + " not supported.");
        }
        return new ArraySet(Arrays.asList(this.mContext.getResources().getStringArray(i)));
    }

    private Set<String> getVendorRequiredAppsSet(String str) {
        int i;
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -920528692:
                if (str.equals("android.app.action.PROVISION_MANAGED_DEVICE")) {
                    c = 0;
                    break;
                }
                break;
            case -514404415:
                if (str.equals("android.app.action.PROVISION_MANAGED_USER")) {
                    c = 1;
                    break;
                }
                break;
            case -340845101:
                if (str.equals("android.app.action.PROVISION_MANAGED_PROFILE")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                i = 17236216;
                break;
            case 1:
                i = 17236218;
                break;
            case 2:
                i = 17236217;
                break;
            default:
                throw new IllegalArgumentException("Provisioning type " + str + " not supported.");
        }
        return new ArraySet(Arrays.asList(this.mContext.getResources().getStringArray(i)));
    }

    private Set<String> getVendorDisallowedAppsSet(String str) {
        int i;
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -920528692:
                if (str.equals("android.app.action.PROVISION_MANAGED_DEVICE")) {
                    c = 0;
                    break;
                }
                break;
            case -514404415:
                if (str.equals("android.app.action.PROVISION_MANAGED_USER")) {
                    c = 1;
                    break;
                }
                break;
            case -340845101:
                if (str.equals("android.app.action.PROVISION_MANAGED_PROFILE")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                i = 17236212;
                break;
            case 1:
                i = 17236214;
                break;
            case 2:
                i = 17236213;
                break;
            default:
                throw new IllegalArgumentException("Provisioning type " + str + " not supported.");
        }
        return new ArraySet(Arrays.asList(this.mContext.getResources().getStringArray(i)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println(TAG);
        indentingPrintWriter.increaseIndent();
        DevicePolicyManagerService.dumpResources(indentingPrintWriter, this.mContext, "required_apps_managed_device", 17236201);
        DevicePolicyManagerService.dumpResources(indentingPrintWriter, this.mContext, "required_apps_managed_user", 17236203);
        DevicePolicyManagerService.dumpResources(indentingPrintWriter, this.mContext, "required_apps_managed_profile", 17236202);
        DevicePolicyManagerService.dumpResources(indentingPrintWriter, this.mContext, "disallowed_apps_managed_device", 17236181);
        DevicePolicyManagerService.dumpResources(indentingPrintWriter, this.mContext, "disallowed_apps_managed_user", 17236183);
        DevicePolicyManagerService.dumpResources(indentingPrintWriter, this.mContext, "disallowed_apps_managed_device", 17236181);
        DevicePolicyManagerService.dumpResources(indentingPrintWriter, this.mContext, "vendor_required_apps_managed_device", 17236216);
        DevicePolicyManagerService.dumpResources(indentingPrintWriter, this.mContext, "vendor_required_apps_managed_user", 17236218);
        DevicePolicyManagerService.dumpResources(indentingPrintWriter, this.mContext, "vendor_required_apps_managed_profile", 17236217);
        DevicePolicyManagerService.dumpResources(indentingPrintWriter, this.mContext, "vendor_disallowed_apps_managed_user", 17236214);
        DevicePolicyManagerService.dumpResources(indentingPrintWriter, this.mContext, "vendor_disallowed_apps_managed_device", 17236212);
        DevicePolicyManagerService.dumpResources(indentingPrintWriter, this.mContext, "vendor_disallowed_apps_managed_profile", 17236213);
        indentingPrintWriter.decreaseIndent();
    }
}
