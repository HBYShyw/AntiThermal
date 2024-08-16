package com.mediatek.server;

import android.content.Context;
import android.util.Slog;
import com.android.server.SystemServiceManager;
import com.android.server.utils.TimingsTraceAndSlog;
import dalvik.system.PathClassLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class MtkSystemServer {
    public static PathClassLoader sClassLoader;
    private static MtkSystemServer sInstance;

    public void addBootEvent(String str) {
    }

    public void setPrameters(TimingsTraceAndSlog timingsTraceAndSlog, SystemServiceManager systemServiceManager, Context context) {
    }

    public boolean startMtkAlarmManagerService() {
        return false;
    }

    public void startMtkBootstrapServices() {
    }

    public void startMtkCoreServices() {
    }

    public void startMtkOtherServices() {
    }

    public static MtkSystemServer getInstance() {
        if (sInstance == null) {
            try {
                PathClassLoader pathClassLoader = new PathClassLoader("/system_ext/framework/mediatek-services.jar", MtkSystemServer.class.getClassLoader());
                sClassLoader = pathClassLoader;
                sInstance = (MtkSystemServer) Class.forName("com.mediatek.server.MtkSystemServerImpl", false, pathClassLoader).getConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (Exception e) {
                Slog.e("MtkSystemServer", "getInstance: " + e.toString());
                sInstance = new MtkSystemServer();
            }
        }
        return sInstance;
    }
}
