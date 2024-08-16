package com.android.server.locales;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class AppSupportedLocalesChangedAtomRecord {
    final int mCallingUid;
    int mTargetUid = -1;
    int mNumLocales = -1;
    boolean mOverrideRemoved = false;
    boolean mSameAsResConfig = false;
    boolean mSameAsPrevConfig = false;
    int mStatus = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppSupportedLocalesChangedAtomRecord(int i) {
        this.mCallingUid = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTargetUid(int i) {
        this.mTargetUid = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setNumLocales(int i) {
        this.mNumLocales = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOverrideRemoved(boolean z) {
        this.mOverrideRemoved = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSameAsResConfig(boolean z) {
        this.mSameAsResConfig = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSameAsPrevConfig(boolean z) {
        this.mSameAsPrevConfig = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setStatus(int i) {
        this.mStatus = i;
    }
}
