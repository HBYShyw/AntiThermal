package com.android.server.accessibility.magnification;

import android.provider.DeviceConfig;
import com.android.internal.annotations.VisibleForTesting;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class MagnificationThumbnailFeatureFlag extends MagnificationFeatureFlagBase {
    private static final String FEATURE_NAME_ENABLE_MAGNIFIER_THUMBNAIL = "enable_magnifier_thumbnail";
    private static final String NAMESPACE = "accessibility";

    @Override // com.android.server.accessibility.magnification.MagnificationFeatureFlagBase
    boolean getDefaultValue() {
        return false;
    }

    @Override // com.android.server.accessibility.magnification.MagnificationFeatureFlagBase
    String getFeatureName() {
        return FEATURE_NAME_ENABLE_MAGNIFIER_THUMBNAIL;
    }

    @Override // com.android.server.accessibility.magnification.MagnificationFeatureFlagBase
    String getNamespace() {
        return NAMESPACE;
    }

    @Override // com.android.server.accessibility.magnification.MagnificationFeatureFlagBase
    public /* bridge */ /* synthetic */ DeviceConfig.OnPropertiesChangedListener addOnChangedListener(Executor executor, Runnable runnable) {
        return super.addOnChangedListener(executor, runnable);
    }

    @Override // com.android.server.accessibility.magnification.MagnificationFeatureFlagBase
    public /* bridge */ /* synthetic */ boolean isFeatureFlagEnabled() {
        return super.isFeatureFlagEnabled();
    }

    @Override // com.android.server.accessibility.magnification.MagnificationFeatureFlagBase
    public /* bridge */ /* synthetic */ void removeOnChangedListener(DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener) {
        super.removeOnChangedListener(onPropertiesChangedListener);
    }

    @Override // com.android.server.accessibility.magnification.MagnificationFeatureFlagBase
    @VisibleForTesting
    public /* bridge */ /* synthetic */ boolean setFeatureFlagEnabled(boolean z) {
        return super.setFeatureFlagEnabled(z);
    }
}
