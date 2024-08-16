package com.android.server.media;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.IMediaResourceMonitor;
import android.os.Binder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import com.android.server.SystemService;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class MediaResourceMonitorService extends SystemService {
    private static final String SERVICE_NAME = "media_resource_monitor";
    private final MediaResourceMonitorImpl mMediaResourceMonitorImpl;
    private static final String TAG = "MediaResourceMonitor";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);

    public MediaResourceMonitorService(Context context) {
        super(context);
        this.mMediaResourceMonitorImpl = new MediaResourceMonitorImpl();
    }

    public void onStart() {
        publishBinderService(SERVICE_NAME, this.mMediaResourceMonitorImpl);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    class MediaResourceMonitorImpl extends IMediaResourceMonitor.Stub {
        MediaResourceMonitorImpl() {
        }

        public void notifyResourceGranted(int i, int i2) throws RemoteException {
            if (MediaResourceMonitorService.DEBUG) {
                Log.d(MediaResourceMonitorService.TAG, "notifyResourceGranted(pid=" + i + ", type=" + i2 + ")");
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                String[] packageNamesFromPid = getPackageNamesFromPid(i);
                if (packageNamesFromPid == null) {
                    return;
                }
                List enabledProfiles = ((UserManager) MediaResourceMonitorService.this.getContext().createContextAsUser(UserHandle.of(ActivityManager.getCurrentUser()), 0).getSystemService(UserManager.class)).getEnabledProfiles();
                if (enabledProfiles.isEmpty()) {
                    return;
                }
                Intent intent = new Intent("android.intent.action.MEDIA_RESOURCE_GRANTED");
                intent.putExtra("android.intent.extra.PACKAGES", packageNamesFromPid);
                intent.putExtra("android.intent.extra.MEDIA_RESOURCE_TYPE", i2);
                Iterator it = enabledProfiles.iterator();
                while (it.hasNext()) {
                    MediaResourceMonitorService.this.getContext().sendBroadcastAsUser(intent, (UserHandle) it.next(), "android.permission.RECEIVE_MEDIA_RESOURCE_USAGE");
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        private String[] getPackageNamesFromPid(int i) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) MediaResourceMonitorService.this.getContext().getSystemService(ActivityManager.class)).getRunningAppProcesses()) {
                if (runningAppProcessInfo.pid == i) {
                    return runningAppProcessInfo.pkgList;
                }
            }
            return null;
        }
    }
}
