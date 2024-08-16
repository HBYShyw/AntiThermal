package com.android.server.display;

import android.content.Context;
import android.media.RemoteDisplay;
import android.os.Handler;
import android.util.Slog;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class ExtendedRemoteDisplayHelper {
    private static final String TAG = "ExtendedRemoteDisplayHelper";
    private static Class sExtRemoteDisplayClass;
    private static Method sExtRemoteDisplayDispose;
    private static Method sExtRemoteDisplayListen;

    ExtendedRemoteDisplayHelper() {
    }

    static {
        try {
            sExtRemoteDisplayClass = Class.forName("com.qualcomm.wfd.ExtendedRemoteDisplay");
        } catch (Throwable unused) {
            Slog.i(TAG, "ExtendedRemoteDisplay Not available.");
        }
        if (sExtRemoteDisplayClass != null) {
            Slog.i(TAG, "ExtendedRemoteDisplay Is available. Find Methods");
            try {
                sExtRemoteDisplayListen = sExtRemoteDisplayClass.getDeclaredMethod("listen", String.class, RemoteDisplay.Listener.class, Handler.class, Context.class);
            } catch (Throwable unused2) {
                Slog.i(TAG, "ExtendedRemoteDisplay.listen Not available.");
            }
            try {
                sExtRemoteDisplayDispose = sExtRemoteDisplayClass.getDeclaredMethod("dispose", new Class[0]);
            } catch (Throwable unused3) {
                Slog.i(TAG, "ExtendedRemoteDisplay.dispose Not available.");
            }
        }
    }

    public static Object listen(String str, RemoteDisplay.Listener listener, Handler handler, Context context) {
        Slog.i(TAG, "ExtendedRemoteDisplay.listen");
        Method method = sExtRemoteDisplayListen;
        if (method == null || sExtRemoteDisplayDispose == null) {
            return null;
        }
        try {
            return method.invoke(null, str, listener, handler, context);
        } catch (IllegalAccessException e) {
            Slog.i(TAG, "ExtendedRemoteDisplay.listen -IllegalAccessException");
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e2) {
            Slog.i(TAG, "ExtendedRemoteDisplay.listen - InvocationTargetException");
            Throwable cause = e2.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
            throw new RuntimeException(e2);
        }
    }

    public static void dispose(Object obj) {
        Slog.i(TAG, "ExtendedRemoteDisplay.dispose");
        try {
            sExtRemoteDisplayDispose.invoke(obj, new Object[0]);
        } catch (IllegalAccessException e) {
            Slog.i(TAG, "ExtendedRemoteDisplay.dispose-IllegalAccessException");
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            Slog.i(TAG, "ExtendedRemoteDisplay.dispose - InvocationTargetException");
            Throwable cause = e2.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
            throw new RuntimeException(e2);
        }
    }

    public static boolean isAvailable() {
        if (sExtRemoteDisplayClass != null && sExtRemoteDisplayDispose != null && sExtRemoteDisplayListen != null) {
            Slog.i(TAG, "ExtendedRemoteDisplay isAvailable() : Available.");
            return true;
        }
        Slog.i(TAG, "ExtendedRemoteDisplay isAvailable() : Not Available.");
        return false;
    }
}
