package android.media;

import android.content.pm.OplusPermissionManager;
import android.os.IBinder;
import android.os.ISecurityPermissionService;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;

/* loaded from: classes.dex */
public class AudioRecordExtImpl implements IAudioRecordExt {
    private static final String TAG = "android.media.AudioRecord";

    public AudioRecordExtImpl(Object obj) {
    }

    public boolean permInterceptInStartRecording() {
        if (SystemProperties.getBoolean("persist.sys.permission.enable", false)) {
            try {
                IBinder binder = ServiceManager.getService(OplusPermissionManager.SERVICE_NAME);
                ISecurityPermissionService controller = ISecurityPermissionService.Stub.asInterface(binder);
                if (controller != null && !controller.checkOplusPermission("android.permission.RECORD_AUDIO", Process.myPid(), Process.myUid())) {
                    Log.d(TAG, "permission denied : RECORD_AUDIO");
                    return true;
                }
            } catch (RemoteException e) {
                Log.e(TAG, "check error.");
            }
        }
        return false;
    }

    public boolean permInterceptInStartRecordingWithEvent() {
        if (SystemProperties.getBoolean("persist.sys.permission.enable", false)) {
            try {
                IBinder binder = ServiceManager.getService(OplusPermissionManager.SERVICE_NAME);
                ISecurityPermissionService controller = ISecurityPermissionService.Stub.asInterface(binder);
                if (controller != null && !controller.checkOplusPermission("android.permission.RECORD_AUDIO", Process.myPid(), Process.myUid())) {
                    Log.d(TAG, "permission denied : RECORD_AUDIO");
                    return true;
                }
            } catch (RemoteException e) {
                Log.e(TAG, "check error.");
            }
        }
        return false;
    }
}
