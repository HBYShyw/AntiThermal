package com.oplus.dynamicframerate;

import android.os.RemoteException;
import android.os.SystemProperties;
import com.oplus.os.WaveformEffect;
import com.oplus.vrr.IOPlusRefreshRate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public class FRTCConfigManager {
    public static final int FRAMERATE_DEFAULT;
    public static final int[][] FRAMERATE_LEVEL_LIST;
    public static final int FRAMERATE_SCROLL_BAE_FADE;
    public static final int[] FRTC_CAPABILITY_LIST;
    public static final int FRTC_INFO_FRTC_CAPABILITY_MASK = 1023;
    public static final int FRTC_INFO_MIN_FRAMERATE_BIT_SHIFT = 11;
    public static final int FRTC_INFO_MIN_FRAMERATE_MASK = 2095104;
    public static final int FRTC_INFO_PACKAGE_ENABLE_MASK = 1024;
    public static final int[][] LEVEL_THRESHOLD_LIST;
    public static final String STRING_FRAMERATE_DEFAULT = "debug.dynamicframerate.framerate.default";
    private static final String TAG;
    private FrameRateConfig mFrameRateConfig;
    private int mFrtcCapability;
    private int mFrtcInfoFlag;
    private boolean mIsHighCapability;
    private IOPlusRefreshRate mOplusRefreshRateService;
    public static final String STRING_THRESHOLD_PROPERTIES_90 = "debug.dynamicframerate.threshold.90";
    public static final int SPEED_PIXEL_PER_SECOND_90 = SystemProperties.getInt(STRING_THRESHOLD_PROPERTIES_90, 960);
    public static final String STRING_THRESHOLD_PROPERTIES_72 = "debug.dynamicframerate.threshold.72";
    public static final int SPEED_PIXEL_PER_SECOND_72 = SystemProperties.getInt(STRING_THRESHOLD_PROPERTIES_72, 720);
    public static final String STRING_THRESHOLD_PROPERTIES_60 = "debug.dynamicframerate.threshold.60";
    public static final int SPEED_PIXEL_PER_SECOND_60 = SystemProperties.getInt(STRING_THRESHOLD_PROPERTIES_60, 480);
    public static final String STRING_THRESHOLD_PROPERTIES_40 = "debug.dynamicframerate.threshold.40";
    public static final int SPEED_PIXEL_PER_SECOND_40 = SystemProperties.getInt(STRING_THRESHOLD_PROPERTIES_40, 80);
    public static final String STRING_THRESHOLD_PROPERTIES_30 = "debug.dynamicframerate.threshold.30";
    public static final int SPEED_PIXEL_PER_SECOND_30 = SystemProperties.getInt(STRING_THRESHOLD_PROPERTIES_30, 30);
    public static final String STRING_FRAMERATE_ENABLE_PROPERTIES_90 = "debug.dynamicframerate.framerate.enable.90";
    public static final boolean FRAMERATE_90_ENABLE = SystemProperties.getBoolean(STRING_FRAMERATE_ENABLE_PROPERTIES_90, true);
    public static final String STRING_FRAMERATE_ENABLE_PROPERTIES_72 = "debug.dynamicframerate.framerate.enable.72";
    public static final boolean FRAMERATE_72_ENABLE = SystemProperties.getBoolean(STRING_FRAMERATE_ENABLE_PROPERTIES_72, true);
    public static final String STRING_FRAMERATE_ENABLE_PROPERTIES_60 = "debug.dynamicframerate.framerate.enable.60";
    public static final boolean FRAMERATE_60_ENABLE = SystemProperties.getBoolean(STRING_FRAMERATE_ENABLE_PROPERTIES_60, true);
    public static final String STRING_FRAMERATE_ENABLE_PROPERTIES_40 = "debug.dynamicframerate.framerate.enable.40";
    public static final boolean FRAMERATE_40_ENABLE = SystemProperties.getBoolean(STRING_FRAMERATE_ENABLE_PROPERTIES_40, false);
    public static final String STRING_FRAMERATE_ENABLE_PROPERTIES_30 = "debug.dynamicframerate.framerate.enable.30";
    public static final boolean FRAMERATE_30_ENABLE = SystemProperties.getBoolean(STRING_FRAMERATE_ENABLE_PROPERTIES_30, false);
    public static final String STRING_WINDOW_ANIMATION_SPEED_RATE = "debug.dynamicframerate.window.animation.speed.rate";
    public static final int WINDOW_ANIMATION_SPEED_RATE = SystemProperties.getInt(STRING_WINDOW_ANIMATION_SPEED_RATE, 10);
    public static final String STRING_SCROLL_ANIMATION_SPEED_RATE = "debug.dynamicframerate.scroll.animation.speed.rate";
    public static final int SCROLL_ANIMATION_SPEED_RATE = SystemProperties.getInt(STRING_SCROLL_ANIMATION_SPEED_RATE, 20);
    public static final String STRING_MULTIWINDOW_IDLE_FRAMERATE = "debug.dynamicframerate.multiwindow.idle.framerate";
    public static final int MULTIWINDOW_IDLE_FRAMERATE = SystemProperties.getInt(STRING_MULTIWINDOW_IDLE_FRAMERATE, 60);

    static {
        int i = SystemProperties.getInt(STRING_FRAMERATE_DEFAULT, 0);
        FRAMERATE_DEFAULT = i;
        FRAMERATE_SCROLL_BAE_FADE = i;
        FRTC_CAPABILITY_LIST = new int[]{120, WaveformEffect.EFFECT_RINGTONE_ROCK};
        FRAMERATE_LEVEL_LIST = new int[][]{new int[]{120, 60}, new int[]{120, 90, 72, 60}};
        LEVEL_THRESHOLD_LIST = new int[][]{new int[]{200}, new int[]{1080, 300, 200}};
        TAG = FRTCConfigManager.class.getSimpleName();
    }

    public FRTCConfigManager(IOPlusRefreshRate oplusRefreshRateService, String pkgName) {
        this.mOplusRefreshRateService = oplusRefreshRateService;
        int fRTCInfo = getFRTCInfo(pkgName);
        this.mFrtcInfoFlag = fRTCInfo;
        this.mFrtcCapability = fRTCInfo & FRTC_INFO_FRTC_CAPABILITY_MASK;
        if (SystemProperties.get(OplusDebugUtil.DEBUG_HIGH_CAPABILITY_PROPERTY).isEmpty()) {
            this.mIsHighCapability = this.mFrtcCapability > 120;
        } else {
            boolean z = SystemProperties.getBoolean(OplusDebugUtil.DEBUG_HIGH_CAPABILITY_PROPERTY, false);
            this.mIsHighCapability = z;
            if (!z) {
                this.mFrtcCapability = FRTC_CAPABILITY_LIST[0];
            }
        }
        initFrameRateConfig(getFrtcCapabilityIndex(this.mFrtcCapability), getMinFramerateOfFlag(this.mFrtcInfoFlag));
        this.mFrameRateConfig.setMuitiWindowIdleFramerate(MULTIWINDOW_IDLE_FRAMERATE);
        OplusDebugUtil.i(TAG, "FRTCConfigManager: FRTC_CAPABILITY = " + this.mFrtcCapability + ", package name = " + pkgName + ", WINDOW_ANIMATION_SPEED_RATE = " + WINDOW_ANIMATION_SPEED_RATE + ", SCROLL_ANIMATION_SPEED_RATE = " + SCROLL_ANIMATION_SPEED_RATE + ", MuitiWindowIdleFramerate = " + this.mFrameRateConfig.mMuitiWindowIdleFramerate + ", PACKAGE_ENABLE = " + ((this.mFrtcInfoFlag & 1024) != 0));
    }

    public FrameRateConfig getFrameRateConfig() {
        return this.mFrameRateConfig;
    }

    public boolean isHighCapability() {
        return this.mIsHighCapability;
    }

    public boolean isPackageEnable() {
        return (this.mFrtcInfoFlag & 1024) != 0;
    }

    private void initFrameRateConfig(int frtcCapabilityIndex, int minFrameRate) {
        if (isDebugMode()) {
            debugModeInit();
        } else {
            this.mFrameRateConfig = new FrameRateConfig(this, FRAMERATE_LEVEL_LIST[frtcCapabilityIndex], LEVEL_THRESHOLD_LIST[frtcCapabilityIndex], FRAMERATE_SCROLL_BAE_FADE, minFrameRate);
        }
        OplusDebugUtil.i(TAG, "initFrameRateConfig: \n\tlevels: " + Arrays.toString(this.mFrameRateConfig.mLevels) + "\n\tthresholds: " + Arrays.toString(this.mFrameRateConfig.mThresholds) + "\n\tscrollbar fade frame rate: " + this.mFrameRateConfig.mScrollBarFadeFrameRate);
    }

    public int getDefaultFrameRate() {
        return FRAMERATE_DEFAULT;
    }

    private int getFrtcCapabilityIndex(int frtcCapability) {
        int i = 0;
        while (true) {
            int[] iArr = FRTC_CAPABILITY_LIST;
            if (i < iArr.length) {
                if (frtcCapability != iArr[i]) {
                    i++;
                } else {
                    return i;
                }
            } else {
                return 0;
            }
        }
    }

    private int getMinFramerateOfFlag(int frtcInfoFlag) {
        return (2095104 & frtcInfoFlag) >> 11;
    }

    private boolean isDebugMode() {
        return (SystemProperties.get(STRING_FRAMERATE_ENABLE_PROPERTIES_90).isEmpty() && SystemProperties.get(STRING_FRAMERATE_ENABLE_PROPERTIES_72).isEmpty() && SystemProperties.get(STRING_FRAMERATE_ENABLE_PROPERTIES_60).isEmpty() && SystemProperties.get(STRING_FRAMERATE_ENABLE_PROPERTIES_40).isEmpty() && SystemProperties.get(STRING_FRAMERATE_ENABLE_PROPERTIES_30).isEmpty()) ? false : true;
    }

    private int getFRTCInfo(String pkgName) {
        IOPlusRefreshRate iOPlusRefreshRate = this.mOplusRefreshRateService;
        if (iOPlusRefreshRate == null) {
            OplusDebugUtil.e(TAG, "getFRTCInfo: error on OplusRefreshRateService.getFRTCInfo mOplusRefreshRateService is null!");
            return 0;
        }
        try {
            int ret = iOPlusRefreshRate.getFRTCInfo(pkgName);
            return ret;
        } catch (RemoteException e) {
            OplusDebugUtil.e(TAG, "getFRTCInfo: error on OplusRefreshRateService.getFRTCInfo, exception info: " + e.getMessage());
            return 0;
        }
    }

    private void debugModeInit() {
        List<Integer> list = new ArrayList<>();
        if (FRAMERATE_90_ENABLE) {
            list.add(90);
        }
        if (FRAMERATE_72_ENABLE) {
            list.add(72);
        }
        if (FRAMERATE_60_ENABLE) {
            list.add(60);
        }
        if (FRAMERATE_40_ENABLE) {
            list.add(40);
        }
        if (FRAMERATE_30_ENABLE) {
            list.add(30);
        }
        int[] levels = new int[list.size() + 1];
        int[] thresholds = new int[list.size()];
        levels[0] = 120;
        for (int i = 0; i < list.size(); i++) {
            levels[i + 1] = list.get(i).intValue();
            switch (levels[i + 1]) {
                case 30:
                    thresholds[i] = SPEED_PIXEL_PER_SECOND_30;
                    break;
                case 40:
                    thresholds[i] = SPEED_PIXEL_PER_SECOND_40;
                    break;
                case 60:
                    thresholds[i] = SPEED_PIXEL_PER_SECOND_60;
                    break;
                case 72:
                    thresholds[i] = SPEED_PIXEL_PER_SECOND_72;
                    break;
                case 90:
                    thresholds[i] = SPEED_PIXEL_PER_SECOND_90;
                    break;
            }
        }
        this.mFrameRateConfig = new FrameRateConfig(levels, thresholds, FRAMERATE_SCROLL_BAE_FADE);
    }

    /* loaded from: classes.dex */
    public class FrameRateConfig {
        int mInputIdleFramerate;
        int[] mLevels;
        int mMinFrameRate;
        int mMuitiWindowIdleFramerate;
        int mScrollBarFadeFrameRate;
        int[] mThresholds;

        public FrameRateConfig(int[] levels, int[] thresholds, int scrollBarFadeFrameRate) {
            this.mLevels = levels;
            this.mScrollBarFadeFrameRate = scrollBarFadeFrameRate;
            this.mThresholds = thresholds;
            this.mMinFrameRate = FRTCConfigManager.FRAMERATE_DEFAULT;
            this.mInputIdleFramerate = 60;
        }

        public FrameRateConfig(FRTCConfigManager this$0, int[] levels, int[] thresholds, int scrollBarFadeFrameRate, int minFrameRate) {
            this(levels, thresholds, scrollBarFadeFrameRate);
            this.mMinFrameRate = minFrameRate;
        }

        public void setMuitiWindowIdleFramerate(int framerate) {
            int i = 0;
            while (true) {
                int[] iArr = this.mLevels;
                if (i < iArr.length) {
                    int i2 = iArr[i];
                    if (framerate < i2) {
                        i++;
                    } else {
                        this.mMuitiWindowIdleFramerate = i2;
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        public int getDefaultFrameRate() {
            return FRTCConfigManager.FRAMERATE_DEFAULT;
        }

        public int getScrollBarFadeFrameRate() {
            return this.mScrollBarFadeFrameRate;
        }

        public int getInputIdleFrameRate() {
            return this.mInputIdleFramerate;
        }

        public int getTargetFrameRate(int velocity) {
            int i = 0;
            while (true) {
                int[] iArr = this.mThresholds;
                if (i < iArr.length) {
                    if (velocity <= iArr[i]) {
                        i++;
                    } else {
                        int i2 = this.mLevels[i];
                        int i3 = this.mMinFrameRate;
                        return i2 < i3 ? i3 : i2;
                    }
                } else {
                    int[] iArr2 = this.mLevels;
                    int i4 = iArr2[iArr.length];
                    int i5 = this.mMinFrameRate;
                    return i4 < i5 ? i5 : iArr2[iArr.length];
                }
            }
        }
    }
}
