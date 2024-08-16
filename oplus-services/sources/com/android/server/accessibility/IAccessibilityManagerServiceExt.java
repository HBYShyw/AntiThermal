package com.android.server.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.IInterface;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.content.PackageMonitor;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAccessibilityManagerServiceExt {
    default boolean addProxyBinder(IBinder iBinder, IInterface iInterface, int i, int i2) {
        return false;
    }

    default boolean checkIfInstalledServicesNotChange(List<ResolveInfo> list, Object obj, Object obj2) {
        return false;
    }

    default AccessibilityNodeInfo getAccessibilityFocusNotLocked(AccessibilityNodeInfo accessibilityNodeInfo, AccessibilityNodeInfo.AccessibilityAction accessibilityAction) {
        return null;
    }

    default void hookPackageMonitorRegister(Context context, PackageMonitor packageMonitor) {
    }

    default boolean removeProxyBinder(IBinder iBinder, IInterface iInterface) {
        return false;
    }

    default ComponentName replaceOplusUiIntent(Context context, int i, ComponentName componentName) {
        return componentName;
    }

    default List<AccessibilityServiceInfo> getAccessibilityServiceAfterCheckCustomizeWhiteList(Context context, List<AccessibilityServiceInfo> list) {
        return new ArrayList(0);
    }
}
