package com.android.server.pm;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class MoveInfo {
    final int mAppId;
    final String mFromCodePath;
    final String mFromUuid;
    final int mMoveId;
    final String mPackageName;
    final String mSeInfo;
    final int mTargetSdkVersion;
    final String mToUuid;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MoveInfo(int i, String str, String str2, String str3, int i2, String str4, int i3, String str5) {
        this.mMoveId = i;
        this.mFromUuid = str;
        this.mToUuid = str2;
        this.mPackageName = str3;
        this.mAppId = i2;
        this.mSeInfo = str4;
        this.mTargetSdkVersion = i3;
        this.mFromCodePath = str5;
    }
}
