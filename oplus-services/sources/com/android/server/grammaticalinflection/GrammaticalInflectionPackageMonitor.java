package com.android.server.grammaticalinflection;

import com.android.internal.content.PackageMonitor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class GrammaticalInflectionPackageMonitor extends PackageMonitor {
    private GrammaticalInflectionBackupHelper mBackupHelper;

    GrammaticalInflectionPackageMonitor(GrammaticalInflectionBackupHelper grammaticalInflectionBackupHelper) {
        this.mBackupHelper = grammaticalInflectionBackupHelper;
    }

    public void onPackageAdded(String str, int i) {
        this.mBackupHelper.onPackageAdded(str, i);
    }

    public void onPackageDataCleared(String str, int i) {
        this.mBackupHelper.onPackageDataCleared();
    }

    public void onPackageRemoved(String str, int i) {
        this.mBackupHelper.onPackageRemoved();
    }
}
