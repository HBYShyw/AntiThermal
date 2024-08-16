package com.android.server;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Slog;
import com.android.server.am.HostingRecord;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ActivityTriggerService extends SystemService {
    public static final int PROC_ADDED_NOTIFICATION = 1;
    public static final int PROC_REMOVED_NOTIFICATION = 0;
    private static String TAG = "ActivityTriggerService";
    private EventHandlerThread eventHandler;

    static native void notifyAction_native(String str, long j, String str2, int i, int i2);

    public ActivityTriggerService(Context context) {
        super(context);
        this.eventHandler = new EventHandlerThread("EventHandlerThread");
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        Slog.i(TAG, "Starting ActivityTriggerService");
        this.eventHandler.start();
        publishLocalService(ActivityTriggerService.class, this);
    }

    public void updateRecord(HostingRecord hostingRecord, ApplicationInfo applicationInfo, int i, int i2) {
        if (hostingRecord != null) {
            this.eventHandler.getHandler().post(new LocalRunnable(applicationInfo.packageName, applicationInfo.longVersionCode, applicationInfo.processName, i, i2));
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class EventHandlerThread extends HandlerThread {
        private Handler handler;

        public EventHandlerThread(String str) {
            super(str);
        }

        @Override // android.os.HandlerThread
        protected void onLooperPrepared() {
            this.handler = new Handler();
        }

        public Handler getHandler() {
            return this.handler;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    static class LocalRunnable implements Runnable {
        private int event;
        private long lvCode;
        private String packageName;
        private int pid;
        private String procName;

        LocalRunnable(String str, long j, String str2, int i, int i2) {
            this.packageName = str;
            this.lvCode = j;
            this.procName = str2;
            this.pid = i;
            this.event = i2;
        }

        @Override // java.lang.Runnable
        public void run() {
            ActivityTriggerService.notifyAction_native(this.packageName, this.lvCode, this.procName, this.pid, this.event);
        }
    }
}
