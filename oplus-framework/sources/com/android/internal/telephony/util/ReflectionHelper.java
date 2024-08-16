package com.android.internal.telephony.util;

import android.os.SystemProperties;
import android.telephony.Rlog;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class ReflectionHelper {
    private static final boolean SWITCH_LOG = "true".equalsIgnoreCase(SystemProperties.get("persist.sys.assert.panic", "false"));
    private static final String TAG = "ReflectionHelper";

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T typeCasting(Class<T> type, Object obj) {
        if (obj != 0 && type.isInstance(obj)) {
            return obj;
        }
        return null;
    }

    public static Object callMethod(Object target, String clsName, String methodName, Class[] parameterTypes, Object[] args) {
        log(target + " callMethod : " + clsName + "." + methodName);
        try {
            Class<?> cls = Class.forName(clsName);
            Method method = cls.getMethod(methodName, parameterTypes);
            method.setAccessible(true);
            Object result = method.invoke(target, args);
            return result;
        } catch (Exception e) {
            log("callDeclaredMethod exception caught : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static Object callMethodOrThrow(Object target, String clsName, String methodName, Class[] parameterTypes, Object[] args) {
        log(target + " callMethodOrThrow : " + clsName + "." + methodName);
        try {
            Class<?> cls = Class.forName(clsName);
            Method method = cls.getMethod(methodName, parameterTypes);
            method.setAccessible(true);
            Object result = method.invoke(target, args);
            return result;
        } catch (Exception e) {
            if (SWITCH_LOG) {
                throw new RuntimeException("callMethodOrThrow");
            }
            log("callDeclaredMethod exception caught : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static Object callDeclaredMethod(Object target, String clsName, String methodName, Class[] parameterTypes, Object[] args) {
        log(target + " callDeclaredMethod : " + clsName + "." + methodName);
        try {
            Class<?> cls = Class.forName(clsName);
            Method method = cls.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            Object result = method.invoke(target, args);
            return result;
        } catch (Exception e) {
            log("callDeclaredMethod exception caught : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static Object callDeclaredMethodOrThrow(Object target, String clsName, String methodName, Class[] parameterTypes, Object[] args) {
        log(target + " callDeclaredMethodOrThrow : " + clsName + "." + methodName);
        try {
            Class<?> cls = Class.forName(clsName);
            Method method = cls.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            Object result = method.invoke(target, args);
            return result;
        } catch (Exception e) {
            if (SWITCH_LOG) {
                throw new RuntimeException("callDeclaredMethodOrThrow");
            }
            log("callDeclaredMethod exception caught : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static Object callDeclaredConstructor(String clsName, Class[] parameterTypes, Object[] args) {
        log("callDeclaredConstructor : " + clsName);
        try {
            Class<?> cls = Class.forName(clsName);
            Constructor con = cls.getDeclaredConstructor(parameterTypes);
            con.setAccessible(true);
            Object result = con.newInstance(args);
            return result;
        } catch (Exception e) {
            log("callDeclaredConstructor exception caught : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static Object callDeclaredConstructorOrThrows(String clsName, Class[] parameterTypes, Object[] args) {
        log("callDeclaredConstructor : " + clsName);
        try {
            Class<?> cls = Class.forName(clsName);
            Constructor con = cls.getDeclaredConstructor(parameterTypes);
            con.setAccessible(true);
            Object result = con.newInstance(args);
            return result;
        } catch (Exception e) {
            if (SWITCH_LOG) {
                throw new RuntimeException("getDeclaredFieldOrThrow");
            }
            log("callDeclaredConstructor exception caught : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static Object getDeclaredField(Object target, String clsName, String fieldName) {
        log(target + " getDeclaredField : " + clsName + "." + fieldName);
        try {
            Class<?> cls = Class.forName(clsName);
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object result = field.get(target);
            return result;
        } catch (Exception e) {
            log("getDeclaredField exception caught : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static Object getDeclaredFieldOrThrow(Object target, String clsName, String fieldName) {
        log(target + " getDeclaredFieldOrThrow : " + clsName + "." + fieldName);
        try {
            Class<?> cls = Class.forName(clsName);
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object result = field.get(target);
            return result;
        } catch (Exception e) {
            if (SWITCH_LOG) {
                throw new RuntimeException("getDeclaredFieldOrThrow");
            }
            log("getDeclaredField exception caught : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void setDeclaredField(Object target, String clsName, String fieldName, Object value) {
        try {
            Class<?> cls = Class.forName(clsName);
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDeclaredFieldOrThrow(Object target, String clsName, String fieldName, Object value) {
        try {
            Class<?> cls = Class.forName(clsName);
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            if (SWITCH_LOG) {
                throw new RuntimeException("setDeclaredFieldOrThrow");
            }
            e.printStackTrace();
        }
    }

    public static void log(String string) {
        Rlog.d(TAG, string);
    }
}
