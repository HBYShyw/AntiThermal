package com.android.server.wm;

import android.content.res.Configuration;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ConfigurationContainerListener {
    default void onMergedOverrideConfigurationChanged(Configuration configuration) {
    }

    default void onRequestedOverrideConfigurationChanged(Configuration configuration) {
    }
}
