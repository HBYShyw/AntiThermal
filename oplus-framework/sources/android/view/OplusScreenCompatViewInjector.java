package android.view;

import android.content.res.CompatibilityInfo;
import android.os.Debug;
import android.os.SystemProperties;
import android.util.DisplayMetrics;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusScreenCompatViewInjector {
    private static final String TAG = "OplusScreenCompatViewInjector";
    public static boolean sIsDisplayCompatApp = false;
    public static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.compat.debug", true);
    public static int sCompatDensity = 420;

    public static void overrideDisplayMetricsIfNeed(DisplayMetrics inoutDm) {
        int i = inoutDm.densityDpi;
        int i2 = sCompatDensity;
        if (i != i2) {
            float f = i2 * 0.00625f;
            inoutDm.density = f;
            inoutDm.scaledDensity = f;
            inoutDm.densityDpi = sCompatDensity;
            inoutDm.xdpi = inoutDm.noncompatXdpi * 0.75f;
            inoutDm.ydpi = inoutDm.noncompatYdpi * 0.75f;
            inoutDm.widthPixels = (int) (inoutDm.noncompatWidthPixels * 0.75f);
            inoutDm.heightPixels = (int) (inoutDm.noncompatHeightPixels * 0.75f);
            if (DEBUG) {
                Log.d(TAG, "DisplayCompat: applyToDisplayMetrics0, inoutDm=" + inoutDm + " noncompatDensityDpi=" + inoutDm.noncompatDensityDpi + " caller:" + Debug.getCallers(10));
            }
        }
    }

    public static void applyCompatInfo(CompatibilityInfo compatInfo, DisplayMetrics outMetrics) {
        if (sIsDisplayCompatApp && outMetrics.densityDpi != sCompatDensity) {
            if (DEBUG) {
                Log.d(TAG, "DisplayCompat: applyCompatInfo, change out=" + outMetrics + " to " + sCompatDensity + " caller:" + Debug.getCallers(10));
            }
            compatInfo.applyToDisplayMetrics(outMetrics);
        }
    }

    public static void updateCompatDensityIfNeed(int density) {
        int toDensity;
        if (sIsDisplayCompatApp && sCompatDensity != (toDensity = (int) (density * 0.75d))) {
            sCompatDensity = toDensity;
            Log.i(TAG, "DisplayCompat: updateCompatDensityIfNeed from " + density + " to density=" + toDensity + " callers=" + Debug.getCallers(5));
        }
    }
}
