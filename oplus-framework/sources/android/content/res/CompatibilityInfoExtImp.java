package android.content.res;

import android.content.pm.ApplicationInfo;
import android.content.pm.IApplicationInfoExt;
import android.util.DisplayMetrics;

/* loaded from: classes.dex */
class CompatibilityInfoExtImp implements ICompatibilityInfoExt {
    private static final boolean DEBUG = false;
    private static final int HAS_OVERRIDE_SCALING = 32;
    private static final int NEEDS_COMPAT_RES = 16;
    private static final int NEVER_NEEDS_COMPAT = 4;
    private static final int OPLUS_NEEDS_DISPLAY_COMPAT = 64;
    private static final int SCALING_REQUIRED = 1;
    private static final String TAG = "CompatibilityInfoExtImp";

    public int getCompatDensity(ApplicationInfo ai, int defaultDensity) {
        IApplicationInfoExt appInfoExt = ai == null ? null : ai.mApplicationInfoExt;
        if (appInfoExt == null) {
            return defaultDensity;
        }
        if (appInfoExt.enableLowResolution()) {
            return appInfoExt.getCompatDensity();
        }
        return defaultDensity;
    }

    public boolean enableOverrideScaling(ApplicationInfo ai) {
        IApplicationInfoExt appInfoExt = ai == null ? null : ai.mApplicationInfoExt;
        if (appInfoExt == null || !appInfoExt.enableLowResolution()) {
            return false;
        }
        return true;
    }

    public int updateCompatFlagsIfNeed(ApplicationInfo ai, int flags) {
        IApplicationInfoExt appInfoExt = ai == null ? null : ai.mApplicationInfoExt;
        if (appInfoExt == null || !appInfoExt.enableLowResolution()) {
            return flags;
        }
        int compatFlags = flags | 64;
        return compatFlags | 4;
    }

    public void overrideDisplayMetricsIfNeed(DisplayMetrics inoutDm, CompatibilityInfo compat, int compatFlags) {
        int compatDensity = compat.applicationDensity;
        if (hasOverrideScaling(compatFlags) && inoutDm.densityDpi != compatDensity) {
            inoutDm.density = compatDensity * 0.00625f;
            inoutDm.scaledDensity = inoutDm.density;
            inoutDm.densityDpi = compatDensity;
        }
    }

    public void applyToConfiguration(Configuration inoutConfig, CompatibilityInfo compat, int compatFlags) {
        int compatDensity = compat.applicationDensity;
        if (hasOverrideScaling(compatFlags) && inoutConfig.densityDpi != compatDensity) {
            inoutConfig.densityDpi = compatDensity;
        }
    }

    public boolean hasOverrideScaling(int compatFlags) {
        return (compatFlags & 64) != 0;
    }
}
