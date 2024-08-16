package com.android.server.location.common;

import com.android.server.location.common.OplusLbsFeatureList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IOplusCommonFeature {
    IOplusCommonFeature getDefault();

    default OplusLbsFeatureList.OplusIndex index() {
        return OplusLbsFeatureList.OplusIndex.End;
    }
}
