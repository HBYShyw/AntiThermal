package com.android.server.location.interfaces;

import android.location.GnssMeasurementRequest;
import android.location.util.identity.CallerIdentity;
import com.android.server.location.common.IOplusCommonFeature;
import com.android.server.location.common.OplusLbsFeatureList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IGnssMeasurementsProviderExt extends IOplusCommonFeature {
    public static final IGnssMeasurementsProviderExt DEFAULT = new IGnssMeasurementsProviderExt() { // from class: com.android.server.location.interfaces.IGnssMeasurementsProviderExt.1
    };

    default void onRegistrationAdded(CallerIdentity callerIdentity, GnssMeasurementRequest gnssMeasurementRequest) {
    }

    default void onRegistrationRemoved(CallerIdentity callerIdentity) {
    }

    @Override // com.android.server.location.common.IOplusCommonFeature
    default OplusLbsFeatureList.OplusIndex index() {
        return OplusLbsFeatureList.OplusIndex.IGnssMeasurementsProviderExt;
    }

    @Override // com.android.server.location.common.IOplusCommonFeature
    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
