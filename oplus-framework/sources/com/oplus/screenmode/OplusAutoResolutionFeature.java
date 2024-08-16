package com.oplus.screenmode;

import android.content.pm.ApplicationInfo;
import android.content.pm.IApplicationInfoExt;
import android.content.res.CompatibilityInfo;
import android.graphics.Point;
import android.os.Debug;
import android.os.SystemProperties;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DisplayInfo;

/* loaded from: classes.dex */
public class OplusAutoResolutionFeature implements IOplusAutoResolutionFeature {
    public static final int DEBUG_CALLER_DEPTH = 8;
    private static final int INIT_UNKNOW = 1;
    private static final String TAG = "ScreenmodeClient";
    private float mAppInvScale;
    private float mAppScale;
    private CompatibilityInfo mCompatInfo;
    private int mInitializeStatus;
    private static final boolean mOplusDebug = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    public static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.compat.debug", false);
    private static boolean sIsDisplayCompatApp = false;
    private static int sCompatDensity = 480;

    private OplusAutoResolutionFeature() {
        this.mCompatInfo = null;
        this.mAppScale = 1.0f;
        this.mAppInvScale = 1.0f;
        this.mInitializeStatus = 1;
        if (DEBUG) {
            Log.d(TAG, "OplusAutoResolutionFeature create");
        }
    }

    /* loaded from: classes.dex */
    private static class InstanceHolder {
        static final OplusAutoResolutionFeature INSTANCE = new OplusAutoResolutionFeature();

        private InstanceHolder() {
        }
    }

    public static OplusAutoResolutionFeature getInstance() {
        OplusAutoResolutionFeature instance = InstanceHolder.INSTANCE;
        return instance;
    }

    @Override // com.oplus.screenmode.IOplusAutoResolutionFeature
    public void overrideDisplayMetricsIfNeed(DisplayMetrics inoutDm) {
        if (inoutDm == null) {
            Log.e(TAG, "overrideDisplayMetricsIfNeed failed for inoutDm null.");
            return;
        }
        int i = inoutDm.densityDpi;
        int i2 = sCompatDensity;
        if (i != i2) {
            float invertedRatio = this.mAppInvScale;
            inoutDm.density = i2 * 0.00625f;
            inoutDm.scaledDensity = inoutDm.density;
            inoutDm.densityDpi = sCompatDensity;
            inoutDm.xdpi = inoutDm.noncompatXdpi * invertedRatio;
            inoutDm.ydpi = inoutDm.noncompatYdpi * invertedRatio;
            inoutDm.widthPixels = (int) (inoutDm.noncompatWidthPixels * invertedRatio);
            inoutDm.heightPixels = (int) (inoutDm.noncompatHeightPixels * invertedRatio);
            if (DEBUG) {
                Log.d(TAG, "DisplayCompat: applyToDisplayMetrics0, inoutDm=" + inoutDm + " noncompatDensityDpi=" + inoutDm.noncompatDensityDpi + " caller:" + Debug.getCallers(8));
            }
        }
    }

    @Override // com.oplus.screenmode.IOplusAutoResolutionFeature
    public void applyCompatInfo(CompatibilityInfo compatInfo, DisplayMetrics outMetrics) {
        if (compatInfo == null || outMetrics == null) {
            Log.e(TAG, "applyCompatInfo failed for param null.");
        } else if (sIsDisplayCompatApp && outMetrics.densityDpi != sCompatDensity) {
            if (DEBUG) {
                Log.d(TAG, "DisplayCompat: applyCompatInfo, change out=" + outMetrics + " to " + sCompatDensity + " caller:" + Debug.getCallers(8));
            }
            compatInfo.applyToDisplayMetrics(outMetrics);
        }
    }

    @Override // com.oplus.screenmode.IOplusAutoResolutionFeature
    public void updateCompatDensityIfNeed(int density) {
        int toDensity;
        if (sIsDisplayCompatApp && sCompatDensity != (toDensity = (int) (density * this.mAppInvScale))) {
            sCompatDensity = toDensity;
            if (DEBUG) {
                Log.i(TAG, "DisplayCompat: updateCompatDensityIfNeed from " + density + " to density=" + toDensity + ", " + Debug.getCallers(8));
            }
        }
    }

    @Override // com.oplus.screenmode.IOplusAutoResolutionFeature
    public boolean isDisplayCompat(String packageName, int uid) {
        if (DEBUG) {
            Log.d(TAG, " isDisplayCompat, pkg:" + packageName);
        }
        return OplusDisplayModeManager.getInstance().isDisplayCompat(packageName, uid);
    }

    @Override // com.oplus.screenmode.IOplusAutoResolutionFeature
    public boolean supportDisplayCompat() {
        if (DEBUG) {
            Log.d(TAG, "supportDisplayCompat  ");
        }
        return sIsDisplayCompatApp;
    }

    @Override // com.oplus.screenmode.IOplusAutoResolutionFeature
    public int displayCompatDensity(int density) {
        if (DEBUG) {
            Log.d(TAG, "displayCompatDensity sCompatDensity=" + sCompatDensity);
        }
        return sCompatDensity;
    }

    @Override // com.oplus.screenmode.IOplusAutoResolutionFeature
    public void initDisplayCompat(ApplicationInfo ai, CompatibilityInfo compatInfo) {
        IApplicationInfoExt appInfoExt = ai == null ? null : ai.mApplicationInfoExt;
        if (appInfoExt != null && appInfoExt.enableLowResolution()) {
            this.mAppScale = appInfoExt.getAppScale();
            this.mAppInvScale = appInfoExt.getAppInvScale();
            sCompatDensity = appInfoExt.getCompatDensity();
            this.mCompatInfo = compatInfo;
            sIsDisplayCompatApp = true;
            Log.d(TAG, "initDisplayCompat " + this.mAppScale + "," + this.mAppInvScale + "," + sCompatDensity + "," + this.mCompatInfo);
            return;
        }
        sIsDisplayCompatApp = false;
    }

    @Override // com.oplus.screenmode.IOplusAutoResolutionFeature
    public void updateCompatRealSize(DisplayInfo displayInfo, Point outSize) {
        if (!sIsDisplayCompatApp) {
            return;
        }
        outSize.x = (int) (displayInfo.logicalWidth * this.mAppInvScale);
        outSize.y = (int) (displayInfo.logicalHeight * this.mAppInvScale);
    }

    @Override // com.oplus.screenmode.IOplusAutoResolutionFeature
    public CompatibilityInfo getCompatibilityInfo() {
        return this.mCompatInfo;
    }
}
