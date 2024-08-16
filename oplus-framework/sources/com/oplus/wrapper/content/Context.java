package com.oplus.wrapper.content;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.UserHandle;
import com.oplus.wrapper.app.IApplicationThread;
import java.io.File;

/* loaded from: classes.dex */
public class Context {
    private final android.content.Context mContext;
    public static final int BIND_FOREGROUND_SERVICE_WHILE_AWAKE = getBindForegroundServiceWhileAwake();
    public static final int BIND_FOREGROUND_SERVICE = getBindForegroundService();

    private static int getBindForegroundServiceWhileAwake() {
        return 33554432;
    }

    private static int getBindForegroundService() {
        return 67108864;
    }

    public Context(android.content.Context context) {
        this.mContext = context;
    }

    public File getSharedPreferencesPath(String name) {
        return this.mContext.getSharedPreferencesPath(name);
    }

    public int getDisplayId() {
        return this.mContext.getDisplayId();
    }

    public android.content.Context createCredentialProtectedStorageContext() {
        return this.mContext.createCredentialProtectedStorageContext();
    }

    public int getThemeResId() {
        return this.mContext.getThemeResId();
    }

    public android.content.Context createPackageContextAsUser(String packageName, int flags, UserHandle user) throws PackageManager.NameNotFoundException {
        return this.mContext.createPackageContextAsUser(packageName, flags, user);
    }

    public android.content.Context createApplicationContext(ApplicationInfo application, int flags) throws PackageManager.NameNotFoundException {
        return this.mContext.createApplicationContext(application, flags);
    }

    public ComponentName startServiceAsUser(android.content.Intent service, UserHandle user) {
        return this.mContext.startServiceAsUser(service, user);
    }

    public android.content.Intent registerReceiverForAllUsers(BroadcastReceiver receiver, android.content.IntentFilter filter, String broadcastPermission, Handler scheduler) {
        return this.mContext.registerReceiverForAllUsers(receiver, filter, broadcastPermission, scheduler);
    }

    public void startActivityAsUser(android.content.Intent intent, UserHandle user) {
        this.mContext.startActivityAsUser(intent, user);
    }

    public ComponentName startForegroundServiceAsUser(android.content.Intent service, UserHandle user) {
        return this.mContext.startForegroundServiceAsUser(service, user);
    }

    public android.content.Intent registerReceiverAsUser(BroadcastReceiver receiver, UserHandle user, android.content.IntentFilter filter, String broadcastPermission, Handler scheduler) {
        return this.mContext.registerReceiverAsUser(receiver, user, filter, broadcastPermission, scheduler);
    }

    public Handler getMainThreadHandler() {
        return this.mContext.getMainThreadHandler();
    }

    public IApplicationThread getIApplicationThread() {
        android.app.IApplicationThread iApplicationThread = this.mContext.getIApplicationThread();
        if (iApplicationThread == null) {
            return null;
        }
        IApplicationThread wrapperApplicationThread = new IApplicationThread.Stub() { // from class: com.oplus.wrapper.content.Context.1
            @Override // com.oplus.wrapper.app.IApplicationThread.Stub, android.os.IInterface
            public IBinder asBinder() {
                return super.asBinder();
            }
        };
        return wrapperApplicationThread;
    }
}
