package com.android.server.wm;

import android.provider.Settings;
import android.util.Slog;
import android.view.DisplayInfo;
import android.view.WindowManager;
import java.util.Objects;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class DisplayWindowSettings {
    private IDisplayWindowSettingsExt mDisplayWindowSettingsExt = (IDisplayWindowSettingsExt) ExtLoader.type(IDisplayWindowSettingsExt.class).base(this).create();
    private final WindowManagerService mService;
    private final SettingsProvider mSettingsProvider;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayWindowSettings(WindowManagerService windowManagerService, SettingsProvider settingsProvider) {
        this.mService = windowManagerService;
        this.mSettingsProvider = settingsProvider;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setUserRotation(DisplayContent displayContent, int i, int i2) {
        DisplayInfo displayInfo = displayContent.getDisplayInfo();
        SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mUserRotationMode = Integer.valueOf(i);
        overrideSettings.mUserRotation = Integer.valueOf(i2);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setForcedSize(DisplayContent displayContent, int i, int i2) {
        String str;
        if (displayContent.isDefaultDisplay) {
            if (i == 0 || i2 == 0) {
                str = "";
            } else {
                str = i + "," + i2;
            }
            Settings.Global.putString(this.mService.mContext.getContentResolver(), "display_size_forced", str);
        }
        DisplayInfo displayInfo = displayContent.getDisplayInfo();
        SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mForcedWidth = i;
        overrideSettings.mForcedHeight = i2;
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setForcedDensity(DisplayInfo displayInfo, int i, int i2) {
        if (displayInfo.displayId == 0) {
            Settings.Secure.putStringForUser(this.mService.mContext.getContentResolver(), "display_density_forced", i == 0 ? "" : Integer.toString(i), i2);
        }
        SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mForcedDensity = i;
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setForcedDensity(DisplayInfo displayInfo, int i) {
        SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mForcedDensity = i;
        Slog.d("DisplayWindowSettings", " setForcedDensity  density= " + i + ", " + displayInfo.uniqueId);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setForcedScalingMode(DisplayContent displayContent, int i) {
        if (displayContent.isDefaultDisplay) {
            Settings.Global.putInt(this.mService.mContext.getContentResolver(), "display_scaling_force", i);
        }
        DisplayInfo displayInfo = displayContent.getDisplayInfo();
        SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mForcedScalingMode = Integer.valueOf(i);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setFixedToUserRotation(DisplayContent displayContent, int i) {
        DisplayInfo displayInfo = displayContent.getDisplayInfo();
        SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mFixedToUserRotation = Integer.valueOf(i);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setIgnoreOrientationRequest(DisplayContent displayContent, boolean z) {
        DisplayInfo displayInfo = displayContent.getDisplayInfo();
        SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mIgnoreOrientationRequest = Boolean.valueOf(z);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    private int getWindowingModeLocked(SettingsProvider.SettingsEntry settingsEntry, DisplayContent displayContent) {
        int i = settingsEntry.mWindowingMode;
        if (i == 5 && !this.mService.mAtmService.mSupportsFreeformWindowManagement) {
            return 1;
        }
        if (i != 0) {
            return i;
        }
        WindowManagerService windowManagerService = this.mService;
        return (windowManagerService.mAtmService.mSupportsFreeformWindowManagement && (windowManagerService.mIsPc || displayContent.forceDesktopMode())) ? 5 : 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getWindowingModeLocked(DisplayContent displayContent) {
        return getWindowingModeLocked(this.mSettingsProvider.getSettings(displayContent.getDisplayInfo()), displayContent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWindowingModeLocked(DisplayContent displayContent, int i) {
        DisplayInfo displayInfo = displayContent.getDisplayInfo();
        SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mWindowingMode = i;
        TaskDisplayArea defaultTaskDisplayArea = displayContent.getDefaultTaskDisplayArea();
        if (defaultTaskDisplayArea != null) {
            defaultTaskDisplayArea.setWindowingMode(i);
        }
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRemoveContentModeLocked(DisplayContent displayContent) {
        int i = this.mSettingsProvider.getSettings(displayContent.getDisplayInfo()).mRemoveContentMode;
        return i == 0 ? displayContent.isPrivate() ? 2 : 1 : i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRemoveContentModeLocked(DisplayContent displayContent, int i) {
        DisplayInfo displayInfo = displayContent.getDisplayInfo();
        SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mRemoveContentMode = i;
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldShowWithInsecureKeyguardLocked(DisplayContent displayContent) {
        Boolean bool = this.mSettingsProvider.getSettings(displayContent.getDisplayInfo()).mShouldShowWithInsecureKeyguard;
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setShouldShowWithInsecureKeyguardLocked(DisplayContent displayContent, boolean z) {
        if (!displayContent.isPrivate() && z) {
            throw new IllegalArgumentException("Public display can't be allowed to show content when locked");
        }
        DisplayInfo displayInfo = displayContent.getDisplayInfo();
        SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mShouldShowWithInsecureKeyguard = Boolean.valueOf(z);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    void setDontMoveToTop(DisplayContent displayContent, boolean z) {
        DisplayInfo displayInfo = displayContent.getDisplayInfo();
        SettingsProvider.SettingsEntry settings = this.mSettingsProvider.getSettings(displayInfo);
        settings.mDontMoveToTop = Boolean.valueOf(z);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, settings);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldShowSystemDecorsLocked(DisplayContent displayContent) {
        if (displayContent.getDisplayId() == 0) {
            return true;
        }
        Boolean bool = this.mSettingsProvider.getSettings(displayContent.getDisplayInfo()).mShouldShowSystemDecors;
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setShouldShowSystemDecorsLocked(DisplayContent displayContent, boolean z) {
        DisplayInfo displayInfo = displayContent.getDisplayInfo();
        SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mShouldShowSystemDecors = Boolean.valueOf(z);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WindowManager.DisplayImePolicy
    public int getImePolicyLocked(DisplayContent displayContent) {
        if (displayContent.getDisplayId() == 0) {
            return 0;
        }
        Integer num = this.mSettingsProvider.getSettings(displayContent.getDisplayInfo()).mImePolicy;
        if (num != null) {
            return num.intValue();
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDisplayImePolicy(DisplayContent displayContent, @WindowManager.DisplayImePolicy int i) {
        DisplayInfo displayInfo = displayContent.getDisplayInfo();
        SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mImePolicy = Integer.valueOf(i);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applySettingsToDisplayLocked(DisplayContent displayContent) {
        applySettingsToDisplayLocked(displayContent, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applySettingsToDisplayLocked(DisplayContent displayContent, boolean z) {
        int initialDisplayDensity;
        SettingsProvider.SettingsEntry settings = this.mSettingsProvider.getSettings(displayContent.getDisplayInfo());
        int windowingModeLocked = getWindowingModeLocked(settings, displayContent);
        TaskDisplayArea defaultTaskDisplayArea = displayContent.getDefaultTaskDisplayArea();
        if (defaultTaskDisplayArea != null && !this.mDisplayWindowSettingsExt.skipSetWindowingMode(displayContent, z, windowingModeLocked)) {
            defaultTaskDisplayArea.setWindowingMode(windowingModeLocked);
        }
        Integer num = settings.mUserRotationMode;
        int intValue = num != null ? num.intValue() : 0;
        Integer num2 = settings.mUserRotation;
        int intValue2 = num2 != null ? num2.intValue() : 0;
        Integer num3 = settings.mFixedToUserRotation;
        displayContent.getDisplayRotation().restoreSettings(intValue, intValue2, num3 != null ? num3.intValue() : 0);
        boolean z2 = settings.mForcedDensity != 0;
        boolean z3 = (settings.mForcedWidth == 0 || settings.mForcedHeight == 0) ? false : true;
        displayContent.mIsDensityForced = z2;
        displayContent.mIsSizeForced = z3;
        Boolean bool = settings.mIgnoreDisplayCutout;
        displayContent.mIgnoreDisplayCutout = bool != null ? bool.booleanValue() : false;
        int i = z3 ? settings.mForcedWidth : displayContent.mInitialDisplayWidth;
        int i2 = z3 ? settings.mForcedHeight : displayContent.mInitialDisplayHeight;
        if (z2) {
            initialDisplayDensity = settings.mForcedDensity;
        } else {
            initialDisplayDensity = displayContent.getInitialDisplayDensity();
        }
        displayContent.updateBaseDisplayMetrics(i, i2, initialDisplayDensity, displayContent.mBaseDisplayPhysicalXDpi, displayContent.mBaseDisplayPhysicalYDpi);
        Integer num4 = settings.mForcedScalingMode;
        displayContent.mDisplayScalingDisabled = (num4 != null ? num4.intValue() : 0) == 1;
        Boolean bool2 = settings.mDontMoveToTop;
        displayContent.mDontMoveToTop = !displayContent.canStealTopFocus() || (bool2 != null ? bool2.booleanValue() : false);
        if (z) {
            applyRotationSettingsToDisplayLocked(displayContent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applyRotationSettingsToDisplayLocked(DisplayContent displayContent) {
        Boolean bool = this.mSettingsProvider.getSettings(displayContent.getDisplayInfo()).mIgnoreOrientationRequest;
        displayContent.setIgnoreOrientationRequest(bool != null ? bool.booleanValue() : false);
        displayContent.getDisplayRotation().resetAllowAllRotations();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateSettingsForDisplay(DisplayContent displayContent) {
        TaskDisplayArea defaultTaskDisplayArea = displayContent.getDefaultTaskDisplayArea();
        if (defaultTaskDisplayArea == null || defaultTaskDisplayArea.getWindowingMode() == getWindowingModeLocked(displayContent)) {
            return false;
        }
        defaultTaskDisplayArea.setWindowingMode(getWindowingModeLocked(displayContent));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface SettingsProvider {
        SettingsEntry getOverrideSettings(DisplayInfo displayInfo);

        SettingsEntry getSettings(DisplayInfo displayInfo);

        void updateOverrideSettings(DisplayInfo displayInfo, SettingsEntry settingsEntry);

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public static class SettingsEntry {
            Boolean mDontMoveToTop;
            Integer mFixedToUserRotation;
            int mForcedDensity;
            int mForcedHeight;
            Integer mForcedScalingMode;
            int mForcedWidth;
            Boolean mIgnoreDisplayCutout;
            Boolean mIgnoreOrientationRequest;
            Integer mImePolicy;
            Boolean mShouldShowSystemDecors;
            Boolean mShouldShowWithInsecureKeyguard;
            Integer mUserRotation;
            Integer mUserRotationMode;
            int mWindowingMode = 0;
            int mRemoveContentMode = 0;

            /* JADX INFO: Access modifiers changed from: package-private */
            public SettingsEntry() {
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public SettingsEntry(SettingsEntry settingsEntry) {
                setTo(settingsEntry);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public boolean setTo(SettingsEntry settingsEntry) {
                boolean z;
                int i = settingsEntry.mWindowingMode;
                if (i != this.mWindowingMode) {
                    this.mWindowingMode = i;
                    z = true;
                } else {
                    z = false;
                }
                if (!Objects.equals(settingsEntry.mUserRotationMode, this.mUserRotationMode)) {
                    this.mUserRotationMode = settingsEntry.mUserRotationMode;
                    z = true;
                }
                if (!Objects.equals(settingsEntry.mUserRotation, this.mUserRotation)) {
                    this.mUserRotation = settingsEntry.mUserRotation;
                    z = true;
                }
                int i2 = settingsEntry.mForcedWidth;
                if (i2 != this.mForcedWidth) {
                    this.mForcedWidth = i2;
                    z = true;
                }
                int i3 = settingsEntry.mForcedHeight;
                if (i3 != this.mForcedHeight) {
                    this.mForcedHeight = i3;
                    z = true;
                }
                int i4 = settingsEntry.mForcedDensity;
                if (i4 != this.mForcedDensity) {
                    this.mForcedDensity = i4;
                    z = true;
                }
                if (!Objects.equals(settingsEntry.mForcedScalingMode, this.mForcedScalingMode)) {
                    this.mForcedScalingMode = settingsEntry.mForcedScalingMode;
                    z = true;
                }
                int i5 = settingsEntry.mRemoveContentMode;
                if (i5 != this.mRemoveContentMode) {
                    this.mRemoveContentMode = i5;
                    z = true;
                }
                if (!Objects.equals(settingsEntry.mShouldShowWithInsecureKeyguard, this.mShouldShowWithInsecureKeyguard)) {
                    this.mShouldShowWithInsecureKeyguard = settingsEntry.mShouldShowWithInsecureKeyguard;
                    z = true;
                }
                if (!Objects.equals(settingsEntry.mShouldShowSystemDecors, this.mShouldShowSystemDecors)) {
                    this.mShouldShowSystemDecors = settingsEntry.mShouldShowSystemDecors;
                    z = true;
                }
                if (!Objects.equals(settingsEntry.mImePolicy, this.mImePolicy)) {
                    this.mImePolicy = settingsEntry.mImePolicy;
                    z = true;
                }
                if (!Objects.equals(settingsEntry.mFixedToUserRotation, this.mFixedToUserRotation)) {
                    this.mFixedToUserRotation = settingsEntry.mFixedToUserRotation;
                    z = true;
                }
                if (!Objects.equals(settingsEntry.mIgnoreOrientationRequest, this.mIgnoreOrientationRequest)) {
                    this.mIgnoreOrientationRequest = settingsEntry.mIgnoreOrientationRequest;
                    z = true;
                }
                if (!Objects.equals(settingsEntry.mIgnoreDisplayCutout, this.mIgnoreDisplayCutout)) {
                    this.mIgnoreDisplayCutout = settingsEntry.mIgnoreDisplayCutout;
                    z = true;
                }
                if (Objects.equals(settingsEntry.mDontMoveToTop, this.mDontMoveToTop)) {
                    return z;
                }
                this.mDontMoveToTop = settingsEntry.mDontMoveToTop;
                return true;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public boolean updateFrom(SettingsEntry settingsEntry) {
                boolean z;
                int i = settingsEntry.mWindowingMode;
                if (i == 0 || i == this.mWindowingMode) {
                    z = false;
                } else {
                    this.mWindowingMode = i;
                    z = true;
                }
                Integer num = settingsEntry.mUserRotationMode;
                if (num != null && !Objects.equals(num, this.mUserRotationMode)) {
                    this.mUserRotationMode = settingsEntry.mUserRotationMode;
                    z = true;
                }
                Integer num2 = settingsEntry.mUserRotation;
                if (num2 != null && !Objects.equals(num2, this.mUserRotation)) {
                    this.mUserRotation = settingsEntry.mUserRotation;
                    z = true;
                }
                int i2 = settingsEntry.mForcedWidth;
                if (i2 != 0 && i2 != this.mForcedWidth) {
                    this.mForcedWidth = i2;
                    z = true;
                }
                int i3 = settingsEntry.mForcedHeight;
                if (i3 != 0 && i3 != this.mForcedHeight) {
                    this.mForcedHeight = i3;
                    z = true;
                }
                int i4 = settingsEntry.mForcedDensity;
                if (i4 != 0 && i4 != this.mForcedDensity) {
                    this.mForcedDensity = i4;
                    z = true;
                }
                Integer num3 = settingsEntry.mForcedScalingMode;
                if (num3 != null && !Objects.equals(num3, this.mForcedScalingMode)) {
                    this.mForcedScalingMode = settingsEntry.mForcedScalingMode;
                    z = true;
                }
                int i5 = settingsEntry.mRemoveContentMode;
                if (i5 != 0 && i5 != this.mRemoveContentMode) {
                    this.mRemoveContentMode = i5;
                    z = true;
                }
                Boolean bool = settingsEntry.mShouldShowWithInsecureKeyguard;
                if (bool != null && !Objects.equals(bool, this.mShouldShowWithInsecureKeyguard)) {
                    this.mShouldShowWithInsecureKeyguard = settingsEntry.mShouldShowWithInsecureKeyguard;
                    z = true;
                }
                Boolean bool2 = settingsEntry.mShouldShowSystemDecors;
                if (bool2 != null && !Objects.equals(bool2, this.mShouldShowSystemDecors)) {
                    this.mShouldShowSystemDecors = settingsEntry.mShouldShowSystemDecors;
                    z = true;
                }
                Integer num4 = settingsEntry.mImePolicy;
                if (num4 != null && !Objects.equals(num4, this.mImePolicy)) {
                    this.mImePolicy = settingsEntry.mImePolicy;
                    z = true;
                }
                Integer num5 = settingsEntry.mFixedToUserRotation;
                if (num5 != null && !Objects.equals(num5, this.mFixedToUserRotation)) {
                    this.mFixedToUserRotation = settingsEntry.mFixedToUserRotation;
                    z = true;
                }
                Boolean bool3 = settingsEntry.mIgnoreOrientationRequest;
                if (bool3 != null && !Objects.equals(bool3, this.mIgnoreOrientationRequest)) {
                    this.mIgnoreOrientationRequest = settingsEntry.mIgnoreOrientationRequest;
                    z = true;
                }
                Boolean bool4 = settingsEntry.mIgnoreDisplayCutout;
                if (bool4 != null && !Objects.equals(bool4, this.mIgnoreDisplayCutout)) {
                    this.mIgnoreDisplayCutout = settingsEntry.mIgnoreDisplayCutout;
                    z = true;
                }
                Boolean bool5 = settingsEntry.mDontMoveToTop;
                if (bool5 == null || Objects.equals(bool5, this.mDontMoveToTop)) {
                    return z;
                }
                this.mDontMoveToTop = settingsEntry.mDontMoveToTop;
                return true;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public boolean isEmpty() {
                return this.mWindowingMode == 0 && this.mUserRotationMode == null && this.mUserRotation == null && this.mForcedWidth == 0 && this.mForcedHeight == 0 && this.mForcedDensity == 0 && this.mForcedScalingMode == null && this.mRemoveContentMode == 0 && this.mShouldShowWithInsecureKeyguard == null && this.mShouldShowSystemDecors == null && this.mImePolicy == null && this.mFixedToUserRotation == null && this.mIgnoreOrientationRequest == null && this.mIgnoreDisplayCutout == null && this.mDontMoveToTop == null;
            }

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj == null || getClass() != obj.getClass()) {
                    return false;
                }
                SettingsEntry settingsEntry = (SettingsEntry) obj;
                return this.mWindowingMode == settingsEntry.mWindowingMode && this.mForcedWidth == settingsEntry.mForcedWidth && this.mForcedHeight == settingsEntry.mForcedHeight && this.mForcedDensity == settingsEntry.mForcedDensity && this.mRemoveContentMode == settingsEntry.mRemoveContentMode && Objects.equals(this.mUserRotationMode, settingsEntry.mUserRotationMode) && Objects.equals(this.mUserRotation, settingsEntry.mUserRotation) && Objects.equals(this.mForcedScalingMode, settingsEntry.mForcedScalingMode) && Objects.equals(this.mShouldShowWithInsecureKeyguard, settingsEntry.mShouldShowWithInsecureKeyguard) && Objects.equals(this.mShouldShowSystemDecors, settingsEntry.mShouldShowSystemDecors) && Objects.equals(this.mImePolicy, settingsEntry.mImePolicy) && Objects.equals(this.mFixedToUserRotation, settingsEntry.mFixedToUserRotation) && Objects.equals(this.mIgnoreOrientationRequest, settingsEntry.mIgnoreOrientationRequest) && Objects.equals(this.mIgnoreDisplayCutout, settingsEntry.mIgnoreDisplayCutout) && Objects.equals(this.mDontMoveToTop, settingsEntry.mDontMoveToTop);
            }

            public int hashCode() {
                return Objects.hash(Integer.valueOf(this.mWindowingMode), this.mUserRotationMode, this.mUserRotation, Integer.valueOf(this.mForcedWidth), Integer.valueOf(this.mForcedHeight), Integer.valueOf(this.mForcedDensity), this.mForcedScalingMode, Integer.valueOf(this.mRemoveContentMode), this.mShouldShowWithInsecureKeyguard, this.mShouldShowSystemDecors, this.mImePolicy, this.mFixedToUserRotation, this.mIgnoreOrientationRequest, this.mIgnoreDisplayCutout, this.mDontMoveToTop);
            }

            public String toString() {
                return "SettingsEntry{mWindowingMode=" + this.mWindowingMode + ", mUserRotationMode=" + this.mUserRotationMode + ", mUserRotation=" + this.mUserRotation + ", mForcedWidth=" + this.mForcedWidth + ", mForcedHeight=" + this.mForcedHeight + ", mForcedDensity=" + this.mForcedDensity + ", mForcedScalingMode=" + this.mForcedScalingMode + ", mRemoveContentMode=" + this.mRemoveContentMode + ", mShouldShowWithInsecureKeyguard=" + this.mShouldShowWithInsecureKeyguard + ", mShouldShowSystemDecors=" + this.mShouldShowSystemDecors + ", mShouldShowIme=" + this.mImePolicy + ", mFixedToUserRotation=" + this.mFixedToUserRotation + ", mIgnoreOrientationRequest=" + this.mIgnoreOrientationRequest + ", mIgnoreDisplayCutout=" + this.mIgnoreDisplayCutout + ", mDontMoveToTop=" + this.mDontMoveToTop + '}';
            }
        }
    }
}
