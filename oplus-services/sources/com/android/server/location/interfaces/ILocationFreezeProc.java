package com.android.server.location.interfaces;

import android.app.PendingIntent;
import android.location.ILocationListener;
import android.location.LocationRequest;
import android.location.util.identity.CallerIdentity;
import com.android.server.location.common.IOplusCommonFeature;
import com.android.server.location.common.OplusLbsFeatureList;
import com.android.server.location.provider.LocationProviderManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ILocationFreezeProc extends IOplusCommonFeature {
    public static final ILocationFreezeProc DEFAULT = new ILocationFreezeProc() { // from class: com.android.server.location.interfaces.ILocationFreezeProc.1
    };
    public static final String Name = "ILocationFreezeProc";

    default boolean freezeLocationProcess(String str, boolean z, int i) {
        return true;
    }

    default void onBinderDied(Object obj, int i, String str) {
    }

    default void removeLocationRequest(Object obj) {
    }

    default boolean storeLocationRequest(LocationProviderManager locationProviderManager, LocationRequest locationRequest, CallerIdentity callerIdentity, int i, PendingIntent pendingIntent) {
        return true;
    }

    default boolean storeLocationRequest(LocationProviderManager locationProviderManager, LocationRequest locationRequest, CallerIdentity callerIdentity, int i, ILocationListener iLocationListener) {
        return true;
    }

    @Override // com.android.server.location.common.IOplusCommonFeature
    default OplusLbsFeatureList.OplusIndex index() {
        return OplusLbsFeatureList.OplusIndex.ILocationFreezeProc;
    }

    @Override // com.android.server.location.common.IOplusCommonFeature
    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
