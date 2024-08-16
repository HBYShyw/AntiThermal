package com.android.server.pm;

import android.net.Uri;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class VerificationInfo {
    final int mInstallerUid;
    final int mOriginatingUid;
    final Uri mOriginatingUri;
    final Uri mReferrer;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VerificationInfo(Uri uri, Uri uri2, int i, int i2) {
        this.mOriginatingUri = uri;
        this.mReferrer = uri2;
        this.mOriginatingUid = i;
        this.mInstallerUid = i2;
    }
}
