package com.android.server.wm;

import android.R;
import android.content.Context;
import android.graphics.Color;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.Function;
import java.util.function.Supplier;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class LetterboxConfiguration {
    static final float DEFAULT_LETTERBOX_ASPECT_RATIO_FOR_MULTI_WINDOW = 1.01f;
    static final int LETTERBOX_BACKGROUND_APP_COLOR_BACKGROUND = 1;
    static final int LETTERBOX_BACKGROUND_APP_COLOR_BACKGROUND_FLOATING = 2;
    static final int LETTERBOX_BACKGROUND_SOLID_COLOR = 0;
    static final int LETTERBOX_BACKGROUND_WALLPAPER = 3;
    static final int LETTERBOX_HORIZONTAL_REACHABILITY_POSITION_CENTER = 1;
    static final int LETTERBOX_HORIZONTAL_REACHABILITY_POSITION_LEFT = 0;
    static final int LETTERBOX_HORIZONTAL_REACHABILITY_POSITION_RIGHT = 2;
    static final float LETTERBOX_POSITION_MULTIPLIER_CENTER = 0.5f;
    static final int LETTERBOX_VERTICAL_REACHABILITY_POSITION_BOTTOM = 2;
    static final int LETTERBOX_VERTICAL_REACHABILITY_POSITION_CENTER = 1;
    static final int LETTERBOX_VERTICAL_REACHABILITY_POSITION_TOP = 0;
    static final float MIN_FIXED_ORIENTATION_LETTERBOX_ASPECT_RATIO = 1.0f;
    private static final String TAG = "ActivityTaskManager";
    final Context mContext;
    private float mDefaultMinAspectRatioForUnresizableApps;
    private int mDefaultPositionForHorizontalReachability;
    private int mDefaultPositionForVerticalReachability;
    private final LetterboxConfigurationDeviceConfig mDeviceConfig;
    private float mFixedOrientationLetterboxAspectRatio;
    private boolean mIsAutomaticReachabilityInBookModeEnabled;
    private boolean mIsCameraCompatRefreshCycleThroughStopEnabled;
    private final boolean mIsCameraCompatSplitScreenAspectRatioEnabled;
    private final boolean mIsCameraCompatTreatmentEnabled;
    private boolean mIsCameraCompatTreatmentRefreshEnabled;
    private boolean mIsCompatFakeFocusEnabled;
    private boolean mIsDisplayAspectRatioEnabledForFixedOrientationLetterbox;
    private final boolean mIsDisplayRotationImmersiveAppCompatPolicyEnabled;
    private boolean mIsEducationEnabled;
    private boolean mIsHorizontalReachabilityEnabled;
    private final boolean mIsPolicyForIgnoringRequestedOrientationEnabled;
    private boolean mIsSplitScreenAspectRatioForUnresizableAppsEnabled;
    private boolean mIsVerticalReachabilityEnabled;
    private int mLetterboxActivityCornersRadius;
    private Color mLetterboxBackgroundColorOverride;
    private Integer mLetterboxBackgroundColorResourceIdOverride;
    private int mLetterboxBackgroundType;
    private int mLetterboxBackgroundWallpaperBlurRadius;
    private float mLetterboxBackgroundWallpaperDarkScrimAlpha;
    private float mLetterboxBookModePositionMultiplier;
    private final LetterboxConfigurationPersister mLetterboxConfigurationPersister;
    private float mLetterboxHorizontalPositionMultiplier;
    private float mLetterboxTabletopModePositionMultiplier;
    private float mLetterboxVerticalPositionMultiplier;
    private boolean mTranslucentLetterboxingEnabled;
    private boolean mTranslucentLetterboxingOverrideEnabled;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    @interface LetterboxBackgroundType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    @interface LetterboxHorizontalReachabilityPosition {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    @interface LetterboxVerticalReachabilityPosition {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LetterboxConfiguration(final Context context) {
        this(context, new LetterboxConfigurationPersister(context, new Supplier() { // from class: com.android.server.wm.LetterboxConfiguration$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final Object get() {
                Integer lambda$new$0;
                lambda$new$0 = LetterboxConfiguration.lambda$new$0(context);
                return lambda$new$0;
            }
        }, new Supplier() { // from class: com.android.server.wm.LetterboxConfiguration$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final Object get() {
                Integer lambda$new$1;
                lambda$new$1 = LetterboxConfiguration.lambda$new$1(context);
                return lambda$new$1;
            }
        }, new Supplier() { // from class: com.android.server.wm.LetterboxConfiguration$$ExternalSyntheticLambda4
            @Override // java.util.function.Supplier
            public final Object get() {
                Integer lambda$new$2;
                lambda$new$2 = LetterboxConfiguration.lambda$new$2(context);
                return lambda$new$2;
            }
        }, new Supplier() { // from class: com.android.server.wm.LetterboxConfiguration$$ExternalSyntheticLambda5
            @Override // java.util.function.Supplier
            public final Object get() {
                Integer lambda$new$3;
                lambda$new$3 = LetterboxConfiguration.lambda$new$3(context);
                return lambda$new$3;
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$new$0(Context context) {
        return Integer.valueOf(readLetterboxHorizontalReachabilityPositionFromConfig(context, false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$new$1(Context context) {
        return Integer.valueOf(readLetterboxVerticalReachabilityPositionFromConfig(context, false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$new$2(Context context) {
        return Integer.valueOf(readLetterboxHorizontalReachabilityPositionFromConfig(context, true));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$new$3(Context context) {
        return Integer.valueOf(readLetterboxVerticalReachabilityPositionFromConfig(context, true));
    }

    @VisibleForTesting
    LetterboxConfiguration(Context context, LetterboxConfigurationPersister letterboxConfigurationPersister) {
        this.mIsCameraCompatTreatmentRefreshEnabled = true;
        this.mIsCameraCompatRefreshCycleThroughStopEnabled = true;
        this.mContext = context;
        LetterboxConfigurationDeviceConfig letterboxConfigurationDeviceConfig = new LetterboxConfigurationDeviceConfig(context.getMainExecutor());
        this.mDeviceConfig = letterboxConfigurationDeviceConfig;
        this.mFixedOrientationLetterboxAspectRatio = context.getResources().getFloat(R.dimen.config_screenBrightnessSettingForVrDefaultFloat);
        this.mLetterboxActivityCornersRadius = context.getResources().getInteger(R.integer.config_nightDisplayColorTemperatureMax);
        this.mLetterboxBackgroundType = readLetterboxBackgroundTypeFromConfig(context);
        this.mLetterboxBackgroundWallpaperBlurRadius = context.getResources().getDimensionPixelSize(R.dimen.config_scrollFactor);
        this.mLetterboxBackgroundWallpaperDarkScrimAlpha = context.getResources().getFloat(R.dimen.config_screen_magnification_scaling_threshold);
        this.mLetterboxHorizontalPositionMultiplier = context.getResources().getFloat(R.dimen.config_signalCutoutWidthFraction);
        this.mLetterboxVerticalPositionMultiplier = context.getResources().getFloat(R.dimen.config_viewConfigurationHoverSlop);
        this.mLetterboxBookModePositionMultiplier = context.getResources().getFloat(R.dimen.config_scrollbarSize);
        this.mLetterboxTabletopModePositionMultiplier = context.getResources().getFloat(R.dimen.config_verticalScrollFactor);
        this.mIsHorizontalReachabilityEnabled = context.getResources().getBoolean(17891739);
        this.mIsVerticalReachabilityEnabled = context.getResources().getBoolean(17891742);
        this.mIsAutomaticReachabilityInBookModeEnabled = context.getResources().getBoolean(17891734);
        this.mDefaultPositionForHorizontalReachability = readLetterboxHorizontalReachabilityPositionFromConfig(context, false);
        this.mDefaultPositionForVerticalReachability = readLetterboxVerticalReachabilityPositionFromConfig(context, false);
        this.mIsEducationEnabled = context.getResources().getBoolean(17891737);
        setDefaultMinAspectRatioForUnresizableApps(context.getResources().getFloat(R.dimen.config_signalCutoutHeightFraction));
        this.mIsSplitScreenAspectRatioForUnresizableAppsEnabled = context.getResources().getBoolean(17891741);
        this.mIsDisplayAspectRatioEnabledForFixedOrientationLetterbox = context.getResources().getBoolean(17891735);
        this.mTranslucentLetterboxingEnabled = context.getResources().getBoolean(17891738);
        boolean z = context.getResources().getBoolean(17891726);
        this.mIsCameraCompatTreatmentEnabled = z;
        this.mIsCameraCompatSplitScreenAspectRatioEnabled = context.getResources().getBoolean(17891725);
        this.mIsCompatFakeFocusEnabled = context.getResources().getBoolean(17891721);
        this.mIsPolicyForIgnoringRequestedOrientationEnabled = context.getResources().getBoolean(17891740);
        boolean z2 = context.getResources().getBoolean(17891736);
        this.mIsDisplayRotationImmersiveAppCompatPolicyEnabled = z2;
        letterboxConfigurationDeviceConfig.updateFlagActiveStatus(z, "enable_compat_camera_treatment");
        letterboxConfigurationDeviceConfig.updateFlagActiveStatus(z2, "enable_display_rotation_immersive_app_compat_policy");
        letterboxConfigurationDeviceConfig.updateFlagActiveStatus(true, "allow_ignore_orientation_request");
        letterboxConfigurationDeviceConfig.updateFlagActiveStatus(this.mIsCompatFakeFocusEnabled, "enable_compat_fake_focus");
        letterboxConfigurationDeviceConfig.updateFlagActiveStatus(this.mTranslucentLetterboxingEnabled, "enable_letterbox_translucent_activity");
        this.mLetterboxConfigurationPersister = letterboxConfigurationPersister;
        letterboxConfigurationPersister.start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isIgnoreOrientationRequestAllowed() {
        return this.mDeviceConfig.getFlag("allow_ignore_orientation_request");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setFixedOrientationLetterboxAspectRatio(float f) {
        this.mFixedOrientationLetterboxAspectRatio = f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetFixedOrientationLetterboxAspectRatio() {
        this.mFixedOrientationLetterboxAspectRatio = this.mContext.getResources().getFloat(R.dimen.config_screenBrightnessSettingForVrDefaultFloat);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getFixedOrientationLetterboxAspectRatio() {
        return this.mFixedOrientationLetterboxAspectRatio;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetDefaultMinAspectRatioForUnresizableApps() {
        setDefaultMinAspectRatioForUnresizableApps(this.mContext.getResources().getFloat(R.dimen.config_signalCutoutHeightFraction));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getDefaultMinAspectRatioForUnresizableApps() {
        return this.mDefaultMinAspectRatioForUnresizableApps;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDefaultMinAspectRatioForUnresizableApps(float f) {
        this.mDefaultMinAspectRatioForUnresizableApps = f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLetterboxActivityCornersRadius(int i) {
        this.mLetterboxActivityCornersRadius = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetLetterboxActivityCornersRadius() {
        this.mLetterboxActivityCornersRadius = this.mContext.getResources().getInteger(R.integer.config_nightDisplayColorTemperatureMax);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLetterboxActivityCornersRounded() {
        return getLetterboxActivityCornersRadius() != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLetterboxActivityCornersRadius() {
        return this.mLetterboxActivityCornersRadius;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Color getLetterboxBackgroundColor() {
        Color color = this.mLetterboxBackgroundColorOverride;
        if (color != null) {
            return color;
        }
        Integer num = this.mLetterboxBackgroundColorResourceIdOverride;
        return Color.valueOf(this.mContext.getResources().getColor(num != null ? num.intValue() : R.color.primary_text_disable_only_holo_light));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLetterboxBackgroundColor(Color color) {
        this.mLetterboxBackgroundColorOverride = color;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLetterboxBackgroundColorResourceId(int i) {
        this.mLetterboxBackgroundColorResourceIdOverride = Integer.valueOf(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetLetterboxBackgroundColor() {
        this.mLetterboxBackgroundColorOverride = null;
        this.mLetterboxBackgroundColorResourceIdOverride = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLetterboxBackgroundType() {
        return this.mLetterboxBackgroundType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLetterboxBackgroundType(int i) {
        this.mLetterboxBackgroundType = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetLetterboxBackgroundType() {
        this.mLetterboxBackgroundType = readLetterboxBackgroundTypeFromConfig(this.mContext);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String letterboxBackgroundTypeToString(int i) {
        if (i == 0) {
            return "LETTERBOX_BACKGROUND_SOLID_COLOR";
        }
        if (i == 1) {
            return "LETTERBOX_BACKGROUND_APP_COLOR_BACKGROUND";
        }
        if (i == 2) {
            return "LETTERBOX_BACKGROUND_APP_COLOR_BACKGROUND_FLOATING";
        }
        if (i == 3) {
            return "LETTERBOX_BACKGROUND_WALLPAPER";
        }
        return "unknown=" + i;
    }

    private static int readLetterboxBackgroundTypeFromConfig(Context context) {
        int integer = context.getResources().getInteger(R.integer.config_nightDisplayColorTemperatureMin);
        if (integer == 0 || integer == 1 || integer == 2 || integer == 3) {
            return integer;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLetterboxBackgroundWallpaperDarkScrimAlpha(float f) {
        this.mLetterboxBackgroundWallpaperDarkScrimAlpha = f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetLetterboxBackgroundWallpaperDarkScrimAlpha() {
        this.mLetterboxBackgroundWallpaperDarkScrimAlpha = this.mContext.getResources().getFloat(R.dimen.config_screen_magnification_scaling_threshold);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getLetterboxBackgroundWallpaperDarkScrimAlpha() {
        return this.mLetterboxBackgroundWallpaperDarkScrimAlpha;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLetterboxBackgroundWallpaperBlurRadius(int i) {
        this.mLetterboxBackgroundWallpaperBlurRadius = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetLetterboxBackgroundWallpaperBlurRadius() {
        this.mLetterboxBackgroundWallpaperBlurRadius = this.mContext.getResources().getDimensionPixelSize(R.dimen.config_scrollFactor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLetterboxBackgroundWallpaperBlurRadius() {
        return this.mLetterboxBackgroundWallpaperBlurRadius;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getLetterboxHorizontalPositionMultiplier(boolean z) {
        if (z) {
            float f = this.mLetterboxBookModePositionMultiplier;
            if (f >= 0.0f && f <= 1.0f) {
                return f;
            }
            Slog.w(TAG, "mLetterboxBookModePositionMultiplier out of bounds (isInBookMode=true): " + this.mLetterboxBookModePositionMultiplier);
            return 0.0f;
        }
        float f2 = this.mLetterboxHorizontalPositionMultiplier;
        if (f2 >= 0.0f && f2 <= 1.0f) {
            return f2;
        }
        Slog.w(TAG, "mLetterboxBookModePositionMultiplier out of bounds (isInBookMode=false):" + this.mLetterboxBookModePositionMultiplier);
        return 0.5f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getLetterboxVerticalPositionMultiplier(boolean z) {
        if (z) {
            float f = this.mLetterboxTabletopModePositionMultiplier;
            if (f < 0.0f || f > 1.0f) {
                return 0.0f;
            }
            return f;
        }
        float f2 = this.mLetterboxVerticalPositionMultiplier;
        if (f2 < 0.0f || f2 > 1.0f) {
            return 0.5f;
        }
        return f2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLetterboxHorizontalPositionMultiplier(float f) {
        this.mLetterboxHorizontalPositionMultiplier = f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLetterboxVerticalPositionMultiplier(float f) {
        this.mLetterboxVerticalPositionMultiplier = f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetLetterboxHorizontalPositionMultiplier() {
        this.mLetterboxHorizontalPositionMultiplier = this.mContext.getResources().getFloat(R.dimen.config_signalCutoutWidthFraction);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetLetterboxVerticalPositionMultiplier() {
        this.mLetterboxVerticalPositionMultiplier = this.mContext.getResources().getFloat(R.dimen.config_viewConfigurationHoverSlop);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getIsHorizontalReachabilityEnabled() {
        return this.mIsHorizontalReachabilityEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getIsVerticalReachabilityEnabled() {
        return this.mIsVerticalReachabilityEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getIsAutomaticReachabilityInBookModeEnabled() {
        return this.mIsAutomaticReachabilityInBookModeEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setIsHorizontalReachabilityEnabled(boolean z) {
        this.mIsHorizontalReachabilityEnabled = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setIsVerticalReachabilityEnabled(boolean z) {
        this.mIsVerticalReachabilityEnabled = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setIsAutomaticReachabilityInBookModeEnabled(boolean z) {
        this.mIsAutomaticReachabilityInBookModeEnabled = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetIsHorizontalReachabilityEnabled() {
        this.mIsHorizontalReachabilityEnabled = this.mContext.getResources().getBoolean(17891739);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetIsVerticalReachabilityEnabled() {
        this.mIsVerticalReachabilityEnabled = this.mContext.getResources().getBoolean(17891742);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetEnabledAutomaticReachabilityInBookMode() {
        this.mIsAutomaticReachabilityInBookModeEnabled = this.mContext.getResources().getBoolean(17891734);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDefaultPositionForHorizontalReachability() {
        return this.mDefaultPositionForHorizontalReachability;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDefaultPositionForVerticalReachability() {
        return this.mDefaultPositionForVerticalReachability;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDefaultPositionForHorizontalReachability(int i) {
        this.mDefaultPositionForHorizontalReachability = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDefaultPositionForVerticalReachability(int i) {
        this.mDefaultPositionForVerticalReachability = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetDefaultPositionForHorizontalReachability() {
        this.mDefaultPositionForHorizontalReachability = readLetterboxHorizontalReachabilityPositionFromConfig(this.mContext, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetDefaultPositionForVerticalReachability() {
        this.mDefaultPositionForVerticalReachability = readLetterboxVerticalReachabilityPositionFromConfig(this.mContext, false);
    }

    private static int readLetterboxHorizontalReachabilityPositionFromConfig(Context context, boolean z) {
        int integer = context.getResources().getInteger(z ? R.integer.config_notificationServiceArchiveSize : R.integer.config_notificationStripRemoteViewSizeBytes);
        if (integer == 0 || integer == 1 || integer == 2) {
            return integer;
        }
        return 1;
    }

    private static int readLetterboxVerticalReachabilityPositionFromConfig(Context context, boolean z) {
        int integer = context.getResources().getInteger(z ? R.integer.config_notificationWarnRemoteViewSizeBytes : R.integer.config_notificationsBatteryFullARGB);
        if (integer == 0 || integer == 1 || integer == 2) {
            return integer;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getHorizontalMultiplierForReachability(boolean z) {
        int letterboxPositionForHorizontalReachability = this.mLetterboxConfigurationPersister.getLetterboxPositionForHorizontalReachability(z);
        if (letterboxPositionForHorizontalReachability == 0) {
            return 0.0f;
        }
        if (letterboxPositionForHorizontalReachability == 1) {
            return 0.5f;
        }
        if (letterboxPositionForHorizontalReachability == 2) {
            return 1.0f;
        }
        throw new AssertionError("Unexpected letterbox position type: " + letterboxPositionForHorizontalReachability);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getVerticalMultiplierForReachability(boolean z) {
        int letterboxPositionForVerticalReachability = this.mLetterboxConfigurationPersister.getLetterboxPositionForVerticalReachability(z);
        if (letterboxPositionForVerticalReachability == 0) {
            return 0.0f;
        }
        if (letterboxPositionForVerticalReachability == 1) {
            return 0.5f;
        }
        if (letterboxPositionForVerticalReachability == 2) {
            return 1.0f;
        }
        throw new AssertionError("Unexpected letterbox position type: " + letterboxPositionForVerticalReachability);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLetterboxPositionForHorizontalReachability(boolean z) {
        return this.mLetterboxConfigurationPersister.getLetterboxPositionForHorizontalReachability(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLetterboxPositionForVerticalReachability(boolean z) {
        return this.mLetterboxConfigurationPersister.getLetterboxPositionForVerticalReachability(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String letterboxHorizontalReachabilityPositionToString(int i) {
        if (i == 0) {
            return "LETTERBOX_HORIZONTAL_REACHABILITY_POSITION_LEFT";
        }
        if (i == 1) {
            return "LETTERBOX_HORIZONTAL_REACHABILITY_POSITION_CENTER";
        }
        if (i == 2) {
            return "LETTERBOX_HORIZONTAL_REACHABILITY_POSITION_RIGHT";
        }
        throw new AssertionError("Unexpected letterbox position type: " + i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String letterboxVerticalReachabilityPositionToString(int i) {
        if (i == 0) {
            return "LETTERBOX_VERTICAL_REACHABILITY_POSITION_TOP";
        }
        if (i == 1) {
            return "LETTERBOX_VERTICAL_REACHABILITY_POSITION_CENTER";
        }
        if (i == 2) {
            return "LETTERBOX_VERTICAL_REACHABILITY_POSITION_BOTTOM";
        }
        throw new AssertionError("Unexpected letterbox position type: " + i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void movePositionForHorizontalReachabilityToNextRightStop(final boolean z) {
        updatePositionForHorizontalReachability(z, new Function() { // from class: com.android.server.wm.LetterboxConfiguration$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Integer lambda$movePositionForHorizontalReachabilityToNextRightStop$4;
                lambda$movePositionForHorizontalReachabilityToNextRightStop$4 = LetterboxConfiguration.lambda$movePositionForHorizontalReachabilityToNextRightStop$4(z, (Integer) obj);
                return lambda$movePositionForHorizontalReachabilityToNextRightStop$4;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$movePositionForHorizontalReachabilityToNextRightStop$4(boolean z, Integer num) {
        return Integer.valueOf(Math.min(num.intValue() + (z ? 2 : 1), 2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void movePositionForHorizontalReachabilityToNextLeftStop(final boolean z) {
        updatePositionForHorizontalReachability(z, new Function() { // from class: com.android.server.wm.LetterboxConfiguration$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Integer lambda$movePositionForHorizontalReachabilityToNextLeftStop$5;
                lambda$movePositionForHorizontalReachabilityToNextLeftStop$5 = LetterboxConfiguration.lambda$movePositionForHorizontalReachabilityToNextLeftStop$5(z, (Integer) obj);
                return lambda$movePositionForHorizontalReachabilityToNextLeftStop$5;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$movePositionForHorizontalReachabilityToNextLeftStop$5(boolean z, Integer num) {
        return Integer.valueOf(Math.max(num.intValue() - (z ? 2 : 1), 0));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void movePositionForVerticalReachabilityToNextBottomStop(final boolean z) {
        updatePositionForVerticalReachability(z, new Function() { // from class: com.android.server.wm.LetterboxConfiguration$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Integer lambda$movePositionForVerticalReachabilityToNextBottomStop$6;
                lambda$movePositionForVerticalReachabilityToNextBottomStop$6 = LetterboxConfiguration.lambda$movePositionForVerticalReachabilityToNextBottomStop$6(z, (Integer) obj);
                return lambda$movePositionForVerticalReachabilityToNextBottomStop$6;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$movePositionForVerticalReachabilityToNextBottomStop$6(boolean z, Integer num) {
        return Integer.valueOf(Math.min(num.intValue() + (z ? 2 : 1), 2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void movePositionForVerticalReachabilityToNextTopStop(final boolean z) {
        updatePositionForVerticalReachability(z, new Function() { // from class: com.android.server.wm.LetterboxConfiguration$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Integer lambda$movePositionForVerticalReachabilityToNextTopStop$7;
                lambda$movePositionForVerticalReachabilityToNextTopStop$7 = LetterboxConfiguration.lambda$movePositionForVerticalReachabilityToNextTopStop$7(z, (Integer) obj);
                return lambda$movePositionForVerticalReachabilityToNextTopStop$7;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$movePositionForVerticalReachabilityToNextTopStop$7(boolean z, Integer num) {
        return Integer.valueOf(Math.max(num.intValue() - (z ? 2 : 1), 0));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getIsEducationEnabled() {
        return this.mIsEducationEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setIsEducationEnabled(boolean z) {
        this.mIsEducationEnabled = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetIsEducationEnabled() {
        this.mIsEducationEnabled = this.mContext.getResources().getBoolean(17891737);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getIsSplitScreenAspectRatioForUnresizableAppsEnabled() {
        return this.mIsSplitScreenAspectRatioForUnresizableAppsEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getIsDisplayAspectRatioEnabledForFixedOrientationLetterbox() {
        return this.mIsDisplayAspectRatioEnabledForFixedOrientationLetterbox;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setIsSplitScreenAspectRatioForUnresizableAppsEnabled(boolean z) {
        this.mIsSplitScreenAspectRatioForUnresizableAppsEnabled = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setIsDisplayAspectRatioEnabledForFixedOrientationLetterbox(boolean z) {
        this.mIsDisplayAspectRatioEnabledForFixedOrientationLetterbox = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetIsSplitScreenAspectRatioForUnresizableAppsEnabled() {
        this.mIsSplitScreenAspectRatioForUnresizableAppsEnabled = this.mContext.getResources().getBoolean(17891741);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetIsDisplayAspectRatioEnabledForFixedOrientationLetterbox() {
        this.mIsDisplayAspectRatioEnabledForFixedOrientationLetterbox = this.mContext.getResources().getBoolean(17891735);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTranslucentLetterboxingEnabled() {
        return this.mTranslucentLetterboxingOverrideEnabled || (this.mTranslucentLetterboxingEnabled && this.mDeviceConfig.getFlag("enable_letterbox_translucent_activity"));
    }

    void setTranslucentLetterboxingEnabled(boolean z) {
        this.mTranslucentLetterboxingEnabled = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTranslucentLetterboxingOverrideEnabled(boolean z) {
        this.mTranslucentLetterboxingOverrideEnabled = z;
        setTranslucentLetterboxingEnabled(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetTranslucentLetterboxingEnabled() {
        setTranslucentLetterboxingEnabled(this.mContext.getResources().getBoolean(17891738));
        setTranslucentLetterboxingOverrideEnabled(false);
    }

    private void updatePositionForHorizontalReachability(boolean z, Function<Integer, Integer> function) {
        this.mLetterboxConfigurationPersister.setLetterboxPositionForHorizontalReachability(z, function.apply(Integer.valueOf(this.mLetterboxConfigurationPersister.getLetterboxPositionForHorizontalReachability(z))).intValue());
    }

    private void updatePositionForVerticalReachability(boolean z, Function<Integer, Integer> function) {
        this.mLetterboxConfigurationPersister.setLetterboxPositionForVerticalReachability(z, function.apply(Integer.valueOf(this.mLetterboxConfigurationPersister.getLetterboxPositionForVerticalReachability(z))).intValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCompatFakeFocusEnabled() {
        return this.mIsCompatFakeFocusEnabled && this.mDeviceConfig.getFlag("enable_compat_fake_focus");
    }

    @VisibleForTesting
    void setIsCompatFakeFocusEnabled(boolean z) {
        this.mIsCompatFakeFocusEnabled = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPolicyForIgnoringRequestedOrientationEnabled() {
        return this.mIsPolicyForIgnoringRequestedOrientationEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCameraCompatSplitScreenAspectRatioEnabled() {
        return this.mIsCameraCompatSplitScreenAspectRatioEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCameraCompatTreatmentEnabled(boolean z) {
        return this.mIsCameraCompatTreatmentEnabled && (!z || this.mDeviceConfig.getFlag("enable_compat_camera_treatment"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCameraCompatRefreshEnabled() {
        return this.mIsCameraCompatTreatmentRefreshEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCameraCompatRefreshEnabled(boolean z) {
        this.mIsCameraCompatTreatmentRefreshEnabled = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetCameraCompatRefreshEnabled() {
        this.mIsCameraCompatTreatmentRefreshEnabled = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCameraCompatRefreshCycleThroughStopEnabled() {
        return this.mIsCameraCompatRefreshCycleThroughStopEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCameraCompatRefreshCycleThroughStopEnabled(boolean z) {
        this.mIsCameraCompatRefreshCycleThroughStopEnabled = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetCameraCompatRefreshCycleThroughStopEnabled() {
        this.mIsCameraCompatRefreshCycleThroughStopEnabled = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDisplayRotationImmersiveAppCompatPolicyEnabled(boolean z) {
        return this.mIsDisplayRotationImmersiveAppCompatPolicyEnabled && (!z || this.mDeviceConfig.getFlag("enable_display_rotation_immersive_app_compat_policy"));
    }
}
