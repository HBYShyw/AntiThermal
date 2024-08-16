package android.hardware.camera2.impl;

import android.app.ActivityThread;
import android.os.Build;
import android.os.SystemProperties;

/* loaded from: classes.dex */
public class CameraCaptureSessionImplExtImpl implements ICameraCaptureSessionImplExt {
    private static final String TAG = "CameraCaptureSessionImplExtImpl";

    public CameraCaptureSessionImplExtImpl(Object o) {
    }

    public boolean modifySkipUnconfigFlag(boolean inSkipUnconfigure) {
        String packageName = ActivityThread.currentPackageName();
        String sysCamName = SystemProperties.get("ro.oplus.system.camera.name", "");
        if (packageName != null && packageName.equals(sysCamName) && Build.isMtkPlatform()) {
            return true;
        }
        return inSkipUnconfigure;
    }
}
