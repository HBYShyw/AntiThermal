package com.android.server.pm;

import android.content.pm.PackageManagerInternal;
import android.util.ArraySet;
import com.android.internal.annotations.GuardedBy;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageObserverHelper {
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private ArraySet<PackageManagerInternal.PackageListObserver> mActiveSnapshot = new ArraySet<>();

    public void addObserver(PackageManagerInternal.PackageListObserver packageListObserver) {
        synchronized (this.mLock) {
            ArraySet<PackageManagerInternal.PackageListObserver> arraySet = new ArraySet<>(this.mActiveSnapshot);
            arraySet.add(packageListObserver);
            this.mActiveSnapshot = arraySet;
        }
    }

    public void removeObserver(PackageManagerInternal.PackageListObserver packageListObserver) {
        synchronized (this.mLock) {
            ArraySet<PackageManagerInternal.PackageListObserver> arraySet = new ArraySet<>(this.mActiveSnapshot);
            arraySet.remove(packageListObserver);
            this.mActiveSnapshot = arraySet;
        }
    }

    public void notifyAdded(String str, int i) {
        ArraySet<PackageManagerInternal.PackageListObserver> arraySet;
        synchronized (this.mLock) {
            arraySet = this.mActiveSnapshot;
        }
        int size = arraySet.size();
        for (int i2 = 0; i2 < size; i2++) {
            arraySet.valueAt(i2).onPackageAdded(str, i);
        }
    }

    public void notifyChanged(String str, int i) {
        ArraySet<PackageManagerInternal.PackageListObserver> arraySet;
        synchronized (this.mLock) {
            arraySet = this.mActiveSnapshot;
        }
        int size = arraySet.size();
        for (int i2 = 0; i2 < size; i2++) {
            arraySet.valueAt(i2).onPackageChanged(str, i);
        }
    }

    public void notifyRemoved(String str, int i) {
        ArraySet<PackageManagerInternal.PackageListObserver> arraySet;
        synchronized (this.mLock) {
            arraySet = this.mActiveSnapshot;
        }
        int size = arraySet.size();
        for (int i2 = 0; i2 < size; i2++) {
            arraySet.valueAt(i2).onPackageRemoved(str, i);
        }
    }
}
