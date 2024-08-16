package com.android.server.devicepolicy;

import android.app.admin.DeviceStateCache;
import android.util.IndentingPrintWriter;
import com.android.internal.annotations.GuardedBy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DeviceStateCacheImpl extends DeviceStateCache {
    public static final int NO_DEVICE_OWNER = -1;
    private final Object mLock = new Object();
    private AtomicInteger mDeviceOwnerType = new AtomicInteger(-1);
    private Map<Integer, Boolean> mHasProfileOwner = new ConcurrentHashMap();
    private Map<Integer, Boolean> mAffiliationWithDevice = new ConcurrentHashMap();

    @GuardedBy({"mLock"})
    private boolean mIsDeviceProvisioned = false;

    public boolean isDeviceProvisioned() {
        return this.mIsDeviceProvisioned;
    }

    public void setDeviceProvisioned(boolean z) {
        synchronized (this.mLock) {
            this.mIsDeviceProvisioned = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDeviceOwnerType(int i) {
        this.mDeviceOwnerType.set(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHasProfileOwner(int i, boolean z) {
        if (z) {
            this.mHasProfileOwner.put(Integer.valueOf(i), Boolean.TRUE);
        } else {
            this.mHasProfileOwner.remove(Integer.valueOf(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHasAffiliationWithDevice(int i, Boolean bool) {
        if (bool.booleanValue()) {
            this.mAffiliationWithDevice.put(Integer.valueOf(i), Boolean.TRUE);
        } else {
            this.mAffiliationWithDevice.remove(Integer.valueOf(i));
        }
    }

    public boolean hasAffiliationWithDevice(int i) {
        return this.mAffiliationWithDevice.getOrDefault(Integer.valueOf(i), Boolean.FALSE).booleanValue();
    }

    public boolean isUserOrganizationManaged(int i) {
        return this.mHasProfileOwner.getOrDefault(Integer.valueOf(i), Boolean.FALSE).booleanValue() || hasEnterpriseDeviceOwner();
    }

    private boolean hasEnterpriseDeviceOwner() {
        return this.mDeviceOwnerType.get() == 0;
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("Device state cache:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("Device provisioned: " + this.mIsDeviceProvisioned);
        indentingPrintWriter.println("Device Owner Type: " + this.mDeviceOwnerType.get());
        indentingPrintWriter.println("Has PO:");
        for (Integer num : this.mHasProfileOwner.keySet()) {
            indentingPrintWriter.println("User " + num + ": " + this.mHasProfileOwner.get(num));
        }
        indentingPrintWriter.decreaseIndent();
    }
}
