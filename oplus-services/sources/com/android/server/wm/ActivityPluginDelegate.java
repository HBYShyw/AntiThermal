package com.android.server.wm;

import android.app.ActivityThread;
import android.os.Environment;
import android.provider.Settings;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivityPluginDelegate {
    private static final String FOREGROUND_ACTIVITY_TRIGGER = "foreground_activity_trigger";
    private static final boolean LOGV = false;
    private static final int MAX_CONNECT_RETRIES = 15;
    private static final String TAG = "ActivityPluginDelegate";
    private static Class activityServiceClass = null;
    private static Object activityServiceObj = null;
    private static boolean extJarAvail = true;
    static boolean isEnabled = false;
    static int mGetFeatureEnableRetryCount = 15;

    public static void activityInvokeNotification(String str, boolean z) {
        if (getFeatureFlag() && extJarAvail && loadActivityExtJar()) {
            try {
                activityServiceClass.getMethod("sendActivityInvokeNotification", String.class, Boolean.TYPE).invoke(activityServiceObj, str, Boolean.valueOf(z));
            } catch (NoSuchMethodException | SecurityException | InvocationTargetException | Exception unused) {
            }
        }
    }

    public static void activitySuspendNotification(String str, boolean z, boolean z2) {
        if (getFeatureFlag() && extJarAvail && loadActivityExtJar()) {
            try {
                Class cls = activityServiceClass;
                Class<?> cls2 = Boolean.TYPE;
                cls.getMethod("sendActivitySuspendNotification", String.class, cls2, cls2).invoke(activityServiceObj, str, Boolean.valueOf(z), Boolean.valueOf(z2));
            } catch (NoSuchMethodException | SecurityException | InvocationTargetException | Exception unused) {
            }
        }
    }

    private static synchronized boolean loadActivityExtJar() {
        synchronized (ActivityPluginDelegate.class) {
            String str = Environment.getSystemExtDirectory().getAbsolutePath() + "/framework/ActivityExt.jar";
            if (activityServiceClass != null && activityServiceObj != null) {
                return true;
            }
            boolean exists = new File(str).exists();
            extJarAvail = exists;
            if (!exists) {
                return exists;
            }
            if (activityServiceClass == null && activityServiceObj == null) {
                try {
                    try {
                        Class loadClass = new PathClassLoader(str, ClassLoader.getSystemClassLoader()).loadClass("com.qualcomm.qti.activityextension.ActivityNotifier");
                        activityServiceClass = loadClass;
                        activityServiceObj = loadClass.newInstance();
                    } catch (Exception unused) {
                        extJarAvail = false;
                        return false;
                    }
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException unused2) {
                    extJarAvail = false;
                    return false;
                }
            }
            return true;
        }
    }

    public static synchronized boolean getFeatureFlag() {
        synchronized (ActivityPluginDelegate.class) {
            boolean z = isEnabled;
            if (!z && mGetFeatureEnableRetryCount != 0) {
                boolean z2 = Settings.Global.getInt(ActivityThread.currentApplication().getApplicationContext().getContentResolver(), FOREGROUND_ACTIVITY_TRIGGER, 1) == 1;
                isEnabled = z2;
                mGetFeatureEnableRetryCount--;
                return z2;
            }
            return z;
        }
    }
}
