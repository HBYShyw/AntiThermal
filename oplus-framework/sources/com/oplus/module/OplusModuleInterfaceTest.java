package com.oplus.module;

import android.util.Log;

/* loaded from: classes.dex */
public interface OplusModuleInterfaceTest {
    public static final String TAG = OplusModuleInterfaceTest.class.getName();

    /* loaded from: classes.dex */
    public static class Dummy implements OplusModuleInterfaceTest {
    }

    default void testOplusFrameworkIntf() {
        Log.d(TAG, "testOplusFrameworkIntf() default");
    }
}
