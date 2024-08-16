package com.android.server.location.interfaces;

import com.android.server.location.common.IOplusCommonFeature;
import com.android.server.location.common.OplusLbsFeatureList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ILocationEventLogExt extends IOplusCommonFeature {
    public static final ILocationEventLogExt DEFAULT = new ILocationEventLogExt() { // from class: com.android.server.location.interfaces.ILocationEventLogExt.1
    };
    public static final String Name = "ILocationEventLogExt";

    default boolean enablePassiveDeliveredLocations(String str) {
        return false;
    }

    @Override // com.android.server.location.common.IOplusCommonFeature
    default OplusLbsFeatureList.OplusIndex index() {
        return OplusLbsFeatureList.OplusIndex.ILocationEventLogExt;
    }

    @Override // com.android.server.location.common.IOplusCommonFeature
    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
