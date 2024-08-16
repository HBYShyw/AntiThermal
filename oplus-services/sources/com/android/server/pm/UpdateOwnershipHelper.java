package com.android.server.pm;

import android.app.ResourcesManager;
import android.content.pm.ApplicationInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.os.IBinder;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.server.pm.parsing.pkg.AndroidPackageInternal;
import com.android.server.pm.parsing.pkg.AndroidPackageUtils;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.component.ParsedUsesPermission;
import com.android.server.usb.descriptors.UsbDescriptor;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UpdateOwnershipHelper {
    private static final int MAX_DENYLIST_SIZE = 500;
    private static final String TAG_OWNERSHIP_OPT_OUT = "deny-ownership";
    private final ArrayMap<String, ArraySet<String>> mUpdateOwnerOptOutsToOwners = new ArrayMap<>(UsbDescriptor.USB_CONTROL_TRANSFER_TIMEOUT_MS);
    private final Object mLock = new Object();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean hasValidOwnershipDenyList(PackageSetting packageSetting) {
        AndroidPackageInternal pkg = packageSetting.getPkg();
        return pkg != null && (packageSetting.isSystem() || packageSetting.isUpdatedSystemApp()) && pkg.getProperties().containsKey("android.app.PROPERTY_LEGACY_UPDATE_OWNERSHIP_DENYLIST") && usesAnyPermission(pkg, "android.permission.INSTALL_PACKAGES", "android.permission.INSTALL_PACKAGE_UPDATES");
    }

    private static boolean usesAnyPermission(AndroidPackage androidPackage, String... strArr) {
        List<ParsedUsesPermission> usesPermissions = androidPackage.getUsesPermissions();
        for (int i = 0; i < usesPermissions.size(); i++) {
            for (String str : strArr) {
                if (str.equals(usesPermissions.get(i).getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x007e, code lost:
    
        android.util.Slog.w("PackageManager", "Deny list defined by " + r0.getPackageName() + " was trucated to maximum size of 500");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ArraySet<String> readUpdateOwnerDenyList(PackageSetting packageSetting) {
        AndroidPackageInternal pkg;
        if (!hasValidOwnershipDenyList(packageSetting) || (pkg = packageSetting.getPkg()) == null) {
            return null;
        }
        ArraySet<String> arraySet = new ArraySet<>(500);
        try {
            int resourceId = pkg.getProperties().get("android.app.PROPERTY_LEGACY_UPDATE_OWNERSHIP_DENYLIST").getResourceId();
            ApplicationInfo generateAppInfoWithoutState = AndroidPackageUtils.generateAppInfoWithoutState(pkg);
            XmlResourceParser xml = ResourcesManager.getInstance().getResources((IBinder) null, generateAppInfoWithoutState.sourceDir, generateAppInfoWithoutState.splitSourceDirs, generateAppInfoWithoutState.resourceDirs, generateAppInfoWithoutState.overlayPaths, generateAppInfoWithoutState.sharedLibraryFiles, (Integer) null, Configuration.EMPTY, (CompatibilityInfo) null, (ClassLoader) null, (List) null).getXml(resourceId);
            while (true) {
                try {
                    if (xml.getEventType() == 1) {
                        break;
                    }
                    if (xml.next() == 2 && TAG_OWNERSHIP_OPT_OUT.equals(xml.getName())) {
                        xml.next();
                        String text = xml.getText();
                        if (text != null && !text.isBlank()) {
                            arraySet.add(text);
                            if (arraySet.size() > 500) {
                                break;
                            }
                        }
                    }
                } finally {
                }
            }
            xml.close();
            return arraySet;
        } catch (Exception e) {
            Slog.e("PackageManager", "Failed to parse update owner list for " + packageSetting.getPackageName(), e);
            return null;
        }
    }

    public void addToUpdateOwnerDenyList(String str, ArraySet<String> arraySet) {
        synchronized (this.mLock) {
            for (int i = 0; i < arraySet.size(); i++) {
                ArraySet<String> putIfAbsent = this.mUpdateOwnerOptOutsToOwners.putIfAbsent(arraySet.valueAt(i), new ArraySet<>(new String[]{str}));
                if (putIfAbsent != null) {
                    putIfAbsent.add(str);
                }
            }
        }
    }

    public void removeUpdateOwnerDenyList(String str) {
        synchronized (this.mLock) {
            for (int size = this.mUpdateOwnerOptOutsToOwners.size() - 1; size >= 0; size--) {
                ArrayMap<String, ArraySet<String>> arrayMap = this.mUpdateOwnerOptOutsToOwners;
                ArraySet<String> arraySet = arrayMap.get(arrayMap.keyAt(size));
                if (arraySet.remove(str) && arraySet.isEmpty()) {
                    this.mUpdateOwnerOptOutsToOwners.removeAt(size);
                }
            }
        }
    }

    public boolean isUpdateOwnershipDenylisted(String str) {
        boolean containsKey;
        synchronized (this.mLock) {
            containsKey = this.mUpdateOwnerOptOutsToOwners.containsKey(str);
        }
        return containsKey;
    }

    public boolean isUpdateOwnershipDenyListProvider(String str) {
        if (str == null) {
            return false;
        }
        synchronized (this.mLock) {
            for (int size = this.mUpdateOwnerOptOutsToOwners.size() - 1; size >= 0; size--) {
                if (this.mUpdateOwnerOptOutsToOwners.valueAt(size).contains(str)) {
                    return true;
                }
            }
            return false;
        }
    }
}
