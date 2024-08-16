package android.debug;

import android.content.Context;
import android.os.SystemProperties;
import android.util.Log;

/* loaded from: classes.dex */
public class CameraLogManager extends IDebugLogManager {
    private static final String CAMERA_LAO_PROP = "persist.sys.camera.lao.enable";
    private static final String TAG = TAG_BASE + CameraLogManager.class.getSimpleName();
    private static volatile CameraLogManager sInstance = null;
    private Context mContext;

    public CameraLogManager(Context context) {
        this.mContext = null;
        this.mContext = context;
    }

    public static CameraLogManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (CameraLogManager.class) {
                if (sInstance == null) {
                    sInstance = new CameraLogManager(context);
                }
            }
        }
        return sInstance;
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOn(long maxSize, String param) {
        Log.v(TAG, "setLogOn, maxSize: " + maxSize + ", param: " + param);
        SystemProperties.set(CAMERA_LAO_PROP, "true");
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOff() {
        Log.v(TAG, "setLogOff");
        SystemProperties.set(CAMERA_LAO_PROP, "false");
    }

    @Override // android.debug.IDebugLogManager
    public void setLogDump() {
    }
}
