package com.oplus.oms.split.splitload;

import android.app.Application;

/* loaded from: classes.dex */
final class Split {
    final String splitApkPath;
    final Application splitApp;
    final String splitName;
    final String splitVersion;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Split(Application splitApp, String splitName, String splitVersion, String splitApkPath) {
        this.splitApp = splitApp;
        this.splitName = splitName;
        this.splitVersion = splitVersion;
        this.splitApkPath = splitApkPath;
    }

    public String toString() {
        return "{" + this.splitName + "," + this.splitApkPath + "}";
    }
}
