package android.hardware.camera2.utils;

import android.hardware.camera2.OplusCameraManager;
import android.util.Log;

/* loaded from: classes.dex */
public class SurfaceUtilsExtImpl implements ISurfaceUtilsExt {
    private static final String TAG = "SurfaceUtilsExtImpl";
    private static SurfaceUtilsExtImpl sInstance;

    public static synchronized SurfaceUtilsExtImpl getInstance() {
        SurfaceUtilsExtImpl surfaceUtilsExtImpl;
        synchronized (SurfaceUtilsExtImpl.class) {
            if (sInstance == null) {
                sInstance = new SurfaceUtilsExtImpl();
                Log.i(TAG, "getInstance success!");
            }
            surfaceUtilsExtImpl = sInstance;
        }
        return surfaceUtilsExtImpl;
    }

    public boolean extendSession() {
        return OplusCameraManager.getInstance().isCameraUnitSession();
    }

    public boolean isPrivilegedApp(String packageName) {
        return OplusCameraManager.getInstance().isPrivilegedApp(packageName);
    }
}
