package com.android.server.net;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.os.Handler;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IOplusNetworkStatsEx extends IOplusCommonFeature {
    public static final IOplusNetworkStatsEx DEFAULT = new IOplusNetworkStatsEx() { // from class: com.android.server.net.IOplusNetworkStatsEx.1
    };
    public static final String NAME = "IOplusNetworkStatsEx";

    default void initArgs(Context context, Handler handler) {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusNetworkStatsEx;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
