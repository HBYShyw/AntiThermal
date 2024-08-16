package com.android.server.power.batterysaver;

import android.content.Context;
import com.android.server.power.batterysaver.BatterySaverPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IBatterySaverPolicyExt {
    default void init(Context context) {
    }

    default void onGetBatterySaverPolicy(int i, int i2, BatterySaverPolicy.Policy policy, boolean z) {
    }

    default void onGetGpsMode(int i, BatterySaverPolicy.Policy policy, boolean z) {
    }

    default void onIsLaunchBoostDisabled(int i, BatterySaverPolicy.Policy policy) {
    }
}
