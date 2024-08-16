package com.android.server.net;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.os.Handler;
import android.util.SparseBooleanArray;
import com.android.server.IOplusCommonManagerServiceEx;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IOplusNetworkPolicyManagerServiceEx extends IOplusCommonManagerServiceEx {
    public static final IOplusNetworkPolicyManagerServiceEx DEFAULT = new IOplusNetworkPolicyManagerServiceEx() { // from class: com.android.server.net.IOplusNetworkPolicyManagerServiceEx.1
    };
    public static final String TYPE_DAILY = "daily";
    public static final String TYPE_MONTH = "month";

    default boolean addThirdPartyRestrictBGWhitelistUidsUL(int i, SparseBooleanArray sparseBooleanArray, SparseBooleanArray sparseBooleanArray2) {
        return false;
    }

    default int getCloneAppUidNL(int i) {
        return i;
    }

    default boolean getGameSpaceMode() {
        return false;
    }

    default NetworkPolicyManagerService getNetworkPolicyManagerService() {
        return null;
    }

    default void googleRestrictInit(Context context, Handler handler, IPackageManager iPackageManager, Object obj) {
    }

    default boolean isCloneUidNL(int i) {
        return false;
    }

    default void removeCloneUidPolicyNL(int i) {
    }

    default void setGameSpaceMode(boolean z) {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusNetworkPolicyManagerServiceEx;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
