package com.android.server.pm;

import java.io.File;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class OriginInfo {
    final boolean mExisting;
    final File mFile;
    final File mResolvedFile;
    final String mResolvedPath;
    final boolean mStaged;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OriginInfo fromNothing() {
        return new OriginInfo(null, false, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OriginInfo fromExistingFile(File file) {
        return new OriginInfo(file, false, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OriginInfo fromStagedFile(File file) {
        return new OriginInfo(file, true, false);
    }

    private OriginInfo(File file, boolean z, boolean z2) {
        this.mFile = file;
        this.mStaged = z;
        this.mExisting = z2;
        if (file != null) {
            this.mResolvedPath = file.getAbsolutePath();
            this.mResolvedFile = file;
        } else {
            this.mResolvedPath = null;
            this.mResolvedFile = null;
        }
    }
}
