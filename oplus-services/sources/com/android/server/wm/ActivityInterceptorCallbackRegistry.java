package com.android.server.wm;

import android.annotation.SystemApi;
import android.os.Binder;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.LocalServices;

@SystemApi(client = SystemApi.Client.SYSTEM_SERVER)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivityInterceptorCallbackRegistry {
    private static final ActivityInterceptorCallbackRegistry sInstance = new ActivityInterceptorCallbackRegistry();

    private ActivityInterceptorCallbackRegistry() {
    }

    public static ActivityInterceptorCallbackRegistry getInstance() {
        return sInstance;
    }

    public void registerActivityInterceptorCallback(int i, ActivityInterceptorCallback activityInterceptorCallback) {
        if (getCallingUid() != 1000) {
            throw new SecurityException("Only system server can register ActivityInterceptorCallback");
        }
        if (!ActivityInterceptorCallback.isValidMainlineOrderId(i)) {
            throw new IllegalArgumentException("id is not in the mainline modules range, please useActivityTaskManagerInternal.registerActivityStartInterceptor(OrderedId, ActivityInterceptorCallback) instead.");
        }
        if (activityInterceptorCallback == null) {
            throw new IllegalArgumentException("The passed ActivityInterceptorCallback can not be null");
        }
        ((ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class)).registerActivityStartInterceptor(i, activityInterceptorCallback);
    }

    public void unregisterActivityInterceptorCallback(int i) {
        if (getCallingUid() != 1000) {
            throw new SecurityException("Only system server can register ActivityInterceptorCallback");
        }
        if (!ActivityInterceptorCallback.isValidMainlineOrderId(i)) {
            throw new IllegalArgumentException("id is not in the mainline modules range, please useActivityTaskManagerInternal.unregisterActivityStartInterceptor(OrderedId) instead.");
        }
        ((ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class)).unregisterActivityStartInterceptor(i);
    }

    @VisibleForTesting
    int getCallingUid() {
        return Binder.getCallingUid();
    }
}
