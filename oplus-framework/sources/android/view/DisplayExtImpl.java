package android.view;

import android.app.IOplusCompactWindowAppManager;
import android.common.OplusFeatureCache;
import android.content.res.CompatibilityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.SystemProperties;
import android.util.Log;
import com.oplus.screenmode.IOplusAutoResolutionFeature;

/* loaded from: classes.dex */
public class DisplayExtImpl implements IDisplayExt {
    private static final boolean DEBUG = true;
    private boolean mIsUltraHdrSupport = SystemProperties.getBoolean("persist.sys.feature.uhdr.support", false);
    IOplusAutoResolutionFeature feature = (IOplusAutoResolutionFeature) OplusFeatureCache.get(IOplusAutoResolutionFeature.DEFAULT);
    IOplusCompactWindowAppManager mCompactWindowAppManager = OplusFeatureCache.getOrCreate(IOplusCompactWindowAppManager.DEFAULT, new Object[0]);

    public DisplayExtImpl(Object base) {
    }

    public boolean supportDisplayCompat() {
        IOplusAutoResolutionFeature iOplusAutoResolutionFeature = this.feature;
        return iOplusAutoResolutionFeature != null && iOplusAutoResolutionFeature.supportDisplayCompat();
    }

    public void updateCompatRealSize(DisplayInfo displayInfo, CompatibilityInfo compat, Point outSize) {
        if (supportDisplayCompat() && compat.applicationScale != 1.0f) {
            outSize.x = (int) (displayInfo.logicalWidth * compat.applicationInvertedScale);
            outSize.y = (int) (displayInfo.logicalHeight * compat.applicationInvertedScale);
            Log.d("DisplayExtImp", "updateCompatRealSize : " + outSize, new Throwable());
        }
    }

    public int getCompactWindowRotation(Resources resources) {
        IOplusCompactWindowAppManager iOplusCompactWindowAppManager = this.mCompactWindowAppManager;
        if (iOplusCompactWindowAppManager != null) {
            return iOplusCompactWindowAppManager.getCompactWindowRotation(resources);
        }
        return -1;
    }

    public DisplayAdjustments getCompactWindowDisplayAdjustment(Resources resources) {
        DisplayAdjustments adjustments;
        IOplusCompactWindowAppManager iOplusCompactWindowAppManager = this.mCompactWindowAppManager;
        if (iOplusCompactWindowAppManager != null && (adjustments = iOplusCompactWindowAppManager.getCompactWindowDisplayAdjustment(resources)) != null && adjustments.getConfiguration().windowConfiguration.getAppBounds() != null) {
            return adjustments;
        }
        return null;
    }

    public DisplayAdjustments getDisplayAdjustmentForCompactWindow(Resources resources, DisplayAdjustments originAdjustments) {
        DisplayAdjustments adjustments;
        IOplusCompactWindowAppManager iOplusCompactWindowAppManager = this.mCompactWindowAppManager;
        if (iOplusCompactWindowAppManager != null && (adjustments = iOplusCompactWindowAppManager.getCompactWindowDisplayAdjustment(resources)) != null && adjustments.getConfiguration().windowConfiguration.getAppBounds() != null) {
            return adjustments;
        }
        return originAdjustments;
    }

    public boolean isUltraHdrSupport() {
        return this.mIsUltraHdrSupport;
    }
}
