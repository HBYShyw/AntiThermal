package com.android.server.biometrics.sensors.tool;

import android.content.Context;
import com.android.server.biometrics.sensors.AcquisitionClient;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBiometricsVibratorUtilsExt {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface IStaticExt {
        default void init(Context context) {
        }

        default boolean vibrateFingerprintError(Context context, AcquisitionClient acquisitionClient) {
            return false;
        }

        default boolean vibrateFingerprintSuccess(Context context, AcquisitionClient acquisitionClient) {
            return false;
        }
    }
}
