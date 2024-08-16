package com.android.server.pm;

import dalvik.system.CloseGuard;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class PackageFreezer implements AutoCloseable {
    private final CloseGuard mCloseGuard;
    private final AtomicBoolean mClosed;
    private final String mPackageName;
    private final PackageManagerService mPm;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PackageFreezer(PackageManagerService packageManagerService) {
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        this.mClosed = atomicBoolean;
        CloseGuard closeGuard = CloseGuard.get();
        this.mCloseGuard = closeGuard;
        this.mPm = packageManagerService;
        this.mPackageName = null;
        atomicBoolean.set(true);
        closeGuard.open("close");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PackageFreezer(String str, int i, String str2, PackageManagerService packageManagerService, int i2) {
        PackageSetting packageLPr;
        this.mClosed = new AtomicBoolean();
        CloseGuard closeGuard = CloseGuard.get();
        this.mCloseGuard = closeGuard;
        this.mPm = packageManagerService;
        this.mPackageName = str;
        synchronized (packageManagerService.mLock) {
            packageManagerService.mFrozenPackages.put(str, Integer.valueOf(packageManagerService.mFrozenPackages.getOrDefault(str, 0).intValue() + 1));
            packageLPr = packageManagerService.mSettings.getPackageLPr(str);
        }
        if (packageLPr != null) {
            packageManagerService.killApplication(packageLPr.getPackageName(), packageLPr.getAppId(), i, str2, i2);
        }
        closeGuard.open("close");
    }

    protected void finalize() throws Throwable {
        try {
            this.mCloseGuard.warnIfOpen();
            close();
        } finally {
            super.finalize();
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mCloseGuard.close();
        if (this.mClosed.compareAndSet(false, true)) {
            synchronized (this.mPm.mLock) {
                int intValue = this.mPm.mFrozenPackages.getOrDefault(this.mPackageName, 0).intValue() - 1;
                if (intValue > 0) {
                    this.mPm.mFrozenPackages.put(this.mPackageName, Integer.valueOf(intValue));
                } else {
                    this.mPm.mFrozenPackages.remove(this.mPackageName);
                }
            }
        }
    }
}
