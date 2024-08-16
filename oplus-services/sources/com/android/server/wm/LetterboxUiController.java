package com.android.server.wm;

import android.R;
import android.app.ActivityManager;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Slog;
import android.view.InsetsSource;
import android.view.InsetsState;
import android.view.RoundedCorner;
import android.view.SurfaceControl;
import android.view.WindowInsets;
import android.view.WindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.DockedDividerUtils;
import com.android.internal.statusbar.LetterboxDetails;
import com.android.server.wm.ActivityRecord;
import com.android.server.wm.DeviceStateController;
import com.android.server.wm.WindowContainer;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class LetterboxUiController {
    private static final Predicate<ActivityRecord> FIRST_OPAQUE_NOT_FINISHING_ACTIVITY_PREDICATE = new Predicate() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda3
        @Override // java.util.function.Predicate
        public final boolean test(Object obj) {
            boolean lambda$static$0;
            lambda$static$0 = LetterboxUiController.lambda$static$0((ActivityRecord) obj);
            return lambda$static$0;
        }
    };

    @VisibleForTesting
    static final int MIN_COUNT_TO_IGNORE_REQUEST_IN_LOOP = 2;

    @VisibleForTesting
    static final int SET_ORIENTATION_REQUEST_COUNTER_TIMEOUT_MS = 1000;
    private static final String TAG = "ActivityTaskManager";
    private static final float UNDEFINED_ASPECT_RATIO = 0.0f;
    private final ActivityRecord mActivityRecord;
    private final Boolean mBooleanPropertyAllowDisplayOrientationOverride;
    private final Boolean mBooleanPropertyAllowForceResizeOverride;
    private final Boolean mBooleanPropertyAllowIgnoringOrientationRequestWhenLoopDetected;
    private final Boolean mBooleanPropertyAllowMinAspectRatioOverride;
    private final Boolean mBooleanPropertyAllowOrientationOverride;
    private final Boolean mBooleanPropertyCameraCompatAllowForceRotation;
    private final Boolean mBooleanPropertyCameraCompatAllowRefresh;
    private final Boolean mBooleanPropertyCameraCompatEnableRefreshViaPause;
    private final Boolean mBooleanPropertyFakeFocus;
    private final Boolean mBooleanPropertyIgnoreRequestedOrientation;
    private boolean mDoubleTapEvent;

    @VisibleForTesting
    ActivityRecord mFirstOpaqueActivityBeneath;
    private ActivityRecord.CompatDisplayInsets mInheritedCompatDisplayInsets;
    private final boolean mIsOverrideAnyOrientationEnabled;
    private final boolean mIsOverrideCameraCompatDisableForceRotationEnabled;
    private final boolean mIsOverrideCameraCompatDisableRefreshEnabled;
    private final boolean mIsOverrideCameraCompatEnableRefreshViaPauseEnabled;
    private final boolean mIsOverrideEnableCompatFakeFocusEnabled;
    private final boolean mIsOverrideEnableCompatIgnoreOrientationRequestWhenLoopDetectedEnabled;
    private final boolean mIsOverrideEnableCompatIgnoreRequestedOrientationEnabled;
    private final boolean mIsOverrideForceNonResizeApp;
    private final boolean mIsOverrideForceResizeApp;
    private final boolean mIsOverrideMinAspectRatio;
    private final boolean mIsOverrideOrientationOnlyForCameraEnabled;
    private final boolean mIsOverrideRespectRequestedOrientationEnabled;
    private final boolean mIsOverrideToNosensorOrientationEnabled;
    private final boolean mIsOverrideToPortraitOrientationEnabled;
    private final boolean mIsOverrideToReverseLandscapeOrientationEnabled;
    private final boolean mIsOverrideUseDisplayLandscapeNaturalOrientationEnabled;
    private boolean mIsRefreshAfterRotationRequested;
    private boolean mIsRelaunchingAfterRequestedOrientationChanged;
    private boolean mLastShouldShowLetterboxUi;
    private Letterbox mLetterbox;
    private WindowContainerListener mLetterboxConfigListener;
    private final LetterboxConfiguration mLetterboxConfiguration;
    private boolean mShowWallpaperForLetterboxBackground;
    private final Point mTmpPoint = new Point();
    private final List<LetterboxUiController> mDestroyListeners = new ArrayList();
    private float mInheritedMinAspectRatio = 0.0f;
    private float mInheritedMaxAspectRatio = 0.0f;
    private long mTimeMsLastSetOrientationRequest = 0;

    @Configuration.Orientation
    private int mInheritedOrientation = 0;
    private int mInheritedAppCompatState = 0;
    private int mSetOrientationRequestCounter = 0;
    private final ILetterboxUiControllerExt mExt = (ILetterboxUiControllerExt) ExtLoader.type(ILetterboxUiControllerExt.class).base(this).create();

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$shouldOverrideForceNonResizeApp$6() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$shouldOverrideForceResizeApp$5() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$shouldOverrideMinAspectRatio$4() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$static$0(ActivityRecord activityRecord) {
        return activityRecord.fillsParent() && !activityRecord.isFinishing();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LetterboxUiController(WindowManagerService windowManagerService, ActivityRecord activityRecord) {
        LetterboxConfiguration letterboxConfiguration = windowManagerService.mLetterboxConfiguration;
        this.mLetterboxConfiguration = letterboxConfiguration;
        this.mActivityRecord = activityRecord;
        PackageManager packageManager = windowManagerService.mContext.getPackageManager();
        String str = activityRecord.packageName;
        Objects.requireNonNull(letterboxConfiguration);
        this.mBooleanPropertyIgnoreRequestedOrientation = readComponentProperty(packageManager, str, new LetterboxUiController$$ExternalSyntheticLambda9(letterboxConfiguration), "android.window.PROPERTY_COMPAT_IGNORE_REQUESTED_ORIENTATION");
        String str2 = activityRecord.packageName;
        Objects.requireNonNull(letterboxConfiguration);
        this.mBooleanPropertyAllowIgnoringOrientationRequestWhenLoopDetected = readComponentProperty(packageManager, str2, new LetterboxUiController$$ExternalSyntheticLambda9(letterboxConfiguration), "android.window.PROPERTY_COMPAT_ALLOW_IGNORING_ORIENTATION_REQUEST_WHEN_LOOP_DETECTED");
        String str3 = activityRecord.packageName;
        Objects.requireNonNull(letterboxConfiguration);
        this.mBooleanPropertyFakeFocus = readComponentProperty(packageManager, str3, new LetterboxUiController$$ExternalSyntheticLambda10(letterboxConfiguration), "android.window.PROPERTY_COMPAT_ENABLE_FAKE_FOCUS");
        this.mBooleanPropertyCameraCompatAllowForceRotation = readComponentProperty(packageManager, activityRecord.packageName, new BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda11
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                boolean lambda$new$1;
                lambda$new$1 = LetterboxUiController.this.lambda$new$1();
                return lambda$new$1;
            }
        }, "android.window.PROPERTY_CAMERA_COMPAT_ALLOW_FORCE_ROTATION");
        this.mBooleanPropertyCameraCompatAllowRefresh = readComponentProperty(packageManager, activityRecord.packageName, new BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda12
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                boolean lambda$new$2;
                lambda$new$2 = LetterboxUiController.this.lambda$new$2();
                return lambda$new$2;
            }
        }, "android.window.PROPERTY_CAMERA_COMPAT_ALLOW_REFRESH");
        this.mBooleanPropertyCameraCompatEnableRefreshViaPause = readComponentProperty(packageManager, activityRecord.packageName, new BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda13
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                boolean lambda$new$3;
                lambda$new$3 = LetterboxUiController.this.lambda$new$3();
                return lambda$new$3;
            }
        }, "android.window.PROPERTY_CAMERA_COMPAT_ENABLE_REFRESH_VIA_PAUSE");
        this.mBooleanPropertyAllowOrientationOverride = readComponentProperty(packageManager, activityRecord.packageName, null, "android.window.PROPERTY_COMPAT_ALLOW_ORIENTATION_OVERRIDE");
        this.mBooleanPropertyAllowDisplayOrientationOverride = readComponentProperty(packageManager, activityRecord.packageName, null, "android.window.PROPERTY_COMPAT_ALLOW_DISPLAY_ORIENTATION_OVERRIDE");
        this.mBooleanPropertyAllowMinAspectRatioOverride = readComponentProperty(packageManager, activityRecord.packageName, null, "android.window.PROPERTY_COMPAT_ALLOW_MIN_ASPECT_RATIO_OVERRIDE");
        this.mBooleanPropertyAllowForceResizeOverride = readComponentProperty(packageManager, activityRecord.packageName, null, "android.window.PROPERTY_COMPAT_ALLOW_RESIZEABLE_ACTIVITY_OVERRIDES");
        this.mIsOverrideAnyOrientationEnabled = isCompatChangeEnabled(265464455L);
        this.mIsOverrideToPortraitOrientationEnabled = isCompatChangeEnabled(265452344L);
        this.mIsOverrideToReverseLandscapeOrientationEnabled = isCompatChangeEnabled(266124927L);
        this.mIsOverrideToNosensorOrientationEnabled = isCompatChangeEnabled(265451093L);
        this.mIsOverrideOrientationOnlyForCameraEnabled = isCompatChangeEnabled(265456536L);
        this.mIsOverrideUseDisplayLandscapeNaturalOrientationEnabled = isCompatChangeEnabled(255940284L);
        this.mIsOverrideRespectRequestedOrientationEnabled = isCompatChangeEnabled(236283604L);
        this.mIsOverrideCameraCompatDisableForceRotationEnabled = isCompatChangeEnabled(263959004L);
        this.mIsOverrideCameraCompatDisableRefreshEnabled = isCompatChangeEnabled(264304459L);
        this.mIsOverrideCameraCompatEnableRefreshViaPauseEnabled = isCompatChangeEnabled(264301586L);
        this.mIsOverrideEnableCompatIgnoreRequestedOrientationEnabled = isCompatChangeEnabled(254631730L);
        this.mIsOverrideEnableCompatIgnoreOrientationRequestWhenLoopDetectedEnabled = isCompatChangeEnabled(273509367L);
        this.mIsOverrideEnableCompatFakeFocusEnabled = isCompatChangeEnabled(263259275L);
        this.mIsOverrideMinAspectRatio = isCompatChangeEnabled(174042980L);
        this.mIsOverrideForceResizeApp = isCompatChangeEnabled(174042936L);
        this.mIsOverrideForceNonResizeApp = isCompatChangeEnabled(181136395L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$1() {
        return this.mLetterboxConfiguration.isCameraCompatTreatmentEnabled(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$2() {
        return this.mLetterboxConfiguration.isCameraCompatTreatmentEnabled(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$3() {
        return this.mLetterboxConfiguration.isCameraCompatTreatmentEnabled(true);
    }

    private static Boolean readComponentProperty(PackageManager packageManager, String str, BooleanSupplier booleanSupplier, String str2) {
        if (booleanSupplier != null && !booleanSupplier.getAsBoolean()) {
            return null;
        }
        try {
            return Boolean.valueOf(packageManager.getProperty(str2, str).getBoolean());
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroy() {
        Letterbox letterbox = this.mLetterbox;
        if (letterbox != null) {
            letterbox.destroy();
            this.mLetterbox = null;
        }
        for (int size = this.mDestroyListeners.size() - 1; size >= 0; size--) {
            this.mDestroyListeners.get(size).updateInheritedLetterbox();
        }
        this.mDestroyListeners.clear();
        WindowContainerListener windowContainerListener = this.mLetterboxConfigListener;
        if (windowContainerListener != null) {
            windowContainerListener.onRemoved();
            this.mLetterboxConfigListener = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onMovedToDisplay(int i) {
        Letterbox letterbox = this.mLetterbox;
        if (letterbox != null) {
            letterbox.onMovedToDisplay(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldIgnoreRequestedOrientation(int i) {
        LetterboxConfiguration letterboxConfiguration = this.mLetterboxConfiguration;
        Objects.requireNonNull(letterboxConfiguration);
        if (shouldEnableWithOverrideAndProperty(new LetterboxUiController$$ExternalSyntheticLambda9(letterboxConfiguration), this.mIsOverrideEnableCompatIgnoreRequestedOrientationEnabled, this.mBooleanPropertyIgnoreRequestedOrientation)) {
            if (this.mIsRelaunchingAfterRequestedOrientationChanged) {
                Slog.w(TAG, "Ignoring orientation update to " + ActivityInfo.screenOrientationToString(i) + " due to relaunching after setRequestedOrientation for " + this.mActivityRecord);
                return true;
            }
            if (isCameraCompatTreatmentActive()) {
                Slog.w(TAG, "Ignoring orientation update to " + ActivityInfo.screenOrientationToString(i) + " due to camera compat treatment for " + this.mActivityRecord);
                return true;
            }
        }
        if (!shouldIgnoreOrientationRequestLoop()) {
            return false;
        }
        Slog.w(TAG, "Ignoring orientation update to " + ActivityInfo.screenOrientationToString(i) + " as orientation request loop was detected for " + this.mActivityRecord);
        return true;
    }

    @VisibleForTesting
    boolean shouldIgnoreOrientationRequestLoop() {
        LetterboxConfiguration letterboxConfiguration = this.mLetterboxConfiguration;
        Objects.requireNonNull(letterboxConfiguration);
        if (!shouldEnableWithOptInOverrideAndOptOutProperty(new LetterboxUiController$$ExternalSyntheticLambda9(letterboxConfiguration), this.mIsOverrideEnableCompatIgnoreOrientationRequestWhenLoopDetectedEnabled, this.mBooleanPropertyAllowIgnoringOrientationRequestWhenLoopDetected)) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.mTimeMsLastSetOrientationRequest < 1000) {
            this.mSetOrientationRequestCounter++;
        } else {
            this.mSetOrientationRequestCounter = 0;
        }
        this.mTimeMsLastSetOrientationRequest = currentTimeMillis;
        return this.mSetOrientationRequestCounter >= 2 && !this.mActivityRecord.isLetterboxedForFixedOrientationAndAspectRatio();
    }

    @VisibleForTesting
    int getSetOrientationRequestCounter() {
        return this.mSetOrientationRequestCounter;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldSendFakeFocus() {
        LetterboxConfiguration letterboxConfiguration = this.mLetterboxConfiguration;
        Objects.requireNonNull(letterboxConfiguration);
        return shouldEnableWithOverrideAndProperty(new LetterboxUiController$$ExternalSyntheticLambda10(letterboxConfiguration), this.mIsOverrideEnableCompatFakeFocusEnabled, this.mBooleanPropertyFakeFocus);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldOverrideMinAspectRatio() {
        return shouldEnableWithOptInOverrideAndOptOutProperty(new BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda7
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                boolean lambda$shouldOverrideMinAspectRatio$4;
                lambda$shouldOverrideMinAspectRatio$4 = LetterboxUiController.lambda$shouldOverrideMinAspectRatio$4();
                return lambda$shouldOverrideMinAspectRatio$4;
            }
        }, this.mIsOverrideMinAspectRatio, this.mBooleanPropertyAllowMinAspectRatioOverride);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldOverrideForceResizeApp() {
        return shouldEnableWithOptInOverrideAndOptOutProperty(new BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda1
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                boolean lambda$shouldOverrideForceResizeApp$5;
                lambda$shouldOverrideForceResizeApp$5 = LetterboxUiController.lambda$shouldOverrideForceResizeApp$5();
                return lambda$shouldOverrideForceResizeApp$5;
            }
        }, this.mIsOverrideForceResizeApp, this.mBooleanPropertyAllowForceResizeOverride);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldOverrideForceNonResizeApp() {
        return shouldEnableWithOptInOverrideAndOptOutProperty(new BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda0
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                boolean lambda$shouldOverrideForceNonResizeApp$6;
                lambda$shouldOverrideForceNonResizeApp$6 = LetterboxUiController.lambda$shouldOverrideForceNonResizeApp$6();
                return lambda$shouldOverrideForceNonResizeApp$6;
            }
        }, this.mIsOverrideForceNonResizeApp, this.mBooleanPropertyAllowForceResizeOverride);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRelaunchingAfterRequestedOrientationChanged(boolean z) {
        this.mIsRelaunchingAfterRequestedOrientationChanged = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRefreshAfterRotationRequested() {
        return this.mIsRefreshAfterRotationRequested;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setIsRefreshAfterRotationRequested(boolean z) {
        this.mIsRefreshAfterRotationRequested = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isOverrideRespectRequestedOrientationEnabled() {
        return this.mIsOverrideRespectRequestedOrientationEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldUseDisplayLandscapeNaturalOrientation() {
        return shouldEnableWithOptInOverrideAndOptOutProperty(new BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda8
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                boolean lambda$shouldUseDisplayLandscapeNaturalOrientation$7;
                lambda$shouldUseDisplayLandscapeNaturalOrientation$7 = LetterboxUiController.this.lambda$shouldUseDisplayLandscapeNaturalOrientation$7();
                return lambda$shouldUseDisplayLandscapeNaturalOrientation$7;
            }
        }, this.mIsOverrideUseDisplayLandscapeNaturalOrientationEnabled, this.mBooleanPropertyAllowDisplayOrientationOverride);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$shouldUseDisplayLandscapeNaturalOrientation$7() {
        ActivityRecord activityRecord = this.mActivityRecord;
        return (activityRecord.mDisplayContent == null || activityRecord.getTask() == null || !this.mActivityRecord.mDisplayContent.getIgnoreOrientationRequest() || this.mActivityRecord.getTask().inMultiWindowMode() || this.mActivityRecord.mDisplayContent.getNaturalOrientation() != 2) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int overrideOrientationIfNeeded(int i) {
        DisplayRotationCompatPolicy displayRotationCompatPolicy;
        int mapOrientationRequest = this.mActivityRecord.mWmService.mapOrientationRequest(i);
        if (Boolean.FALSE.equals(this.mBooleanPropertyAllowOrientationOverride)) {
            return mapOrientationRequest;
        }
        ActivityRecord activityRecord = this.mActivityRecord;
        DisplayContent displayContent = activityRecord.mDisplayContent;
        if (this.mIsOverrideOrientationOnlyForCameraEnabled && displayContent != null && ((displayRotationCompatPolicy = displayContent.mDisplayRotationCompatPolicy) == null || !displayRotationCompatPolicy.isActivityEligibleForOrientationOverride(activityRecord))) {
            return mapOrientationRequest;
        }
        if (this.mIsOverrideToReverseLandscapeOrientationEnabled && (ActivityInfo.isFixedOrientationLandscape(mapOrientationRequest) || this.mIsOverrideAnyOrientationEnabled)) {
            Slog.w(TAG, "Requested orientation  " + ActivityInfo.screenOrientationToString(mapOrientationRequest) + " for " + this.mActivityRecord + " is overridden to " + ActivityInfo.screenOrientationToString(8));
            return 8;
        }
        if (!this.mIsOverrideAnyOrientationEnabled && ActivityInfo.isFixedOrientation(mapOrientationRequest)) {
            return mapOrientationRequest;
        }
        if (this.mIsOverrideToPortraitOrientationEnabled) {
            Slog.w(TAG, "Requested orientation  " + ActivityInfo.screenOrientationToString(mapOrientationRequest) + " for " + this.mActivityRecord + " is overridden to " + ActivityInfo.screenOrientationToString(1));
            return 1;
        }
        if (!this.mIsOverrideToNosensorOrientationEnabled) {
            return mapOrientationRequest;
        }
        Slog.w(TAG, "Requested orientation  " + ActivityInfo.screenOrientationToString(mapOrientationRequest) + " for " + this.mActivityRecord + " is overridden to " + ActivityInfo.screenOrientationToString(5));
        return 5;
    }

    boolean isOverrideOrientationOnlyForCameraEnabled() {
        return this.mIsOverrideOrientationOnlyForCameraEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldRefreshActivityForCameraCompat() {
        return shouldEnableWithOptOutOverrideAndProperty(new BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda5
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                boolean lambda$shouldRefreshActivityForCameraCompat$8;
                lambda$shouldRefreshActivityForCameraCompat$8 = LetterboxUiController.this.lambda$shouldRefreshActivityForCameraCompat$8();
                return lambda$shouldRefreshActivityForCameraCompat$8;
            }
        }, this.mIsOverrideCameraCompatDisableRefreshEnabled, this.mBooleanPropertyCameraCompatAllowRefresh);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$shouldRefreshActivityForCameraCompat$8() {
        return this.mLetterboxConfiguration.isCameraCompatTreatmentEnabled(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldRefreshActivityViaPauseForCameraCompat() {
        return shouldEnableWithOverrideAndProperty(new BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda2
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                boolean lambda$shouldRefreshActivityViaPauseForCameraCompat$9;
                lambda$shouldRefreshActivityViaPauseForCameraCompat$9 = LetterboxUiController.this.lambda$shouldRefreshActivityViaPauseForCameraCompat$9();
                return lambda$shouldRefreshActivityViaPauseForCameraCompat$9;
            }
        }, this.mIsOverrideCameraCompatEnableRefreshViaPauseEnabled, this.mBooleanPropertyCameraCompatEnableRefreshViaPause);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$shouldRefreshActivityViaPauseForCameraCompat$9() {
        return this.mLetterboxConfiguration.isCameraCompatTreatmentEnabled(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldForceRotateForCameraCompat() {
        return shouldEnableWithOptOutOverrideAndProperty(new BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda23
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                boolean lambda$shouldForceRotateForCameraCompat$10;
                lambda$shouldForceRotateForCameraCompat$10 = LetterboxUiController.this.lambda$shouldForceRotateForCameraCompat$10();
                return lambda$shouldForceRotateForCameraCompat$10;
            }
        }, this.mIsOverrideCameraCompatDisableForceRotationEnabled, this.mBooleanPropertyCameraCompatAllowForceRotation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$shouldForceRotateForCameraCompat$10() {
        return this.mLetterboxConfiguration.isCameraCompatTreatmentEnabled(true);
    }

    private boolean isCameraCompatTreatmentActive() {
        DisplayRotationCompatPolicy displayRotationCompatPolicy;
        ActivityRecord activityRecord = this.mActivityRecord;
        DisplayContent displayContent = activityRecord.mDisplayContent;
        return (displayContent == null || (displayRotationCompatPolicy = displayContent.mDisplayRotationCompatPolicy) == null || !displayRotationCompatPolicy.isTreatmentEnabledForActivity(activityRecord)) ? false : true;
    }

    private boolean isCompatChangeEnabled(long j) {
        return this.mActivityRecord.info.isChangeEnabled(j);
    }

    private boolean shouldEnableWithOptOutOverrideAndProperty(BooleanSupplier booleanSupplier, boolean z, Boolean bool) {
        return (!booleanSupplier.getAsBoolean() || Boolean.FALSE.equals(bool) || z) ? false : true;
    }

    private boolean shouldEnableWithOptInOverrideAndOptOutProperty(BooleanSupplier booleanSupplier, boolean z, Boolean bool) {
        return booleanSupplier.getAsBoolean() && !Boolean.FALSE.equals(bool) && z;
    }

    private boolean shouldEnableWithOverrideAndProperty(BooleanSupplier booleanSupplier, boolean z, Boolean bool) {
        if (booleanSupplier.getAsBoolean() && !Boolean.FALSE.equals(bool)) {
            return Boolean.TRUE.equals(bool) || z;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasWallpaperBackgroundForLetterbox() {
        return this.mShowWallpaperForLetterboxBackground;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getLetterboxInsets() {
        Letterbox letterbox = this.mLetterbox;
        if (letterbox != null) {
            return letterbox.getInsets();
        }
        return new Rect();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getLetterboxInnerBounds(Rect rect) {
        Letterbox letterbox = this.mLetterbox;
        if (letterbox != null) {
            rect.set(letterbox.getInnerFrame());
            WindowState findMainWindow = this.mActivityRecord.findMainWindow();
            if (findMainWindow != null) {
                adjustBoundsForTaskbar(findMainWindow, rect);
                return;
            }
            return;
        }
        rect.setEmpty();
    }

    private void getLetterboxOuterBounds(Rect rect) {
        Letterbox letterbox = this.mLetterbox;
        if (letterbox != null) {
            rect.set(letterbox.getOuterFrame());
        } else {
            rect.setEmpty();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFullyTransparentBarAllowed(Rect rect) {
        Letterbox letterbox = this.mLetterbox;
        return letterbox == null || letterbox.notIntersectsOrFullyContains(rect);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateLetterboxSurface(WindowState windowState) {
        updateLetterboxSurface(windowState, this.mActivityRecord.getSyncTransaction());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateLetterboxSurface(WindowState windowState, SurfaceControl.Transaction transaction) {
        WindowState findMainWindow = this.mActivityRecord.findMainWindow();
        if (findMainWindow == windowState || windowState == null || findMainWindow == null) {
            layoutLetterbox(windowState);
            Letterbox letterbox = this.mLetterbox;
            if (letterbox == null || !letterbox.needsApplySurfaceChanges()) {
                return;
            }
            this.mLetterbox.applySurfaceChanges(transaction);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void layoutLetterbox(WindowState windowState) {
        WindowState findMainWindow = this.mActivityRecord.findMainWindow();
        if (findMainWindow != null) {
            if (windowState == null || findMainWindow == windowState) {
                updateRoundedCornersIfNeeded(findMainWindow);
                WindowState findMainWindow2 = this.mActivityRecord.findMainWindow(false);
                if (findMainWindow2 != null && findMainWindow2 != findMainWindow) {
                    updateRoundedCornersIfNeeded(findMainWindow2);
                }
                updateWallpaperForLetterbox(findMainWindow);
                if (shouldShowLetterboxUi(findMainWindow)) {
                    if (this.mLetterbox == null) {
                        Letterbox letterbox = new Letterbox(new Supplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda14
                            @Override // java.util.function.Supplier
                            public final Object get() {
                                SurfaceControl.Builder lambda$layoutLetterbox$11;
                                lambda$layoutLetterbox$11 = LetterboxUiController.this.lambda$layoutLetterbox$11();
                                return lambda$layoutLetterbox$11;
                            }
                        }, this.mActivityRecord.mWmService.mTransactionFactory, new Supplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda15
                            @Override // java.util.function.Supplier
                            public final Object get() {
                                boolean shouldLetterboxHaveRoundedCorners;
                                shouldLetterboxHaveRoundedCorners = LetterboxUiController.this.shouldLetterboxHaveRoundedCorners();
                                return Boolean.valueOf(shouldLetterboxHaveRoundedCorners);
                            }
                        }, new Supplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda16
                            @Override // java.util.function.Supplier
                            public final Object get() {
                                Color letterboxBackgroundColor;
                                letterboxBackgroundColor = LetterboxUiController.this.getLetterboxBackgroundColor();
                                return letterboxBackgroundColor;
                            }
                        }, new Supplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda17
                            @Override // java.util.function.Supplier
                            public final Object get() {
                                return Boolean.valueOf(LetterboxUiController.this.hasWallpaperBackgroundForLetterbox());
                            }
                        }, new Supplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda18
                            @Override // java.util.function.Supplier
                            public final Object get() {
                                int letterboxWallpaperBlurRadius;
                                letterboxWallpaperBlurRadius = LetterboxUiController.this.getLetterboxWallpaperBlurRadius();
                                return Integer.valueOf(letterboxWallpaperBlurRadius);
                            }
                        }, new Supplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda19
                            @Override // java.util.function.Supplier
                            public final Object get() {
                                float letterboxWallpaperDarkScrimAlpha;
                                letterboxWallpaperDarkScrimAlpha = LetterboxUiController.this.getLetterboxWallpaperDarkScrimAlpha();
                                return Float.valueOf(letterboxWallpaperDarkScrimAlpha);
                            }
                        }, new IntConsumer() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda20
                            @Override // java.util.function.IntConsumer
                            public final void accept(int i) {
                                LetterboxUiController.this.handleHorizontalDoubleTap(i);
                            }
                        }, new IntConsumer() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda21
                            @Override // java.util.function.IntConsumer
                            public final void accept(int i) {
                                LetterboxUiController.this.handleVerticalDoubleTap(i);
                            }
                        }, new Supplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda22
                            @Override // java.util.function.Supplier
                            public final Object get() {
                                return LetterboxUiController.this.getLetterboxParentSurface();
                            }
                        });
                        this.mLetterbox = letterbox;
                        letterbox.attachInput(findMainWindow);
                    }
                    if (this.mActivityRecord.isInLetterboxAnimation()) {
                        this.mActivityRecord.getTask().getPosition(this.mTmpPoint);
                    } else {
                        this.mActivityRecord.getPosition(this.mTmpPoint);
                    }
                    Rect fixedRotationTransformDisplayBounds = this.mActivityRecord.getFixedRotationTransformDisplayBounds();
                    if (fixedRotationTransformDisplayBounds == null) {
                        if (this.mActivityRecord.inMultiWindowMode() || (this.mActivityRecord.getRootTask() != null && this.mActivityRecord.getRootTask().getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0]))) {
                            fixedRotationTransformDisplayBounds = this.mActivityRecord.getTaskFragment().getBounds();
                        } else {
                            fixedRotationTransformDisplayBounds = this.mActivityRecord.getRootTask().getParent().getBounds();
                        }
                    }
                    this.mExt.interceptLayoutLetterbox(fixedRotationTransformDisplayBounds, hasInheritedLetterboxBehavior() ? this.mActivityRecord.getBounds() : findMainWindow.getFrame(), this.mTmpPoint, findMainWindow, this.mLetterbox);
                    this.mActivityRecord.getTask().dispatchTaskInfoChangedIfNeeded(true);
                    return;
                }
                Letterbox letterbox2 = this.mLetterbox;
                if (letterbox2 != null) {
                    letterbox2.hide();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ SurfaceControl.Builder lambda$layoutLetterbox$11() {
        return this.mActivityRecord.makeChildSurface(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFromDoubleTap() {
        boolean z = this.mDoubleTapEvent;
        this.mDoubleTapEvent = false;
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceControl getLetterboxParentSurface() {
        if (this.mActivityRecord.isInLetterboxAnimation()) {
            return this.mActivityRecord.getTask().getSurfaceControl();
        }
        return this.mActivityRecord.getSurfaceControl();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldLetterboxHaveRoundedCorners() {
        return this.mLetterboxConfiguration.isLetterboxActivityCornersRounded() && this.mActivityRecord.fillsParent();
    }

    private boolean isDisplayFullScreenAndInPosture(DeviceStateController.DeviceState deviceState, boolean z) {
        Task task = this.mActivityRecord.getTask();
        DisplayContent displayContent = this.mActivityRecord.mDisplayContent;
        return displayContent != null && displayContent.getDisplayRotation().isDeviceInPosture(deviceState, z) && task != null && task.getWindowingMode() == 1;
    }

    private boolean isDisplayFullScreenAndSeparatingHinge() {
        Task task = this.mActivityRecord.getTask();
        DisplayContent displayContent = this.mActivityRecord.mDisplayContent;
        return displayContent != null && displayContent.getDisplayRotation().isDisplaySeparatingHinge() && task != null && task.getWindowingMode() == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getHorizontalPositionMultiplier(Configuration configuration) {
        boolean isFullScreenAndBookModeEnabled = isFullScreenAndBookModeEnabled();
        if (isHorizontalReachabilityEnabled(configuration)) {
            return this.mLetterboxConfiguration.getHorizontalMultiplierForReachability(isFullScreenAndBookModeEnabled);
        }
        return this.mLetterboxConfiguration.getLetterboxHorizontalPositionMultiplier(isFullScreenAndBookModeEnabled);
    }

    private boolean isFullScreenAndBookModeEnabled() {
        return isDisplayFullScreenAndInPosture(DeviceStateController.DeviceState.HALF_FOLDED, false) && this.mLetterboxConfiguration.getIsAutomaticReachabilityInBookModeEnabled();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getVerticalPositionMultiplier(Configuration configuration) {
        boolean isDisplayFullScreenAndInPosture = isDisplayFullScreenAndInPosture(DeviceStateController.DeviceState.HALF_FOLDED, true);
        if (isVerticalReachabilityEnabled(configuration)) {
            return this.mLetterboxConfiguration.getVerticalMultiplierForReachability(isDisplayFullScreenAndInPosture);
        }
        return this.mLetterboxConfiguration.getLetterboxVerticalPositionMultiplier(isDisplayFullScreenAndInPosture);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getFixedOrientationLetterboxAspectRatio(Configuration configuration) {
        if (shouldUseSplitScreenAspectRatio(configuration)) {
            return getSplitScreenAspectRatio();
        }
        if (this.mActivityRecord.shouldCreateCompatDisplayInsets()) {
            return getDefaultMinAspectRatioForUnresizableApps();
        }
        return getDefaultMinAspectRatio();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void recomputeConfigurationForCameraCompatIfNeeded() {
        if (isOverrideOrientationOnlyForCameraEnabled() || isCameraCompatSplitScreenAspectRatioAllowed()) {
            this.mActivityRecord.recomputeConfiguration();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCameraCompatSplitScreenAspectRatioAllowed() {
        return this.mLetterboxConfiguration.isCameraCompatSplitScreenAspectRatioEnabled() && !this.mActivityRecord.shouldCreateCompatDisplayInsets();
    }

    private boolean shouldUseSplitScreenAspectRatio(Configuration configuration) {
        DeviceStateController.DeviceState deviceState = DeviceStateController.DeviceState.HALF_FOLDED;
        return (isDisplayFullScreenAndInPosture(deviceState, false) && ((getHorizontalPositionMultiplier(configuration) > 0.5f ? 1 : (getHorizontalPositionMultiplier(configuration) == 0.5f ? 0 : -1)) != 0)) || isDisplayFullScreenAndInPosture(deviceState, true) || (isCameraCompatSplitScreenAspectRatioAllowed() && isCameraCompatTreatmentActive());
    }

    private float getDefaultMinAspectRatioForUnresizableApps() {
        if (this.mLetterboxConfiguration.getIsSplitScreenAspectRatioForUnresizableAppsEnabled() && this.mActivityRecord.getDisplayContent() != null) {
            return getSplitScreenAspectRatio();
        }
        if (this.mLetterboxConfiguration.getDefaultMinAspectRatioForUnresizableApps() > 1.0f) {
            return this.mLetterboxConfiguration.getDefaultMinAspectRatioForUnresizableApps();
        }
        return getDefaultMinAspectRatio();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getSplitScreenAspectRatio() {
        DisplayContent displayContent = this.mActivityRecord.getDisplayContent();
        if (displayContent == null) {
            return getDefaultMinAspectRatioForUnresizableApps();
        }
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.harmful_app_message_padding_right) - (DockedDividerUtils.getDividerInsets(getResources()) * 2);
        Rect rect = new Rect(displayContent.getWindowConfiguration().getAppBounds());
        if (rect.width() >= rect.height()) {
            rect.inset(dimensionPixelSize / 2, 0);
            rect.right = rect.centerX();
        } else {
            rect.inset(0, dimensionPixelSize / 2);
            rect.bottom = rect.centerY();
        }
        return ActivityRecord.computeAspectRatio(rect);
    }

    private float getDefaultMinAspectRatio() {
        DisplayContent displayContent = this.mActivityRecord.getDisplayContent();
        if (displayContent == null || !this.mLetterboxConfiguration.getIsDisplayAspectRatioEnabledForFixedOrientationLetterbox()) {
            return this.mLetterboxConfiguration.getFixedOrientationLetterboxAspectRatio();
        }
        return ActivityRecord.computeAspectRatio(new Rect(displayContent.getBounds()));
    }

    Resources getResources() {
        return this.mActivityRecord.mWmService.mContext.getResources();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLetterboxPositionForVerticalReachability() {
        return this.mLetterboxConfiguration.getLetterboxPositionForVerticalReachability(isDisplayFullScreenAndSeparatingHinge());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLetterboxPositionForHorizontalReachability() {
        return this.mLetterboxConfiguration.getLetterboxPositionForHorizontalReachability(isFullScreenAndBookModeEnabled());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void handleHorizontalDoubleTap(int i) {
        if (!isHorizontalReachabilityEnabled() || this.mActivityRecord.isInTransition()) {
            return;
        }
        if (this.mLetterbox.getInnerFrame().left > i || this.mLetterbox.getInnerFrame().right < i) {
            boolean z = isDisplayFullScreenAndSeparatingHinge() && this.mLetterboxConfiguration.getIsAutomaticReachabilityInBookModeEnabled();
            int letterboxPositionForHorizontalReachability = this.mLetterboxConfiguration.getLetterboxPositionForHorizontalReachability(z);
            if (this.mLetterbox.getInnerFrame().left > i) {
                this.mLetterboxConfiguration.movePositionForHorizontalReachabilityToNextLeftStop(z);
                logLetterboxPositionChange(letterboxPositionForHorizontalReachability == 1 ? 1 : 4);
            } else if (this.mLetterbox.getInnerFrame().right < i) {
                this.mLetterboxConfiguration.movePositionForHorizontalReachabilityToNextRightStop(z);
                logLetterboxPositionChange(letterboxPositionForHorizontalReachability == 1 ? 3 : 2);
            }
            this.mDoubleTapEvent = true;
            this.mActivityRecord.recomputeConfiguration();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void handleVerticalDoubleTap(int i) {
        if (!isVerticalReachabilityEnabled() || this.mActivityRecord.isInTransition()) {
            return;
        }
        if (this.mLetterbox.getInnerFrame().top > i || this.mLetterbox.getInnerFrame().bottom < i) {
            boolean isDisplayFullScreenAndSeparatingHinge = isDisplayFullScreenAndSeparatingHinge();
            int letterboxPositionForVerticalReachability = this.mLetterboxConfiguration.getLetterboxPositionForVerticalReachability(isDisplayFullScreenAndSeparatingHinge);
            if (this.mLetterbox.getInnerFrame().top > i) {
                this.mLetterboxConfiguration.movePositionForVerticalReachabilityToNextTopStop(isDisplayFullScreenAndSeparatingHinge);
                logLetterboxPositionChange(letterboxPositionForVerticalReachability == 1 ? 5 : 8);
            } else if (this.mLetterbox.getInnerFrame().bottom < i) {
                this.mLetterboxConfiguration.movePositionForVerticalReachabilityToNextBottomStop(isDisplayFullScreenAndSeparatingHinge);
                logLetterboxPositionChange(letterboxPositionForVerticalReachability == 1 ? 7 : 6);
            }
            this.mDoubleTapEvent = true;
            this.mActivityRecord.recomputeConfiguration();
        }
    }

    private boolean isHorizontalReachabilityEnabled(Configuration configuration) {
        return this.mLetterboxConfiguration.getIsHorizontalReachabilityEnabled() && configuration.windowConfiguration.getWindowingMode() == 1 && configuration.orientation == 2 && this.mActivityRecord.getOrientationForReachability() == 1 && configuration.windowConfiguration.getAppBounds().height() <= this.mActivityRecord.getScreenResolvedBounds().height();
    }

    @VisibleForTesting
    boolean isHorizontalReachabilityEnabled() {
        return isHorizontalReachabilityEnabled(this.mActivityRecord.getParent().getConfiguration());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLetterboxDoubleTapEducationEnabled() {
        return isHorizontalReachabilityEnabled() || isVerticalReachabilityEnabled();
    }

    private boolean isVerticalReachabilityEnabled(Configuration configuration) {
        return this.mLetterboxConfiguration.getIsVerticalReachabilityEnabled() && configuration.windowConfiguration.getWindowingMode() == 1 && configuration.orientation == 1 && this.mActivityRecord.getOrientationForReachability() == 2 && configuration.windowConfiguration.getBounds().width() == this.mActivityRecord.getScreenResolvedBounds().width();
    }

    @VisibleForTesting
    boolean isVerticalReachabilityEnabled() {
        return isVerticalReachabilityEnabled(this.mActivityRecord.getParent().getConfiguration());
    }

    @VisibleForTesting
    boolean shouldShowLetterboxUi(WindowState windowState) {
        boolean z = false;
        if (!windowState.getWrapper().getExtImpl().letterBoxEnabledForCompactWin(windowState)) {
            return false;
        }
        if (windowState.getWrapper().getExtImpl().needLetterBoxSurface(isSurfaceVisible(windowState), this.mActivityRecord, windowState)) {
            return true;
        }
        if (this.mIsRelaunchingAfterRequestedOrientationChanged || !isSurfaceReadyToShow(windowState)) {
            return this.mLastShouldShowLetterboxUi;
        }
        if ((this.mActivityRecord.isInLetterboxAnimation() || isSurfaceVisible(windowState) || windowState.getWrapper().getExtImpl().shouldShowLetterboxUi(windowState)) && windowState.areAppWindowBoundsLetterboxed() && (windowState.getAttrs().flags & 1048576) == 0) {
            z = true;
        }
        this.mLastShouldShowLetterboxUi = z;
        return z;
    }

    @VisibleForTesting
    boolean isSurfaceReadyToShow(WindowState windowState) {
        return windowState.isDrawn() || windowState.isDragResizeChanged();
    }

    @VisibleForTesting
    boolean isSurfaceVisible(WindowState windowState) {
        return windowState.isOnScreen() && (this.mActivityRecord.isVisible() || this.mActivityRecord.isVisibleRequested());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Color getLetterboxBackgroundColor() {
        WindowState findMainWindow = this.mActivityRecord.findMainWindow();
        if (findMainWindow == null || findMainWindow.isLetterboxedForDisplayCutout() || this.mExt.shouldUseBlackLetterboxBackground(this.mActivityRecord)) {
            return Color.valueOf(-16777216);
        }
        int letterboxBackgroundType = this.mLetterboxConfiguration.getLetterboxBackgroundType();
        ActivityManager.TaskDescription taskDescription = this.mActivityRecord.taskDescription;
        if (letterboxBackgroundType == 0) {
            return this.mLetterboxConfiguration.getLetterboxBackgroundColor();
        }
        if (letterboxBackgroundType != 1) {
            if (letterboxBackgroundType != 2) {
                if (letterboxBackgroundType == 3) {
                    if (hasWallpaperBackgroundForLetterbox()) {
                        return Color.valueOf(-16777216);
                    }
                    Slog.w(TAG, "Wallpaper option is selected for letterbox background but blur is not supported by a device or not supported in the current window configuration or both alpha scrim and blur radius aren't provided so using solid color background");
                } else {
                    throw new AssertionError("Unexpected letterbox background type: " + letterboxBackgroundType);
                }
            } else if (taskDescription != null && taskDescription.getBackgroundColorFloating() != 0) {
                return Color.valueOf(taskDescription.getBackgroundColorFloating());
            }
        } else if (taskDescription != null && taskDescription.getBackgroundColor() != 0) {
            return Color.valueOf(taskDescription.getBackgroundColor());
        }
        return this.mLetterboxConfiguration.getLetterboxBackgroundColor();
    }

    private void updateRoundedCornersIfNeeded(WindowState windowState) {
        SurfaceControl surfaceControl = windowState.getSurfaceControl();
        if (surfaceControl == null || !surfaceControl.isValid()) {
            return;
        }
        this.mActivityRecord.getSyncTransaction().setCrop(surfaceControl, getCropBoundsIfNeeded(windowState)).setCornerRadius(surfaceControl, getRoundedCornersRadius(windowState));
    }

    @VisibleForTesting
    Rect getCropBoundsIfNeeded(WindowState windowState) {
        if (!requiresRoundedCorners(windowState) || this.mActivityRecord.isInLetterboxAnimation()) {
            return null;
        }
        Rect rect = new Rect(this.mActivityRecord.getBounds());
        if (hasInheritedLetterboxBehavior() && (rect.width() != windowState.mRequestedWidth || rect.height() != windowState.mRequestedHeight)) {
            return null;
        }
        adjustBoundsForTaskbar(windowState, rect);
        float f = windowState.mInvGlobalScale;
        if (f != 1.0f && f > 0.0f) {
            rect.scale(f);
        }
        rect.offsetTo(0, 0);
        return rect;
    }

    private boolean requiresRoundedCorners(WindowState windowState) {
        return isLetterboxedNotForDisplayCutout(windowState) && this.mLetterboxConfiguration.isLetterboxActivityCornersRounded();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRoundedCornersRadius(WindowState windowState) {
        int min;
        if (!requiresRoundedCorners(windowState)) {
            return 0;
        }
        if (this.mLetterboxConfiguration.getLetterboxActivityCornersRadius() >= 0) {
            min = this.mLetterboxConfiguration.getLetterboxActivityCornersRadius();
        } else {
            InsetsState insetsState = windowState.getInsetsState();
            min = Math.min(getInsetsStateCornerRadius(insetsState, 3), getInsetsStateCornerRadius(insetsState, 2));
        }
        float f = windowState.mInvGlobalScale;
        return (f == 1.0f || f <= 0.0f) ? min : (int) (f * min);
    }

    @VisibleForTesting
    InsetsSource getExpandedTaskbarOrNull(WindowState windowState) {
        InsetsState insetsState = windowState.getInsetsState();
        for (int sourceSize = insetsState.sourceSize() - 1; sourceSize >= 0; sourceSize--) {
            InsetsSource sourceAt = insetsState.sourceAt(sourceSize);
            if (sourceAt.getType() == WindowInsets.Type.navigationBars() && sourceAt.insetsRoundedCornerFrame() && sourceAt.isVisible()) {
                return sourceAt;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getIsRelaunchingAfterRequestedOrientationChanged() {
        return this.mIsRelaunchingAfterRequestedOrientationChanged;
    }

    private void adjustBoundsForTaskbar(WindowState windowState, Rect rect) {
        InsetsSource expandedTaskbarOrNull = getExpandedTaskbarOrNull(windowState);
        if (expandedTaskbarOrNull != null) {
            rect.bottom = Math.min(rect.bottom, expandedTaskbarOrNull.getFrame().top);
        }
    }

    private int getInsetsStateCornerRadius(InsetsState insetsState, int i) {
        RoundedCorner roundedCorner = insetsState.getRoundedCorners().getRoundedCorner(i);
        if (roundedCorner == null) {
            return 0;
        }
        return roundedCorner.getRadius();
    }

    private boolean isLetterboxedNotForDisplayCutout(WindowState windowState) {
        return shouldShowLetterboxUi(windowState) && !windowState.isLetterboxedForDisplayCutout();
    }

    private void updateWallpaperForLetterbox(WindowState windowState) {
        boolean z = this.mLetterboxConfiguration.getLetterboxBackgroundType() == 3 && isLetterboxedNotForDisplayCutout(windowState) && (getLetterboxWallpaperBlurRadius() > 0 || getLetterboxWallpaperDarkScrimAlpha() > 0.0f) && (getLetterboxWallpaperBlurRadius() <= 0 || isLetterboxWallpaperBlurSupported());
        if (this.mShowWallpaperForLetterboxBackground != z) {
            this.mShowWallpaperForLetterboxBackground = z;
            this.mActivityRecord.requestUpdateWallpaperIfNeeded();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getLetterboxWallpaperBlurRadius() {
        int letterboxBackgroundWallpaperBlurRadius = this.mLetterboxConfiguration.getLetterboxBackgroundWallpaperBlurRadius();
        if (letterboxBackgroundWallpaperBlurRadius < 0) {
            return 0;
        }
        return letterboxBackgroundWallpaperBlurRadius;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getLetterboxWallpaperDarkScrimAlpha() {
        float letterboxBackgroundWallpaperDarkScrimAlpha = this.mLetterboxConfiguration.getLetterboxBackgroundWallpaperDarkScrimAlpha();
        if (letterboxBackgroundWallpaperDarkScrimAlpha < 0.0f || letterboxBackgroundWallpaperDarkScrimAlpha >= 1.0f) {
            return 0.0f;
        }
        return letterboxBackgroundWallpaperDarkScrimAlpha;
    }

    private boolean isLetterboxWallpaperBlurSupported() {
        return ((WindowManager) this.mLetterboxConfiguration.mContext.getSystemService(WindowManager.class)).isCrossWindowBlurEnabled();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        WindowState findMainWindow = this.mActivityRecord.findMainWindow();
        if (findMainWindow == null) {
            return;
        }
        boolean areAppWindowBoundsLetterboxed = findMainWindow.areAppWindowBoundsLetterboxed();
        printWriter.println(str + "areBoundsLetterboxed=" + areAppWindowBoundsLetterboxed);
        if (areAppWindowBoundsLetterboxed) {
            printWriter.println(str + "  letterboxReason=" + getLetterboxReasonString(findMainWindow));
            printWriter.println(str + "  activityAspectRatio=" + ActivityRecord.computeAspectRatio(this.mActivityRecord.getBounds()));
            boolean shouldShowLetterboxUi = shouldShowLetterboxUi(findMainWindow);
            printWriter.println(str + "shouldShowLetterboxUi=" + shouldShowLetterboxUi);
            if (shouldShowLetterboxUi) {
                printWriter.println(str + "  letterboxBackgroundColor=" + Integer.toHexString(getLetterboxBackgroundColor().toArgb()));
                printWriter.println(str + "  letterboxBackgroundType=" + LetterboxConfiguration.letterboxBackgroundTypeToString(this.mLetterboxConfiguration.getLetterboxBackgroundType()));
                printWriter.println(str + "  letterboxCornerRadius=" + getRoundedCornersRadius(findMainWindow));
                if (this.mLetterboxConfiguration.getLetterboxBackgroundType() == 3) {
                    printWriter.println(str + "  isLetterboxWallpaperBlurSupported=" + isLetterboxWallpaperBlurSupported());
                    printWriter.println(str + "  letterboxBackgroundWallpaperDarkScrimAlpha=" + getLetterboxWallpaperDarkScrimAlpha());
                    printWriter.println(str + "  letterboxBackgroundWallpaperBlurRadius=" + getLetterboxWallpaperBlurRadius());
                }
                printWriter.println(str + "  isHorizontalReachabilityEnabled=" + isHorizontalReachabilityEnabled());
                printWriter.println(str + "  isVerticalReachabilityEnabled=" + isVerticalReachabilityEnabled());
                printWriter.println(str + "  letterboxHorizontalPositionMultiplier=" + getHorizontalPositionMultiplier(this.mActivityRecord.getParent().getConfiguration()));
                printWriter.println(str + "  letterboxVerticalPositionMultiplier=" + getVerticalPositionMultiplier(this.mActivityRecord.getParent().getConfiguration()));
                printWriter.println(str + "  letterboxPositionForHorizontalReachability=" + LetterboxConfiguration.letterboxHorizontalReachabilityPositionToString(this.mLetterboxConfiguration.getLetterboxPositionForHorizontalReachability(false)));
                printWriter.println(str + "  letterboxPositionForVerticalReachability=" + LetterboxConfiguration.letterboxVerticalReachabilityPositionToString(this.mLetterboxConfiguration.getLetterboxPositionForVerticalReachability(false)));
                printWriter.println(str + "  fixedOrientationLetterboxAspectRatio=" + this.mLetterboxConfiguration.getFixedOrientationLetterboxAspectRatio());
                printWriter.println(str + "  defaultMinAspectRatioForUnresizableApps=" + this.mLetterboxConfiguration.getDefaultMinAspectRatioForUnresizableApps());
                printWriter.println(str + "  isSplitScreenAspectRatioForUnresizableAppsEnabled=" + this.mLetterboxConfiguration.getIsSplitScreenAspectRatioForUnresizableAppsEnabled());
                printWriter.println(str + "  isDisplayAspectRatioEnabledForFixedOrientationLetterbox=" + this.mLetterboxConfiguration.getIsDisplayAspectRatioEnabledForFixedOrientationLetterbox());
            }
        }
    }

    private String getLetterboxReasonString(WindowState windowState) {
        return this.mActivityRecord.inSizeCompatMode() ? "SIZE_COMPAT_MODE" : this.mActivityRecord.isLetterboxedForFixedOrientationAndAspectRatio() ? "FIXED_ORIENTATION" : windowState.isLetterboxedForDisplayCutout() ? "DISPLAY_CUTOUT" : this.mActivityRecord.isAspectRatioApplied() ? "ASPECT_RATIO" : "UNKNOWN_REASON";
    }

    private int letterboxHorizontalReachabilityPositionToLetterboxPosition(int i) {
        if (i == 0) {
            return 3;
        }
        if (i == 1) {
            return 2;
        }
        if (i == 2) {
            return 4;
        }
        throw new AssertionError("Unexpected letterbox horizontal reachability position type: " + i);
    }

    private int letterboxVerticalReachabilityPositionToLetterboxPosition(int i) {
        if (i == 0) {
            return 5;
        }
        if (i == 1) {
            return 2;
        }
        if (i == 2) {
            return 6;
        }
        throw new AssertionError("Unexpected letterbox vertical reachability position type: " + i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLetterboxPositionForLogging() {
        if (isHorizontalReachabilityEnabled()) {
            return letterboxHorizontalReachabilityPositionToLetterboxPosition(getLetterboxConfiguration().getLetterboxPositionForHorizontalReachability(isDisplayFullScreenAndInPosture(DeviceStateController.DeviceState.HALF_FOLDED, false)));
        }
        if (isVerticalReachabilityEnabled()) {
            return letterboxVerticalReachabilityPositionToLetterboxPosition(getLetterboxConfiguration().getLetterboxPositionForVerticalReachability(isDisplayFullScreenAndInPosture(DeviceStateController.DeviceState.HALF_FOLDED, true)));
        }
        return 0;
    }

    private LetterboxConfiguration getLetterboxConfiguration() {
        return this.mLetterboxConfiguration;
    }

    private void logLetterboxPositionChange(int i) {
        this.mActivityRecord.mTaskSupervisor.getActivityMetricsLogger().logLetterboxPositionChange(this.mActivityRecord, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LetterboxDetails getLetterboxDetails() {
        WindowState findMainWindow = this.mActivityRecord.findMainWindow();
        if (this.mLetterbox != null && findMainWindow != null && !findMainWindow.isLetterboxedForDisplayCutout()) {
            Rect rect = new Rect();
            Rect rect2 = new Rect();
            getLetterboxInnerBounds(rect);
            getLetterboxOuterBounds(rect2);
            if (!rect.isEmpty() && !rect2.isEmpty()) {
                return new LetterboxDetails(rect, rect2, findMainWindow.mAttrs.insetsFlags.appearance);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateInheritedLetterbox() {
        final WindowContainer parent = this.mActivityRecord.getParent();
        if (parent != null && this.mLetterboxConfiguration.isTranslucentLetterboxingEnabled()) {
            WindowContainerListener windowContainerListener = this.mLetterboxConfigListener;
            if (windowContainerListener != null) {
                windowContainerListener.onRemoved();
                clearInheritedConfig();
            }
            ActivityRecord activity = this.mActivityRecord.getTask().getActivity(FIRST_OPAQUE_NOT_FINISHING_ACTIVITY_PREDICATE, this.mActivityRecord, false, true);
            this.mFirstOpaqueActivityBeneath = activity;
            if (activity == null || activity.isEmbedded()) {
                this.mActivityRecord.recomputeConfiguration();
                return;
            }
            if (this.mActivityRecord.getTask() == null || this.mActivityRecord.fillsParent() || this.mActivityRecord.hasCompatDisplayInsetsWithoutInheritance()) {
                return;
            }
            this.mFirstOpaqueActivityBeneath.mLetterboxUiController.mDestroyListeners.add(this);
            inheritConfiguration(this.mFirstOpaqueActivityBeneath);
            this.mLetterboxConfigListener = WindowContainer.overrideConfigurationPropagation(this.mActivityRecord, this.mFirstOpaqueActivityBeneath, new WindowContainer.ConfigurationMerger() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda4
                @Override // com.android.server.wm.WindowContainer.ConfigurationMerger
                public final Configuration merge(Configuration configuration, Configuration configuration2) {
                    Configuration lambda$updateInheritedLetterbox$12;
                    lambda$updateInheritedLetterbox$12 = LetterboxUiController.this.lambda$updateInheritedLetterbox$12(parent, configuration, configuration2);
                    return lambda$updateInheritedLetterbox$12;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Configuration lambda$updateInheritedLetterbox$12(WindowContainer windowContainer, Configuration configuration, Configuration configuration2) {
        resetTranslucentOverrideConfig(configuration2);
        Rect bounds = windowContainer.getWindowConfiguration().getBounds();
        Rect bounds2 = configuration2.windowConfiguration.getBounds();
        Rect bounds3 = configuration.windowConfiguration.getBounds();
        int i = bounds.left;
        bounds2.set(i, bounds.top, bounds3.width() + i, bounds.top + bounds3.height());
        configuration2.windowConfiguration.setAppBounds(new Rect());
        inheritConfiguration(this.mFirstOpaqueActivityBeneath);
        return configuration2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasInheritedLetterboxBehavior() {
        return this.mLetterboxConfigListener != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasInheritedOrientation() {
        return hasInheritedLetterboxBehavior() && this.mActivityRecord.getOverrideOrientation() != -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getInheritedMinAspectRatio() {
        return this.mInheritedMinAspectRatio;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getInheritedMaxAspectRatio() {
        return this.mInheritedMaxAspectRatio;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getInheritedAppCompatState() {
        return this.mInheritedAppCompatState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Configuration.Orientation
    public int getInheritedOrientation() {
        return this.mInheritedOrientation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord.CompatDisplayInsets getInheritedCompatDisplayInsets() {
        return this.mInheritedCompatDisplayInsets;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearInheritedCompatDisplayInsets() {
        this.mInheritedCompatDisplayInsets = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean applyOnOpaqueActivityBelow(final Consumer<ActivityRecord> consumer) {
        return ((Boolean) findOpaqueNotFinishingActivityBelow().map(new Function() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Boolean lambda$applyOnOpaqueActivityBelow$13;
                lambda$applyOnOpaqueActivityBelow$13 = LetterboxUiController.lambda$applyOnOpaqueActivityBelow$13(consumer, (ActivityRecord) obj);
                return lambda$applyOnOpaqueActivityBelow$13;
            }
        }).orElse(Boolean.FALSE)).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Boolean lambda$applyOnOpaqueActivityBelow$13(Consumer consumer, ActivityRecord activityRecord) {
        consumer.accept(activityRecord);
        return Boolean.TRUE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Optional<ActivityRecord> findOpaqueNotFinishingActivityBelow() {
        if (!hasInheritedLetterboxBehavior() || this.mActivityRecord.getTask() == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.mFirstOpaqueActivityBeneath);
    }

    private static void resetTranslucentOverrideConfig(Configuration configuration) {
        configuration.orientation = 0;
        configuration.compatScreenWidthDp = 0;
        configuration.screenWidthDp = 0;
        configuration.compatScreenHeightDp = 0;
        configuration.screenHeightDp = 0;
        configuration.compatSmallestScreenWidthDp = 0;
        configuration.smallestScreenWidthDp = 0;
    }

    private void inheritConfiguration(ActivityRecord activityRecord) {
        if (this.mActivityRecord.getMinAspectRatio() != 0.0f) {
            this.mInheritedMinAspectRatio = activityRecord.getMinAspectRatio();
        }
        if (this.mActivityRecord.getMaxAspectRatio() != 0.0f) {
            this.mInheritedMaxAspectRatio = activityRecord.getMaxAspectRatio();
        }
        this.mInheritedOrientation = activityRecord.getRequestedConfigurationOrientation();
        this.mInheritedAppCompatState = activityRecord.getAppCompatState();
        this.mInheritedCompatDisplayInsets = activityRecord.getCompatDisplayInsets();
    }

    private void clearInheritedConfig() {
        ActivityRecord activityRecord = this.mFirstOpaqueActivityBeneath;
        if (activityRecord != null) {
            activityRecord.mLetterboxUiController.mDestroyListeners.remove(this);
        }
        this.mFirstOpaqueActivityBeneath = null;
        this.mLetterboxConfigListener = null;
        this.mInheritedMinAspectRatio = 0.0f;
        this.mInheritedMaxAspectRatio = 0.0f;
        this.mInheritedOrientation = 0;
        this.mInheritedAppCompatState = 0;
        this.mInheritedCompatDisplayInsets = null;
    }
}
