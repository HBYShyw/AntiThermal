package com.android.server.media;

import android.provider.DeviceConfig;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class MediaFeatureFlagManager {
    static final String FEATURE_AUDIO_STRATEGIES_IS_USING_LEGACY_CONTROLLER = "BluetoothRouteController__enable_legacy_bluetooth_routes_controller";
    private static final String NAMESPACE_MEDIA_BETTER_TOGETHER = "media_better_together";
    private static final MediaFeatureFlagManager sInstance = new MediaFeatureFlagManager();

    @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    @interface MediaFeatureFlag {
    }

    private MediaFeatureFlagManager() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MediaFeatureFlagManager getInstance() {
        return sInstance;
    }

    public boolean getBoolean(String str, boolean z) {
        return DeviceConfig.getBoolean(NAMESPACE_MEDIA_BETTER_TOGETHER, str, z);
    }
}
