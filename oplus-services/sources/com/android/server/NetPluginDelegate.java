package com.android.server;

import android.net.NetworkSpecifier;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.Slog;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class NetPluginDelegate {
    private static final boolean LOGV = true;
    private static final String TAG = "NetPluginDelegate";
    private static boolean extJarAvail = true;
    private static Object tcpBufferManagerObj = null;
    private static Class tcpBufferRelay = null;
    private static boolean vendorPropJarAvail = true;
    private static Object vendorPropManagerObj;
    private static Class vendorPropRelay;

    public static String get5GTcpBuffers(String str, NetworkSpecifier networkSpecifier) {
        Slog.v(TAG, "get5GTcpBuffers");
        if (!extJarAvail || !loadConnExtJar()) {
            return str;
        }
        try {
            Object invoke = tcpBufferRelay.getMethod("get5GTcpBuffers", String.class, NetworkSpecifier.class).invoke(tcpBufferManagerObj, str, networkSpecifier);
            return (invoke == null || !(invoke instanceof String)) ? str : (String) invoke;
        } catch (NoSuchMethodException | SecurityException | InvocationTargetException e) {
            Log.w(TAG, "Failed to invoke get5GTcpBuffers()");
            e.printStackTrace();
            extJarAvail = false;
            return str;
        } catch (Exception e2) {
            Log.w(TAG, "Error calling get5GTcpBuffers Method on extension jar");
            e2.printStackTrace();
            extJarAvail = false;
            return str;
        }
    }

    public static void registerHandler(Handler handler) {
        Slog.v(TAG, "registerHandler");
        if (extJarAvail && loadConnExtJar()) {
            try {
                tcpBufferRelay.getMethod("registerHandler", Handler.class).invoke(tcpBufferManagerObj, handler);
            } catch (NoSuchMethodException | SecurityException | InvocationTargetException e) {
                Log.w(TAG, "Failed to call registerHandler");
                e.printStackTrace();
                extJarAvail = false;
            } catch (Exception e2) {
                Log.w(TAG, "Error calling registerHandler Method on extension jar");
                e2.printStackTrace();
                extJarAvail = false;
            }
        }
    }

    private static synchronized boolean loadConnExtJar() {
        synchronized (NetPluginDelegate.class) {
            String str = Environment.getSystemExtDirectory().getAbsolutePath() + "/framework/ConnectivityExt.jar";
            if (tcpBufferRelay != null && tcpBufferManagerObj != null) {
                return true;
            }
            boolean exists = new File(str).exists();
            extJarAvail = exists;
            if (!exists) {
                Log.w(TAG, "ConnectivityExt jar file not present");
                return false;
            }
            if (tcpBufferRelay == null && tcpBufferManagerObj == null) {
                Slog.v(TAG, "loading ConnectivityExt jar");
                try {
                    Class loadClass = new PathClassLoader(str, ClassLoader.getSystemClassLoader()).loadClass("com.qualcomm.qti.net.connextension.TCPBufferManager");
                    tcpBufferRelay = loadClass;
                    tcpBufferManagerObj = loadClass.newInstance();
                    Slog.v(TAG, "ConnectivityExt jar loaded");
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    Log.w(TAG, "Failed to find, instantiate or access ConnectivityExt jar ");
                    e.printStackTrace();
                    extJarAvail = false;
                    return false;
                } catch (Exception e2) {
                    Log.w(TAG, "unable to load ConnectivityExt jar");
                    e2.printStackTrace();
                    extJarAvail = false;
                    return false;
                }
            }
            return true;
        }
    }
}
