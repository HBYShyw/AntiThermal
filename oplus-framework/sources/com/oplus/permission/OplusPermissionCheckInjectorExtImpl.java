package com.oplus.permission;

import android.common.OplusFeatureCache;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.net.Uri;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class OplusPermissionCheckInjectorExtImpl implements IOplusPermissionCheckInjectorExt {
    private static volatile OplusPermissionCheckInjectorExtImpl sInstance = null;

    public OplusPermissionCheckInjectorExtImpl(Object obj) {
    }

    public static OplusPermissionCheckInjectorExtImpl getInstance(Object obj) {
        if (sInstance == null) {
            synchronized (OplusPermissionCheckInjectorExtImpl.class) {
                if (sInstance == null) {
                    sInstance = new OplusPermissionCheckInjectorExtImpl(obj);
                }
            }
        }
        return sInstance;
    }

    public boolean checkPermission(String permission, int pid, int uid, String access) {
        return OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkPermission(permission, pid, uid, access);
    }

    public boolean checkPermission(Intent intent, int pid, int uid, String access) {
        return OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkPermission(intent, pid, uid, access);
    }

    public boolean checkUriPermission(Uri uri, int pid, int uid, String access) {
        return OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkUriPermission(uri, pid, uid, access);
    }

    public boolean checkApplyBatchPermission(ArrayList<ContentProviderOperation> operations, int pid, int uid, String access) {
        return OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkApplyBatchPermission(operations, pid, uid, access);
    }
}
