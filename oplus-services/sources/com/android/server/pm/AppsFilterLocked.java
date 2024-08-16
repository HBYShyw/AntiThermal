package com.android.server.pm;

import com.android.server.pm.AppsFilterBase;
import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class AppsFilterLocked extends AppsFilterBase {
    protected final Object mForceQueryableLock = new Object();
    protected final Object mQueriesViaPackageLock = new Object();
    protected final Object mQueriesViaComponentLock = new Object();
    protected final Object mImplicitlyQueryableLock = new Object();
    protected final Object mQueryableViaUsesLibraryLock = new Object();
    protected final Object mProtectedBroadcastsLock = new Object();
    protected final Object mQueryableViaUsesPermissionLock = new Object();
    protected final Object mCacheLock = new Object();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AppsFilterBase
    public boolean isForceQueryable(int i) {
        boolean isForceQueryable;
        synchronized (this.mForceQueryableLock) {
            isForceQueryable = super.isForceQueryable(i);
        }
        return isForceQueryable;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AppsFilterBase
    public boolean isQueryableViaPackage(int i, int i2) {
        boolean isQueryableViaPackage;
        synchronized (this.mQueriesViaPackageLock) {
            isQueryableViaPackage = super.isQueryableViaPackage(i, i2);
        }
        return isQueryableViaPackage;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AppsFilterBase
    public boolean isQueryableViaComponent(int i, int i2) {
        boolean isQueryableViaComponent;
        synchronized (this.mQueriesViaComponentLock) {
            isQueryableViaComponent = super.isQueryableViaComponent(i, i2);
        }
        return isQueryableViaComponent;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AppsFilterBase
    public boolean isImplicitlyQueryable(int i, int i2) {
        boolean isImplicitlyQueryable;
        synchronized (this.mImplicitlyQueryableLock) {
            isImplicitlyQueryable = super.isImplicitlyQueryable(i, i2);
        }
        return isImplicitlyQueryable;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AppsFilterBase
    public boolean isRetainedImplicitlyQueryable(int i, int i2) {
        boolean isRetainedImplicitlyQueryable;
        synchronized (this.mImplicitlyQueryableLock) {
            isRetainedImplicitlyQueryable = super.isRetainedImplicitlyQueryable(i, i2);
        }
        return isRetainedImplicitlyQueryable;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AppsFilterBase
    public boolean isQueryableViaUsesLibrary(int i, int i2) {
        boolean isQueryableViaUsesLibrary;
        synchronized (this.mQueryableViaUsesLibraryLock) {
            isQueryableViaUsesLibrary = super.isQueryableViaUsesLibrary(i, i2);
        }
        return isQueryableViaUsesLibrary;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AppsFilterBase
    public boolean isQueryableViaUsesPermission(int i, int i2) {
        boolean isQueryableViaUsesPermission;
        synchronized (this.mQueryableViaUsesPermissionLock) {
            isQueryableViaUsesPermission = super.isQueryableViaUsesPermission(i, i2);
        }
        return isQueryableViaUsesPermission;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AppsFilterBase
    public boolean shouldFilterApplicationUsingCache(int i, int i2, int i3) {
        boolean shouldFilterApplicationUsingCache;
        synchronized (this.mCacheLock) {
            shouldFilterApplicationUsingCache = super.shouldFilterApplicationUsingCache(i, i2, i3);
        }
        return shouldFilterApplicationUsingCache;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AppsFilterBase
    public void dumpForceQueryable(PrintWriter printWriter, Integer num, AppsFilterBase.ToString<Integer> toString) {
        synchronized (this.mForceQueryableLock) {
            super.dumpForceQueryable(printWriter, num, toString);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AppsFilterBase
    public void dumpQueriesViaPackage(PrintWriter printWriter, Integer num, AppsFilterBase.ToString<Integer> toString) {
        synchronized (this.mQueriesViaPackageLock) {
            super.dumpQueriesViaPackage(printWriter, num, toString);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AppsFilterBase
    public void dumpQueriesViaComponent(PrintWriter printWriter, Integer num, AppsFilterBase.ToString<Integer> toString) {
        synchronized (this.mQueriesViaComponentLock) {
            super.dumpQueriesViaComponent(printWriter, num, toString);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AppsFilterBase
    public void dumpQueriesViaImplicitlyQueryable(PrintWriter printWriter, Integer num, int[] iArr, AppsFilterBase.ToString<Integer> toString) {
        synchronized (this.mImplicitlyQueryableLock) {
            super.dumpQueriesViaImplicitlyQueryable(printWriter, num, iArr, toString);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AppsFilterBase
    public void dumpQueriesViaUsesLibrary(PrintWriter printWriter, Integer num, AppsFilterBase.ToString<Integer> toString) {
        synchronized (this.mQueryableViaUsesLibraryLock) {
            super.dumpQueriesViaUsesLibrary(printWriter, num, toString);
        }
    }
}
