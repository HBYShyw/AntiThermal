package com.oplus.multiapp;

import android.util.Log;
import java.lang.reflect.Constructor;

/* loaded from: classes.dex */
public abstract class BaseOplusCommonAppFactory {
    private static final String TAG = "BaseOplusCommonAppFactory";

    abstract IOplusMultiApp getOplusMultiApp();

    protected static Object newInstance(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        Constructor constructor = clazz.getConstructor(new Class[0]);
        return constructor.newInstance(new Object[0]);
    }

    protected void warn(String methodName) {
        Log.w(TAG, methodName);
    }
}
