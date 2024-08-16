package com.android.server.devicepolicy;

import android.app.admin.DeviceAdminInfo;
import android.content.ComponentName;
import com.android.internal.util.JournaledFile;
import java.util.List;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface PolicyUpgraderDataProvider {
    Function<ComponentName, DeviceAdminInfo> getAdminInfoSupplier(int i);

    List<String> getPlatformSuspendedPackages(int i);

    int[] getUsersForUpgrade();

    JournaledFile makeDevicePoliciesJournaledFile(int i);

    JournaledFile makePoliciesVersionJournaledFile(int i);
}
