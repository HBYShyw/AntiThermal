package com.android.server.accessibility.magnification;

import android.content.Context;
import android.provider.DeviceConfig;
import com.android.internal.annotations.VisibleForTesting;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AlwaysOnMagnificationFeatureFlag extends MagnificationFeatureFlagBase {
    private static final String FEATURE_NAME_ENABLE_ALWAYS_ON_MAGNIFICATION = "AlwaysOnMagnifier__enable_always_on_magnifier";
    private static final String NAMESPACE = "window_manager";
    private Context mContext;

    @Override // com.android.server.accessibility.magnification.MagnificationFeatureFlagBase
    String getFeatureName() {
        return FEATURE_NAME_ENABLE_ALWAYS_ON_MAGNIFICATION;
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public AlwaysOnMagnificationFeatureFlag(Context context) {
        this.mContext = context;
    }

    @Override // com.android.server.accessibility.magnification.MagnificationFeatureFlagBase
    boolean getDefaultValue() {
        return this.mContext.getResources().getBoolean(17891752);
    }
}
